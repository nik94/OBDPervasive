package com.example.fede_xps.pervasivesystemsminiproject.OBD.commands.control;

/**
 * Created by david on 25/03/2016.
 */

import com.example.fede_xps.pervasivesystemsminiproject.OBD.commands.PercentageObdCommand;
import com.example.fede_xps.pervasivesystemsminiproject.OBD.enums.AvailableCommandNames;


public class EquivalentRatioCommand extends PercentageObdCommand {


    /**
     * Default ctor.
     */
    public EquivalentRatioCommand() {
        super("01 44");
    }

    /**
     * Copy ctor.
     *
     * @param other a {@link com.github.pires.obd.commands.control.EquivalentRatioCommand} object.
     */
    public EquivalentRatioCommand(EquivalentRatioCommand other) {
        super(other);
    }

    /** {@inheritDoc} */
    @Override
    protected void performCalculations() {
        // ignore first two bytes [hh hh] of the response
        int a = buffer.get(2);
        int b = buffer.get(3);
        percentage = (a * 256 + b) / 32768;
    }


    /**
     * <p>getRatio.</p>
     *
     * @return a double.
     */
    public double getRatio() {
        return (double) percentage;
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return AvailableCommandNames.EQUIV_RATIO.getValue();
    }

}
