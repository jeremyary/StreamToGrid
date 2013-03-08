package jary.twitter.listener;

/**
 * types of messages to be consumer from twitter sample stream
 *
 * @author <a href='mailto:jeremy.ary@gmail.com'>jary</a>
 */
public enum MessageType {

    /** a "tweet"; status update */
    STATUS,

    /** notification to delete a previous or potentially upcoming status */
    DELETE,

    /** request to scrum geo info on a range of specified user's statuses */
    SCRUB,

    /** count of missed activity of track due to throttling of hose */
    TRACK,

    /** notification that application is too slow to consume from twitter's end */
    STALL
}
