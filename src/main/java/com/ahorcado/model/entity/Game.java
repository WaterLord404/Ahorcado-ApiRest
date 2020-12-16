package com.ahorcado.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Game implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String letters;
	
	private String hiddenWord;
	
	private String secretWord;
	
	private int mistakes;
	
	private boolean active;
	
	private String address;

	public Game() { }
	
	public Game(String secretWord) {
		super();
		this.secretWord = secretWord;

		this.letters = new String();
		this.hiddenWord = new String();
		this.address = new String();
		this.mistakes = 0;
		this.active = true;
	}
	
	
	/*
	 * Id de la partida
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_GAME")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	
	/*
	 * Letras mencionadas
	 */
	@Column(name = "LETTERS")
	public String getLetters() {
		return letters;
	}
	public void setLetters(String letters) {
		this.letters = letters;
	}

	
	/*
	 * Palabra a resolver, ej: *****
	 */
	@Column(name = "HIDDEN_WORD")
	public String getHiddenWord() {
		return hiddenWord;
	}
	public void setHiddenWord(String hiddenWord) {
		this.hiddenWord = hiddenWord;
	}


	/*
	 * Palabra secreta
	 */
	@JsonIgnore
	@Column(name = "SECRET_WORD", nullable = false)
	public String getSecretWord() {
		return secretWord;
	}
	public void setSecretWord(String secretWord) {
		this.secretWord = secretWord;
	}


	/*
	 * Errores (permitidos 7)
	 */
	@Column(name = "MISTAKES")
	public int getMistakes() {
		return mistakes;
	}
	public void setMistakes(int mistakes) {
		this.mistakes = mistakes;
	}

	
	/*
	 * Estado de la partida
	 */
	@JsonIgnore
	@Column(name = "ACTIVE")
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}

	
	/*
	 * Lista de ips de los jugadores
	 */
	@JsonIgnore
	@Column(name = "ADDRESS")
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
