package com.example.fede_xps.pervasivesystemsminiproject.OBD.commands.engine;

import com.example.fede_xps.pervasivesystemsminiproject.OBD.commands.temperature.TemperatureCommand;
import com.example.fede_xps.pervasivesystemsminiproject.OBD.enums.AvailableCommandNames;


public class OilTempCommand extends TemperatureCommand {

    /**
     * Default ctor.
     */
    public OilTempCommand() {
        super("01 5C");
    }

    /**
     * Copy ctor.
     *
     */
    public OilTempCommand(OilTempCommand other) {
        super(other);
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return AvailableCommandNames.ENGINE_OIL_TEMP.getValue();
    }

}
