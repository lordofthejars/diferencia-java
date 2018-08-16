package com.lordofthejars.diferencia.arquillian;

import com.lordofthejars.diferencia.core.Diferencia;
import com.lordofthejars.diferencia.gateway.DiferenciaAdminClient;
import java.lang.annotation.Annotation;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.test.spi.enricher.resource.ResourceProvider;

public class DiferenciaAdminResourceProvider implements ResourceProvider {

    @Inject
    Instance<Diferencia> diferenciaInstance;

    @Override
    public boolean canProvide(Class<?> type) {
        return DiferenciaAdminClient.class.isAssignableFrom(type);
    }

    @Override
    public Object lookup(ArquillianResource resource, Annotation... qualifiers) {
        final Diferencia diferencia = diferenciaInstance.get();

        if (diferencia != null) {
            return diferencia.getDiferenciaAdminClient();
        }

        return null;
    }
}
