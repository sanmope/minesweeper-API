package com.deviget.codeChallenge.minesweeper.service;

import javax.validation.Valid;

import com.deviget.codeChallenge.minesweeper.model.GameResponse;
import com.deviget.codeChallenge.minesweeper.model.GridRequest;
import com.deviget.codeChallenge.minesweeper.model.MarkRequest;

public interface GameService {

    GameResponse getGame(String userName);

	GameResponse createGame( GridRequest request);

	GameResponse setMark(String userName, String markType,MarkRequest request);

}
