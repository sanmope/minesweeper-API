package com.deviget.codeChallenge.minesweeper.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class Grid {
    
    @NotEmpty
	private String name;

	@NotNull
	@Min(1)
    @Max(841) //this number comes out from multiplying the rows with the collumns (rows -1)(columns -1)
    private int mines;

    @NotNull
	@Min(2)
	@Max(30)
    private int rows;
    
    @NotNull
	@Min(2)
	@Max(30)
	private int columns;

}