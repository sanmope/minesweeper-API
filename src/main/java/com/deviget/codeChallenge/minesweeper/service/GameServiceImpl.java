package com.deviget.codeChallenge.minesweeper.service;

import java.util.Optional;

import com.deviget.codeChallenge.minesweeper.model.Game;
import com.deviget.codeChallenge.minesweeper.model.GameBean;
import com.deviget.codeChallenge.minesweeper.model.GameStates;
import com.deviget.codeChallenge.minesweeper.repository.GameRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.modelmapper.ModelMapper;

@Service
public class GameServiceImpl implements GameService{
    
    @Autowired
    private GameRepository gameRepository;   
    @Autowired
	private ModelMapper modelMapper; 
    
    
    @Override
    public GameBean getGame(String userName){
        
        Optional<Game> game = gameRepository.findGameByUserNameAndState(userName, GameStates.ACTIVE);
        return game.map(gameMap -> modelMapper.map(gameMap,GameBean.class)).get();
        
    }

}
