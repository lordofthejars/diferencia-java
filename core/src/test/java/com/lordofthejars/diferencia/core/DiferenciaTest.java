package com.lordofthejars.diferencia.core;

import com.lordofthejars.diferencia.api.DiferenciaConfiguration;
import com.lordofthejars.diferencia.api.DiferenciaConfigurationUpdate;
import com.lordofthejars.diferencia.api.Stat;
import com.lordofthejars.diferencia.api.Stats;
import com.lordofthejars.diferencia.gateway.DiferenciaAdminClient;
import com.lordofthejars.diferencia.gateway.DiferenciaClient;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.file.Paths;
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

    private final OkHttpClient client = new OkHttpClient();
    private Diferencia diferencia;

    @Test
    public void should_become_healthy() throws IOException {

        // Precondition
        assumeTrue(isUpAndRunning("http://now.httpbin.org"));

        // Given
        diferencia = new Diferencia("http://now.httpbin.org/", "http://now.httpbin.org/");

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
    public void should_mirror_requests_between_primary_and_candidate_and_fail_if_different() throws IOException {

        // Precondition
        assumeTrue(isUpAndRunning("http://now.httpbin.org"));

        // Given
        diferencia = new Diferencia("http://now.httpbin.org/", "http://now.httpbin.org/");

        // When
        diferencia.start();

        // Then
        final String diferenciaUrl = diferencia.getDiferenciaUrl();
        final Response response = sendRequest(diferenciaUrl, "/");
        assertThat(response.code()).isEqualTo(HttpURLConnection.HTTP_PRECON_FAILED);
    }

    @Test
    public void should_mirror_requests_between_primary_candidate_and_secondary_with_noise_detection() throws IOException {

        // Precondition
        assumeTrue(isUpAndRunning("http://now.httpbin.org"));

        // Given
        final DiferenciaConfiguration.Builder configurationBuilder =
            new DiferenciaConfiguration.Builder("http://now.httpbin.org", "http://now.httpbin.org")
                .withSecondary("http://now.httpbin.org").withNoiseDetection(true);

        diferencia = new Diferencia(configurationBuilder);
        
        // When
        diferencia.start();

        // Then
        final String diferenciaUrl = diferencia.getDiferenciaUrl(); 
        final Response response = sendRequest(diferenciaUrl, "/"); 
        assertThat(response.code()).isEqualTo(HttpURLConnection.HTTP_OK);

    }

    @Test
    public void should_mirror_requests_between_primary_and_candidate_and_update_with_admin_client() throws IOException {

        // Precondition
        assumeTrue(isUpAndRunning("http://now.httpbin.org"));

        // Given
        diferencia = new Diferencia("http://now.httpbin.org/", "http://now.httpbin.org/");

        // When
        diferencia.start();

        //If we do not update the configuration, the test should fail
        final DiferenciaConfigurationUpdate build = new DiferenciaConfigurationUpdate.Builder().withNoiseDetection(true)
            .withSecondary("http://now.httpbin.org/")
            .build();
        diferencia.getDiferenciaAdminClient().updateConfig(build);

        // Then
        final String diferenciaUrl = diferencia.getDiferenciaUrl();
        final Response response = sendRequest(diferenciaUrl, "/");
        assertThat(response.code()).isEqualTo(HttpURLConnection.HTTP_OK);
    }

    @Test
    public void should_return_zero_elements_in_stat_api_if_no_regression_occurred() throws IOException {

        // Precondition
        assumeTrue(isUpAndRunning("http://now.httpbin.org"));

        // Given

        final DiferenciaConfiguration.Builder configurationBuilder =
            new DiferenciaConfiguration.Builder("http://now.httpbin.org", "http://now.httpbin.org")
                .withSecondary("http://now.httpbin.org").withNoiseDetection(true);
        diferencia = new Diferencia(configurationBuilder.build());

        diferencia.diferenciaHome = Paths.get(LOCAL_DIFERENCIA);

        // When
        diferencia.start();
        final String diferenciaUrl = diferencia.getDiferenciaUrl();
        final Response response = sendRequest(diferenciaUrl, "/");

        // Then
        assertThat(response.code()).isEqualTo(HttpURLConnection.HTTP_OK);
        final DiferenciaAdminClient diferenciaAdminClient = diferencia.getDiferenciaAdminClient();
        final Stats stats = diferenciaAdminClient.stats();
        assertThat(stats.isEmpty()).isTrue();

    }

    @Test
    public void should_return_failed_elements_in_stat_api_if_regression_occurred() throws IOException {

        // Precondition
        assumeTrue(isUpAndRunning("http://now.httpbin.org"));

        // Given
        final DiferenciaConfiguration.Builder configurationBuilder =
            new DiferenciaConfiguration.Builder("http://now.httpbin.org", "http://now.httpbin.org");
        diferencia = new Diferencia(configurationBuilder.build());

        diferencia.diferenciaHome = Paths.get(LOCAL_DIFERENCIA);

        // When
        diferencia.start();
        final String diferenciaUrl = diferencia.getDiferenciaUrl();
        final Response response = sendRequest(diferenciaUrl, "/");

        // Then
        assertThat(response.code()).isEqualTo(HttpURLConnection.HTTP_PRECON_FAILED);
        final DiferenciaAdminClient diferenciaAdminClient = diferencia.getDiferenciaAdminClient();
        final Stats stats = diferenciaAdminClient.stats();

        assertThat(stats.isEmpty()).isFalse();
        assertThat(stats.getStats()).containsExactly(new Stat("GET", "/", 1));
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

    @After
    public void close() {
        if (diferencia != null) {
            diferencia.close();
        }
    }

}
