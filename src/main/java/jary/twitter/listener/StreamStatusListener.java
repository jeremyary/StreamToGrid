package jary.twitter.listener;

import jary.annotation.Slf4j;
import jary.twitter.exception.StallException;
import jary.twitter.model.ScrubRequest;
import org.slf4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;

import java.util.HashMap;
import java.util.Map;

/**
 * listener class for dealing with various message types rcv'd from streaming API thread
 *
 * @author <a href='mailto:jeremy.ary@gmail.com'>jary</a>
 */
@Component
public class StreamStatusListener implements StatusListener {

    @Slf4j
    private Logger log;

    /** template for send/receive to rabbit queues */
    @Autowired
    private AmqpTemplate amqpTemplate;

    /** status message queue where we'll shove stuff to keep from lagging on the API end */
    @Value("${rabbit.queues.twitter.stream.status}")
    private String statusQueue;

    /** delete message queue */
    @Value("${rabbit.queues.twitter.stream.delete}")
    private String deleteQueue;

    /** geo information scrum message queue */
    @Value("${rabbit.queues.twitter.stream.scrub}")
    private String scrubQueue;

    /** track the number of messages passed on during sample */
    private Map<MessageType, Integer> messageCounts = new HashMap<MessageType, Integer>();
    
    /**
     * queue status received from stream
     *
     * @param status tweet from stream
     */
    @Override
    public void onStatus(Status status) {
        incrementCount(MessageType.STATUS);
        amqpTemplate.convertAndSend(statusQueue, status);
    }

    /**
     * queue deletion notice to process removal from grid (if we have it)
     *
     * @param statusDeletionNotice deletion notice from stream
     */
    @Override
    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
        incrementCount(MessageType.DELETE);
        amqpTemplate.convertAndSend(deleteQueue, statusDeletionNotice);
    }

    /**
     * we've missed out on X number of tweets regarding something tracked, so just need to note what's been missed
     *
     * @param numberOfLimitedStatuses number of tweets we missed due to non-firehose sampling (not 100% of all tweets)
     */
    @Override
    public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
        incrementCount(MessageType.TRACK);
        log.info("Got track limitation notice:" + numberOfLimitedStatuses);
    }

    /**
     * request to remove any geocode info we have on a group of statuses for a user
     *
     * @param userId twitter user id
     * @param upToStatusId indicates which messages to scrub
     */
    @Override
    public void onScrubGeo(long userId, long upToStatusId) {
        incrementCount(MessageType.SCRUB);
        amqpTemplate.convertAndSend(scrubQueue, new ScrubRequest(userId, upToStatusId));
    }

    /**
     * uh oh, we're moving too slow for twitter, make sure we adjust before we're disconnected
     *
     * @param warning stall warning from stream
     */
    @Override
    public void onStallWarning(StallWarning warning) {
        incrementCount(MessageType.STALL);
        throw new StallException("STALL WARNING: " + warning);
    }

    /**
     * something went horribly, horribly long. Don't look...it's terrible. Shh, trust me.
     *
     * @param ex exception to handle
     */
    @Override
    public void onException(Exception ex) {
        log.error(ex.getMessage());
    }

    /**
     * increment count of received message in map of specific {@link MessageType}
     *
     * @param type MessageType whose count to increment
     */
    private void incrementCount(MessageType type) {
        if (!messageCounts.containsKey(type)) {
            messageCounts.put(type, 1);
        } else {
            messageCounts.put(type, messageCounts.get(type) + 1);
        }
    }

    /**
     * getter for messageCounts to expose for test reporting
     *
     * @return map of message type counts rec'd
     */
    public Map<MessageType, Integer> getMessageCounts() {
        return messageCounts;
    }

    /**
     * setter for amqpTemplate for testing purposes
     *
     * @param amqpTemplate
     */
    public void setAmqpTemplate(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    /**
     * setter for status queue for testing purposes
     *
     * @param statusQueue queue name
     */
    public void setStatusQueue(String statusQueue) {
        this.statusQueue = statusQueue;
    }

    /**
     * setter for delete queue for testing purposes
     *
     * @param deleteQueue queue name
     */
    public void setDeleteQueue(String deleteQueue) {
        this.deleteQueue = deleteQueue;
    }

    /**
     * setter for scrub queue for testing purposes
     *
     * @param scrubQueue queue name
     */
    public void setScrubQueue(String scrubQueue) {
        this.scrubQueue = scrubQueue;
    }

    /**
     * setter for messageCounts for testing purposes
     *
     * @param messageCounts
     */
    public void setMessageCounts(Map<MessageType, Integer> messageCounts) {
        this.messageCounts = messageCounts;
    }
}