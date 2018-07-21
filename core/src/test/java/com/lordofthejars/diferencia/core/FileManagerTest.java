package com.lordofthejars.diferencia.core;

import java.io.IOException;
import java.nio.file.Path;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FileManagerTest {

    @Test
    public void should_copy_file_from_classpath_to_temp_file() throws IOException {

        // Given
        FileManager fileManager = new FileManager("binaries/hello.txt");

        // When
        final Path copyToTemp = fileManager.copyToTemp();

        // Then
        assertThat(copyToTemp)
            .exists()
            .hasContent("Hello World");
    }

}
