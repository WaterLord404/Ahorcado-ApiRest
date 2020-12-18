package com.ahorcado.services;

import java.util.List;

import com.ahorcado.model.entity.Game;

import javassist.NotFoundException;

public interface GameServiceI {

	/**
	 * Devuelve todas las partidas activas
	 * 
	 * @return List<Game>
	 */
	public List<Game> getGames();
	
	/**
	 * Devulve una partida activa
	 * 
	 * @param Long idGame
	 * @return Game
	 */
	public Game getGame(Long idGame);
	
	/**
	 * Crea una nueva partida con una palabra secreta y genera la palabra oculta
	 * 
	 * @param String secretWord
	 * @return Game
	 */
	public Game addGame(String secretWord);
	
	/**
	 * Envia una letra a una partida
	 * 
	 * @param Long idGame
	 * @param String letter
	 * @return Game
	 * @throws NotFoundException 
	 */
	public Game sendLetter(Long idGame, String letter, String request) throws NotFoundException;
}
