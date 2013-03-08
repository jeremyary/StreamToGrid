package jary.twitter.aggregator;

import twitter4j.Status;

/**
 * common interface for status aggregators
 *
 * @author <a href='mailto:jeremy.ary@gmail.com'>jary</a>
 */
public interface Aggregator {

    /**
     * categorize status according to aggregator
     */
    public void aggregate(Status status);
}