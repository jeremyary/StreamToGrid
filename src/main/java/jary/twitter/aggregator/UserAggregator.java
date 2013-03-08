package jary.twitter.aggregator;

import jary.datagrid.DataGridMediator;
import jary.drools.RuleDelegate;
import jary.drools.factory.SessionFactory;
import jary.drools.SessionMediator;
import jary.drools.model.SessionType;
import jary.drools.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import twitter4j.Status;

import java.util.ArrayList;
import java.util.List;

/**
 * aggregate and persist statuses based on hashtag information
 * utilizes data grid map String::Statuses to aggregate by hashtag
 *
 * @author <a href='mailto:jeremy.ary@gmail.com'>jary</a>
 */
@Component
public class UserAggregator implements Aggregator {

    /**
     * data grid mediator, holds map username::statuses
     */
    @Autowired
    DataGridMediator mediator;

    /**
     * rule delegate to hand off to each session builder instance
     */
    @Autowired
    private RuleDelegate delegate;

    /**
     * rule session builder serving user-based rule session
     */
    @Autowired
    private SessionFactory sessionFactory;

    /**
     * extract the user name from the status and add to the map of username::statuses,
     * then build a rule session and fire rules
     *
     * @param status tweet from stream
     */
    public void aggregate(Status status) {
        putOnMap(status.getUser().getName(), status);
        User user = new User(status.getUser().getName(), mediator.getUserMap().get(status.getUser().getName()));

        //construct/trigger session
        SessionMediator sessionMediator = sessionFactory.build(SessionType.USER);
        sessionMediator.insert(user);
        sessionMediator.fire();
    }

    /**
     * add username::status to data grid map
     *
     * @param tagText text of hashtag
     * @param status  status to add
     */
    private void putOnMap(String tagText, Status status) {

        // could be a new username, or need to append to list
        List<Status> statusList;
        if (!mediator.getUserMap().containsKey(tagText)) {
            statusList = new ArrayList<Status>();
        } else {
            statusList = mediator.getUserMap().get(tagText);
        }

        // have to aggregate and re-put, due to HZ's impl of IMap
        statusList.add(status);
        mediator.getUserMap().put(tagText, statusList);
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