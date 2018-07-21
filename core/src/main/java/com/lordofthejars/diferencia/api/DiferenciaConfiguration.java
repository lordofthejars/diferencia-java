package com.lordofthejars.diferencia.api;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DiferenciaConfiguration {

    private static final String PORT = "--port";
    private static final String SERVICE_NAME = "--serviceName";
    private static final String PRIMARY  = "--primary";
    private static final String SECONDARY = "--secondary";
    private static final String CANDIDATE = "--candidate";
    private static final String DIFFERENCE = "--difference";
    private static final String UNSAFE = "--unsafe";
    private static final String NOISE_DETECTION = "--noisedetection";
    private static final String STORE_RESULTS = "--storeResults";
    private static final String LOG_LEVEL = "--logLevel";
    private static final String HEADERS = "--headers";
    private static final String IGNORE_HEADER_VALUES = "--ignoreHeadersValues";
    private static final String IGNORE_VALUES = "--ignoreValues";
    private static final String IGNORE_VALUES_FILE = "--ignoreValuesFile";
    private static final String PROMETHEUS = "--prometheus";
    private static final String PROMETHEUS_PORT = "--prometheusPort";
    private static final String ADMIN_PORT = "--adminPort";
    private static final String INSECURE_SKIP_VERIFY = "--insecureSkipVerify";
    private static final String CA_CERT = "--caCert";
    private static final String CLIENT_CERT = "--clientCert";
    private static final String CLIENT_KEY = "--clientKey";

    private Integer port;
    private String serviceName;
    private String primary;
    private String secondary;
    private String candidate;
    private String storeResults;
    private DiferenciaMode differenceMode;
    private Boolean noiseDetection;
    private Boolean allowUnsafeOperations;
    private Boolean prometheus;
    private Integer prometheusPort;
    private String logLevel;
    private Boolean headers;
    private List<String> ignoreHeadersValues;
    private List<String> ignoreValues;
    private String ignoreValuesFile;
    private Boolean insecureSkipVerify;
    private String caCert;
    private String clientCert;
    private String clientKey;
    private Integer adminPort;

    private List<String> extraArguments = null;

    private DiferenciaConfiguration() {
    }

    public List<String> getCliArguments() {
        final List<String> arguments = new ArrayList<>();

        if (port != null) {
            arguments.add(PORT);
            arguments.add(String.valueOf(port));
        }

        if (serviceName != null) {
            arguments.add(SERVICE_NAME);
            arguments.add(serviceName);
        }

        if (primary != null) {
            arguments.add(PRIMARY);
            arguments.add(primary);
        }

        if (secondary != null) {
            arguments.add(SECONDARY);
            arguments.add(secondary);
        }

        if (candidate != null) {
            arguments.add(CANDIDATE);
            arguments.add(candidate);
        }

        if (storeResults != null) {
            arguments.add(STORE_RESULTS);
            arguments.add(storeResults);
        }

        if (differenceMode != null) {
            arguments.add(DIFFERENCE);
            arguments.add(differenceMode.getName());
        }

        if (noiseDetection != null) {
            arguments.add(NOISE_DETECTION);
            arguments.add(String.valueOf(noiseDetection));
        }

        if (allowUnsafeOperations != null) {
            arguments.add(UNSAFE);
            arguments.add(String.valueOf(allowUnsafeOperations));
        }

        if (prometheus != null) {
            arguments.add(PROMETHEUS);
            arguments.add(String.valueOf(prometheus));
        }

        if (prometheusPort != null) {
            arguments.add(PROMETHEUS_PORT);
            arguments.add(String.valueOf(prometheusPort));
        }

        if (logLevel != null) {
            arguments.add(LOG_LEVEL);
            arguments.add(logLevel);
        }

        if (headers != null) {
            arguments.add(HEADERS);
            arguments.add(String.valueOf(headers));
        }

        if (ignoreHeadersValues != null) {
            arguments.add(IGNORE_HEADER_VALUES);
            arguments.add(toCsv(ignoreHeadersValues));
        }

        if (ignoreValues != null) {
            arguments.add(IGNORE_VALUES);
            arguments.add(toCsv(ignoreValues));
        }

        if (ignoreValuesFile != null) {
            arguments.add(IGNORE_VALUES_FILE);
            arguments.add(ignoreValuesFile);
        }

        if (adminPort != null) {
            arguments.add(ADMIN_PORT);
            arguments.add(String.valueOf(adminPort));
        }

        if (insecureSkipVerify != null) {
            arguments.add(INSECURE_SKIP_VERIFY);
            arguments.add(String.valueOf(insecureSkipVerify));
        }

        if (caCert != null) {
            arguments.add(CA_CERT);
            arguments.add(caCert);
        }

        if (clientCert != null) {
            arguments.add(CLIENT_CERT);
            arguments.add(clientCert);
        }

        if (clientKey != null) {
            arguments.add(CLIENT_KEY);
            arguments.add(clientKey);
        }

        if (extraArguments != null) {
            arguments.addAll(extraArguments);
        }

        return arguments;
    }

    private String toCsv(List<String> args) {
        return args.stream()
            .collect(Collectors.joining(", "));
    }

    public static class DiferenciaConfigurationBuilder {
        private DiferenciaConfiguration diferenciaConfiguration;

        private DiferenciaConfigurationBuilder() {
        }

        public static final DiferenciaConfigurationBuilder newConfigurationBuilder(String primary, String candidate) {
            DiferenciaConfigurationBuilder diferenciaConfigurationBuilder = new DiferenciaConfigurationBuilder();
            diferenciaConfigurationBuilder.diferenciaConfiguration = new DiferenciaConfiguration();
            diferenciaConfigurationBuilder.diferenciaConfiguration.primary = primary;
            diferenciaConfigurationBuilder.diferenciaConfiguration.candidate = candidate;

            return diferenciaConfigurationBuilder;
        }

        public DiferenciaConfigurationBuilder withPort(Integer port) {
            diferenciaConfiguration.port = port;
            return this;
        }

        public DiferenciaConfigurationBuilder withServiceName(String serviceName) {
            diferenciaConfiguration.serviceName = serviceName;
            return this;
        }

        public DiferenciaConfigurationBuilder withSecondary(String secondary) {
            diferenciaConfiguration.secondary = secondary;
            return this;
        }

        public DiferenciaConfigurationBuilder withStoreResults(String storeResults) {
            diferenciaConfiguration.storeResults = storeResults;
            return this;
        }

        public DiferenciaConfigurationBuilder withDiferenciaMode(DiferenciaMode diferenciaMode) {
            diferenciaConfiguration.differenceMode = diferenciaMode;
            return this;
        }

        public DiferenciaConfigurationBuilder withNoiseDetection(Boolean noiseDetection) {
            diferenciaConfiguration.noiseDetection = noiseDetection;
            return this;
        }

        public DiferenciaConfigurationBuilder withAllowUnsafeOperations(Boolean unsafe) {
            diferenciaConfiguration.allowUnsafeOperations = unsafe;
            return this;
        }

        public DiferenciaConfigurationBuilder withPrometheus(Boolean prometheus) {
            diferenciaConfiguration.prometheus = prometheus;
            return this;
        }

        public DiferenciaConfigurationBuilder withPrometheusPort(Integer port) {
            diferenciaConfiguration.prometheusPort = port;
            return this;
        }

        public DiferenciaConfigurationBuilder withLogLevel(String loglevel) {
            diferenciaConfiguration.logLevel = loglevel;
            return  this;
        }

        public DiferenciaConfigurationBuilder withHeaders(Boolean headers) {
            diferenciaConfiguration.headers = headers;
            return this;
        }

        public DiferenciaConfigurationBuilder withIgnoreHeadersValues(List<String> ignoreHeadersValues) {
            diferenciaConfiguration.ignoreHeadersValues = ignoreHeadersValues;
            return this;
        }

        public DiferenciaConfigurationBuilder withIgnoreValues(List<String> ignoreValues) {
            diferenciaConfiguration.ignoreValues = ignoreValues;
            return this;
        }

        public DiferenciaConfigurationBuilder withIgnoreValuesFile(String ignoreValuesFile) {
            diferenciaConfiguration.ignoreValuesFile = ignoreValuesFile;
            return this;
        }

        public DiferenciaConfigurationBuilder withAdminPort(Integer adminPort) {
            diferenciaConfiguration.adminPort = adminPort;
            return this;
        }

        public DiferenciaConfigurationBuilder withInsecureSkipVerify(Boolean insecureSkipVerify) {
            diferenciaConfiguration.insecureSkipVerify = insecureSkipVerify;
            return this;
        }

        public DiferenciaConfigurationBuilder withCaCert(Path caCert) {
            diferenciaConfiguration.caCert = caCert.toAbsolutePath().toString();
            return this;
        }

        public DiferenciaConfigurationBuilder withClientCert(Path clientCert) {
            diferenciaConfiguration.clientCert = clientCert.toAbsolutePath().toString();
            return this;
        }

        public DiferenciaConfigurationBuilder withClientKey(Path clientKey) {
            diferenciaConfiguration.clientKey = clientKey.toAbsolutePath().toString();
            return this;
        }

        public DiferenciaConfiguration build() {
            return diferenciaConfiguration;
        }

    }

}
