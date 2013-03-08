package jary.drools;

import com.hazelcast.core.IMap;
import jary.datagrid.DataGridMediator;
import jary.drools.model.Hashtag;
import jary.drools.model.User;
import org.junit.Before;
import org.junit.Test;
import twitter4j.Status;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * tests for class {@link RuleDelegate}
 *
 * @author <a href='mailto:jeremy.ary@gmail.com'>jary</a>
 */
public class RuleDelegateTest {

    private RuleDelegate delegate;

    @Before
    public void setUp() {
        delegate = new RuleDelegate();
    }

    @Test
    public void testhandleHashtagTrend() {

        IMap<String, List<Status>> trendMap = mock(IMap.class);

        DataGridMediator mediator = mock(DataGridMediator.class);
        when(mediator.getTrendMap()).thenReturn(trendMap);
        delegate.setMediator(mediator);

        delegate.handleTrend(new Hashtag("foo", new ArrayList<Status>()));

        verify(trendMap).put("foo", new ArrayList<Status>());
    }

    @Test
    public void testhandleUserTrend() {

        IMap<String, List<Status>> trendMap = mock(IMap.class);

        DataGridMediator mediator = mock(DataGridMediator.class);
        when(mediator.getTrendMap()).thenReturn(trendMap);
        delegate.setMediator(mediator);

        delegate.handleTrend(new User("foo", new ArrayList<Status>()));

        verify(trendMap).put("foo", new ArrayList<Status>());
    }
}