package com.lordofthejars.diferencia.core;

import com.lordofthejars.diferencia.api.DiferenciaConfiguration;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.Optional;

public class Diferencia implements AutoCloseable {

    private DiferenciaConfiguration diferenciaConfiguration;
    private InstallManager installManager = new InstallManager();
    private DiferenciaExecutor diferenciaExecutor = new DiferenciaExecutor();

    Optional<Thread> shutdownThread = Optional.empty();

    public Diferencia(DiferenciaConfiguration diferenciaConfiguration) {
        this.diferenciaConfiguration = diferenciaConfiguration;
    }

    public Diferencia(DiferenciaConfiguration.DiferenciaConfigurationBuilder diferenciaConfigurationBuilder) {
        this.diferenciaConfiguration = diferenciaConfigurationBuilder.build();
    }

    public void start() {
        shutdownThread = Optional.of(new Thread(this::close));
        Runtime.getRuntime().addShutdownHook(shutdownThread.get());
    }

    @Override
    public void close() {

        try {
            shutdownThread.ifPresent(Runtime.getRuntime()::removeShutdownHook);
        } catch (IllegalStateException e) {
            // Ignoring this exception as it only means that the JVM is already shutting down
        }
    }

    static void checkPortInUse(int port) {
        try (final ServerSocket ignored = new ServerSocket(port, 1, InetAddress.getLoopbackAddress())) {
        } catch (IOException e) {
            throw new IllegalStateException("Port is already in use: " + port);
        }
    }
}
