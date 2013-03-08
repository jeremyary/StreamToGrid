package jary.datagrid;

import com.hazelcast.core.IMap;
import twitter4j.Status;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;

/**
 * manage lifecycle and maps of data grid
 *
 * @author <a href='mailto:jeremy.ary@gmail.com'>jary</a>
 */
public abstract class DataGridMediator {

    /** map of statuses categorized by hashtag */
    private IMap<String, List<Status>> hashtagMap;

    /** map of statuses categorized by user */
    private IMap<String, List<Status>> userMap;

    /** map of hashtag trends, collection of further-queried statuses
     * from trending hashtag identified by sample stream */
    private IMap<String, List<Status>> trendMap;

    /**
     * initialization method to establish connection with cluster when context stands up
     */
    @PostConstruct
    public abstract void init();

    /**
     * proper shut down of client on context exit
     */
    @PreDestroy
    public abstract void destroy();

    /**
     * flush grid instances, allows fresh start
     *
     * @return
     */
    public abstract void flush();

    /**
     * getter for hashtagMap, status categorized by hashtag key
     *
     * @return map of statuses by hashtag
     */
    public IMap<String, List<Status>> getHashtagMap() {
        return hashtagMap;
    }

    /**
     * getter for userMap, status categorized by username key
     *
     * @return map of statuses by username
     */
    public IMap<String, List<Status>> getUserMap() {
        return userMap;
    }

    /**
     * getter for trendMap, hashtag trend statuses
     *
     * @return map of statuses by hashtag
     */
    public IMap<String, List<Status>> getTrendMap() {
        return trendMap;
    }

    /**
     * setter for hashtagMap
     *
     * @param hashtagMap
     */
    public void setHashtagMap(IMap<String, List<Status>> hashtagMap) {
        this.hashtagMap = hashtagMap;
    }

    /**
     * setter for userMap
     *
     * @param userMap
     */
    public void setUserMap(IMap<String, List<Status>> userMap) {
        this.userMap = userMap;
    }

    /**
     * setter for trendMap
     *
     * @param trendMap
     */
    public void setTrendMap(IMap<String, List<Status>> trendMap) {
        this.trendMap = trendMap;
    }
}