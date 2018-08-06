package com.lordofthejars.diferencia.assertj;

import com.lordofthejars.diferencia.api.Stats;
import com.lordofthejars.diferencia.core.Diferencia;
import com.lordofthejars.diferencia.gateway.DiferenciaAdminClient;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DiferenciaAssertionsTest {

    private static final String statsDocument = "[{"
        + "\"endpoint\": {"
        + "\"method\": \"GET\","
        + "\"path\": \"/\""
        + "},"
        + "\"errors\": 1"
        + "}]";

    @Mock
    Diferencia diferencia;

    @Mock
    DiferenciaAdminClient diferenciaAdminClient;

    @Test
    public void should_assert_if_no_errors_found() throws IOException {

        // Given

        when(diferenciaAdminClient.stats()).thenReturn(new Stats());
        when(diferencia.getDiferenciaAdminClient()).thenReturn(diferenciaAdminClient);

        // When
        assertThatCode(() -> DiferenciaAssertions.assertThat(diferencia).hasNoErrors())

        // Then
        .doesNotThrowAnyException();

    }

    @Test
    public void should_not_assert_if_error_found() throws IOException {

        // Given

        final Stats stats = Stats.fromInputStream(new ByteArrayInputStream(statsDocument.getBytes()));
        when(diferenciaAdminClient.stats()).thenReturn(stats);
        when(diferencia.getDiferenciaAdminClient()).thenReturn(diferenciaAdminClient);

        // When
        assertThatCode(() -> DiferenciaAssertions.assertThat(diferencia).hasNoErrors())

        // Then
        .hasMessageContaining("next errors found");

    }

    @Test
    public void should_assert_if_there_are_error_but_not_in_filtered_list() throws IOException {

        // Given
        final Stats stats = Stats.fromInputStream(new ByteArrayInputStream(statsDocument.getBytes()));
        when(diferenciaAdminClient.stats()).thenReturn(stats);
        when(diferencia.getDiferenciaAdminClient()).thenReturn(diferenciaAdminClient);

        // When
        assertThatCode(() -> DiferenciaAssertions.assertThat(diferencia)
            // Only exception thrown if it is a POST in / path
            .withFilter("POST", "/")
            .hasNoErrors())

        // Then
        .doesNotThrowAnyException();

    }

    @Test
    public void should_not_assert_if_error_fond_in_filtered_list_() throws IOException {

        // Given

        final Stats stats = Stats.fromInputStream(new ByteArrayInputStream(statsDocument.getBytes()));
        when(diferenciaAdminClient.stats()).thenReturn(stats);
        when(diferencia.getDiferenciaAdminClient()).thenReturn(diferenciaAdminClient);

        // When
        assertThatCode(() -> DiferenciaAssertions.assertThat(diferencia)
            .withFilter("GET", "/")
            .hasNoErrors())

        // Then
        .hasMessageContaining("it was found with errors");

    }

}
