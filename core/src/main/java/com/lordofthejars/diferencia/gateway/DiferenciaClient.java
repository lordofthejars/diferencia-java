package com.lordofthejars.diferencia.gateway;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class DiferenciaClient {

    private static final String HEALTH_ENDPOINT = "/healthdif";

    private final String url;

    public DiferenciaClient(String url) {
        url = url.trim();
        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }

        this.url = url.trim();
    }

    public boolean healthy() throws IOException {
        HttpURLConnection con = null;
        try {
            URL obj = new URL(buildEndpoint(this.url, HEALTH_ENDPOINT));
            con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(200);
            int responseCode = con.getResponseCode();

            return responseCode == HttpURLConnection.HTTP_OK;

        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
    }

    public String getUrl() {
        return url;
    }

    private String buildEndpoint(String url, String path) {
        return url + path;
    }
}
