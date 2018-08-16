package com.lordofthejars.diferencia.arquillian;

import com.lordofthejars.diferencia.api.DiferenciaConfiguration;
import com.lordofthejars.diferencia.core.Diferencia;
import java.io.IOException;
import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.core.api.annotation.ApplicationScoped;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.test.spi.event.suite.AfterSuite;
import org.jboss.arquillian.test.spi.event.suite.BeforeSuite;

class DiferenciaSuiteLifecycle {

    @Inject
    @ApplicationScoped
    InstanceProducer<Diferencia> diferenciaInstanceProducer;

    private Diferencia diferencia;

    public void startDiferencia(@Observes BeforeSuite event, DiferenciaConfiguration diferenciaConfiguration) {
        this.diferencia = new Diferencia(diferenciaConfiguration);

        try {
            this.diferencia.start();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        diferenciaInstanceProducer.set(this.diferencia);
    }

    public void stopDiferencia(@Observes AfterSuite event) {
        if (diferencia != null) {
            this.diferencia.close();
        }
    }

}
