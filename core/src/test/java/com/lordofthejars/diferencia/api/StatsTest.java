package com.lordofthejars.diferencia.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StatsTest {

    @Test
    public void should_parse_stats_api() throws IOException {

        // Given

        final InputStream statsOutput = StatsTest.class.getClassLoader().getResourceAsStream("stats.json");

        // When

        final Stats stats = Stats.fromInputStream(statsOutput);

        // Then

        assertThat(stats.getStats()).hasSize(1);
        final Stat stat = stats.getStats().get(0);

        assertThat(stat.getSuccess()).isEqualTo(0);
        assertThat(stat.getErrors()).isEqualTo(1);

        final List<ErrorDetail> errorDetails = stat.getErrorDetails();
        assertThat(errorDetails).hasSize(1);

        final ErrorDetail errorDetail = errorDetails.get(0);
        assertThat(errorDetail.getBodyDiff()).isNotEmpty();
        assertThat(errorDetail.getOriginalBody()).isEmpty();
        assertThat(errorDetail.getFullUri()).isNotEmpty();
        assertThat(errorDetail.getHeadersDiff()).isEmpty();
        assertThat(errorDetail.getStatusDiff()).isEmpty();
        final Map<String, String> originalHeaders = errorDetail.getOriginalHeaders();
        assertThat(originalHeaders).hasSize(2);

        assertThat(originalHeaders)
            .containsEntry("Accept", "*/*")
            .containsEntry("User-Agent", "curl/7.54.0");

    }


}
