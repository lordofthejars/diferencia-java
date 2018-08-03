package com.lordofthejars.diferencia.api;

import java.util.Objects;

public class Stat {

    private final String method;
    private final String path;
    private final int errors;

    public Stat(String method, String path, int errors) {
        this.method = method;
        this.path = path;
        this.errors = errors;
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
        sb.append('}');
        return sb.toString();
    }
}
