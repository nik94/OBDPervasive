package com.example.fede_xps.pervasivesystemsminiproject.OBD.commands.control;

/**
 * Created by david on 25/03/2016.
 */

import com.example.fede_xps.pervasivesystemsminiproject.OBD.commands.ObdCommand;
import com.example.fede_xps.pervasivesystemsminiproject.OBD.commands.SystemOfUnits;
import com.example.fede_xps.pervasivesystemsminiproject.OBD.enums.AvailableCommandNames;

class DistanceSinceCCCommand extends ObdCommand
        implements SystemOfUnits {

    private int km = 0;

    /**
     * Default ctor.
     */
    public DistanceSinceCCCommand() {
        super("01 31");
    }

    /**
     * Copy ctor.
     *
     * @param other a {@link com.github.pires.obd.commands.control.DistanceSinceCCCommand} object.
     */
    public DistanceSinceCCCommand(
            DistanceSinceCCCommand other) {
        super(other);
    }

    /** {@inheritDoc} */
    @Override
    protected void performCalculations() {
        // ignore first two bytes [01 31] of the response
        km = buffer.get(2) * 256 + buffer.get(3);
    }

    /**
     * <p>getFormattedResult.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getFormattedResult() {
        return useImperialUnits ? String.format("%.2f%s", getImperialUnit(), getResultUnit())
                : String.format("%d%s", km, getResultUnit());
    }

    /** {@inheritDoc} */
    @Override
    public String getCalculatedResult() {
        return useImperialUnits ? String.valueOf(getImperialUnit()) : String.valueOf(km);
    }

    /** {@inheritDoc} */
    @Override
    public String getResultUnit() {
        return useImperialUnits ? "m" : "km";
    }

    /** {@inheritDoc} */
    @Override
    public float getImperialUnit() {
        return km * 0.621371192F;
    }

    /**
     * <p>Getter for the field <code>km</code>.</p>
     *
     * @return a int.
     */
    public int getKm() {
        return km;
    }

    /**
     * <p>Setter for the field <code>km</code>.</p>
     *
     * @param km a int.
     */
    public void setKm(int km) {
        this.km = km;
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return AvailableCommandNames.DISTANCE_TRAVELED_AFTER_CODES_CLEARED
                .getValue();
    }

}