package com.lordofthejars.diferencia.arquillian;

import com.lordofthejars.diferencia.arquillian.spi.DiferenciaConfigurationUrlResolver;
import java.lang.annotation.Annotation;
import java.net.URL;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.container.test.impl.enricher.resource.URLResourceProvider;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.spi.ServiceLoader;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.test.spi.enricher.resource.ResourceProvider;

public class InContainerDiferenciaConfigurationUrlResolverService implements DiferenciaConfigurationUrlResolver {

    @Inject
    Instance<ServiceLoader> serviceLoader;

    @Override
    public boolean canBeResolved(String url) {

        if (url == null || url.isEmpty()) {
            return true;
        }
        return !url.startsWith("http");
    }

    @Override
    public URL resolve(final String name) {

        final ResourceProvider urlResourceProvider = serviceLoader.get()
            .onlyOne(ResourceProvider.class, URLResourceProvider.class);

        if (urlResourceProvider != null) {

            OperateOnDeployment[] operateOnDeployments = createOperateOnDeployment(name);
            ArquillianResource arquillianResource = createArquillianResource();

            System.out.println("Before");
            URL u = (URL) urlResourceProvider.lookup(arquillianResource, operateOnDeployments);
            System.out.println("After");
            return u;

        }

        return null;
    }

    private ArquillianResource createArquillianResource() {
        return new ArquillianResource() {

                    @Override
                    public Class<? extends Annotation> annotationType() {
                        return ArquillianResource.class;
                    }

                    @Override
                    public Class<?> value() {
                        return ArquillianResource.class;
                    }
                };
    }

    private OperateOnDeployment[] createOperateOnDeployment(final String name) {
        OperateOnDeployment[] operateOnDeployments = new OperateOnDeployment[0];
        if (name != null && !name.isEmpty()) {
            operateOnDeployments = new OperateOnDeployment[1];
            operateOnDeployments[0] = new OperateOnDeployment(){

                @Override
                public Class<? extends Annotation> annotationType() {
                    return OperateOnDeployment.class;
                }

                @Override
                public String value() {
                    return name;
                }
            };
        }
        return operateOnDeployments;
    }
}
