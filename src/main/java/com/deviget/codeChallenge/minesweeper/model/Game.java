package com.deviget.codeChallenge.minesweeper.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

import org.hibernate.annotations.TypeDef;

import lombok.Builder;
import lombok.*;

@Data
@Entity
//@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
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
	private GameStates state;

	//@Type(type = "jsonb")
	//@Column(columnDefinition = "jsonb")
	//private Cell[][] mines;

	@Column
	private Boolean redFlag;

	@Column
	private Boolean questionMark;

    private Cell[][] mines;

	public Game(Cell[][] mines, String userName) {
		this.mines = mines;
		this.userName = userName;
		this.state = GameStates.ACTIVE;
		this.redFlag = false;
		this.questionMark = false;
	}
}