package  com.example.fede_xps.pervasivesystemsminiproject.OBD.commands.pressure;

import com.example.fede_xps.pervasivesystemsminiproject.OBD.enums.AvailableCommandNames;

/**
 * <p>FuelPressureCommand class.</p>
 *
 * @author pires
 * @version $Id: $Id
 */
public class FuelPressureCommand extends PressureCommand {

    /**
     * <p>Constructor for FuelPressureCommand.</p>
     */
    public FuelPressureCommand() {
        super("01 0A");
    }

    /**
     * <p>Constructor for FuelPressureCommand.</p>
     *
     */
    public FuelPressureCommand(FuelPressureCommand other) {
        super(other);
    }

    /**
     * {@inheritDoc}
     * <p>
     * TODO describe of why we multiply by 3
     */
    @Override
    protected final int preparePressureValue() {
        return buffer.get(2) * 3;
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return AvailableCommandNames.FUEL_PRESSURE.getValue();
    }

}
