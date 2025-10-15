package com.LSM.home.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.LSM.home.dto.BoardDto;
import com.LSM.home.entity.Board;
import com.LSM.home.entity.SiteUser;
import com.LSM.home.repository.BoardRepository;
import com.LSM.home.repository.UserRepository;

import jakarta.validation.Valid;



@RestController
@RequestMapping("/api/board")
public class BoardController {

    

	@Autowired
	private BoardRepository boardRepository;
	
	@Autowired
	private UserRepository userRepository;
	
//	//전체 게시글 조회->페이징 처리x
//	@GetMapping
//	public List<Board> list() {
//		return boardRepository.findAll();
//	}
	//게시글 조회->페이징처리o
	@GetMapping
	public ResponseEntity<?> pagingList(@RequestParam(name="page",defaultValue = "0")int page,
					  @RequestParam(name="size",defaultValue = "10")int size) {
		//page->사용자가 요청한 페이지번호, size->한페이지당 보여질 글갯수
		if(page < 0) {
			page = 0;
		}
		if(size <= 0) {
			size = 10;
		}
		//pageble객체 생성->findall에 들어감
		Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
		Page<Board> boardPage = boardRepository.findAll(pageable);//DB에서 페이징된 게시글만 조회
		//boardPage 포함정보->
		//1.해당페이지 게시글 리스트 ->boardPage.getContent()
		//2.현재 페이지 번호 ->boardPage.getNumber()
		//3.전체 페이지 수 ->boardPage.getTotalpages()
		//4.전체 게시글 수 ->boardPage.getTotalElements()
		
		Map<String,Object> pagingResponse = new HashMap<>(); //Map앞에는 문자열, 뒤에는 모든 타입이반환되게 object
		pagingResponse.put("posts",boardPage.getContent() );//페이징된 현재 페이지에 해당하는 게시글 리스트 10개
		pagingResponse.put("currentPage",boardPage.getNumber() );//현재 페이지 번호
		pagingResponse.put("totalPages",boardPage.getTotalPages() );//모든 페이지 수
		pagingResponse.put("items",boardPage.getTotalElements() ); //게시판 모든 글 수 (long)
		
		
		return ResponseEntity.ok(pagingResponse);
	}
	
	
	
	
	//게시글 작성
//	@PostMapping
//	public ResponseEntity<?> write(@RequestBody Board req, Authentication auth) {
//		//auth.getname->로그인한 유저 이름
//		SiteUser siteUser = userRepository.findByUsername(auth.getName())
//				.orElseThrow(()->new UsernameNotFoundException("사용자 없음"));
//		//siteuser->현재 로그인한 유저의 레코드
//		Board board = new Board();
//		board.setTitle(req.getTitle()); //유저가 입력한 글제목
//		board.setContent(req.getContent());
//		board.setAuthor(siteUser);
//		boardRepository.save(board);
//		
//		
//		return ResponseEntity.ok(board);
//	}
	@PostMapping //validation적용
	public ResponseEntity<?> write(@Valid @RequestBody BoardDto boardDto, BindingResult bindingResult ,Authentication auth) {
		//사용자 로그인 여부 확인
		if(auth == null) { //참이면 로그인x->글쓰기권한 없음->error코드 반환
			return ResponseEntity.status(401).body("로그인후 작성가능합니다");
		}
		if(bindingResult.hasErrors()) { //참이면 에러
			Map<String, String> errors = new HashMap<>();
			bindingResult.getFieldErrors().forEach(
				err -> {
					errors.put(err.getField(),err.getDefaultMessage());
				}
			);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
		}
		
		//auth.getname->로그인한 유저 이름
		SiteUser siteUser = userRepository.findByUsername(auth.getName())
				.orElseThrow(()->new UsernameNotFoundException("사용자 없음"));
		//siteuser->현재 로그인한 유저의 레코드

		
		Board board = new Board();
		board.setTitle(boardDto.getTitle()); //유저가 입력한 글제목
		board.setContent(boardDto.getContent());
		board.setAuthor(siteUser);
		boardRepository.save(board);
		
		
		return ResponseEntity.ok(board);
	}
	
	//특정 게시글 번호(id)로 조회(글 상세보기) 읽기,select라 getmapping
	@GetMapping("/{id}")
	public ResponseEntity<?> getPost(@PathVariable("id") Long id) {
//		Board board = boardRepository.findById(id)
//				.orElseThrow(()->new EntityNotFoundException("사용자 없음"));
		Optional<Board> _board = boardRepository.findById(id);
		if(_board.isPresent()) { //참이면 글 조회 성공
			return ResponseEntity.ok(_board.get()); //해당 id글 반환
		} else { //거짓이면 해당글 조회 실패
			return ResponseEntity.status(404).body("해당게시글은 존재하지 않습니다");
		}
	}
	
	//특정 id의 글 삭제 (권한설정->로그인후 본인글만 삭제)
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletePost(@PathVariable("id") Long id, Authentication auth) {
		Optional<Board> _board = boardRepository.findById(id);
		
		if(_board.isEmpty()) { //참이면 삭제할글 가져오기
			return ResponseEntity.status(404).body("삭제되지 않았습니다");
		}
		
		if(auth == null || !auth.getName().equals(_board.get().getAuthor().getUsername())) {
			return ResponseEntity.status(403).body("삭제권한이 없습니다");
		}
		boardRepository.delete(_board.get());
		return ResponseEntity.ok("삭제완료"); //해당 id글 반환
		}
	
	
	//게시글 수정 (권한설정->로그인후 본인글만 수정)
	@PutMapping("/{id}")
	public ResponseEntity<?> updatePost(@PathVariable("id") Long id, @RequestBody Board updateBoard, Authentication auth) {
		Optional<Board> _board = boardRepository.findById(id);
		if(_board.isEmpty()) { //참이면 수정할 글 존재하지않음
			return ResponseEntity.status(404).body("해당 게시글이 존재하지 않습니다");
		}
		if(auth == null || !auth.getName().equals(_board.get().getAuthor().getUsername())) {
			return ResponseEntity.status(403).body("수정권한이 없습니다");
		}
		Board oldPost = _board.get(); //기존 게시글
		oldPost.setTitle(updateBoard.getTitle());
		oldPost.setContent(updateBoard.getContent());
		boardRepository.save(oldPost);
		return ResponseEntity.ok(oldPost);
	}
	
}
