package com.lordofthejars.diferencia.arquillian;

import com.lordofthejars.diferencia.arquillian.api.DiferenciaUrl;
import com.lordofthejars.diferencia.core.Diferencia;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.arquillian.container.chameleon.api.ChameleonTarget;
import org.arquillian.container.chameleon.runner.ArquillianChameleon;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(ArquillianChameleon.class)
@ChameleonTarget("wildfly:11.0.0.Final:managed")
@RunAsClient
@Ignore
public class DiferenciaContainerTest {

    @Deployment(testable = false, name = "v1")
    public static WebArchive deployServiceV1() {
        return ShrinkWrap.create(WebArchive.class)
            .addClasses(V1Application.class, V1Resource.class);
    }

    @Deployment(testable = false, name = "v11")
    public static WebArchive deployServiceV11() {
        return ShrinkWrap.create(WebArchive.class)
            .addClasses(V1Application.class, V11Resource.class);
    }

    @DiferenciaUrl
    URL diferencia;

    @Test
    public void should_fail_if_responses_are_different() throws IOException {
        // Given
        final OkHttpClient client = new OkHttpClient();

        // When
        final Response response = sendRequest(diferencia.toExternalForm(), "/v1/hello", client);

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
