package com.deviget.codeChallenge.minesweeper.model;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class MarkRequest {

    @NotNull
    private int row;

    @NotNull
    private int collumn;
}

