package com.lordofthejars.diferencia.api;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DiferenciaConfigurationUpdateTest {

    @Test
    public void should_serialize_configuration_object() {

        // Given
        final DiferenciaConfigurationUpdate diferenciaConfigurationUpdate = new DiferenciaConfigurationUpdate.Builder().withNoiseDetection(true)
            .withSecondary("http://localhost")
            .withMode(DiferenciaMode.SUBSET)
            .build();

        // When
        final String configurationString = diferenciaConfigurationUpdate.toJson();

        // Then
        final JsonValue parse = Json.parse(configurationString);
        final JsonObject configurationJsonObject = parse.asObject();

        assertThat(configurationJsonObject.getString("noiseDetection", ""))
            .isEqualTo("true");
        assertThat(configurationJsonObject.getString("secondary", ""))
            .isEqualTo("http://localhost");
        assertThat(configurationJsonObject.getString("mode", ""))
            .isEqualTo("Subset");
    }

}
