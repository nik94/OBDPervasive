package com.example.fede_xps.pervasivesystemsminiproject.OBD.commands.protocol;


public class AdaptiveTimingCommand extends ObdProtocolCommand {

    /**
     * <p>Constructor for AdaptiveTimingCommand.</p>
     *
     * @param mode a int.
     */
    public AdaptiveTimingCommand(int mode) {
        super("AT AT" + mode);
    }

    /**
     * <p>Constructor for AdaptiveTimingCommand.</p>
     *
     */
    public AdaptiveTimingCommand(AdaptiveTimingCommand other) {
        super(other);
    }

    /** {@inheritDoc} */
    @Override
    public String getFormattedResult() {
        return getResult();
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return "Adaptive timing set";
    }

}
