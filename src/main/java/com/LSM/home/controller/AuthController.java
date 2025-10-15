package com.LSM.home.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException.BadRequest;

import com.LSM.home.dto.SiteUserDto;
import com.LSM.home.entity.SiteUser;
import com.LSM.home.repository.UserRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	 
	//회원가입->성공 실패
//	@PostMapping("/signup")
//	public ResponseEntity<?> signup(@RequestBody SiteUser req) { //?를 써서 어떤 타입이던 반환되도록
//		//사용자 이름(username)이 DB에 존재하는지 확인
//		if(userRepository.findByUsername(req.getUsername()).isPresent()) {
//			//참이면->이미지 해당 username존재하므로 가입불가
//			return ResponseEntity.badRequest().body("이미 존재하는 사용자명입니다"); //가입실패
//		}
//		req.setPassword(passwordEncoder.encode(req.getPassword()));
//		userRepository.save(req);
//		return ResponseEntity.ok("회원가입 성공"); //가입성공
//		//return ResponseEntity.ok(req); //가입성공 후 해당 엔티티 반환
//	}
	@PostMapping("/signup") //validation적용
	public ResponseEntity<?> signup(@Valid @RequestBody SiteUserDto siteUserDto, BindingResult bindingResult) { //?를 써서 어떤 타입이던 반환되도록
		if(bindingResult.hasErrors()) { //참이면 에러
			Map<String, String> errors = new HashMap<>(); //map으로 만들어야 프론트전달 가능
			bindingResult.getFieldErrors().forEach(
				err -> {
					errors.put(err.getField(),err.getDefaultMessage());
				}
			);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
		}
		
		SiteUser siteUser = new SiteUser();// 엔티티 객체 선언 requestbody를 쓰면 불러올수 있는데 dto를 쓰기때문에 선언해줘야함
		siteUser.setUsername(siteUserDto.getUsername());
		siteUser.setPassword(siteUserDto.getPassword());
		
		//사용자 이름(username)이 DB에 존재하는지 확인
		if(userRepository.findByUsername(siteUser.getUsername()).isPresent()) {
			//참이면->이미지 해당 username존재하므로 가입불가
			Map<String, String> error = new HashMap<>();
			error.put("iderror", "이미 존재하는 사용자명입니다");
			return ResponseEntity.badRequest().body(error); //가입실패
		}
		siteUser.setPassword(passwordEncoder.encode(siteUser.getPassword()));
		userRepository.save(siteUser);
		return ResponseEntity.ok("회원가입 성공"); //가입성공
		//return ResponseEntity.ok(req); //가입성공 후 해당 엔티티 반환
	}
	
	//로그인
	@GetMapping("/me") //현재 로그인한 사용자 정보를 가져오는 요청
	public ResponseEntity<?> me(org.springframework.security.core.Authentication auth) {
		return ResponseEntity.ok(Map.of("username", auth.getName()));
	}
	
	
}
