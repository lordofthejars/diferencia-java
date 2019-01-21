package com.lordofthejars.diferencia.api;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class DiferenciaConfiguration {

    private static final int DEFAULT_PORT = 8080;
    private static final int DEFAULT_PROMETHEUS_PORT = 8081;
    private static final int DEFAULT_ADMIN_PORT = 8082;

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
    private static final String FORCE_PLAIN_TEXT = "--forcePlainText";
    private static final String MIRRORING = "--mirroring";
    private static final String RETURN_RESULT = "--returnResult";

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
    private Boolean forcePlainText;
    private Boolean mirroring;
    private Boolean returnResult;

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

        if (forcePlainText != null) {
            arguments.add(FORCE_PLAIN_TEXT);
            arguments.add(String.valueOf(forcePlainText));
        }

        if (mirroring != null) {
            arguments.add(MIRRORING);
            arguments.add(String.valueOf(mirroring));
        }

        if (returnResult != null) {
            arguments.add(RETURN_RESULT);
            arguments.add(String.valueOf(returnResult));
        }

        if (extraArguments != null) {
            arguments.addAll(extraArguments);
        }

        return arguments;
    }

    public Integer getPort() {
        return port;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getPrimary() {
        return primary;
    }

    public String getSecondary() {
        return secondary;
    }

    public String getCandidate() {
        return candidate;
    }

    public String getStoreResults() {
        return storeResults;
    }

    public DiferenciaMode getDifferenceMode() {
        return differenceMode;
    }

    public Boolean getNoiseDetection() {
        return noiseDetection;
    }

    public Boolean getAllowUnsafeOperations() {
        return allowUnsafeOperations;
    }

    public Boolean getPrometheus() {
        return prometheus;
    }

    public Integer getPrometheusPort() {
        return prometheusPort;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public Boolean getHeaders() {
        return headers;
    }

    public List<String> getIgnoreHeadersValues() {
        return ignoreHeadersValues;
    }

    public List<String> getIgnoreValues() {
        return ignoreValues;
    }

    public String getIgnoreValuesFile() {
        return ignoreValuesFile;
    }

    public Boolean getInsecureSkipVerify() {
        return insecureSkipVerify;
    }

    public String getCaCert() {
        return caCert;
    }

    public String getClientCert() {
        return clientCert;
    }

    public String getClientKey() {
        return clientKey;
    }

    public Integer getAdminPort() {
        return adminPort;
    }

    public Boolean getForcePlainText() {
        return forcePlainText;
    }

    public Boolean getMirroring() {
        return mirroring;
    }

    public static String getReturnResult() {
        return RETURN_RESULT;
    }

    public List<String> getExtraArguments() {
        return extraArguments;
    }

    public int getPortOrDefault() {
        if (this.port == null) {
            return DEFAULT_PORT;
        }

        return this.port;
    }

    public int getPrometheusPortOrDefault() {
        if (this.prometheusPort == null) {
            return DEFAULT_PROMETHEUS_PORT;
        }

        return this.prometheusPort;
    }

    public int getAdminPortOrDefault() {
        if (this.adminPort == null) {
            return DEFAULT_ADMIN_PORT;
        }

        return this.adminPort;
    }

    public boolean isHttps() {
        return false;
    }

    public boolean isPrometheusEnabled() {
        return this.prometheus != null && this.prometheus;
    }

    private String toCsv(List<String> args) {
        return args.stream()
            .collect(Collectors.joining(", "));
    }

    public static class Builder {
        private DiferenciaConfiguration diferenciaConfiguration;

        public Builder(final DiferenciaConfiguration diferenciaConfiguration) {
            this.diferenciaConfiguration = new DiferenciaConfiguration();
            copyMatchingFields(diferenciaConfiguration, this.diferenciaConfiguration);
        }

        private static void copyMatchingFields( Object fromObj, Object toObj ) {
            if ( fromObj == null || toObj == null )
                throw new NullPointerException("Source and destination objects must be non-null");

            Class fromClass = fromObj.getClass();
            Class toClass = toObj.getClass();

            Field[] fields = fromClass.getDeclaredFields();
            for ( Field f : fields ) {
                try {
                    Field t = toClass.getDeclaredField( f.getName() );

                    if ( t.getType() == f.getType() ) {
                        if ( t.getType() == String.class
                            || t.getType() == int.class || t.getType() == Integer.class
                            || t.getType() == char.class || t.getType() == Character.class
                            || t.getType() == boolean.class ||t.getType() == Boolean.class
                            || t.getType().isEnum()) {
                            f.setAccessible(true);
                            t.setAccessible(true);
                            t.set( toObj, f.get(fromObj) );
                        } else if ( t.getType() == List.class  ) {
                            f.setAccessible(true);
                            t.setAccessible(true);
                            final Object o = t.get(fromObj);
                            if (o != null) {
                                // Since we know that all are ArrayLists and of type string
                                final List obj = (List) o;
                                final List cloned = new ArrayList();
                                cloned.addAll(obj);
                                t.set(toObj, cloned);
                            }
                        }
                    }
                } catch (NoSuchFieldException ex) {
                } catch (IllegalAccessException ex) {
                }
            }
        }

        public Builder(final Map<String, String> configuration) {
            this.diferenciaConfiguration = new DiferenciaConfiguration();

            this.diferenciaConfiguration.primary = configuration.getOrDefault("primary", "");
            this.diferenciaConfiguration.candidate = configuration.getOrDefault("candidate", "");
            this.diferenciaConfiguration.secondary = configuration.getOrDefault("secondary", null);

            if (configuration.containsKey("port")) {
                this.diferenciaConfiguration.port = Integer.valueOf(configuration.get("port"));
            }

            if (configuration.containsKey("prometheusPort")) {
                this.diferenciaConfiguration.prometheusPort = Integer.valueOf(configuration.get("prometheusPort"));
            }

            if (configuration.containsKey("adminPort")) {
                this.diferenciaConfiguration.adminPort = Integer.valueOf(configuration.get("adminPort"));
            }

            this.diferenciaConfiguration.serviceName = configuration.getOrDefault("serviceName", null);
            this.diferenciaConfiguration.storeResults = configuration.getOrDefault("storeResults", null);

            if (configuration.containsKey("differenceMode")) {
                this.diferenciaConfiguration.differenceMode =
                    DiferenciaMode.valueOf(configuration.get("differenceMode").toUpperCase());
            }

            if (configuration.containsKey("noiseDetection")) {
                this.diferenciaConfiguration.noiseDetection =
                    Boolean.valueOf(configuration.get("noiseDetection"));
            }
            if (configuration.containsKey("ignoreValues")) {
                this.diferenciaConfiguration.ignoreValues = Arrays.asList(configuration.get("ignoreValues").split(",\\s*"));
            }

            this.diferenciaConfiguration.ignoreValuesFile = configuration.getOrDefault("ignoreValuesFile", null);

            if (configuration.containsKey("headers")) {
                this.diferenciaConfiguration.headers = Boolean.valueOf(configuration.get("headers"));
            }

            if (configuration.containsKey("ignoreHeadersValues")) {
                this.diferenciaConfiguration.ignoreHeadersValues = Arrays.asList(configuration.get("ignoreHeadersValues").split(",\\s*"));
            }

            if (configuration.containsKey("allowUnsafeOperartions")) {
                diferenciaConfiguration.allowUnsafeOperations =
                    Boolean.valueOf(configuration.get("allowUnsafeOperartions"));
            }

            if (configuration.containsKey("insecureSkipVerify")) {
                diferenciaConfiguration.insecureSkipVerify =
                    Boolean.valueOf(configuration.get("insecureSkipVerify"));
            }

            diferenciaConfiguration.caCert = configuration.getOrDefault("caCert", null);
            diferenciaConfiguration.clientCert = configuration.getOrDefault("clientCert", null);
            diferenciaConfiguration.clientKey = configuration.getOrDefault("clientKey", null);

            if (configuration.containsKey("prometheus")) {
                diferenciaConfiguration.prometheus = Boolean.valueOf(configuration.get("prometheus"));
            }

            if (configuration.containsKey("forcePlainText")) {
                diferenciaConfiguration.forcePlainText = Boolean.valueOf(configuration.get("forcePlainText"));
            }

            if (configuration.containsKey("mirroring")) {
                diferenciaConfiguration.mirroring = Boolean.valueOf(configuration.get("mirroring"));
            }

            if (configuration.containsKey("returnResult")) {
                diferenciaConfiguration.returnResult = Boolean.valueOf(configuration.get("returnResult"));
            }

        }

        public Builder(final InputStream inputStream) {
            this.diferenciaConfiguration = new DiferenciaConfiguration();
            try (final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                final JsonValue parse = Json.parse(reader);
                final JsonObject configuration = parse.asObject();

                this.diferenciaConfiguration.primary = configuration.getString("primary", "");
                this.diferenciaConfiguration.candidate = configuration.getString("candidate", "");
                this.diferenciaConfiguration.secondary = configuration.getString("secondary", null);
                this.diferenciaConfiguration.port = configuration.getInt("port", 0);
                this.diferenciaConfiguration.prometheusPort = configuration.getInt("prometheusPort", 0);
                this.diferenciaConfiguration.adminPort = configuration.getInt("adminPort", 0);
                this.diferenciaConfiguration.serviceName = configuration.getString("serviceName", "");
                this.diferenciaConfiguration.storeResults = configuration.getString("storeResults", null);
                this.diferenciaConfiguration.differenceMode = DiferenciaMode.valueOf(configuration.getString("differenceMode", "").toUpperCase());
                this.diferenciaConfiguration.noiseDetection = configuration.getBoolean("noiseDetection", false);
                this.diferenciaConfiguration.mirroring = configuration.getBoolean("mirroring", false);
                this.diferenciaConfiguration.returnResult = configuration.getBoolean("returnResult", false);

                final JsonValue ignoreValues = configuration.get("ignoreValues");
                if (ignoreValues != null) {
                    this.diferenciaConfiguration.ignoreValues = toListOfString(ignoreValues);
                }

                this.diferenciaConfiguration.ignoreValuesFile = configuration.getString("ignoreValuesFile", null);
                this.diferenciaConfiguration.headers = configuration.getBoolean("headers", false);

                final JsonValue ignoreHeaders = configuration.get("ignoreHeadersValues");
                if (ignoreHeaders != null) {
                    this.diferenciaConfiguration.ignoreHeadersValues = toListOfString(ignoreHeaders);
                }

                diferenciaConfiguration.allowUnsafeOperations = configuration.getBoolean("allowUnsafeOperartions", false);
                diferenciaConfiguration.insecureSkipVerify = configuration.getBoolean("insecureSkipVerify", false);
                diferenciaConfiguration.caCert = configuration.getString("caCert", null);
                diferenciaConfiguration.clientCert = configuration.getString("clientCert", null);
                diferenciaConfiguration.clientKey = configuration.getString("clientKey", null);
                diferenciaConfiguration.prometheus = configuration.getBoolean("prometheus", false);
                diferenciaConfiguration.forcePlainText = configuration.getBoolean("forcePlainText", false);

            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        private List<String> toListOfString(JsonValue value) {
            Iterable<JsonValue> valueIterable = () -> value.asArray().iterator();

            return StreamSupport.stream(valueIterable.spliterator(), false)
                .map(element -> element.asString())
                .collect(Collectors.toList());
        }

        public Builder(String primary, String candidate) {
            this.diferenciaConfiguration = new DiferenciaConfiguration();
            this.diferenciaConfiguration.primary = primary;
            this.diferenciaConfiguration.candidate = candidate;

        }

        public Builder withPrimary(String primary) {
            diferenciaConfiguration.primary = primary;
            return this;
        }

        public Builder withCandidate(String candidate) {
            diferenciaConfiguration.candidate = candidate;
            return this;
        }

        public Builder withPort(Integer port) {
            diferenciaConfiguration.port = port;
            return this;
        }

        public Builder withServiceName(String serviceName) {
            diferenciaConfiguration.serviceName = serviceName;
            return this;
        }

        public Builder withSecondary(String secondary) {
            diferenciaConfiguration.secondary = secondary;
            return this;
        }

        public Builder withStoreResults(String storeResults) {
            diferenciaConfiguration.storeResults = storeResults;
            return this;
        }

        public Builder withDiferenciaMode(DiferenciaMode diferenciaMode) {
            diferenciaConfiguration.differenceMode = diferenciaMode;
            return this;
        }

        public Builder withNoiseDetection(Boolean noiseDetection) {
            diferenciaConfiguration.noiseDetection = noiseDetection;
            return this;
        }

        public Builder withAllowUnsafeOperations(Boolean unsafe) {
            diferenciaConfiguration.allowUnsafeOperations = unsafe;
            return this;
        }

        public Builder withPrometheus(Boolean prometheus) {
            diferenciaConfiguration.prometheus = prometheus;
            return this;
        }

        public Builder withPrometheusPort(Integer port) {
            diferenciaConfiguration.prometheusPort = port;
            return this;
        }

        public Builder withLogLevel(String loglevel) {
            diferenciaConfiguration.logLevel = loglevel;
            return  this;
        }

        public Builder withHeaders(Boolean headers) {
            diferenciaConfiguration.headers = headers;
            return this;
        }

        public Builder withIgnoreHeadersValues(List<String> ignoreHeadersValues) {
            diferenciaConfiguration.ignoreHeadersValues = ignoreHeadersValues;
            return this;
        }

        public Builder withIgnoreValues(List<String> ignoreValues) {
            diferenciaConfiguration.ignoreValues = ignoreValues;
            return this;
        }

        public Builder withIgnoreValuesFile(String ignoreValuesFile) {
            diferenciaConfiguration.ignoreValuesFile = ignoreValuesFile;
            return this;
        }

        public Builder withAdminPort(Integer adminPort) {
            diferenciaConfiguration.adminPort = adminPort;
            return this;
        }

        public Builder withInsecureSkipVerify(Boolean insecureSkipVerify) {
            diferenciaConfiguration.insecureSkipVerify = insecureSkipVerify;
            return this;
        }

        public Builder withCaCert(Path caCert) {
            diferenciaConfiguration.caCert = caCert.toAbsolutePath().toString();
            return this;
        }

        public Builder withClientCert(Path clientCert) {
            diferenciaConfiguration.clientCert = clientCert.toAbsolutePath().toString();
            return this;
        }

        public Builder withClientKey(Path clientKey) {
            diferenciaConfiguration.clientKey = clientKey.toAbsolutePath().toString();
            return this;
        }

        public Builder withForcePlainText(Boolean forcePlainText) {
            diferenciaConfiguration.forcePlainText = forcePlainText;
            return this;
        }

        public Builder withExtraArguments(List<String> arguments) {
            diferenciaConfiguration.extraArguments = arguments;
            return this;
        }

        public Builder withMirroring(Boolean mirroring) {
            diferenciaConfiguration.mirroring = mirroring;
            return this;
        }

        public Builder withReturnResult(Boolean returnResult) {
            diferenciaConfiguration.returnResult = returnResult;
            return this;
        }

        public DiferenciaConfiguration build() {
            return diferenciaConfiguration;
        }

    }

}
