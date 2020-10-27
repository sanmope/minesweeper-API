package com.deviget.codeChallenge.minesweeper.repository;

import java.util.Optional;

import com.deviget.codeChallenge.minesweeper.model.Game;
import com.deviget.codeChallenge.minesweeper.model.GameStates;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game,Long>{

	Optional<Game> findGameByUserNameAndState(String userName, GameStates State);
}
