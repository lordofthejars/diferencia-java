package com.lordofthejars.diferencia.gateway;

import com.lordofthejars.diferencia.api.DiferenciaConfigurationUpdate;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

public class DiferenciaAdminClient {

    private static final String CONFIGURATION_ENDPOINT = "/configuration";
    private final String url;

    public DiferenciaAdminClient(String url) {
        url = url.trim();
        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }

        this.url = url.trim();
    }

    public void updateConfig(DiferenciaConfigurationUpdate diferenciaConfigurationUpdate) throws IOException {

        URL endpoint = new URL(buildEndpoint(this.url, CONFIGURATION_ENDPOINT));
        HttpURLConnection con = (HttpURLConnection) endpoint.openConnection();
        con.setRequestMethod("PUT");
        con.setDoOutput(true);

        try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
            wr.writeBytes(diferenciaConfigurationUpdate.toJson());
            wr.flush();
        }

        final int responseCode = con.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException(String.format("Error code is not OK but %d", responseCode));
        }

    }

    private String buildEndpoint(String url, String path) {
        return url + path;
    }

}
