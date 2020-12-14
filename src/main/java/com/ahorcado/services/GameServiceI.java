package com.ahorcado.services;

import java.util.List;

import com.ahorcado.model.entity.Game;

public interface GameServiceI {

	public List<Game> getGames();
	
	public Game addGame(String secretWord);
	
	public Game sendLetter(Long idGame, String letter);
}
