package com.lordofthejars.diferencia.arquillian;

import com.lordofthejars.diferencia.api.DiferenciaConfiguration;
import com.lordofthejars.diferencia.arquillian.spi.DiferenciaConfigurationUrlResolver;
import java.net.MalformedURLException;
import java.net.URL;
import org.assertj.core.api.Assertions;
import org.jboss.arquillian.container.test.impl.enricher.resource.URLResourceProvider;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.spi.ServiceLoader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DiferenciaClassLifecycleTest {

    @Mock
    ServiceLoader urlResolverServiceLoader;

    @Mock
    ServiceLoader urlResourceProviderServiceLoader;

    @Mock
    URLResourceProvider urlResourceProvider;

    @Before
    public void setupMocks() throws MalformedURLException {

        when(urlResourceProviderServiceLoader.onlyOne(URLResourceProvider.class))
            .thenReturn(urlResourceProvider);

        when(urlResourceProvider.lookup(any(), any())).thenReturn(new URL("http://localhost"));

        final InContainerDiferenciaConfigurationUrlResolverService inContainerDiferenciaConfigurationUrlResolverService =
            new InContainerDiferenciaConfigurationUrlResolverService();
        inContainerDiferenciaConfigurationUrlResolverService.serviceLoader = () -> urlResourceProviderServiceLoader;


        when(urlResolverServiceLoader.onlyOne(DiferenciaConfigurationUrlResolver.class))
            .thenReturn(inContainerDiferenciaConfigurationUrlResolverService);
    }

    @Test
    public void should_not_update_urls_if_specified() {

        // Given
        DiferenciaClassLifecycle diferenciaClassLifecycle = new DiferenciaClassLifecycle();
        diferenciaClassLifecycle.serviceLoader = () -> urlResolverServiceLoader;

        final DiferenciaConfiguration configuration =
            new DiferenciaConfiguration.Builder("http://primary", "http://candidate")
                .withSecondary("http://secondary").build();

        // When
        final DiferenciaConfiguration updatedConfiguration =
            diferenciaClassLifecycle.updateDiferenciaConfiguration(configuration);

        // Then
        assertThat(updatedConfiguration.getPrimary()).isEqualTo("http://primary");
        assertThat(updatedConfiguration.getCandidate()).isEqualTo("http://candidate");
        assertThat(updatedConfiguration.getSecondary()).isEqualTo("http://secondary");
    }

    @Test
    public void should_throw_exception_if_no_primary_set() {

        // Given
        DiferenciaClassLifecycle diferenciaClassLifecycle = new DiferenciaClassLifecycle();
        diferenciaClassLifecycle.serviceLoader = () -> urlResolverServiceLoader;

        final DiferenciaConfiguration configuration =
            new DiferenciaConfiguration.Builder("", "http://candidate").build();

        // When
        Throwable thrown = catchThrowable(() ->
            diferenciaClassLifecycle.updateDiferenciaConfiguration(configuration));

        // Then
        assertThat(thrown)
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void should_change_primary_if_none_url_is_provided() {

        // Given
        DiferenciaClassLifecycle diferenciaClassLifecycle = new DiferenciaClassLifecycle();
        diferenciaClassLifecycle.serviceLoader = () -> urlResolverServiceLoader;

        final DiferenciaConfiguration configuration =
            new DiferenciaConfiguration.Builder("myPrimaryDeployment", "http://candidate").build();

        // When
        final DiferenciaConfiguration updatedConfiguration =
            diferenciaClassLifecycle.updateDiferenciaConfiguration(configuration);

        // Then
        assertThat(updatedConfiguration.getPrimary()).isEqualTo("http://localhost");
        assertThat(updatedConfiguration.getCandidate()).isEqualTo("http://candidate");

    }

    @Test
    public void should_change_secondary_if_none_url_is_provided() {

        // Given
        DiferenciaClassLifecycle diferenciaClassLifecycle = new DiferenciaClassLifecycle();
        diferenciaClassLifecycle.serviceLoader = () -> urlResolverServiceLoader;

        final DiferenciaConfiguration configuration =
            new DiferenciaConfiguration.Builder("http://primary", "http://candidate")
                .withSecondary("mySecondaryDeployment").build();

        // When
        final DiferenciaConfiguration updatedConfiguration =
            diferenciaClassLifecycle.updateDiferenciaConfiguration(configuration);

        // Then
        assertThat(updatedConfiguration.getPrimary()).isEqualTo("http://primary");
        assertThat(updatedConfiguration.getCandidate()).isEqualTo("http://candidate");
        assertThat(updatedConfiguration.getSecondary()).isEqualTo("http://localhost");

    }

    @Test
    public void should_throw_exception_if_secondary_not_set_and_noise_detection() {

        // Given
        DiferenciaClassLifecycle diferenciaClassLifecycle = new DiferenciaClassLifecycle();
        diferenciaClassLifecycle.serviceLoader = () -> urlResolverServiceLoader;

        final DiferenciaConfiguration configuration =
            new DiferenciaConfiguration.Builder("http://primary", "http://candidate")
                .withNoiseDetection(true).build();

        // When
        Throwable thrown = catchThrowable(() ->
            diferenciaClassLifecycle.updateDiferenciaConfiguration(configuration));

        // Then
        assertThat(thrown)
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void should_change_candidate_if_none_url_is_provided() {

        // Given
        DiferenciaClassLifecycle diferenciaClassLifecycle = new DiferenciaClassLifecycle();
        diferenciaClassLifecycle.serviceLoader = () -> urlResolverServiceLoader;

        final DiferenciaConfiguration configuration =
            new DiferenciaConfiguration.Builder("http://primary", "myCandidateDeployment").build();

        // When
        final DiferenciaConfiguration updatedConfiguration =
            diferenciaClassLifecycle.updateDiferenciaConfiguration(configuration);

        // Then
        assertThat(updatedConfiguration.getPrimary()).isEqualTo("http://primary");
        assertThat(updatedConfiguration.getCandidate()).isEqualTo("http://localhost");

    }

    @Test
    public void should_change_candidate_if_empty() {

        // Given
        DiferenciaClassLifecycle diferenciaClassLifecycle = new DiferenciaClassLifecycle();
        diferenciaClassLifecycle.serviceLoader = () -> urlResolverServiceLoader;

        final DiferenciaConfiguration configuration =
            new DiferenciaConfiguration.Builder("http://primary", "").build();

        // When
        final DiferenciaConfiguration updatedConfiguration =
            diferenciaClassLifecycle.updateDiferenciaConfiguration(configuration);

        // Then
        assertThat(updatedConfiguration.getPrimary()).isEqualTo("http://primary");
        assertThat(updatedConfiguration.getCandidate()).isEqualTo("http://localhost");

    }

}
