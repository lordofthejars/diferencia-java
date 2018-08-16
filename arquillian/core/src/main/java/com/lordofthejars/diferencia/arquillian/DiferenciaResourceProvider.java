package com.lordofthejars.diferencia.arquillian;

import com.lordofthejars.diferencia.core.Diferencia;
import java.lang.annotation.Annotation;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.test.spi.enricher.resource.ResourceProvider;

class DiferenciaResourceProvider implements ResourceProvider {

    @Inject
    Instance<Diferencia> diferenciaInstance;

    @Override
    public boolean canProvide(Class<?> type) {
        return Diferencia.class.isAssignableFrom(type);
    }

    @Override
    public Object lookup(ArquillianResource resource, Annotation... qualifiers) {
        return diferenciaInstance.get();
    }
}
