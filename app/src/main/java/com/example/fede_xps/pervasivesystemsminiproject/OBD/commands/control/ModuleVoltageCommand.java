package com.example.fede_xps.pervasivesystemsminiproject.OBD.commands.control;

/**
 * Created by david on 25/03/2016.
 */

import com.example.fede_xps.pervasivesystemsminiproject.OBD.commands.ObdCommand;
import com.example.fede_xps.pervasivesystemsminiproject.OBD.enums.AvailableCommandNames;


public class ModuleVoltageCommand extends ObdCommand {

    // Equivalent ratio (V)
    private double voltage = 0.00;

    /**
     * Default ctor.
     */
    public ModuleVoltageCommand() {
        super("01 42");
    }

    /**
     * Copy ctor.
     *
     * @param other a {@link com.github.pires.obd.commands.control.ModuleVoltageCommand} object.
     */
    public ModuleVoltageCommand(ModuleVoltageCommand other) {
        super(other);
    }

    /** {@inheritDoc} */
    @Override
    protected void performCalculations() {
        // ignore first two bytes [hh hh] of the response
        int a = buffer.get(2);
        int b = buffer.get(3);
        voltage = (a * 256 + b) / 1000;
    }

    /** {@inheritDoc} */
    @Override
    public String getFormattedResult() {
        return String.format("%.1f%s", voltage, getResultUnit());
    }

    /** {@inheritDoc} */
    @Override
    public String getResultUnit() {
        return "V";
    }

    /** {@inheritDoc} */
    @Override
    public String getCalculatedResult() {
        return String.valueOf(voltage);
    }

    /**
     * <p>Getter for the field <code>voltage</code>.</p>
     *
     * @return a double.
     */
    public double getVoltage() {
        return voltage;
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return AvailableCommandNames.CONTROL_MODULE_VOLTAGE.getValue();
    }

}
