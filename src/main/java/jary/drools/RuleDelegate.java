package jary.drools;

import jary.annotation.Slf4j;
import jary.datagrid.DataGridMediator;
import jary.drools.model.Hashtag;
import jary.drools.model.User;
import jary.twitter.Client;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Delegate responsible for spinning off async tasks spawned from rule sessions
 *
 * @author <a href='mailto:jeremy.ary@gmail.com'>jary</a>
 */
@Component
public class RuleDelegate {

    @Slf4j
    private Logger log;

    /** mediator providing grid for map access */
    @Autowired
    DataGridMediator mediator;

    @Autowired
    Client twitterClient;

    /**
     * async handler to place trend into data grid without slowing session return &
     * stream processing
     */
    @Async
    public void handleTrend(Hashtag hashtag) {

        // trend identified by rule session. for now, just make note of it and throw it over
        // to trendMap.

        // TODO: expand upon gathering capability with additional client queries
        // TODO: will provide more statuses than what's captured from sample stream
        // TODO: will need throttle monitoring or query batching every X mins with executor
        // List<Status> statusList = twitterClient.search(hashtag.getText());

        mediator.getTrendMap().put(hashtag.getText(), hashtag.getStatusList());
    }

    /**
     * async handler to place trending info from user into data grid without slowing session return &
     * stream processing
     */
    @Async
    public void handleTrend(User user) {

        // trend identified by rule session. for now, just make note of it and throw it over
        // to trendMap.

        // TODO: expand upon gathering capability with additional client queries
        // TODO: will provide more statuses than what's captured from sample stream
        // TODO: will need throttle monitoring or query batching every X mins with executor
        // List<Status> statusList = twitterClient.search(user.getUsername());

        mediator.getTrendMap().put(user.getUsername(), user.getStatusList());
    }

    /**
     * setter for mediator for testing purposes
     *
     * @param mediator
     */
    public void setMediator(DataGridMediator mediator) {
        this.mediator = mediator;
    }
}