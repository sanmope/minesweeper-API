package com.deviget.codeChallenge.minesweeper.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameBean {
	private String name;
	private boolean redFlag;
    private boolean questionMark;
    private GameStates state;
	private Cell[][] mines;
}