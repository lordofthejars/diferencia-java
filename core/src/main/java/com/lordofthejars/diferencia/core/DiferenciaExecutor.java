package com.lordofthejars.diferencia.core;

import com.lordofthejars.diferencia.api.DiferenciaConfiguration;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

class DiferenciaExecutor {

    private static final Logger logger = Logger.getLogger(DiferenciaExecutor.class.getName());

    private Process startedProcess;

    void execute(Path binary, DiferenciaConfiguration diferenciaConfiguration) {

        if (startedProcess != null) {
            logger.log(Level.WARNING, "Local Diferencia is already running.");
            return;
        }

        final List<String> command = new ArrayList<>();
        command.add(binary.toString());
        command.add("start");
        command.addAll(diferenciaConfiguration.getCliArguments());

        logger.info(String.format("Executing binary at %s", command));

        try {
            startedProcess = new ProcessBuilder(command)
                .directory(binary.getParent().toFile())
                .start();
        } catch (IOException e) {
            throw new IllegalStateException("Could not start Hoverfly process", e);
        }
    }
}
