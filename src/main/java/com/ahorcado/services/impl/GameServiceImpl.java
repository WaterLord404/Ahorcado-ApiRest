package com.ahorcado.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ahorcado.model.entity.Game;
import com.ahorcado.model.repository.GameRepository;
import com.ahorcado.services.GameServiceI;

@Service
public class GameServiceImpl implements GameServiceI {

	@Autowired
	private GameRepository gameRepository;

	/**
	 * Devuelve todas las partidas
	 * 
	 * @return List<Game>
	 */
	@Override
	public List<Game> getGames() {
		return gameRepository.findAll();
	}

	/**
	 * Crea una nueva partida con una palabra secreta y genera la palabra oculta
	 * 
	 * @return Game
	 */
	@Override
	public Game addGame(String secretWord) {
		Game game = new Game(secretWord.toUpperCase());

		if (isOnlyAWord(secretWord)) {
			game.setHiddenWord(generateHiddenWord(secretWord));

			return gameRepository.save(game);

		} else {
			return null;
		}
	}

	@Override
	public Game sendLetter(Long idGame, String letter) {
		// Obtiene la partida
		Game game = gameRepository.findById(idGame).get();

		// Comprueba si es una letra
		if (isALetter(letter)) {

			// Comprueba si la letra introducida no está repetida
			if (game.getLetters().indexOf(letter.charAt(0)) == -1) {

				// Añade la letra
				game.setLetters(game.getLetters() + letter + ",");

				gameRepository.save(game);
			}
		}

		return game;
	}

	/**
	 * Comprueba si la palabra secreta es una palabra
	 * 
	 * @param secretWord
	 * @return boolean
	 */
	private boolean isOnlyAWord(String secretWord) {
		if (secretWord.indexOf(" ") == -1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Genera la palabra oculta
	 * 
	 * @param secretWord
	 * @return
	 */
	private String generateHiddenWord(String secretWord) {
		StringBuilder hiddenWord = new StringBuilder();

		for (int i = 0; i < secretWord.length(); i++) {
			hiddenWord.append("*");
		}

		return hiddenWord.toString();
	}

	/**
	 * Comprueba si es una letra
	 * 
	 * @param letter
	 * @return boolean
	 */
	private boolean isALetter(String letter) {
		if (letter.length() == 1) {

			Character c = letter.charAt(0);

			if (c >= 'A' && c <= 'Z' && c != ' ') {
				return true;
			}
		}
		return false;
	}

}
