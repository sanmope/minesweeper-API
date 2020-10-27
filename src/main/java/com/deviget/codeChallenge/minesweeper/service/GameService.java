package com.deviget.codeChallenge.minesweeper.service;

import javax.validation.Valid;

import com.deviget.codeChallenge.minesweeper.model.GameBean;
import com.deviget.codeChallenge.minesweeper.model.GridRequest;

public interface GameService {

    GameBean getGame(String userName);

	GameBean createGame( GridRequest request);

}
