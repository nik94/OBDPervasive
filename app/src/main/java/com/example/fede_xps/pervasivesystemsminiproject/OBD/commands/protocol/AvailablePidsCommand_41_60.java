package  com.example.fede_xps.pervasivesystemsminiproject.OBD.commands.protocol;

import com.example.fede_xps.pervasivesystemsminiproject.OBD.enums.AvailableCommandNames;

/**
 * Retrieve available PIDs ranging from 41 to 60.
 *

 */
public class AvailablePidsCommand_41_60 extends AvailablePidsCommand {

    /**
     * Default ctor.
     */
    public AvailablePidsCommand_41_60() {
        super("01 40");
    }

    /**
     * Copy ctor.
     *
     */
    public AvailablePidsCommand_41_60(AvailablePidsCommand_41_60 other) {
        super(other);
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return AvailableCommandNames.PIDS_41_60.getValue();
    }
}
