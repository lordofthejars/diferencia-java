package com.lordofthejars.diferencia.junit;

import com.lordofthejars.diferencia.assertj.DiferenciaAssertions;
import com.lordofthejars.diferencia.core.Diferencia;
import com.lordofthejars.diferencia.junit.api.DiferenciaConfig;
import com.lordofthejars.diferencia.junit.api.DiferenciaCore;
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(DiferenciaExtension.class)
@DiferenciaCore(primary = "http://worldclockapi.com", candidate = "http://worldclockapi.com",
    config = @DiferenciaConfig(noiseDetection = true, secondary = "http://worldclockapi.com"))
public class DiferenciaExtensionTest {

    private final OkHttpClient client = new OkHttpClient();

    @Test
    public void should_use_diferencia_to_detect_any_possible_regression(Diferencia diferencia) throws IOException {

        final String diferenciaUrl = diferencia.getDiferenciaUrl();
        sendRequest(diferenciaUrl, "/api/json/cet/now");

        DiferenciaAssertions.assertThat(diferencia).hasNoErrors();

    }

    private Response sendRequest(String diferenciaUrl, String path) throws IOException {

        final Request request = new Request.Builder()
            .addHeader("Content-Type", "application/json")
            .url(diferenciaUrl + path)
            .build();

        return client.newCall(request).execute();
    }

}
