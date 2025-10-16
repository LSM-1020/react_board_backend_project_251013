package com.LSM.home.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.LSM.home.dto.CommentDto;
import com.LSM.home.entity.Board;
import com.LSM.home.entity.Comment;
import com.LSM.home.entity.SiteUser;
import com.LSM.home.repository.BoardRepository;
import com.LSM.home.repository.CommentRepository;
import com.LSM.home.repository.UserRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BoardRepository boardRepository;
	
	
	
	//댓글 작성
	@PostMapping("/{boardId}")
	public ResponseEntity<?> writeComment(@PathVariable("boardId") Long boardId,
			@Valid @RequestBody CommentDto commentDto,
			Authentication auth
			) {
		//원 게시글의 존재여부 확인
		Optional<Board> _board = boardRepository.findById(boardId);
		if(_board.isEmpty()) { //참이면 해당 원 게시글 존재하지 않음
			return ResponseEntity.badRequest().body("게시글이 존재하지 않습니다");
		}
		//로그인한 유저의 siteuser객체
		SiteUser user = userRepository.findByUsername(auth.getName()).orElseThrow();		
		Comment comment = new Comment();
		comment.setBoard(_board.get());
		comment.setAuthor(user);
		comment.setContent(commentDto.getContent());
		commentRepository.save(comment); //작성된 comment 엔티티를 db에 삽입		
		return ResponseEntity.ok(comment); //db에 등록된 댓글객체 200응답과 함께 반환
	}
	
	//댓글 조회-> 댓글이 달린 원 게시글의 id가 필요 - >게시글 id로 댓글조회->게시글 id찾기
	@GetMapping("/{boardId}")
	public ResponseEntity<?> getComments(@PathVariable("boardId") Long boardId) {
		Optional<Board> _board = boardRepository.findById(boardId);
		if(_board.isEmpty()) { //참이면 해당 원 게시글 존재하지 않음
			return ResponseEntity.badRequest().body("게시글이 존재하지 않습니다");
		}
		List<Comment> comments = commentRepository.findByBoard(_board.get());
		return ResponseEntity.ok(comments);
	}
	

}
