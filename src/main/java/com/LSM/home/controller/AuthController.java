package com.LSM.home.controller;

import java.util.Map;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.LSM.home.entity.SiteUser;
import com.LSM.home.repository.UserRepository;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	 
	//회원가입->성공 실패
	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody SiteUser req) { //?를 써서 어떤 타입이던 반환되도록
		//사용자 이름(username)이 DB에 존재하는지 확인
		if(userRepository.findByUsername(req.getUsername()).isPresent()) {
			//참이면->이미지 해당 username존재하므로 가입불가
			return ResponseEntity.badRequest().body("이미 존재하는 사용자명입니다"); //가입실패
		}
		req.setPassword(passwordEncoder.encode(req.getPassword()));
		userRepository.save(req);
		return ResponseEntity.ok("회원가입 성공"); //가입성공
		//return ResponseEntity.ok(req); //가입성공 후 해당 엔티티 반환
	}
	
	//로그인
	@GetMapping("/me") //현재 로그인한 사용자 정보를 가져오는 요청
	public ResponseEntity<?> me(org.springframework.security.core.Authentication auth) {
		return ResponseEntity.ok(Map.of("username", auth.getName()));
	}
	
	
}
