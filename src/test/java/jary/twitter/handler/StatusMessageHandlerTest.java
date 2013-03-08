package jary.twitter.handler;

import jary.twitter.aggregator.Aggregator;
import jary.twitter.aggregator.HashtagAggregator;
import jary.twitter.aggregator.UserAggregator;
import org.junit.Before;
import org.junit.Test;
import twitter4j.Status;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * tests for class {@link StatusMessageHandler}
 *
 * @author <a href='mailto:jeremy.ary@gmail.com'>jary</a>
 */
public class StatusMessageHandlerTest {


    StatusMessageHandler handler;

    @Before
    public void setUp() {
        this.handler = new StatusMessageHandler();
    }

    @Test
    public void testHandle() {

        Status status = mock(Status.class);

        HashtagAggregator hashtagAggregator = mock(HashtagAggregator.class);
        UserAggregator userAggregator = mock(UserAggregator.class);

        List<Aggregator> aggregators = new ArrayList<Aggregator>();
        aggregators.add(hashtagAggregator);
        aggregators.add(userAggregator);
        handler.setAggregatorList(aggregators);

        handler.handle(status);

        verify(hashtagAggregator).aggregate(status);
        verify(userAggregator).aggregate(status);
    }
}
