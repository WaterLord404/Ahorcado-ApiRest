package com.ahorcado.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ahorcado.model.entity.Game;

@Repository
public interface GameRepository extends JpaRepository<Game, Long>{

	public List<Game> findAllGameByActive(boolean active);
	
}
