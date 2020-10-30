package com.deviget.codeChallenge.minesweeper.service;

import com.deviget.codeChallenge.minesweeper.model.GameResponse;
import com.deviget.codeChallenge.minesweeper.model.GridRequest;
import com.deviget.codeChallenge.minesweeper.model.MarkRequest;
import com.deviget.codeChallenge.minesweeper.model.StepRequest;

public interface GameService {

    GameResponse getGame(String userName);

	GameResponse createGame( GridRequest request);

	GameResponse setMark(String userName, String markType,MarkRequest request);

	GameResponse stepOn(String userName, StepRequest request);

}
