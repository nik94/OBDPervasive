package com.example.fede_xps.pervasivesystemsminiproject.OBD.commands.engine;

import com.example.fede_xps.pervasivesystemsminiproject.OBD.commands.ObdCommand;
import com.example.fede_xps.pervasivesystemsminiproject.OBD.enums.AvailableCommandNames;

/**
 * Engine runtime.
 *
 * @author pires
 * @version $Id: $Id
 */
public class RuntimeCommand extends ObdCommand {

    private int value = 0;

    /**
     * Default ctor.
     */
    public RuntimeCommand() {
        super("01 1F");
    }

    /**
     * Copy ctor.
     *
     * @param other a {@link com.github.pires.obd.commands.engine.RuntimeCommand} object.
     */
    public RuntimeCommand(RuntimeCommand other) {
        super(other);
    }

    /** {@inheritDoc} */
    @Override
    protected void performCalculations() {
        // ignore first two bytes [01 0C] of the response
        value = buffer.get(2) * 256 + buffer.get(3);
    }

    /** {@inheritDoc} */
    @Override
    public String getFormattedResult() {
        // determine time
        final String hh = String.format("%02d", value / 3600);
        final String mm = String.format("%02d", (value % 3600) / 60);
        final String ss = String.format("%02d", value % 60);
        return String.format("%s:%s:%s", hh, mm, ss);
    }

    /** {@inheritDoc} */
    @Override
    public String getCalculatedResult() {
        return String.valueOf(value);
    }

    /** {@inheritDoc} */
    @Override
    public String getResultUnit() {
        return "s";
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return AvailableCommandNames.ENGINE_RUNTIME.getValue();
    }

}
