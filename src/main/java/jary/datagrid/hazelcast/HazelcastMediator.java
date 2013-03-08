package jary.datagrid.hazelcast;

import com.hazelcast.client.ClientConfig;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.core.Instance;
import jary.datagrid.DataGridMediator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import twitter4j.Status;

import java.util.Collection;
import java.util.List;

/**
 * manage lifecycle and maps of interest from Hazelcast
 *
 * @author <a href='mailto:jeremy.ary@gmail.com'>jary</a>
 */
@Component
public class HazelcastMediator extends DataGridMediator {

    /** client configuration, group and cluster info from properties */
    @Autowired
    ClientConfig clientConfig;

    /** client for cluster connectivity */
    HazelcastInstance client;

    /**
     * initialization method to establish connection with cluster when context stands up
     */
    public void init() {

        // go ahead and fetch maps of interest
        IMap<String, List<Status>> hashtagMap = getClient().getMap("hashtagMap");
        setHashtagMap(hashtagMap);

        IMap<String, List<Status>> userMap = getClient().getMap("userMap");
        setUserMap(userMap);

        IMap<String, List<Status>> trendMap = getClient().getMap("trendMap");
        setTrendMap(trendMap);
    }

    /**
     * proper shut down of client on context exit
     */
    public void destroy() {
        if (client != null) {
            client.getLifecycleService().shutdown();
        }
    }

    /**
     * getter for client, renews connectivity if needed
     *
     * @return
     */
    public void flush() {

        // go with fresh instances, prevents shutdown on map type changes
        Collection<Instance> instances = getClient().getInstances();
        for (Instance instance : instances) {
            instance.destroy();
        }
    }

    /**
     * setter for client
     *
     * @param client
     */
    public void setClient(HazelcastInstance client) {
        this.client = client;
    }

    /**
     * getter for client, renews connectivity if needed
     *
     * @return client instance
     */
    public HazelcastInstance getClient() {
        if (client == null) {
            client = HazelcastClient.newHazelcastClient(clientConfig);
        } else if (!client.getLifecycleService().isRunning()) {
            client.getLifecycleService().restart();
        }
        return client;
    }
}