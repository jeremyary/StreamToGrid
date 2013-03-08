package jary.twitter.listener;

import jary.amqp.AmqpMediator;
import jary.annotation.Slf4j;
import jary.datagrid.DataGridMediator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

import java.util.Map;

/**
 * test class utilizing {@link StreamStatusListener} to exercise some basic end-to-end functionality for
 * reading stream into data grid maps
 *
 * @author <a href='mailto:jeremy.ary@gmail.com'>jary</a>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring.xml", "classpath:spring-properties.xml"})
public class StreamStatusListenerIntegrationTest {

    @Slf4j
    private Logger log;

    /**
     * custom listener that contains counts of each message type and passes on to handlers
     */
    @Autowired
    StreamStatusListener streamStatusListener;

    /**
     * connectivity and lifecycle manager for hazelcast cluster
     */
    @Autowired
    DataGridMediator dataGridMediator;

    /**
     * amqp queue manager and admin access layer
     */
    @Autowired
    AmqpMediator amqpMediator;

    /**
     * ensure we're working with empty queues and maps
     */
    @Before
    public void setUp() {
        amqpMediator.flush();
        dataGridMediator.flush();
    }

    /**
     * open twitter sample stream for period of time, which queues the messages, then
     * consumed by processes that should categorize them to hazelcast maps by
     * hashtag and by username
     *
     * @throws Exception
     */
    @Test
    public void testStreamListener() throws Exception {

        TwitterStream stream = new TwitterStreamFactory().getInstance();

        try {
            // put our listener on stream that will drive action
            stream.addListener(streamStatusListener);

            // start taking in data from stream
            stream.sample();

            // allow some time for collection on stream thread
            Thread.sleep(30000);

            stream.shutdown();

            // give the queues time to catch up, large amounts can cause some lag on the async
            // queue-receiving threads adding to our maps
            Thread.sleep(5000);

            // make sure we actually got data into our aggregator maps
            Assert.assertNotNull(dataGridMediator.getHashtagMap());
            assert dataGridMediator.getHashtagMap().size() > 0;

            // user map, too
            Assert.assertNotNull(dataGridMediator.getUserMap());
            assert dataGridMediator.getUserMap().size() > 0;

            report(streamStatusListener, dataGridMediator);

        } catch (Exception exception) {
            log.error(exception.getMessage());
        }
    }

    /**
     * output something useful for comparison against data grid instances
     *
     * @param listener stream listener provides message count
     * @param mediator contains data grid instances for count reads
     */
    private void report(StreamStatusListener listener, DataGridMediator mediator) {

        log.info(" ================= REPORT ============= ");
        for (Map.Entry<MessageType, Integer> entry : listener.getMessageCounts().entrySet()) {
            log.info("PROCESSED {} TYPE {} MESSAGES", entry.getValue(), entry.getKey());
        }

        log.info("namespace {}: {}", mediator.getHashtagMap().getName(),
                mediator.getHashtagMap().size());

        log.info("namespace {}: {}", mediator.getUserMap().getName(),
                mediator.getUserMap().size());

        log.info("namespace {}: {}", mediator.getTrendMap().getName(),
                mediator.getTrendMap().size());
    }
}