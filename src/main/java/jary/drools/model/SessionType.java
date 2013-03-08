package jary.drools.model;

/**
 * enum providing {@link jary.drools.factory.SessionFactory} with criteria needed
 * to hand back a session with the proper knowledge package loaded
 *
 * @author <a href='mailto:jeremy.ary@gmail.com'>jary</a>
 */
public enum SessionType {

    /** session to deal with hashtags */
    HASHTAG,

    /** session to deal with users */
    USER
}
