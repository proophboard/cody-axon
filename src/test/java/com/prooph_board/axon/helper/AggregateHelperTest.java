package com.prooph_board.axon.helper;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class AggregateHelperTest {

    @Test
    void testCreate() {
        assertDoesNotThrow(() -> AggregateHelper.create("com.test", "Giftcard"));
    }
}
