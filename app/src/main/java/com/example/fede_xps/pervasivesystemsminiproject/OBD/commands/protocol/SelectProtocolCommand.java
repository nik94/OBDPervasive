package  com.example.fede_xps.pervasivesystemsminiproject.OBD.commands.protocol;

import com.example.fede_xps.pervasivesystemsminiproject.OBD.enums.ObdProtocols;

/**
 * Select the protocol to use.

 */
public class SelectProtocolCommand extends ObdProtocolCommand {

    private final ObdProtocols protocol;

    /**
     * <p>Constructor for SelectProtocolCommand.</p>
     *
     */
    public SelectProtocolCommand(final ObdProtocols protocol) {
        super("AT SP " + protocol.getValue());
        this.protocol = protocol;
    }

    /** {@inheritDoc} */
    @Override
    public String getFormattedResult() {
        return getResult();
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return "Select Protocol " + protocol.name();
    }

}
