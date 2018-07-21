package com.lordofthejars.diferencia.core;

public class OperativeSystemConfig {

    private static final String DIFERENCIA_BINARY_FORMAT = "diferencia_%s_amd64%s";

    private String osName;

    OperativeSystemConfig(String osName) {
        this.osName = osName;
    }

    String resolveDiferenciaBinary() {
        final OperatingSystemFamily operativeSystem = OperatingSystemFamily.resolve(this.osName);

        String extension = "";

        if (operativeSystem == OperatingSystemFamily.WINDOWS) {
            extension = ".exe";
        }

        return String.format(DIFERENCIA_BINARY_FORMAT, operativeSystem.getLabel(), extension);

    }


}
