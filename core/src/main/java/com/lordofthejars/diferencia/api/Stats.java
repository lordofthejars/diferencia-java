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
import java.util.List;

public class Stats {

    private List<Stat> stats = new ArrayList<>();

    public List<Stat> getStats() {
        return stats;
    }

    public boolean isEmpty() {
        return this.stats.isEmpty();
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

                currentStats.stats.add(new Stat(method, path, errors));
            }
        }

        return currentStats;

    }

}
