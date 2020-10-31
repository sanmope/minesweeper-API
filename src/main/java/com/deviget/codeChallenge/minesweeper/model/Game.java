package com.deviget.codeChallenge.minesweeper.model;

import java.time.LocalDateTime;

import javax.persistence.*;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import lombok.Builder;
import lombok.*;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Game {
    
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long id;

	@Column
	private String userName;

	@Column
	private Integer mines;

	@Column
	@Enumerated(EnumType.STRING)
	private State state;

	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
    private Cell[][] grid;

	@Column
	private LocalDateTime TimeTracker;

	public Game(Cell[][] grid, String userName) {
		this.grid = grid;
		this.userName = userName;
		this.state = State.ACTIVE;
	}

	public void setMines(Integer mines){
		this.mines = mines;
	}

	public int getMines()	{
		return this.mines;
	}
}
