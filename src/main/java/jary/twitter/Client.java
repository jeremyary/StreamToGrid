package jary.twitter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import twitter4j.*;

import java.util.ArrayList;
import java.util.List;

/**
 * search for a given query against twitter
 *
 * @author <a href='mailto:jeremy.ary@gmail.com'>jary</a>
 */
@Component
public class Client {

    /**
     * factory to provide client instance
     */
    @Autowired
    TwitterFactory twitterFactory;

    /**
     * twitter client
     */
    private Twitter client;

    /**
     * search for a string with twitter client and capture all relevant statuses returned
     *
     * @param queryString string to search for
     * @return list of relevant statuses
     */
    public List<Status> search(String queryString) {

        if (client == null) {
            this.client = twitterFactory.getInstance();
        }

        List<Status> relevantStatusList = new ArrayList<Status>();

        try {

            Query query = new Query(queryString);
            QueryResult result;

            // potentially changed into a multi-search approach by twitter, need to capture all
            do {
                result = client.search(query);
                relevantStatusList.addAll(result.getTweets());
            } while ((query = result.nextQuery()) != null);

        } catch (TwitterException ex) {
            ex.printStackTrace();
            System.out.println("search failed: " + ex.getMessage());
        }
        return relevantStatusList;
    }

    /**
     * setter for twitterFactory for testing purposes
     *
     * @param twitterFactory
     */
    public void setTwitterFactory(TwitterFactory twitterFactory) {
        this.twitterFactory = twitterFactory;
    }
}