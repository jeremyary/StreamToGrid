package jary.drools.model;

import twitter4j.Status;

import java.util.ArrayList;
import java.util.List;

/**
 * model representing a data grid map entry, one hashtag with all status entrants
 *
 * @author <a href='mailto:jeremy.ary@gmail.com'>jary</a>
 */
public class Hashtag {

    /**
     * text of hashtag
     */
    private String text;

    /**
     * statusList using hashtag
     */
    private List<Status> statusList = new ArrayList<Status>();

    /**
     * constructor
     *
     * @param text     text of hashtag
     * @param statusList list of statusList featuring hashtag
     */
    public Hashtag(String text, List<Status> statusList) {
        this.text = text;
        this.statusList = statusList;
    }

    /**
     * getter for text
     *
     * @return text of hashtag
     */
    public String getText() {
        return text;
    }

    /**
     * getter for statusList
     *
     * @return list of statusList using hashtag
     */
    public List<Status> getStatusList() {
        return statusList;
    }
}