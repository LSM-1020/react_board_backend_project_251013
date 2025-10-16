package com.LSM.home.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Comment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //자동 증가
	private Long id;
	
	@Column(nullable = false, length = 500)
	private String content; //댓글 내용
	
	@CreationTimestamp
	private LocalDateTime createDate; //댓글 입력 날짜 시간
	
	@ManyToOne(fetch = FetchType.LAZY) //한명이 여러개의 코멘트 작성 가능, 불필요한 join방지, many to one일때는 항상 명시
	@JoinColumn(name = "author_id") //db에서 자동으로 author_id로 되긴하는데 명시해주면 join되는 테이블이 명시해준 외래키 이름으로 바뀜
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private SiteUser author;
	
	//댓글이 달릴 원글 id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "board_id")
	@JsonIgnore
	private Board board;
	
	
	
}
