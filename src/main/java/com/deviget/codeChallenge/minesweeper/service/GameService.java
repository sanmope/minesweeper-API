package com.deviget.codeChallenge.minesweeper.service;

import java.time.LocalDateTime;

import com.deviget.codeChallenge.minesweeper.model.GameResponse;
import com.deviget.codeChallenge.minesweeper.model.GridRequest;
import com.deviget.codeChallenge.minesweeper.model.MarkRequest;
import com.deviget.codeChallenge.minesweeper.model.StepRequest;

public interface GameService {

	/**
	 * Returns a game with a grid for the Active Game persisted asociated with that user
	 * @param userName
	 * @return
	 */
	GameResponse getGame(String userName);

	/**
	 * Creates a new Game persisted in Database,
	 * Given a username, the ammount of mines, the rows of the grid, and the collums of it.
	 * @param request
	 * @return
	 */
	GameResponse createGame( GridRequest request);

	/**
	 * Assign a mark to a cell with a question mark or a red_flag. Receives a row and a collumn.
	 * @param userName
	 * @param markType
	 * @param request
	 * @return
	 */
	GameResponse setMark(String userName, String markType,MarkRequest request);

	/**
	 * Creates a movement for a cell, a Step on it. and returns whether it blowed or won the game.
	 * @param userName
	 * @param request
	 * @return
	 */
	GameResponse stepOn(String userName, StepRequest request);

	/**
	 * Returns the time spent in the game so far.
	 * @param userName
	 * @return
	 */
	Long getTimeTracker(String userName);

}
