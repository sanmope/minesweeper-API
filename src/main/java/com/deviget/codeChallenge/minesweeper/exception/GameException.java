package com.deviget.codeChallenge.minesweeper.exception;

public class GameException extends RuntimeException{
    
    public GameException(String messageException){
        super(messageException);
    }

    public GameException(String messageException, Throwable exception){
        super(messageException,exception);
    }
    
}
