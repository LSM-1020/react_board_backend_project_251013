package com.LSM.home.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
	
	@ManyToOne //한명이 여러개의 코멘트 작성 가능
	private SiteUser author;
	
	//댓글이 달릴 원글 id
	@ManyToOne
	private Board board;
	
	
	
}
