package jary.twitter.handler;

/**
 * interface for streaming API message handlers
 *
 * @author <a href='mailto:jeremy.ary@gmail.com'>jary</a>
 */
public interface MessageHandler {

    public void handle(Object object);
}
