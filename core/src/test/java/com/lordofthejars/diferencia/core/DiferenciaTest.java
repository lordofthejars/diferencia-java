package com.lordofthejars.diferencia.core;

import com.lordofthejars.diferencia.api.DiferenciaConfiguration;
import com.lordofthejars.diferencia.api.DiferenciaConfigurationUpdate;
import com.lordofthejars.diferencia.api.DiferenciaMode;
import com.lordofthejars.diferencia.api.Stat;
import com.lordofthejars.diferencia.api.Stats;
import com.lordofthejars.diferencia.gateway.DiferenciaAdminClient;
import com.lordofthejars.diferencia.gateway.DiferenciaClient;
import java.io.IOException;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.nio.file.Paths;
import java.util.List;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.After;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assume.assumeTrue;

public class DiferenciaTest {

    //Used for testing with diferencia versions not released yet
    private static final String LOCAL_DIFERENCIA = "/Users/alex/go/src/github.com/lordofthejars/diferencia/diferencia";
    public static final String CLOCK_HOST = "http://worldclockapi.com";
    public static final String PATH_NOW = "/api/json/cet/now";

    private final OkHttpClient client = new OkHttpClient();
    private Diferencia diferencia;

    @Test
    public void should_become_healthy() throws IOException {

        // Precondition
        assumeTrue(isUpAndRunning(CLOCK_HOST));

        // Given
        diferencia = new Diferencia("http://worldclockapi.com/", CLOCK_HOST);

        // When
        diferencia.start();

        // Then
        final DiferenciaClient diferenciaClient = diferencia.getDiferenciaClient();
        assertThat(diferenciaClient.healthy()).isEqualTo(true);
    }

    @Test
    public void should_mirror_requests_between_primary_and_candidate() throws IOException {

        // Precondition
        assumeTrue(isUpAndRunning("http://rest.ensembl.org"));

        // Given
        diferencia = new Diferencia("http://rest.ensembl.org", "http://rest.ensembl.org");

        // When
        diferencia.start();

        // Then
        final String diferenciaUrl = diferencia.getDiferenciaUrl();
        final Response response = sendRequest(diferenciaUrl, "/info/ping");
        assertThat(response.code()).isEqualTo(HttpURLConnection.HTTP_OK);
    }

    @Test
    public void should_enable_mirroring_mode() throws IOException {

        // Precondition
        assumeTrue(isUpAndRunning(CLOCK_HOST));

        // Given
        final DiferenciaConfiguration.Builder configurationBuilder =
            new DiferenciaConfiguration.Builder(CLOCK_HOST, CLOCK_HOST)
                .withMirroring(true);
        diferencia = new Diferencia(configurationBuilder);

        // When
        diferencia.start();

        // Then
        final String diferenciaUrl = diferencia.getDiferenciaUrl();
        final Response response = sendRequest(diferenciaUrl, PATH_NOW);

        final JsonReader jsonReader = Json.createReader(new StringReader(response.body().string()));
        final JsonObject body = jsonReader.readObject();
        assertThat(body.get("currentFileTime")).isNotNull();

    }

    @Test
    public void should_mirror_requests_between_primary_and_candidate_and_fail_if_different() throws IOException {

        // Precondition
        assumeTrue(isUpAndRunning(CLOCK_HOST));

        // Given
        diferencia = new Diferencia(CLOCK_HOST, CLOCK_HOST);

        // When
        diferencia.start();

        // Then
        final String diferenciaUrl = diferencia.getDiferenciaUrl();
        final Response response = sendRequest(diferenciaUrl, PATH_NOW);
        assertThat(response.code()).isEqualTo(HttpURLConnection.HTTP_PRECON_FAILED);
    }

    @Test
    public void should_mirror_requests_between_primary_candidate_and_secondary_with_noise_detection() throws IOException {

        // Precondition
        assumeTrue(isUpAndRunning(CLOCK_HOST));

        // Given
        final DiferenciaConfiguration.Builder configurationBuilder =
            new DiferenciaConfiguration.Builder(CLOCK_HOST, CLOCK_HOST)
                .withSecondary(CLOCK_HOST).withNoiseDetection(true);

        diferencia = new Diferencia(configurationBuilder);
        
        // When
        diferencia.start();

        // Then
        final String diferenciaUrl = diferencia.getDiferenciaUrl(); 
        final Response response = sendRequest(diferenciaUrl, PATH_NOW);
        assertThat(response.code()).isEqualTo(HttpURLConnection.HTTP_OK);

    }

    @Test
    public void should_mirror_requests_between_primary_and_candidate_and_update_with_admin_client() throws IOException {

        // Precondition
        assumeTrue(isUpAndRunning(CLOCK_HOST));

        // Given
        diferencia = new Diferencia(CLOCK_HOST, CLOCK_HOST);

        // When
        diferencia.start();

        //If we do not update the configuration, the test should fail
        final DiferenciaConfigurationUpdate build = new DiferenciaConfigurationUpdate.Builder().withNoiseDetection(true)
            .withSecondary(CLOCK_HOST)
            .build();
        diferencia.getDiferenciaAdminClient().updateConfig(build);

        // Then
        final String diferenciaUrl = diferencia.getDiferenciaUrl();
        final Response response = sendRequest(diferenciaUrl, PATH_NOW);
        assertThat(response.code()).isEqualTo(HttpURLConnection.HTTP_OK);
    }

    @Test
    public void should_read_current_configuration() throws IOException {

        // Given
        diferencia = new Diferencia(CLOCK_HOST, CLOCK_HOST);

        // When
        diferencia.start();
        final DiferenciaConfiguration configuration = diferencia.getDiferenciaAdminClient().configuration();

        assertThat(configuration.getNoiseDetection()).isEqualTo(false);
        assertThat(configuration.getPrimary()).isEqualTo(CLOCK_HOST);
        assertThat(configuration.getDifferenceMode()).isEqualTo(DiferenciaMode.STRICT);
    }

    @Test
    public void should_return_success_in_stat_api_if_no_regression_occurred() throws IOException {

        // Precondition
        assumeTrue(isUpAndRunning(CLOCK_HOST));

        // Given

        final DiferenciaConfiguration.Builder configurationBuilder =
            new DiferenciaConfiguration.Builder(CLOCK_HOST, CLOCK_HOST)
                .withSecondary(CLOCK_HOST).withNoiseDetection(true);
        diferencia = new Diferencia(configurationBuilder.build());

        // When
        diferencia.start();
        final String diferenciaUrl = diferencia.getDiferenciaUrl();
        final Response response = sendRequest(diferenciaUrl, PATH_NOW);

        // Then
        assertThat(response.code()).isEqualTo(HttpURLConnection.HTTP_OK);

        final DiferenciaAdminClient diferenciaAdminClient = diferencia.getDiferenciaAdminClient();
        final Stats stats = diferenciaAdminClient.stats();
        final List<Stat> statList = stats.getStats();
        assertThat(statList).hasSize(1);
        Stat stat = statList.get(0);

        assertThat(stat.getMethod()).isEqualTo("GET");
        assertThat(stat.getPath()).isEqualTo(PATH_NOW);
        assertThat(stat.getSuccess()).isEqualTo(1);
        assertThat(stat.getErrors()).isEqualTo(0);
        assertThat(stat.getAveragePrimaryDuration()).isGreaterThan(0.0);
        assertThat(stat.getAverageCandidateDuration()).isGreaterThan(0.0);

    }

    @Test
    public void should_return_result_status_if_return_result_flag_set() throws IOException {

        // Precondition
        assumeTrue(isUpAndRunning(CLOCK_HOST));

        // Given
        final DiferenciaConfiguration.Builder configurationBuilder =
            new DiferenciaConfiguration.Builder(CLOCK_HOST, CLOCK_HOST)
                .withSecondary(CLOCK_HOST).withNoiseDetection(true).withReturnResult(true);

        diferencia = new Diferencia(configurationBuilder);

        // When
        diferencia.start();

        // Then
        final String diferenciaUrl = diferencia.getDiferenciaUrl();
        final Response response = sendRequest(diferenciaUrl, PATH_NOW);
        assertThat(response.code()).isEqualTo(HttpURLConnection.HTTP_OK);
        assertThat(response.body().string()).isNotEmpty();
    }

    @Test
    public void should_return_failed_elements_in_stat_api_if_regression_occurred() throws IOException {

        // Precondition
        assumeTrue(isUpAndRunning(CLOCK_HOST));

        // Given
        final DiferenciaConfiguration.Builder configurationBuilder =
            new DiferenciaConfiguration.Builder(CLOCK_HOST, CLOCK_HOST);
        diferencia = new Diferencia(configurationBuilder.build());

        // When
        diferencia.start();
        final String diferenciaUrl = diferencia.getDiferenciaUrl();
        final Response response = sendRequest(diferenciaUrl, PATH_NOW);

        // Then
        assertThat(response.code()).isEqualTo(HttpURLConnection.HTTP_PRECON_FAILED);
        final DiferenciaAdminClient diferenciaAdminClient = diferencia.getDiferenciaAdminClient();
        final Stats stats = diferenciaAdminClient.stats();

        final List<Stat> statList = stats.getStats();
        assertThat(statList).hasSize(1);
        Stat stat = statList.get(0);

        assertThat(stat.getMethod()).isEqualTo("GET");
        assertThat(stat.getPath()).isEqualTo(PATH_NOW);
        assertThat(stat.getSuccess()).isEqualTo(0);
        assertThat(stat.getErrors()).isEqualTo(1);
        assertThat(stat.getAveragePrimaryDuration()).isZero();
        assertThat(stat.getAverageCandidateDuration()).isZero();
    }

    private Response sendRequest(String diferenciaUrl, String path) throws IOException {

        final Request request = new Request.Builder()
            .addHeader("Content-Type", "application/json")
            .url(diferenciaUrl + path)
            .build();

        return client.newCall(request).execute();
    }

    private boolean isUpAndRunning(String url) throws IOException {
        final Request request = new Request.Builder()
            .url(url)
            .build();

        return client.newCall(request).execute()
            .code() == HttpURLConnection.HTTP_OK;
    }

    private void useLocalDiferencia() {
        diferencia.diferenciaHome = Paths.get(LOCAL_DIFERENCIA);
    }

    @After
    public void close() {
        if (diferencia != null) {
            diferencia.close();
        }
    }

}
