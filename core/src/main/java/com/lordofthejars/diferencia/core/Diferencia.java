package com.lordofthejars.diferencia.core;

import com.lordofthejars.diferencia.api.DiferenciaConfiguration;
import com.lordofthejars.diferencia.gateway.DiferenciaAdminClient;
import com.lordofthejars.diferencia.gateway.DiferenciaClient;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static org.awaitility.Awaitility.given;

public class Diferencia implements AutoCloseable {

    private static final Logger logger = Logger.getLogger(Diferencia.class.getName());

    private DiferenciaConfiguration diferenciaConfiguration;
    private InstallManager installManager = new InstallManager();
    private DiferenciaExecutor diferenciaExecutor = new DiferenciaExecutor();

    private DiferenciaClient diferenciaClient;
    private DiferenciaAdminClient diferenciaAdminClient;

    Optional<Thread> shutdownThread = Optional.empty();

    //Now for testing purposes, but we might open this to the future to any implementators
    Path diferenciaHome;

    public Diferencia(String primary, String candidate) {
        this(new DiferenciaConfiguration.Builder(primary, candidate));
    }

    public Diferencia(DiferenciaConfiguration diferenciaConfiguration) {
        this.diferenciaConfiguration = diferenciaConfiguration;
    }

    public Diferencia(DiferenciaConfiguration.Builder builder) {
        this.diferenciaConfiguration = builder.build();
    }

    public void start() throws IOException {

        checkPortInUse(this.diferenciaConfiguration.getPortOrDefault());
        if (this.diferenciaConfiguration.isPrometheusEnabled()) {
            checkPortInUse(this.diferenciaConfiguration.getPrometheusPortOrDefault());
        }
        checkPortInUse(this.diferenciaConfiguration.getAdminPortOrDefault());

        this.diferenciaClient = createDiferenciaClient();
        this.diferenciaAdminClient = createDiferenciaAdminClient();

        shutdownThread = Optional.of(new Thread(this::close));
        Runtime.getRuntime().addShutdownHook(shutdownThread.get());


        final Path install = diferenciaHome == null ? installManager.install() : diferenciaHome;
        diferenciaExecutor.execute(install, diferenciaConfiguration);

        waitForDiferenciaToBecomeHealthy();
    }

    private void waitForDiferenciaToBecomeHealthy() {
        given().ignoreExceptions()
            .await().atMost(10, TimeUnit.SECONDS).until(diferenciaClient::healthy);
    }

    private DiferenciaAdminClient createDiferenciaAdminClient() {
        String protocol = this.diferenciaConfiguration.isHttps() ? "https" : "http";
        String host = "localhost";
        int port = this.diferenciaConfiguration.getAdminPortOrDefault();

        return new DiferenciaAdminClient(String.format("%s://%s:%d", protocol, host, port));
    }

    private DiferenciaClient createDiferenciaClient() {
        String protocol = this.diferenciaConfiguration.isHttps() ? "https" : "http";
        String host = "localhost";
        int port = this.diferenciaConfiguration.getPortOrDefault();

        return new DiferenciaClient(String.format("%s://%s:%d", protocol, host, port));
    }

    public String getDiferenciaUrl() {
        return this.diferenciaClient.getUrl();
    }

    @Override
    public void close() {

        logger.info("Destroying Diferencia process");

        diferenciaExecutor.destroy();

        if (diferenciaHome == null) {
            try {
                installManager.uninstall();
            } catch (IOException e) {
                // We can ignore it, since it is generated in tmp directory, it will be deleted next time OS is booted up
            }
        }

        try {
            shutdownThread.ifPresent(Runtime.getRuntime()::removeShutdownHook);
        } catch (IllegalStateException e) {
            // Ignoring this exception as it only means that the JVM is already shutting down
        }
    }

    public DiferenciaAdminClient getDiferenciaAdminClient() {
        return diferenciaAdminClient;
    }

    public DiferenciaClient getDiferenciaClient() {
        return diferenciaClient;
    }

    static void checkPortInUse(int port) {
        try (final ServerSocket ignored = new ServerSocket(port, 1, InetAddress.getLoopbackAddress())) {
        } catch (IOException e) {
            throw new IllegalStateException("Port is already in use: " + port);
        }
    }
}
