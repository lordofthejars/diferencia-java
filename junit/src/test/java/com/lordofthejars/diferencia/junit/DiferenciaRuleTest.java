package com.lordofthejars.diferencia.junit;

import java.io.IOException;
import java.net.HttpURLConnection;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.ClassRule;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DiferenciaRuleTest {

    @ClassRule
    public static DiferenciaRule diferenciaRule =
        new DiferenciaRule("http://now.httpbin.org/", "http://now.httpbin.org/");

    private final OkHttpClient client = new OkHttpClient();

    @Test
    public void should_use_diferencia_to_detect_any_possible_regression() throws IOException {
        final String diferenciaUrl = diferenciaRule.getDiferenciaUrl();
        final Response response = sendRequest(diferenciaUrl, "/");
        assertThat(response.code()).isEqualTo(HttpURLConnection.HTTP_PRECON_FAILED);
    }

    private Response sendRequest(String diferenciaUrl, String path) throws IOException {

        final Request request = new Request.Builder()
            .addHeader("Content-Type", "application/json")
            .url(diferenciaUrl + path)
            .build();

        return client.newCall(request).execute();
    }

}
