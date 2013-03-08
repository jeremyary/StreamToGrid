package jary.drools.model;

import twitter4j.Status;

import java.util.ArrayList;
import java.util.List;

/**
 * model representing a data grid map entry, one hashtag with all status entrants
 *
 * @author <a href='mailto:jeremy.ary@gmail.com'>jary</a>
 */
public class User {

    /**
     * username
     */
    private String username;

    /**
     * statusList from user
     */
    private List<Status> statusList = new ArrayList<Status>();

    /**
     * constructor
     *
     * @param username   username
     * @param statusList list of statusList from user
     */
    public User(String username, List<Status> statusList) {
        this.username = username;
        this.statusList = statusList;
    }

    /**
     * getter for username
     *
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * getter for statusList
     *
     * @return list of statusList from user
     */
    public List<Status> getStatusList() {
        return statusList;
    }
}