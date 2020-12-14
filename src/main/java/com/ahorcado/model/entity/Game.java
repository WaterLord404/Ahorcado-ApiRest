package com.ahorcado.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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

	public Game() { }
	
	public Game(String secretWord) {
		super();
		this.secretWord = secretWord;

		this.letters = new String();
		this.hiddenWord = new String();
		this.mistakes = 0;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_GAME")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	
	@Column(name = "LETTERS")
	public String getLetters() {
		return letters;
	}
	public void setLetters(String letters) {
		this.letters = letters;
	}

	
	@Column(name = "HIDDEN_WORD")
	public String getHiddenWord() {
		return hiddenWord;
	}
	public void setHiddenWord(String hiddenWord) {
		this.hiddenWord = hiddenWord;
	}

	
	@Column(name = "SECRET_WORD", nullable = false)
	public String getSecretWord() {
		return secretWord;
	}
	public void setSecretWord(String secretWord) {
		this.secretWord = secretWord;
	}

	
	@Column(name = "MISTAKES")
	public int getMistakes() {
		return mistakes;
	}
	public void setMistakes(int mistakes) {
		this.mistakes = mistakes;
	}

	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
