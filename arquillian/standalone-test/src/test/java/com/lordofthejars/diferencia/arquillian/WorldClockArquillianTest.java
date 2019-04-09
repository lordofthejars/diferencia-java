package com.lordofthejars.diferencia.arquillian;

import com.lordofthejars.diferencia.arquillian.api.DiferenciaUrl;
import com.lordofthejars.diferencia.core.Diferencia;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Arquillian.class)
public class WorldClockArquillianTest {

    @DiferenciaUrl
    URL diferencia;

    @Test
    public void should_use_diferencia_to_detect_any_possible_regression() throws IOException {

        // Given
        final OkHttpClient client = new OkHttpClient();

        // When
        final Response response = sendRequest(diferencia.toExternalForm(), "/api/json/cet/now", client);

        // Then
        assertThat(response.code()).isEqualTo(HttpURLConnection.HTTP_PRECON_FAILED);
    }

    private Response sendRequest(String diferenciaUrl, String path, OkHttpClient client) throws IOException {

        final Request request = new Request.Builder()
            .addHeader("Content-Type", "application/json")
            .url(diferenciaUrl + path)
            .build();

        return client.newCall(request).execute();
    }

}
