package com.prooph_board.axon.helper;

public record AggregatePair(
        Class<?> command,
        Class<?> event
) {

}
