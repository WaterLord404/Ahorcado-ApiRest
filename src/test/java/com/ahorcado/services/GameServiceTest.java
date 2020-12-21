package com.ahorcado.services;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import com.ahorcado.model.entity.Game;
import com.ahorcado.model.repository.GameRepository;
import com.ahorcado.services.impl.GameServiceImpl;

import javassist.NotFoundException;

class GameServiceTest {

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

		assert (sut.getGamesActive().isEmpty());
	}

	@Test
	public void getLogsTest() {
		when(mockedRepo.findAll()).thenReturn(new ArrayList<>());

		assert (sut.getLogs().isEmpty());
	}
	
	@Test
	public void getGameTest() {
		when(mockedRepo.findGameById(Mockito.anyLong())).thenReturn(mockedGame);

		assert (sut.getGame((long) 1) != null);
	}

	
	@Test
	public void addGameTest() {
		assertEquals (sut.addGame("test").getHiddenWord(), "****");
		assert (sut.addGame("Error error") == null);
		assert (sut.addGame("3rr0r") == null);
	}

	
	@ParameterizedTest
	@MethodSource("sendLetterDataTest")
	public void sendLetterTest(Game game, String letter, String request, String resultLetters) throws NotFoundException {
		when(mockedRepo.findGameById(Mockito.anyLong())).thenReturn(game);

		sut.sendLetter(game.getId(), letter, request);
		
		assert(game.getLetters().equals(resultLetters));
	}
	
	private static Stream<Arguments> sendLetterDataTest() {
		Game game = new Game("test");
		game.setId((long) 1);
		
		return Stream.of(
				Arguments.of(game, "t", "192.1", "T"), 		// ok
				Arguments.of(game, "t", "192.2", "T"),		// same lettr err
				Arguments.of(game, "e", "192.3", "TE"), 	// ok
				Arguments.of(game, "st", "192.4", "TE"),	// not word err
				Arguments.of(game, "2", "192.5", "TE"),		// not lettr err
				Arguments.of(game, "s", "192.6", "TES")		// ok
				);
	}
	
	
	@Test
	public void generatedWordTest() throws NotFoundException {
		Game game = new Game("TEST");
		game.setId((long) 1);
		game.setHiddenWord("****");
		
		when(mockedRepo.findGameById(Mockito.anyLong())).thenReturn(game);

		assert(sut.sendLetter(game.getId(), "t", "192").getHiddenWord().equals("T**T"));
	}
	
	
	@Test
	public void ipAlreadyPlayedExceptionTest(){
		try {
			when(mockedRepo.findGameById(Mockito.anyLong())).thenReturn(mockedGame);
			when(mockedGame.getId()).thenReturn((long) 1);
			when(mockedGame.getAddress()).thenReturn("192");
			when(mockedGame.isActive()).thenReturn(true);
			
			sut.sendLetter(mockedGame.getId(), "t", "192");
			assert(false);
			
		}catch (Exception e) {
			assertEquals(e.getMessage(), "No puedes enviar mas de una letra a la partida");
		}
	}
	
	
	@Test
	public void gameNotFoundTest(){
		try {
			sut.sendLetter((long)1, "test", "192");
			assert(false);
			
		}catch (Exception e) {
			assertEquals(e.getMessage(), "Partida no encontrada");
		}
	}
	
	
	@Test
	public void gameNotActiveTest(){
		try {
			when(mockedRepo.findGameById(Mockito.anyLong())).thenReturn(mockedGame);
			sut.sendLetter((long)1, "test", "192");
			assert(false);
			
		}catch (Exception e) {
			assertEquals(e.getMessage(), "Partida no activa");
		}
	}

}
