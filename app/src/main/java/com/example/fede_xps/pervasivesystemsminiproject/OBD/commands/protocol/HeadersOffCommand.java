package  com.example.fede_xps.pervasivesystemsminiproject.OBD.commands.protocol;

/**
 * Turn-off headers.
 *
 * @author pires
 * @version $Id: $Id
 */
public class HeadersOffCommand extends ObdProtocolCommand {

    /**
     * <p>Constructor for HeadersOffCommand.</p>
     */
    public HeadersOffCommand() {
        super("ATH0");
    }

    /**
     * <p>Constructor for HeadersOffCommand.</p>
     *
     */
    public HeadersOffCommand(HeadersOffCommand other) {
        super(other);
    }

    /** {@inheritDoc} */
    @Override
    public String getFormattedResult() {
        return getResult();
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return "Headers disabled";
    }

}
