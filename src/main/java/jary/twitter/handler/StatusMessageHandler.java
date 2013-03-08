package jary.twitter.handler;

import jary.annotation.Slf4j;
import jary.twitter.aggregator.Aggregator;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import twitter4j.Status;

import java.util.List;

/**
 * handler for status messages from twitter gardenhose (partial) stream
 *
 * @author <a href='mailto:jeremy.ary@gmail.com'>jary</a>
 */
@Component
public class StatusMessageHandler implements MessageHandler {

    @Slf4j
    private Logger log;

    /** list of all status aggregators that will categorize and structure statuses in 'interesting' ways and persist */
    @Autowired
    List<Aggregator> aggregatorList;

    /**
     * received status from stream, add to our hazelcast map
     *
     * @param object status to add
     */
    public void handle(Object object) {

        // convert to Status and hand off to each of the aggregators
        Status status = (Status) object;
        for (Aggregator aggregator : aggregatorList) {
            aggregator.aggregate(status);
        }
    }

    /**
     * setter for aggregatorList for testing purposes
     *
     * @param aggregatorList
     */
    public void setAggregatorList(List<Aggregator> aggregatorList) {
        this.aggregatorList = aggregatorList;
    }
}