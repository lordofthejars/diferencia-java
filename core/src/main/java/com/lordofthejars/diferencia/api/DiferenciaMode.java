package com.lordofthejars.diferencia.api;

public enum DiferenciaMode {

    STRICT("Strict"), SUBSET("Subset"), SCHEMA("Schema");

    private String name;
    DiferenciaMode(String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }
}
