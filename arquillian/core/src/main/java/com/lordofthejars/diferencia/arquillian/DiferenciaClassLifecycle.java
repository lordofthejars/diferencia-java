package com.lordofthejars.diferencia.arquillian;

import com.lordofthejars.diferencia.api.DiferenciaConfiguration;
import com.lordofthejars.diferencia.arquillian.spi.DiferenciaConfigurationUrlResolver;
import com.lordofthejars.diferencia.core.Diferencia;
import java.io.IOException;
import java.net.URL;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.core.api.annotation.ApplicationScoped;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.core.spi.ServiceLoader;
import org.jboss.arquillian.test.spi.event.enrichment.AfterEnrichment;
import org.jboss.arquillian.test.spi.event.suite.AfterClass;
import org.jboss.arquillian.test.spi.event.suite.BeforeClass;

class DiferenciaClassLifecycle {

    @Inject
    @ApplicationScoped
    InstanceProducer<Diferencia> diferenciaInstanceProducer;

    @Inject
    Instance<ServiceLoader> serviceLoader;

    private Diferencia diferencia;

    public void startDiferencia(@Observes BeforeClass event, DiferenciaConfiguration diferenciaConfiguration) {
        this.diferencia = new Diferencia(updateDiferenciaConfiguration(diferenciaConfiguration));

        try {
            this.diferencia.start();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        diferenciaInstanceProducer.set(this.diferencia);
    }

    protected DiferenciaConfiguration updateDiferenciaConfiguration(DiferenciaConfiguration diferenciaConfiguration) {
        final DiferenciaConfigurationUrlResolver diferenciaConfigurationUrlResolver =
            getDiferenciaConfigurationUrlResolver();

        final DiferenciaConfiguration.Builder newConfiguration = new DiferenciaConfiguration.Builder(diferenciaConfiguration);

        final String primary = diferenciaConfiguration.getPrimary();

        if (primary == null || primary.isEmpty()) {
            throw new IllegalArgumentException("Primary cannot be null");
        }

        if (diferenciaConfigurationUrlResolver.canBeResolved(primary)) {
            final URL resolvedPrimary = diferenciaConfigurationUrlResolver.resolve(primary);
            newConfiguration.withPrimary(resolvedPrimary.toExternalForm());
        }

        final String secondary = diferenciaConfiguration.getSecondary();
        if ((diferenciaConfiguration.getNoiseDetection() != null && diferenciaConfiguration.getNoiseDetection()) && (secondary == null || secondary.isEmpty())) {
            throw new IllegalArgumentException("Secondary cannot be null if noise detection is enabled");
        }

        if (secondary !=null && diferenciaConfigurationUrlResolver.canBeResolved(secondary)) {
            final URL resolvedSecondary = diferenciaConfigurationUrlResolver.resolve(secondary);
            newConfiguration.withSecondary(resolvedSecondary.toExternalForm());
        }

        if (diferenciaConfigurationUrlResolver.canBeResolved(diferenciaConfiguration.getCandidate())) {
            final URL resolvedCandidate = diferenciaConfigurationUrlResolver.resolve(diferenciaConfiguration.getCandidate());
            newConfiguration.withCandidate(resolvedCandidate.toExternalForm());
        }

        return newConfiguration.build();
    }

    private DiferenciaConfigurationUrlResolver getDiferenciaConfigurationUrlResolver() {
        return serviceLoader.get().onlyOne(DiferenciaConfigurationUrlResolver.class);
    }

    public void stopDiferencia(@Observes AfterClass event) {
        if (diferencia != null) {
            this.diferencia.close();
        }
    }

}
