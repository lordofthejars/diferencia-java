package com.lordofthejars.diferencia.api;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Stats {

    private List<Stat> stats = new ArrayList<>();

    public List<Stat> getStats() {
        return stats;
    }

    public boolean isEmpty() {
        return this.stats.isEmpty();
    }

    public boolean containsError(final Stat stat) {
        return stats.stream()
            .filter(s -> s.getMethod().equals(stat.getMethod()) && s.getPath().equals(stat.getPath()))
            .filter(s -> s.getErrors() > 0)
            .findAny()
            .isPresent();
    }

    /**
     * Gets Stats from input stream and closes the stream at the end.
     * @param is where data is present. It is closed at the end.
     * @return Stats object.
     * @throws IOException
     */
    public static final Stats fromInputStream(final InputStream is) throws IOException {

        final Stats currentStats = new Stats();

        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            final JsonValue parse = Json.parse(reader);

            final JsonArray arrayOfStats = parse.asArray();

            for (JsonValue value : arrayOfStats) {
                final JsonObject stat = value.asObject();
                final JsonObject endpoint = stat.get("endpoint").asObject();
                final String method = endpoint.getString("method", "");
                final String path = endpoint.getString("path", "");
                final int errors = stat.getInt("errors", 0);
                final int success = stat.getInt("success", 0);
                final double averagePrimaryDuration = stat.getDouble("averagePrimaryDuration", 0.0);
                final double averageCandidateDuration = stat.getDouble("averageCandidateDuration", 0.0);
                final List<ErrorDetail> errorDetailList = parseErrorDetails(stat);

                currentStats.stats.add(
                    new Stat(method, path, errors, success, averagePrimaryDuration, averageCandidateDuration, errorDetailList)
                );
            }
        }

        return currentStats;

    }

    private static List<ErrorDetail> parseErrorDetails(JsonObject stat) {
        final List<ErrorDetail> errorDetailsList = new ArrayList<>();
        final JsonValue errorDetails = stat.get("errorDetails");

        if (errorDetails != null) {
            for (JsonValue errorInfo : errorDetails.asArray()) {
                final JsonObject errorInfoObject = errorInfo.asObject();
                final String fullUri = errorInfoObject.getString("fullURI", "");
                final String originalBody = errorInfoObject.getString("originalBody", "");
                final String headerDiff = errorInfoObject.getString("headerDiff", "");
                final String bodyDiff = errorInfoObject.getString("bodyDiff", "");
                final String statusDiff = errorInfoObject.getString("statusDiff", "");
                final Map<String, String> headers = parseHeaders(errorInfoObject);

                errorDetailsList.add(new ErrorDetail(fullUri, headers, originalBody, bodyDiff, headerDiff, statusDiff));
            }
        }


        return errorDetailsList;

    }

    private static Map<String, String> parseHeaders(JsonObject errorInfo) {
        final JsonValue originalHeaders = errorInfo.get("originalHeaders");
        final Map<String, String> headers = new HashMap<>();
        if (originalHeaders != null) {
            final JsonObject originalHeadersObject = originalHeaders.asObject();
            for (JsonObject.Member member : originalHeadersObject) {
                headers.put(member.getName(), serializeHeaderValues(member.getValue().asArray()));
            }
        }

        return headers;
    }

    private static String serializeHeaderValues(JsonArray headerValues) {
        return StreamSupport.stream(headerValues.spliterator(), false)
            .map(JsonValue::asString)
            .collect(Collectors.joining(","));
    }

}
