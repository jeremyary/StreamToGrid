package jary.twitter.handler;

import jary.annotation.Slf4j;
import jary.datagrid.DataGridMediator;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * handler for status messages from twitter gardenhose (partial) stream
 *
 * @author <a href='mailto:jeremy.ary@gmail.com'>jary</a>
 */
@Component
public class DeleteMessageHandler implements MessageHandler {

    @Slf4j
    private Logger log;

    /** manager to fetch hazelcast map of statuses rec'd from stream userId::status */
    @Autowired
    DataGridMediator mediator;

    /**
     * look through our map for matching statuses under each hashtag and remove to honor notice
     *
     * @param object deletion notice from stream
     */
    public void handle(Object object) {

        StatusDeletionNotice deletionNotice = (StatusDeletionNotice) object;
        for (Map.Entry<String, List<Status>> entry : mediator.getHashtagMap().entrySet()) {

            Iterator iter = entry.getValue().iterator();
            while (iter.hasNext()) {
                Status status = (Status) iter.next();
                if (status.getId() == deletionNotice.getStatusId()) {
                    iter.remove();
                }
            }
        }
    }
}