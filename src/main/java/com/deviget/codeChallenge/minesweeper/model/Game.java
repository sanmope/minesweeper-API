package com.deviget.codeChallenge.minesweeper.model;

import javax.persistence.*;


import lombok.Builder;
import lombok.*;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Game {
    
	@Id
	@GeneratedValue(generator = "id_generator")
	@SequenceGenerator(
			name = "id_generator",
			sequenceName = "id_generator",
			initialValue = 1000
	)
	private long id;

	@Column
	private String userName;

	@Column
	@Enumerated(EnumType.STRING)
	private State state;

	@Column
	private Boolean redFlag;

	@Column
	private Boolean questionMark;

    private Cell[][] mines;

	public Game(Cell[][] mines, String userName) {
		this.mines = mines;
		this.userName = userName;
		this.state = state.ACTIVE;
		this.redFlag = false;
		this.questionMark = false;
	}
}
