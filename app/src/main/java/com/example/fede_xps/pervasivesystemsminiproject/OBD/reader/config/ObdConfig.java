package com.example.fede_xps.pervasivesystemsminiproject.OBD.reader.config;

import java.util.ArrayList;

import com.example.fede_xps.pervasivesystemsminiproject.OBD.commands.ObdCommand;
import com.example.fede_xps.pervasivesystemsminiproject.OBD.commands.SpeedCommand;
import com.example.fede_xps.pervasivesystemsminiproject.OBD.commands.control.DistanceMILOnCommand;
import com.example.fede_xps.pervasivesystemsminiproject.OBD.commands.control.DtcNumberCommand;
import com.example.fede_xps.pervasivesystemsminiproject.OBD.commands.control.EquivalentRatioCommand;
import com.example.fede_xps.pervasivesystemsminiproject.OBD.commands.control.IgnitionMonitorCommand;
import com.example.fede_xps.pervasivesystemsminiproject.OBD.commands.control.ModuleVoltageCommand;
import com.example.fede_xps.pervasivesystemsminiproject.OBD.commands.control.TimingAdvanceCommand;
import com.example.fede_xps.pervasivesystemsminiproject.OBD.commands.control.TroubleCodesCommand;
import com.example.fede_xps.pervasivesystemsminiproject.OBD.commands.control.VinCommand;
import com.example.fede_xps.pervasivesystemsminiproject.OBD.commands.engine.AbsoluteLoadCommand;
import com.example.fede_xps.pervasivesystemsminiproject.OBD.commands.engine.LoadCommand;
import com.example.fede_xps.pervasivesystemsminiproject.OBD.commands.engine.MassAirFlowCommand;
import com.example.fede_xps.pervasivesystemsminiproject.OBD.commands.engine.OilTempCommand;
import com.example.fede_xps.pervasivesystemsminiproject.OBD.commands.engine.RPMCommand;
import com.example.fede_xps.pervasivesystemsminiproject.OBD.commands.engine.RuntimeCommand;
import com.example.fede_xps.pervasivesystemsminiproject.OBD.commands.engine.ThrottlePositionCommand;
import com.example.fede_xps.pervasivesystemsminiproject.OBD.commands.fuel.AirFuelRatioCommand;
import com.example.fede_xps.pervasivesystemsminiproject.OBD.commands.fuel.ConsumptionRateCommand;
import com.example.fede_xps.pervasivesystemsminiproject.OBD.commands.fuel.FindFuelTypeCommand;
import com.example.fede_xps.pervasivesystemsminiproject.OBD.commands.fuel.FuelLevelCommand;
import com.example.fede_xps.pervasivesystemsminiproject.OBD.commands.fuel.FuelTrimCommand;
import com.example.fede_xps.pervasivesystemsminiproject.OBD.commands.fuel.WidebandAirFuelRatioCommand;
import com.example.fede_xps.pervasivesystemsminiproject.OBD.commands.pressure.BarometricPressureCommand;
import com.example.fede_xps.pervasivesystemsminiproject.OBD.commands.pressure.FuelPressureCommand;
import com.example.fede_xps.pervasivesystemsminiproject.OBD.commands.pressure.FuelRailPressureCommand;
import com.example.fede_xps.pervasivesystemsminiproject.OBD.commands.pressure.IntakeManifoldPressureCommand;
import com.example.fede_xps.pervasivesystemsminiproject.OBD.commands.temperature.AirIntakeTemperatureCommand;
import com.example.fede_xps.pervasivesystemsminiproject.OBD.commands.temperature.AmbientAirTemperatureCommand;
import com.example.fede_xps.pervasivesystemsminiproject.OBD.commands.temperature.EngineCoolantTemperatureCommand;
import com.example.fede_xps.pervasivesystemsminiproject.OBD.enums.FuelTrim;

/**
 */
public final class ObdConfig {

    public static ArrayList<ObdCommand> getCommands() {
        ArrayList<ObdCommand> cmds = new ArrayList<>();

        // Control
        cmds.add(new ModuleVoltageCommand());
        cmds.add(new EquivalentRatioCommand());
        cmds.add(new DistanceMILOnCommand());
        cmds.add(new DtcNumberCommand());
        cmds.add(new TimingAdvanceCommand());
        cmds.add(new TroubleCodesCommand());
        cmds.add(new VinCommand());
        cmds.add(new IgnitionMonitorCommand());
        cmds.add(new TroubleCodesCommand());

        // Engine
        cmds.add(new LoadCommand());
        cmds.add(new RPMCommand());
        cmds.add(new RuntimeCommand());
        cmds.add(new MassAirFlowCommand());
        cmds.add(new ThrottlePositionCommand());
        cmds.add(new AbsoluteLoadCommand());
        cmds.add(new OilTempCommand());


        // Fuel
        cmds.add(new FindFuelTypeCommand());
        cmds.add(new ConsumptionRateCommand());
        cmds.add(new FuelLevelCommand());
        cmds.add(new FuelTrimCommand(FuelTrim.LONG_TERM_BANK_1));
        cmds.add(new FuelTrimCommand(FuelTrim.LONG_TERM_BANK_2));
        cmds.add(new FuelTrimCommand(FuelTrim.SHORT_TERM_BANK_1));
        cmds.add(new FuelTrimCommand(FuelTrim.SHORT_TERM_BANK_2));
        cmds.add(new AirFuelRatioCommand());
        cmds.add(new WidebandAirFuelRatioCommand());

        // Pressure
        cmds.add(new BarometricPressureCommand());
        cmds.add(new FuelPressureCommand());
        cmds.add(new FuelRailPressureCommand());
        cmds.add(new IntakeManifoldPressureCommand());

        // Temperature
        cmds.add(new AirIntakeTemperatureCommand());
        cmds.add(new AmbientAirTemperatureCommand());
        cmds.add(new EngineCoolantTemperatureCommand());

        // Misc
        cmds.add(new SpeedCommand());

        return cmds;
    }
}
