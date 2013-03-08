package jary.datagrid.hazelcast;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.LifecycleService;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * tests for class {@link HazelcastMediator}
 *
 * @author <a href='mailto:jeremy.ary@gmail.com'>jary</a>
 */
public class HazelcastMediatorTest {

    private HazelcastMediator mediator;

    @Before
    public void setUp() {
        mediator = new HazelcastMediator();
    }

    @Test
    public void testInit() {

        HazelcastInstance client = mock(HazelcastInstance.class);
        LifecycleService service = mock(LifecycleService.class);

        when(service.isRunning()).thenReturn(true);
        when(client.getLifecycleService()).thenReturn(service);

        mediator.setClient(client);

        mediator.init();

        verify(client, times(3)).getLifecycleService();
        verify(service, times(3)).isRunning();

        verify(client).getMap("hashtagMap");
        verify(client).getMap("userMap");
        verify(client).getMap("trendMap");
    }

    @Test
    public void testDestroy() {

        HazelcastInstance client = mock(HazelcastInstance.class);
        LifecycleService service = mock(LifecycleService.class);

        when(client.getLifecycleService()).thenReturn(service);
        mediator.setClient(client);

        mediator.destroy();

        verify(client).getLifecycleService();
        verify(service).shutdown();
    }
}