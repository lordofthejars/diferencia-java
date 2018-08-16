package com.lordofthejars.diferencia.api;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;
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


    @Test
    public void should_read_configuration_from_map() {

        // Given
        final Map<String, String> conf = new HashMap<>();
        conf.put("primary", "http://localhost");
        conf.put("noiseDetection", "true");
        conf.put("ignoreValues", "a,b,c");
        conf.put("ignoreHeadersValues", "a, b, c");

        final DiferenciaConfiguration.Builder builder =
            new DiferenciaConfiguration.Builder(conf);

        // When
        final DiferenciaConfiguration diferenciaConfiguration = builder.build();

        // Then
        assertThat(diferenciaConfiguration.getPrimary()).isEqualTo("http://localhost");
        assertThat(diferenciaConfiguration.getNoiseDetection()).isEqualTo(true);
        assertThat(diferenciaConfiguration.getIgnoreValues())
            .containsExactly("a", "b", "c");
        assertThat(diferenciaConfiguration.getIgnoreHeadersValues())
            .containsExactly("a", "b", "c");

    }

    @Test
    public void should_clone_configuration() {

        // Given
        final Map<String, String> conf = new HashMap<>();
        conf.put("primary", "http://localhost");
        conf.put("noiseDetection", "true");
        conf.put("ignoreValues", "a,b,c");
        conf.put("differenceMode", "Strict");

        final DiferenciaConfiguration.Builder builder =
            new DiferenciaConfiguration.Builder(conf);
        final DiferenciaConfiguration originalDiferenciaConfiguration = builder.build();

        // When
        final DiferenciaConfiguration diferenciaConfiguration = new DiferenciaConfiguration.Builder(originalDiferenciaConfiguration).build();

        // Then
        assertThat(diferenciaConfiguration.getPrimary()).isEqualTo("http://localhost");
        assertThat(diferenciaConfiguration.getNoiseDetection()).isEqualTo(true);
        assertThat(diferenciaConfiguration.getDifferenceMode()).isEqualTo(DiferenciaMode.STRICT);
        assertThat(diferenciaConfiguration.getIgnoreValues()).containsExactly("a", "b", "c");

    }

}
