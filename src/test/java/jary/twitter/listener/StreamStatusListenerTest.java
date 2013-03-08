package jary.twitter.listener;

import jary.twitter.model.ScrubRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.amqp.core.AmqpTemplate;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * tests for class {@link StreamStatusListener}
 *
 * @author <a href='mailto:jeremy.ary@gmail.com'>jary</a>
 */
public class StreamStatusListenerTest {

    StreamStatusListener listener;

    @Before
    public void setUp() {
        listener = new StreamStatusListener();
    }

    @Test
    public void testOnStatus() {

        listener.setStatusQueue("foo");

        Status status = mock(Status.class);

        AmqpTemplate amqpTemplate = mock(AmqpTemplate.class);
        listener.setAmqpTemplate(amqpTemplate);

        Map<MessageType, Integer> messageCounts = new HashMap<MessageType, Integer>();
        listener.setMessageCounts(messageCounts);

        listener.onStatus(status);

        verify(amqpTemplate).convertAndSend("foo", status);
        assert messageCounts.size() == 1;
    }

    @Test
    public void testOnDeletionNotice() {

        listener.setDeleteQueue("foo");

        StatusDeletionNotice notice = mock(StatusDeletionNotice.class);

        AmqpTemplate amqpTemplate = mock(AmqpTemplate.class);
        listener.setAmqpTemplate(amqpTemplate);

        Map<MessageType, Integer> messageCounts = new HashMap<MessageType, Integer>();
        listener.setMessageCounts(messageCounts);

        listener.onDeletionNotice(notice);

        verify(amqpTemplate).convertAndSend("foo", notice);
        assert messageCounts.size() == 1;
    }

    @Test
    public void testOnTrackLimitationNotice() {

        int numberOfLimitedStatuses = 1;

        Map<MessageType, Integer> messageCounts = new HashMap<MessageType, Integer>();
        listener.setMessageCounts(messageCounts);

        listener.onTrackLimitationNotice(numberOfLimitedStatuses);

        assert messageCounts.size() == 1;
    }

    @Test
    public void testOnScrubGeo() {

        listener.setScrubQueue("foo");

        long userId = 1L;
        long upToStatusId = 2L;

        AmqpTemplate amqpTemplate = mock(AmqpTemplate.class);
        listener.setAmqpTemplate(amqpTemplate);

        Map<MessageType, Integer> messageCounts = new HashMap<MessageType, Integer>();
        listener.setMessageCounts(messageCounts);

        listener.onScrubGeo(userId, upToStatusId);

        verify(amqpTemplate).convertAndSend(eq("foo"), any(ScrubRequest.class));
        assert messageCounts.size() == 1;
    }
}