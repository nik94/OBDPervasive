package  com.example.fede_xps.pervasivesystemsminiproject.OBD.commands.protocol;

import com.example.fede_xps.pervasivesystemsminiproject.OBD.enums.AvailableCommandNames;

/**
 * Retrieve available PIDs ranging from 21 to 40.

 */
public class AvailablePidsCommand_21_40 extends AvailablePidsCommand {

    /**
     * Default ctor.
     */
    public AvailablePidsCommand_21_40() {
        super("01 20");
    }

    /**
     * Copy ctor.
     *
     */
    public AvailablePidsCommand_21_40(AvailablePidsCommand_21_40 other) {
        super(other);
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return AvailableCommandNames.PIDS_21_40.getValue();
    }
}
