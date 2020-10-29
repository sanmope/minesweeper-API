package com.deviget.codeChallenge.minesweeper.service;

import javax.validation.Valid;

import com.deviget.codeChallenge.minesweeper.model.GameResponse;
import com.deviget.codeChallenge.minesweeper.model.GridRequest;

public interface GameService {

    GameResponse getGame(String userName);

	GameResponse createGame( GridRequest request);

}
