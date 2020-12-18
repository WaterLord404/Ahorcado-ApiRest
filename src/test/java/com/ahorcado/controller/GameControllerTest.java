package com.ahorcado.controller;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.ahorcado.model.entity.Game;
import com.ahorcado.model.repository.GameRepository;
import com.ahorcado.services.GameServiceI;

import javassist.NotFoundException;

@WebMvcTest(GameController.class)
class GameControllerTest {

	@Autowired
	private MockMvc mockMVC;

	@MockBean
	private GameServiceI mockedService;

	@MockBean
	private GameRepository mockedRepo;
	
	private final static String ROOT_PATH = "/game";

	@Test
	public void getGamesActiveTest() throws Exception {
		Game game = new Game("test");
		List<Game> games = new ArrayList<>();
		games.add(game);
		
		Mockito.when(mockedService.getGamesActive())
			.thenReturn(new ArrayList<Game>())
			.thenReturn(games);
		
		
		mockMVC.perform(MockMvcRequestBuilders.get(ROOT_PATH))
			.andExpect(status().isNotFound());
		
		mockMVC.perform(MockMvcRequestBuilders.get(ROOT_PATH))
			.andExpect(status().isOk());
	}
	
	@Test
	public void getLogsTest() throws Exception {
		Game game = new Game("test");
		List<Game> games = new ArrayList<>();
		games.add(game);
		
		Mockito.when(mockedService.getLogs())
			.thenReturn(new ArrayList<Game>())
			.thenReturn(games);
		
		
		mockMVC.perform(MockMvcRequestBuilders.get(ROOT_PATH + "/logs"))
			.andExpect(status().isNotFound());
		
		mockMVC.perform(MockMvcRequestBuilders.get(ROOT_PATH + "/logs"))
			.andExpect(status().isOk());
	}
	
	@Test
	public void getGameTest() throws Exception {
		Mockito.when(mockedService.getGame(Mockito.anyLong()))
			.thenReturn(null)
			.thenReturn(new Game());
		
		mockMVC.perform(MockMvcRequestBuilders.get(ROOT_PATH + "/1"))
			.andExpect(status().isNotFound());
		
		mockMVC.perform(MockMvcRequestBuilders.get(ROOT_PATH + "/1"))
			.andExpect(status().isOk());
	}
	
	@Test
	public void addGameTest() throws Exception {
		
		Mockito.when(mockedService.addGame(Mockito.anyString()))
			.thenReturn(new Game())
			.thenReturn(null);
		
		mockMVC.perform(post(ROOT_PATH + "/test")
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(	"{\n" + 
							"    \"id\": 1,\n" + 
							"    \"letters\": \"\",\n" + 
							"    \"hiddenWord\": \"****\",\n" + 
							"    \"mistakes\": 0\n" + 
							"}"))
				.andExpect(status().isCreated());
		
		
		mockMVC.perform(post(ROOT_PATH + "/error error")
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isConflict());
	}
	
	@Test
	public void sendLetterTest() throws Exception {
		
		Mockito.when(mockedService
				.sendLetter(Mockito.anyLong(), Mockito.anyString(), Mockito.anyString()))
			.thenReturn(new Game("TEST"))
			.thenReturn(null);
		
		mockMVC.perform(post(ROOT_PATH + "/" + 1 + "/" + "t")
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(	"{\n" + 
							"    \"id\": 1,\n" + 
							"    \"letters\": \"T\",\n" + 
							"    \"hiddenWord\": \"T**T\",\n" + 
							"    \"mistakes\": 0\n" + 
							"}"))
				.andExpect(status().isCreated());
		
		
		mockMVC.perform(post(ROOT_PATH + "/" + 1 + "/" + "t")
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isConflict());
	}
	
	@Test
	public void sendLetterExceptionTest() throws Exception {
		try {
			Mockito.when(mockedService
					.sendLetter(Mockito.anyLong(), Mockito.anyString(), Mockito.anyString()))
			.thenThrow(new NotFoundException("test"));
			
			mockMVC.perform(post(ROOT_PATH + "/" + 1 + "/" + "t"));
			
		}catch (NotFoundException e) {
			
			mockMVC.perform(post(ROOT_PATH + "/" + 1 + "/" + "t")
					.header("Response", "test"))
					.andExpect(status().isNotFound());
		}
	}
}
