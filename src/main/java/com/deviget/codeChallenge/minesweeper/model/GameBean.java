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
	private String userName;
	private boolean redFlag;
    private boolean questionMark;
    private State state;
	private Cell[][] mines;
}