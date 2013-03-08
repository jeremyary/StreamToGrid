package jary.twitter;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import twitter4j.QueryResult;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * tests for class {@link Client}
 *
 * @author <a href='mailto:jeremy.ary@gmail.com'>jary</a>
 */
public class ClientTest {


    Client client;

    @Before
    public void setUp() {
        client = new Client();
    }

    @Ignore //client search still work in progress
    @Test
    public void testSearch() throws TwitterException {

        QueryResult result = mock(QueryResult.class);
        when(result.getTweets()).thenReturn(null);

        TwitterFactory twitterFactory = new TwitterFactory();
        client.setTwitterFactory(twitterFactory);

        client.search("foo");
    }
}