package com.deviget.codeChallenge.minesweeper.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameResponse {
	private String userName;
	private State state;
	private Cell[][] grid;
}