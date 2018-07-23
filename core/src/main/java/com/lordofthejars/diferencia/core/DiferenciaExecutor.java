package com.lordofthejars.diferencia.core;

import com.lordofthejars.diferencia.api.DiferenciaConfiguration;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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

        logger.info(String.format("Executing binary: %s", command.stream().collect(Collectors.joining(" "))));

        try {
            startedProcess = new ProcessBuilder(command)
                .directory(binary.getParent().toFile())
                .start();
        } catch (IOException e) {
            throw new IllegalStateException("Could not start Hoverfly process", e);
        }
    }

    void destroy() {
        if (this.startedProcess != null) {
            this.startedProcess.destroy();

            // Some platforms terminate process asynchronously, eg. Windows, and cannot guarantee that synchronous file deletion
            // can acquire file lock
            // lordofthejars: Thanks Hoverfly-Java for this tip
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            Future<Integer> future = executorService.submit((Callable<Integer>) this.startedProcess::waitFor);
            try {
                future.get(5, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                logger.log(Level.WARNING, "Timeout when waiting for Diferencia process to terminate.");
            }
            executorService.shutdownNow();
        }
    }
}
