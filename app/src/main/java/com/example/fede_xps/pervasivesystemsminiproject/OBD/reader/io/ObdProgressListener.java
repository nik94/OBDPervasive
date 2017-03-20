package com.example.fede_xps.pervasivesystemsminiproject.OBD.reader.io;

public interface ObdProgressListener {

    void stateUpdate(final ObdCommandJob job);

}