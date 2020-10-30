package com.deviget.codeChallenge.minesweeper.model;

import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StepRequest {
    
    @NotNull
	private int column;

	@NotNull
	private int row;
}
