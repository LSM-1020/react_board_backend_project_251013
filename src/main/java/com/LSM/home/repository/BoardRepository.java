package com.LSM.home.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.LSM.home.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {

}
