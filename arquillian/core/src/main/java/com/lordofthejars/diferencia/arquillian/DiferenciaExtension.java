package com.lordofthejars.diferencia.arquillian;

import com.lordofthejars.diferencia.arquillian.spi.DiferenciaConfigurationUrlResolver;
import org.jboss.arquillian.core.spi.LoadableExtension;
import org.jboss.arquillian.test.spi.TestEnricher;
import org.jboss.arquillian.test.spi.enricher.resource.ResourceProvider;

public class DiferenciaExtension implements LoadableExtension  {

    @Override
    public void register(ExtensionBuilder extensionBuilder) {

        extensionBuilder
            .observer(DiferenciaConfigurator.class)
            .service(ResourceProvider.class, DiferenciaResourceProvider.class)
            .service(ResourceProvider.class, DiferenciaAdminResourceProvider.class)
            .service(TestEnricher.class, DiferenciaUrlTestEnricher.class);


        // Arquillian Container integration
        // Only register if container-test-spi is on classpath
        if (Validate.classExists("org.jboss.arquillian.container.test.spi.client.deployment.AuxiliaryArchiveAppender")
            && doesNotContainStandaloneExtension() && isConfiguredWithDeploymentParameters()) {

            extensionBuilder
                .observer(DiferenciaClassLifecycle.class)
                .service(DiferenciaConfigurationUrlResolver.class, InContainerDiferenciaConfigurationUrlResolverService.class);

        } else {
            extensionBuilder.observer(DiferenciaSuiteLifecycle.class);
        }

    }

    private boolean isConfiguredWithDeploymentParameters() {
        return true;
    }

    private boolean doesNotContainStandaloneExtension() {
        final boolean junitStandalone =
            Validate.classExists("org.jboss.arquillian.junit.standalone.JUnitStandaloneExtension");
        final boolean testngStandalone =
            Validate.classExists("org.jboss.arquillian.testng.standalone.TestNGStandaloneExtension");
        final boolean spockStandalone =
            Validate.classExists("org.jboss.arquillian.spock.standalone.SpockStandaloneExtension");

        return !junitStandalone && !testngStandalone && !spockStandalone;
    }

}
