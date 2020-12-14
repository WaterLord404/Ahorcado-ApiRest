package com.ahorcado.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ahorcado.services.GameServiceI;

@RestController
@RequestMapping(path = "/game")
public class GameController {

	@Autowired
	private GameServiceI gameService;

	@GetMapping
	public ResponseEntity<?> getGames() {
		return ResponseEntity.ok(gameService.getGames());
	}
	
	@GetMapping(path = "/{idGame}")
	public ResponseEntity<?> getGame(@PathVariable Long idGame) {
		return ResponseEntity.ok(gameService.getGame(idGame));
	}

	@PostMapping
	public ResponseEntity<?> addGame(@RequestBody String secretWord) {
		return ResponseEntity.ok(gameService.addGame(secretWord.toUpperCase()));
	}

	@PostMapping(path = "/{idGame}/{letter}")
	public ResponseEntity<?> sendLetter(@PathVariable Long idGame, @PathVariable String letter) {
		return ResponseEntity.ok(gameService.sendLetter(idGame, letter.toUpperCase()));
	}
}
