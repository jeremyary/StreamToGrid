package jary.twitter.aggregator;

import jary.datagrid.DataGridMediator;
import jary.drools.factory.SessionFactory;
import jary.drools.SessionMediator;
import jary.drools.model.Hashtag;
import jary.drools.model.SessionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import twitter4j.HashtagEntity;
import twitter4j.Status;

import java.util.ArrayList;
import java.util.List;

/**
 * aggregate and persist statuses based on hashtag information
 * utilizes data grid map of String::Statuses to aggregate by username
 *
 * @author <a href='mailto:jeremy.ary@gmail.com'>jary</a>
 */
@Component
public class HashtagAggregator implements Aggregator {

    /**
     * data grid mediator, holds map of hashtag::statuses
     */
    @Autowired
    DataGridMediator mediator;

    /**
     * rule session builder serving hashtag-based rule session
     */
    @Autowired
    private SessionFactory sessionFactory;

    /**
     * extract hash tags from status and add to map categorized hashtag::statuses,
     * build and trigger rule session for hashtag data
     *
     * @param status tweet from stream
     */
    public void aggregate(Status status) {

        HashtagEntity[] hashTags = status.getHashtagEntities();
        for (HashtagEntity tag : hashTags) {
            putOnMap(tag.getText(), status);
            Hashtag hashtag = new Hashtag(tag.getText(), mediator.getHashtagMap().get(tag.getText()));

            //construct/trigger session
            SessionMediator sessionMediator = sessionFactory.build(SessionType.HASHTAG);
            sessionMediator.insert(hashtag);
            sessionMediator.fire();
        }
    }

    /**
     * add hashtag::status to data grid map
     *
     * @param tagText text of hashtag
     * @param status  status to add
     */
    private void putOnMap(String tagText, Status status) {

        // could be a new hashtag, or need to append to list
        List<Status> statusList;
        if (!mediator.getHashtagMap().containsKey(tagText)) {
            statusList = new ArrayList<Status>();
        } else {
            statusList = mediator.getHashtagMap().get(tagText);
        }

        // have to aggregate and re-put, due to HZ's impl of IMap
        statusList.add(status);
        mediator.getHashtagMap().put(tagText, statusList);
    }

    /**
     * setter for mediator for testing purposes
     *
     * @param mediator
     */
    public void setMediator(DataGridMediator mediator) {
        this.mediator = mediator;
    }

    /**
     * setter for sessionFactory for testing purposes
     *
     * @param sessionFactory
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}