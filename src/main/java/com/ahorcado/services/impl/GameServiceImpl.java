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
	 * Devuelve todas las partidas activas
	 * 
	 * @return List<Game>
	 */
	@Override
	public List<Game> getGames() {
		return gameRepository.findAllGameByActive(true);
	}

	/**
	 * Devulve una partida activa
	 * 
	 * @param Long idGame
	 * @return Game
	 */
	@Override
	public Game getGame(Long idGame) {
		return gameRepository.findById(idGame).get();
	}

	/**
	 * Crea una nueva partida con una palabra secreta y genera la palabra oculta
	 * 
	 * @param String secretWord
	 * @return Game
	 */
	@Override
	public Game addGame(String secretWord) {
		Game game = new Game(secretWord);

		if (isOnlyAWord(secretWord)) {

			game.setHiddenWord(generateHiddenWord(secretWord));

			return gameRepository.save(game);

		} else {
			return null;
		}
	}

	/**
	 * Envia una letra a una partida
	 * 
	 * @param Long   idGame
	 * @param String letter
	 * @return Game
	 */
	@Override
	public Game sendLetter(Long idGame, String letter) {
		// Obtiene la partida
		Game game = gameRepository.findById(idGame).get();

		// Comprueba si la partida está activa
		if (game.isActive()) {

			// Comprueba si es una letra
			if (isALetter(letter)) {

				// Comprueba si la letra introducida no está repetida && si existe en la palabra
				// secreta
				if (game.getLetters().indexOf(letter.charAt(0)) == -1
						&& existLetterIntoHiddenWord(game.getSecretWord(), letter)) {

					// Añade la letra a la lista de letras
					game.setLetters(game.getLetters() + letter);

					// Dibuja letra en la palabra oculta
					game.setHiddenWord(drawLettersOnHiddenWord(game.getHiddenWord(), game.getSecretWord(), letter));

					// Comprueba si se ha ganado la partida
					if (isTheGameWon(game)) {

						// Desactiva la partida
						game.setActive(false);
					}
					
				} else {
					// Añade un fallo
					game.setMistakes(game.getMistakes() + 1);

					// Comprueba si ha perdido la partida
					if (isTheGameLost(game)) {

						// Desactiva la partida
						game.setActive(false);
					}
				}

				gameRepository.save(game);
			}
		}

		return game;
	}

	/**
	 * Comprueba si la palabra secreta es una palabra
	 * 
	 * @param String secretWord
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
	 * @param String secretWord
	 * @return String
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
	 * @param String letter
	 * @return boolean
	 */
	private boolean isALetter(String letter) {
		if (letter.length() == 1) {

			Character c = letter.charAt(0);

			if (c >= 'A' && c <= 'Z') {
				return true;
			}
		}
		return false;
	}

	/**
	 * Comprueba si se ha perdido la partida
	 * 
	 * @param Game game
	 * @return boolean
	 */
	private boolean isTheGameLost(Game game) {
		if (game.getMistakes() == 7) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Comprueba si se ha ganado la partida
	 *
	 * @param Game game
	 * @return boolean
	 */
	private boolean isTheGameWon(Game game) {
		if (game.getSecretWord().equals(game.getHiddenWord())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Comprueba si existe la letra en la palabra secreta
	 * 
	 * @param secretWord
	 * @param String     letter
	 * @return boolean
	 */
	private boolean existLetterIntoHiddenWord(String secretWord, String letter) {
		if (secretWord.indexOf(letter) != -1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Dibuja la letra en la palabra oculta
	 * 
	 * @param String secretWord
	 * @param String letter
	 * @return String
	 */
	private String drawLettersOnHiddenWord(String hiddenWord, String secretWord, String letter) {
		StringBuilder resul = new StringBuilder();

		// Recorre la palabra secreta
		for (int i = 0; i < secretWord.length(); i++) {
			
			// Comprueba si el caracter en la posicion de i es igual que la letra.
			// Si no son iguales concatena el caracter en la posicion de i de la palabra oculta
			if (secretWord.charAt(i) == letter.charAt(0)) {				
				resul.append(secretWord.charAt(i));

			} else {
				resul.append(hiddenWord.charAt(i));
			}
		}

		return resul.toString();
	}
}
