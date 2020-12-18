package com.ahorcado.services;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.ahorcado.model.entity.Game;
import com.ahorcado.model.repository.GameRepository;
import com.ahorcado.services.impl.GameServiceImpl;

import javassist.NotFoundException;

public class GameServiceTest {

	private GameServiceImpl sut;

	private GameRepository mockedRepo;

	private Game mockedGame;

	@BeforeEach
	private void init() {
		mockedRepo = mock(GameRepository.class);
		mockedGame = mock(Game.class);

		sut = new GameServiceImpl(mockedRepo);
	}

	@Test
	public void getGamesTest() {
		when(mockedRepo.findAllGamesByActive(Mockito.anyBoolean())).thenReturn(new ArrayList<>());

		assert (sut.getGames().isEmpty());
	}

	@Test
	public void getGameTest() {
		when(mockedRepo.findGameById(Mockito.anyLong())).thenReturn(mockedGame);

		assert (sut.getGame((long) 1) != null);
	}

	@Test
	public void addGameTest() {
		assert (sut.addGame("test").isActive());
		assert (sut.addGame("Error error") == null);
		assert (sut.addGame("3rr0r") == null);
	}

	@Test
	public void sendLetterTest() throws NotFoundException {
		Game game = new Game("test");
		
		when(mockedRepo.findGameById(Mockito.anyLong())).thenReturn(game);

		try {
			
			sut.sendLetter(game.getId(), "a", "192.168.0.1");
			
			assert(false);
		} catch (NotFoundException e) {
			assert(true);
		}

		game.setId((long) 1);
		assert(sut.sendLetter(game.getId(), "a", "192.168.0.1") != null);

		game.setActive(true);

		assert(sut.sendLetter(game.getId(), "t", "192.168.0.2")
				.getLetters().equals("AT"));
		// TES PARAMETRIZADO
	}
}
