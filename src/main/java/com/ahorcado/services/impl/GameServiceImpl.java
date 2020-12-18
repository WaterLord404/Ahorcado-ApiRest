package com.ahorcado.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ahorcado.model.entity.Game;
import com.ahorcado.model.repository.GameRepository;
import com.ahorcado.services.GameServiceI;

import javassist.NotFoundException;

@Service
public class GameServiceImpl implements GameServiceI {

	@Autowired
	private GameRepository gameRepository;

	public GameServiceImpl(GameRepository gameRepository) {
		this.gameRepository = gameRepository;
	}

	/**
	 * Devuelve todas las partidas activas
	 * 
	 * @return List<Game>
	 */
	@Override
	public List<Game> getGamesActive() {
		return gameRepository.findAllGamesByActive(true);
	}

	/**
	 * Devuelve todas las partidas
	 * 
	 * @return List<Game>
	 */
	@Override
	public List<Game> getLogs() {
		return gameRepository.findAll();
	}

	/**
	 * Devulve una partida activa
	 * 
	 * @param Long idGame
	 * @return Game
	 */
	@Override
	public Game getGame(Long idGame) {
		return gameRepository.findGameById(idGame);
	}

	/**
	 * Crea una nueva partida con una palabra secreta y genera la palabra oculta
	 * 
	 * @param String secretWord
	 * @return Game
	 */
	@Override
	public Game addGame(String secretWord) {

		// Inicializa
		Game game = null;
		secretWord = secretWord.toUpperCase();

		// Comprueba si es una palabra y si solo contiene caracteres
		if (isOnlyAWord(secretWord) && isNotOnlyCharacters(secretWord)) {

			// Crea la partida
			game = new Game(secretWord);

			// Genera la palabra oculta
			game.setHiddenWord(generateHiddenWord(secretWord));

			// Persiste la partida en BD
			gameRepository.save(game);
		}

		return game;
	}

	/**
	 * Envia una letra a una partida
	 * 
	 * @param Long   idGame
	 * @param String letter
	 * @return Game
	 * @throws NotFoundException
	 */
	@Override
	public Game sendLetter(Long idGame, String letter, String request) throws NotFoundException {

		// Cambia la letra a mayusculas
		letter = letter.toUpperCase();

		// Obtiene la partida
		Game game = gameRepository.findGameById(idGame);

		//// EXCEPTIONS && VALIDATOR ////
		// Comprueba si la partida existe,
		// si la partida está activa,
		// si esa ip ha jugado,
		// si no es una letra y si no es 1 sola letra
		if (game == null) {
			throw new NotFoundException("Partida no encontrada");

		} else if (!game.isActive()) {
			throw new NotFoundException("Partida no activa");

		} else if (ipAlreadyPlayed(game, request)) {
			throw new NotFoundException("No puedes enviar mas de una letra a la partida");
			
		} else if (!isNotOnlyCharacters(letter) || letter.length() != 1) {
			game = null;			

			
		//// LOGIC ////
		// Comprueba si la letra introducida no está repetida && si existe en la palabra
		} else if (game.getLetters().indexOf(letter.charAt(0)) == -1
				&& existLetterIntoHiddenWord(game.getSecretWord(), letter)) {

			// Dibuja letra en la palabra oculta
			game.setHiddenWord(
					drawLettersOnHiddenWord(game.getHiddenWord(), game.getSecretWord(), letter));
			
		} else {
			// Añade un fallo
			game.setMistakes(game.getMistakes() + 1);
		}

		//// SAVE ////
		// Comprueba si no es nula la partida
		if (game != null) {

			// Guarda la ip
			game.setAddress(game.getAddress() + request + "-");

			// Comprueba si ha ganado o perdido la partida
			if (isTheGameLost(game) || isTheGameWon(game)) {
				
				// Desactiva la partida
				game.setActive(false);
				
			// Comprueba si no existe la letra en la lista de letras, si es así la añade a
			// la lista
			} else if (notExistLetterIntoLetters(game.getLetters(), letter)) {

				// Añade la letra a la lista de letras
				game.setLetters(game.getLetters() + letter);
			}

			// Persiste la partida en BD
			gameRepository.save(game);
		}

		return game;
	}

	/**
	 * Comprueba si ya ha jugado esa ip
	 * 
	 * @param game
	 * @param request
	 * @return boolean
	 */
	protected boolean ipAlreadyPlayed(Game game, String request) {
		return game.getAddress().indexOf(request) != -1;
	}

	/**
	 * Comprueba si existe la letra en la lista de letras
	 * 
	 * @param letters
	 * @param letter
	 * @return boolean
	 */
	private boolean notExistLetterIntoLetters(String letters, String letter) {
		return letters.indexOf(letter.charAt(0)) == -1;
	}

	/**
	 * Comprueba si es solo una palabra
	 * 
	 * @param String secretWord
	 * @return boolean
	 */
	private boolean isOnlyAWord(String secretWord) {
		return secretWord.indexOf(" ") == -1;
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
	 * Comprueba si la palabra está compuesta solo por letras
	 * 
	 * @param String letters
	 * @return boolean
	 */
	private boolean isNotOnlyCharacters(String word) {
		boolean resul = true;

		for (int i = 0; i < word.length(); i++) {

			Character c = word.charAt(i);

			if (!(c >= 'A' && c <= 'Z' || c == 'Ñ')) {
				resul = false;
			}
		}
		return resul;
	}

	/**
	 * Comprueba si se ha perdido la partida
	 * 
	 * @param Game game
	 * @return boolean
	 */
	private boolean isTheGameLost(Game game) {
		return game.getMistakes() == 7;
	}

	/**
	 * Comprueba si se ha ganado la partida
	 *
	 * @param Game game
	 * @return boolean
	 */
	private boolean isTheGameWon(Game game) {
		return game.getSecretWord().equals(game.getHiddenWord());
	}

	/**
	 * Comprueba si existe la letra en la palabra secreta
	 * 
	 * @param secretWord
	 * @param String     letter
	 * @return boolean
	 */
	private boolean existLetterIntoHiddenWord(String secretWord, String letter) {
		return secretWord.indexOf(letter) != -1;
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

			// Comprueba si el caracter en la posicion de i de la palabra secreta es igual
			// que la letra.
			if (secretWord.charAt(i) == letter.charAt(0)) {
				// Revela la letra
				resul.append(secretWord.charAt(i));

			} else {
				// Concatena un *
				resul.append(hiddenWord.charAt(i));
			}
		}

		return resul.toString();
	}

}
