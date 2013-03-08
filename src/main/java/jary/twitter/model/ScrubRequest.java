package jary.twitter.model;

import java.io.Serializable;

/**
 * wrapper fro streaming API scrub requests to queue
 *
 * @author <a href='mailto:jeremy.ary@gmail.com'>jary</a>
 */
public class ScrubRequest implements Serializable {

    /** user id of scrub request */
    private long userId;

    /** status range indicator for scrub messages */
    private long upToStatusId;

    /**
     * constructor
     *
     * @param userId user id of scrub request
     * @param upToStatusId range indicator of request
     */
    public ScrubRequest(long userId, long upToStatusId) {
        setUserId(userId);
        setUpToStatusId(upToStatusId);
    }

    /**
     * getter for user id
     *
     * @return long user id of request
     */
    public long getUserId() {
        return userId;
    }

    /**
     * setter for user id
     *
     * @param userId id of requested scrub user profile
     */
    public void setUserId(long userId) {
        this.userId = userId;
    }

    /**
     * getter for upToStatusId
     *
     * @return long range indicator
     */
    public long getUpToStatusId() {
        return upToStatusId;
    }

    /**
     * setter for upToStatusId
     *
     * @param upToStatusId range indicator to set
     */
    public void setUpToStatusId(long upToStatusId) {
        this.upToStatusId = upToStatusId;
    }
}