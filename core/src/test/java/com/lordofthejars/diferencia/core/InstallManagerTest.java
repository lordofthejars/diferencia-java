package com.lordofthejars.diferencia.core;

import java.io.IOException;
import java.nio.file.Path;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InstallManagerTest {

    @Mock
    OperativeSystemConfig operativeSystemConfig;

    @Test
    public void should_install_diferencia_to_be_run() throws IOException {

        // Given
        when(operativeSystemConfig.resolveDiferenciaBinary()).thenReturn("diferencia_darwin_amd64");
        final InstallManager installManager = new InstallManager();
        installManager.operativeSystemConfig = operativeSystemConfig;

        // When
        final Path installPath = installManager.install();

        // Then
        assertThat(installPath)
            .exists()
            .isExecutable();
    }

    @Test
    public void should_uninstall_diferencia() throws IOException {

        // Given
        when(operativeSystemConfig.resolveDiferenciaBinary()).thenReturn("diferencia_darwin_amd64");
        final InstallManager installManager = new InstallManager();
        installManager.operativeSystemConfig = operativeSystemConfig;

        // When
        final Path installPath = installManager.install();
        installManager.uninstall();

        // Then
        assertThat(installPath)
            .doesNotExist();
    }

}
