package com.lordofthejars.diferencia.api;

import java.io.ByteArrayInputStream;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DiferenciaConfigurationTest {

    private static final String CONFIG = "{"
        + "\"primary\": \"http://localhost\","
        + "\"port\": 8080,"
        + "\"differenceMode\": \"Strict\","
        + "\"noiseDetection\": true"
        + "}";

    @Test
    public void should_read_configuration_from_json() {

        // Given
        final DiferenciaConfiguration.Builder builder =
            new DiferenciaConfiguration.Builder(new ByteArrayInputStream(CONFIG.getBytes()));

        // When
        final DiferenciaConfiguration diferenciaConfiguration = builder.build();

        // Then
        assertThat(diferenciaConfiguration.getPrimary()).isEqualTo("http://localhost");
        assertThat(diferenciaConfiguration.getPort()).isEqualTo(8080);
        assertThat(diferenciaConfiguration.getDifferenceMode()).isEqualTo(DiferenciaMode.STRICT);
        assertThat(diferenciaConfiguration.getNoiseDetection()).isEqualTo(true);
    }


}
