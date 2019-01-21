package com.lordofthejars.diferencia.api;

import java.util.Objects;

public class Stat {

    private final String method;
    private final String path;
    private final int errors;
    private final int success;
    private final double averagePrimaryDuration;
    private final double averageCandidateDuration;

    public Stat(String method, String path, int errors, int success, double averagePrimaryDuration, double averageCandidateDuration) {
        this.method = method;
        this.path = path;
        this.errors = errors;
        this.success = success;
        this.averageCandidateDuration = averageCandidateDuration;
        this.averagePrimaryDuration = averagePrimaryDuration;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public int getErrors() {
        return errors;
    }

    public int getSuccess() {
        return success;
    }

    public double getAverageCandidateDuration() {
        return averageCandidateDuration;
    }

    public double getAveragePrimaryDuration() {
        return averagePrimaryDuration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Stat stat = (Stat) o;
        return errors == stat.errors &&
            Objects.equals(method, stat.method) &&
            Objects.equals(path, stat.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, path, errors);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Stat{");
        sb.append("method='").append(method).append('\'');
        sb.append(", path='").append(path).append('\'');
        sb.append(", errors=").append(errors);
        sb.append(", success=").append(success);
        sb.append(", averagePrimaryDuration=").append(averagePrimaryDuration);
        sb.append(", averageCandidateDuration=").append(averageCandidateDuration);
        sb.append('}');
        return sb.toString();
    }
}
