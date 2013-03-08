package jary.twitter.exception;

/**
 * exception encountered when we've stalled twitter stream sampling
 *
 * @author <a href='mailto:jeremy.ary@gmail.com'>jary</a>
 */
public class StallException extends RuntimeException {

    public StallException(String message) {
        super(message);
    }
}
