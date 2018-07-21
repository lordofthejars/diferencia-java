package com.lordofthejars.diferencia.core;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

final class FileManager {

    private String fileclasspath;
    private String name;

    FileManager(String fileclasspath) {
        this.fileclasspath = fileclasspath;
        this.name = this.fileclasspath.substring(this.fileclasspath.lastIndexOf('/') + 1);
    }

    Path copyToTemp() throws IOException {
        final Path diferenciaTempDirectory = Files.createTempDirectory("diferencia");

        final Path output = diferenciaTempDirectory.resolve(this.name);
        try(final InputStream stream = loadResource()) {
            Files.copy(stream, output, StandardCopyOption.REPLACE_EXISTING);
        }
        return output;
    }

    private InputStream loadResource() {
        return Thread.currentThread()
            .getContextClassLoader()
            .getResourceAsStream(this.fileclasspath);
    }

    String getFileclasspath() {
        return fileclasspath;
    }
}