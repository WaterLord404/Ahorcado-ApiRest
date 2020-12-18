package com.ahorcado.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ahorcado.model.entity.Game;
import com.ahorcado.services.GameServiceI;

import javassist.NotFoundException;

@RestController
@RequestMapping(path = "/game")
@CrossOrigin(origins = "*")

public class GameController {

	@Autowired
	private GameServiceI gameService;

	/**
	 * Devuelve todas las partidas activas
	 * 
	 * @return ResponseEntity
	 */
	@CrossOrigin(origins = "*")
	@GetMapping
	public ResponseEntity<?> getGamesActive() {

		return !gameService.getGamesActive().isEmpty() 
				? ResponseEntity.ok(gameService.getGamesActive())
				: ResponseEntity.notFound().build();
	}
	
	/**
	 * Devuelve los logs
	 * 
	 * @return ResponseEntity
	 */
	@CrossOrigin(origins = "http://localhost:8080") // Solo accesible desde la ip local
	@GetMapping(path = "/logs")
	public ResponseEntity<?> getLogs() {
		
		return !gameService.getLogs().isEmpty() 
				? ResponseEntity.ok(gameService.getLogs().toString())
				: ResponseEntity.notFound().build();
	}

	/**
	 * Devuelve la informacion de una partida
	 * 
	 * @param idGame
	 * @return ResponseEntity
	 */
	@CrossOrigin(origins = "*")
	@GetMapping(path = "/{idGame}")
	public ResponseEntity<?> getGame(@PathVariable Long idGame) {

		return gameService.getGame(idGame) != null 
				? ResponseEntity.ok(gameService.getGame(idGame))
				: ResponseEntity.notFound().build();
	}

	/**
	 * Crea una partida
	 * 
	 * @param secretWord
	 * @return ResponseEntity
	 */
	@CrossOrigin(origins = "http://localhost:8080") // Solo accesible desde la ip local
	@PostMapping(path = "/{secretWord}")
	public ResponseEntity<?> addGame(@PathVariable String secretWord) {

		Game game = gameService.addGame(secretWord);

		return game != null 
				? ResponseEntity.status(HttpStatus.CREATED).body(game)
				: ResponseEntity.status(HttpStatus.CONFLICT).body(game);
	}

	/**
	 * Envia una letra a una partida
	 * 
	 * @param idGame
	 * @param letter
	 * @param request
	 * @return ResponseEntity
	 * @throws NotFoundException
	 */
	@CrossOrigin(origins = "*")
	@PostMapping(path = "/{idGame}/{letter}")
	public ResponseEntity<?> sendLetter(@PathVariable Long idGame, @PathVariable String letter,
			HttpServletRequest requestClient) throws NotFoundException {

		try {
			String request = requestClient.getLocalAddr().toString();

			Game game = gameService.sendLetter(idGame, letter, request);

			return game != null 
					? ResponseEntity.status(HttpStatus.CREATED).body(game)
					: ResponseEntity.status(HttpStatus.CONFLICT).body(game);
					
		} catch (NotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.header("Response", e.getMessage()).build();
		}

	}
}
