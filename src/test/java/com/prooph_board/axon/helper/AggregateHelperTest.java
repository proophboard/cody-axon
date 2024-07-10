package com.prooph_board.axon.helper;

import com.test.api.CardIssuedEvent;
import com.test.api.CardRedeemedEvent;
import com.test.api.IssueCardCommand;
import com.test.api.RedeemCardCommand;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AggregateHelperTest {

    @Test
    void testCreate() {
        assertDoesNotThrow(() -> AggregateHelper.create(
                "com.test",
                "Giftcard",
                new AggregatePair(IssueCardCommand.class, CardIssuedEvent.class),
                List.of(new AggregatePair(RedeemCardCommand.class, CardRedeemedEvent.class))
        ));
    }
}
