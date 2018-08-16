package com.lordofthejars.diferencia.arquillian;

import com.lordofthejars.diferencia.api.DiferenciaConfiguration;
import java.util.Map;
import org.jboss.arquillian.config.descriptor.api.ArquillianDescriptor;
import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.core.api.annotation.ApplicationScoped;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;

class DiferenciaConfigurator {

    private static final String EXTENSION_NAME = "diferencia";

    @Inject
    @ApplicationScoped
    private InstanceProducer<DiferenciaConfiguration> diferenciaConfigurationInstanceProducer;

    public void configure(@Observes ArquillianDescriptor arquillianDescriptor) {
        final Map<String, String> config = arquillianDescriptor.extension(EXTENSION_NAME).getExtensionProperties();

        final DiferenciaConfiguration.Builder configurationBuilder = new DiferenciaConfiguration.Builder(config);
        diferenciaConfigurationInstanceProducer.set(configurationBuilder.build());
    }

}
