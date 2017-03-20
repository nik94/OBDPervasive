package com.example.fede_xps.pervasivesystemsminiproject.OBD.commands.control;

/**
 * Created by david on 25/03/2016.
 */


import com.example.fede_xps.pervasivesystemsminiproject.OBD.commands.PercentageObdCommand;
import com.example.fede_xps.pervasivesystemsminiproject.OBD.enums.AvailableCommandNames;


public class TimingAdvanceCommand extends PercentageObdCommand {

    /**
     * <p>Constructor for TimingAdvanceCommand.</p>
     */
    public TimingAdvanceCommand() {
        super("01 0E");
    }

    /**
     * <p>Constructor for TimingAdvanceCommand.</p>
     *
     * @param other a {@link com.github.pires.obd.commands.control.TimingAdvanceCommand} object.
     */
    public TimingAdvanceCommand(TimingAdvanceCommand other) {
        super(other);
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return AvailableCommandNames.TIMING_ADVANCE.getValue();
    }

}
