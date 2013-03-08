package jary.datagrid.hazelcast;

import com.hazelcast.client.ClientConfig;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import org.junit.Test;

import java.util.Map;

/**
 * test class to exercise the java hazelcast client with an external ec2 cluster
 *
 * @author <a href='mailto:jeremy.ary@gmail.com'>jary</a>
 */
public class HazelcastClientTest {

    @Test
    public void testEc2Cluster() {

        ClientConfig clientConfig = new ClientConfig();
        clientConfig.getGroupConfig().setName("dev").setPassword("dev-pass");
        clientConfig.addAddress("hazelcastLoadBalancer-1653182937.us-east-1.elb.amazonaws.com");

        HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);

        // get initial map
        Map<String, String> testMap = client.getMap("testMap");
        testMap.put("foo", "bar");
        testMap.put("j", "ro");
        testMap.put("test", "ing");
        assert testMap.size() == 3;

        // assert put will update, not add additional records per key
        testMap.put("foo", "bars");
        testMap.put("j", "ros");
        testMap.put("test", "ings");
        assert testMap.size() == 3;

        // re-fetch, just to be sure the count's accurate on the map from cluster
        testMap = client.getMap("testMap");
        assert testMap.size() == 3;

        // empty the map and shut it down
        testMap.clear();
        client.getLifecycleService().shutdown();
    }
}
