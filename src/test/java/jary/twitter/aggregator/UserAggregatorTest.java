package jary.twitter.aggregator;

import com.hazelcast.core.IMap;
import jary.datagrid.DataGridMediator;
import jary.drools.SessionMediator;
import jary.drools.factory.SessionFactory;
import jary.drools.model.SessionType;
import org.junit.Before;
import org.junit.Test;
import twitter4j.Status;
import twitter4j.User;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * tests for class {@link UserAggregator}
 *
 * @author <a href='mailto:jeremy.ary@gmail.com'>jary</a>
 */
public class UserAggregatorTest {

    private UserAggregator aggregator;

    @Before
    public void setUp() {

        aggregator = new UserAggregator();
    }

    @Test
    public void testAggregateWithNewStatusList() {

        User user = mock(User.class);
        when(user.getName()).thenReturn("foo");
        Status status = mock(Status.class);
        when(status.getUser()).thenReturn(user);

        IMap <String, List<Status>> userMap = mock(IMap.class);
        when(userMap.containsKey("foo")).thenReturn(false);

        DataGridMediator dataGridMediator = mock(DataGridMediator.class);
        when(dataGridMediator.getUserMap()).thenReturn(userMap);
        aggregator.setMediator(dataGridMediator);

        SessionMediator sessionMediator = mock(SessionMediator.class);
        SessionFactory factory = mock(SessionFactory.class);
        when(factory.build(SessionType.USER)).thenReturn(sessionMediator);
        aggregator.setSessionFactory(factory);

        aggregator.aggregate(status);

        verify(userMap).put(eq("foo"), anyList());
        verify(sessionMediator).insert(any());
        verify(sessionMediator).fire();
    }

    @Test
    public void testAggregateWithPreExistingStatusList() {

        User user = mock(User.class);
        when(user.getName()).thenReturn("foo");
        Status status = mock(Status.class);
        when(status.getUser()).thenReturn(user);

        List<Status> statusList = new ArrayList<Status>();
        IMap <String, List<Status>> userMap = mock(IMap.class);
        when(userMap.containsKey("foo")).thenReturn(true);
        when(userMap.get("foo")).thenReturn(statusList);

        DataGridMediator dataGridMediator = mock(DataGridMediator.class);
        when(dataGridMediator.getUserMap()).thenReturn(userMap);
        aggregator.setMediator(dataGridMediator);

        SessionMediator sessionMediator = mock(SessionMediator.class);
        SessionFactory factory = mock(SessionFactory.class);
        when(factory.build(SessionType.USER)).thenReturn(sessionMediator);
        aggregator.setSessionFactory(factory);

        aggregator.aggregate(status);

        verify(userMap).put("foo", statusList);
        verify(sessionMediator).insert(any());
        verify(sessionMediator).fire();
    }
}
