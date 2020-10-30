package com.deviget.codeChallenge.minesweeper.model;

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
	@Enumerated(EnumType.STRING)
	private State state;

	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
    private Cell[][] grid;

	public Game(Cell[][] grid, String userName) {
		this.grid = grid;
		this.userName = userName;
		this.state = State.ACTIVE;
	}
}
