package  com.example.fede_xps.pervasivesystemsminiproject.OBD.commands.fuel;

import com.example.fede_xps.pervasivesystemsminiproject.OBD.commands.ObdCommand;
import com.example.fede_xps.pervasivesystemsminiproject.OBD.enums.AvailableCommandNames;


public class ConsumptionRateCommand extends ObdCommand {

    private float fuelRate = -1.0f;

    /**
     * <p>Constructor for ConsumptionRateCommand.</p>
     */
    public ConsumptionRateCommand() {
        super("01 5E");
    }

    /**
     * <p>Constructor for ConsumptionRateCommand.</p>
     *
     * @param other a {@link com.github.pires.obd.commands.fuel.ConsumptionRateCommand} object.
     */
    public ConsumptionRateCommand(ConsumptionRateCommand other) {
        super(other);
    }

    /** {@inheritDoc} */
    @Override
    protected void performCalculations() {
        // ignore first two bytes [hh hh] of the response
        fuelRate = (buffer.get(2) * 256 + buffer.get(3)) * 0.05f;
    }

    /** {@inheritDoc} */
    @Override
    public String getFormattedResult() {
        return String.format("%.1f%s", fuelRate, getResultUnit());
    }

    /** {@inheritDoc} */
    @Override
    public String getCalculatedResult() {
        return String.valueOf(fuelRate);
    }

    /** {@inheritDoc} */
    @Override
    public String getResultUnit() {
        return "L/h";
    }

    /**
     * <p>getLitersPerHour.</p>
     *
     * @return a float.
     */
    public float getLitersPerHour() {
        return fuelRate;
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return AvailableCommandNames.FUEL_CONSUMPTION_RATE.getValue();
    }

}
