package jary.twitter.aggregator;

import com.hazelcast.core.IMap;
import jary.datagrid.DataGridMediator;
import jary.drools.SessionMediator;
import jary.drools.factory.SessionFactory;
import jary.drools.model.SessionType;
import org.junit.Before;
import org.junit.Test;
import twitter4j.HashtagEntity;
import twitter4j.Status;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * tests for class {@link HashtagAggregator}
 *
 * @author <a href='mailto:jeremy.ary@gmail.com'>jary</a>
 */
public class HashtagAggregatorTest {

    private HashtagAggregator aggregator;

    @Before
    public void setUp() {

        aggregator = new HashtagAggregator();
    }

    @Test
    public void testAggregateWithNewStatusList() {

        HashtagEntity hashtagEntity = mock(HashtagEntity.class);
        when(hashtagEntity.getText()).thenReturn("foo");
        HashtagEntity[] entities = {hashtagEntity};

        Status status = mock(Status.class);
        when(status.getHashtagEntities()).thenReturn(entities);

        IMap<String, List<Status>> hashtagMap = mock(IMap.class);
        when(hashtagMap.containsKey("foo")).thenReturn(false);

        DataGridMediator dataGridMediator = mock(DataGridMediator.class);
        when(dataGridMediator.getHashtagMap()).thenReturn(hashtagMap);
        aggregator.setMediator(dataGridMediator);

        SessionMediator sessionMediator = mock(SessionMediator.class);
        SessionFactory factory = mock(SessionFactory.class);
        when(factory.build(SessionType.HASHTAG)).thenReturn(sessionMediator);
        aggregator.setSessionFactory(factory);

        aggregator.aggregate(status);

        verify(hashtagMap).put(eq("foo"), anyList());
        verify(sessionMediator).insert(any());
        verify(sessionMediator).fire();
    }

    @Test
    public void testAggregateWithPreExistingStatusList() {

        HashtagEntity hashtagEntity = mock(HashtagEntity.class);
        when(hashtagEntity.getText()).thenReturn("foo");
        HashtagEntity[] entities = {hashtagEntity};

        Status status = mock(Status.class);
        when(status.getHashtagEntities()).thenReturn(entities);

        List<Status> statusList = new ArrayList<Status>();
        IMap<String, List<Status>> hashtagMap = mock(IMap.class);
        when(hashtagMap.containsKey("foo")).thenReturn(true);
        when(hashtagMap.get("foo")).thenReturn(statusList);

        DataGridMediator dataGridMediator = mock(DataGridMediator.class);
        when(dataGridMediator.getHashtagMap()).thenReturn(hashtagMap);
        aggregator.setMediator(dataGridMediator);

        SessionMediator sessionMediator = mock(SessionMediator.class);
        SessionFactory factory = mock(SessionFactory.class);
        when(factory.build(SessionType.HASHTAG)).thenReturn(sessionMediator);
        aggregator.setSessionFactory(factory);

        aggregator.aggregate(status);

        verify(hashtagMap).put("foo", statusList);
        verify(sessionMediator).insert(any());
        verify(sessionMediator).fire();
    }
}
