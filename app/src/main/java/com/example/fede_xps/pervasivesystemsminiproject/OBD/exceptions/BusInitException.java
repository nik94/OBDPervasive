package  com.example.fede_xps.pervasivesystemsminiproject.OBD.exceptions;

/**
 * Thrown when there is a "BUS INIT. ERROR" message
 *
 * @author pires
 * @version $Id: $Id
 */
public class BusInitException extends ResponseException {

    /**
     * <p>Constructor for BusInitException.</p>
     */
    public BusInitException() {
        super("BUS INIT. ERROR");
    }

}
