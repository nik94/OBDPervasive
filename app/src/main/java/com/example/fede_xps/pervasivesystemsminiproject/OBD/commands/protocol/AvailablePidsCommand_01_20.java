package  com.example.fede_xps.pervasivesystemsminiproject.OBD.commands.protocol;

import com.example.fede_xps.pervasivesystemsminiproject.OBD.enums.AvailableCommandNames;

/**
 * Retrieve available PIDs ranging from 01 to 20.
 *

 */
public class AvailablePidsCommand_01_20 extends AvailablePidsCommand {

    /**
     * Default ctor.
     */
    public AvailablePidsCommand_01_20() {
        super("01 00");
    }

    /**
     * Copy ctor.
     *
     */
    public AvailablePidsCommand_01_20(AvailablePidsCommand_01_20 other) {
        super(other);
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return AvailableCommandNames.PIDS_01_20.getValue();
    }
}
