package com.lordofthejars.diferencia.core;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OperativeSystemConfigTest {


    @Test
    public void should_generate_executable_name_for_mac() {

        // Given
        String osName = "Mac OS X";
        final OperativeSystemConfig operativeSystemConfig = new OperativeSystemConfig(osName);

        // When
        final String binaryFile = operativeSystemConfig.resolveDiferenciaBinary();

        // Then
        assertThat(binaryFile)
            .isEqualTo("diferencia_darwin_amd64");

    }

    @Test
    public void should_generate_executable_name_for_linux() {

        // Given
        String osName = "Linux";
        final OperativeSystemConfig operativeSystemConfig = new OperativeSystemConfig(osName);

        // When
        final String binaryFile = operativeSystemConfig.resolveDiferenciaBinary();

        // Then
        assertThat(binaryFile)
            .isEqualTo("diferencia_linux_amd64");

    }

    @Test
    public void should_generate_executable_name_for_windows() {

        // Given
        String osName = "Windows 2000";
        final OperativeSystemConfig operativeSystemConfig = new OperativeSystemConfig(osName);

        // When
        final String binaryFile = operativeSystemConfig.resolveDiferenciaBinary();

        // Then
        assertThat(binaryFile)
            .isEqualTo("diferencia_windows_amd64.exe");

    }

}
