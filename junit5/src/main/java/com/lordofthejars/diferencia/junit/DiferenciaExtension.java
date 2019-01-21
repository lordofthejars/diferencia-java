package com.lordofthejars.diferencia.junit;

import com.lordofthejars.diferencia.api.DiferenciaConfiguration;
import com.lordofthejars.diferencia.api.DiferenciaMode;
import com.lordofthejars.diferencia.core.Diferencia;
import com.lordofthejars.diferencia.junit.api.DiferenciaConfig;
import com.lordofthejars.diferencia.junit.api.DiferenciaCore;
import java.lang.reflect.AnnotatedElement;
import java.nio.file.Paths;
import java.util.Arrays;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import static org.junit.platform.commons.support.AnnotationSupport.isAnnotated;

public class DiferenciaExtension implements BeforeAllCallback, AfterAllCallback, ParameterResolver {

    private Diferencia diferencia;

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {

        if (this.diferencia != null) {
            this.diferencia.close();
            this.diferencia = null;
        }

    }

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {

        if (this.diferencia == null) {

            AnnotatedElement annotatedElement =
                context.getElement().orElseThrow(() -> new IllegalStateException("No test class found."));

            if (isAnnotated(annotatedElement, DiferenciaCore.class)) {

                this.diferencia = new Diferencia(toConfig(annotatedElement.getAnnotation(DiferenciaCore.class)));
                this.diferencia.start();
            } else {
                throw new IllegalStateException(
                    "Diferencia test class must be annotated with @DiferenciaCore to configure it.");
            }
        }

    }

    private DiferenciaConfiguration toConfig(DiferenciaCore diferenciaCore) {

        final DiferenciaConfiguration.Builder builder =
            new DiferenciaConfiguration.Builder(diferenciaCore.primary(), diferenciaCore.candidate());

        DiferenciaConfig config = diferenciaCore.config();

        if (config.allowUnsafeOperations()) {
            builder.withAllowUnsafeOperations(config.allowUnsafeOperations());
        }

        if (config.headers()) {
            builder.withHeaders(config.headers());
        }

        if (config.insecureSkipVerify()) {
            builder.withInsecureSkipVerify(config.insecureSkipVerify());
        }

        if (config.noiseDetection()) {
            builder.withNoiseDetection(config.noiseDetection());
        }

        if (config.prometheus()) {
            builder.withPrometheus(config.prometheus());
        }

        if (config.adminPort() != 0) {
            builder.withAdminPort(config.adminPort());
        }

        if (config.port() != 0) {
            builder.withPort(config.port());
        }

        if (config.prometheusPort() != 0) {
            builder.withPrometheusPort(config.prometheusPort());
        }

        if (!config.caCert().isEmpty()) {
            builder.withCaCert(Paths.get(config.caCert()));
        }

        if (!config.clientCert().isEmpty()) {
            builder.withClientCert(Paths.get(config.clientCert()));
        }

        if (!config.clientKey().isEmpty()) {
            builder.withClientKey(Paths.get(config.clientKey()));
        }

        if (!config.ignoreValuesFile().isEmpty()) {
            builder.withIgnoreValuesFile(config.ignoreValuesFile());
        }

        if (!config.logLevel().isEmpty()) {
            builder.withLogLevel(config.logLevel());
        }

        if (!config.secondary().isEmpty()) {
            builder.withSecondary(config.secondary());
        }

        if (!config.serviceName().isEmpty()) {
            builder.withServiceName(config.serviceName());
        }

        if (!config.storeResults().isEmpty()) {
            builder.withStoreResults(config.storeResults());
        }

        if (config.differenceMode() != DiferenciaMode.STRICT) {
            builder.withDiferenciaMode(config.differenceMode());
        }

        if (config.ignoreValues().length > 0) {
            builder.withIgnoreValues(Arrays.asList(config.ignoreValues()));
        }

        if (config.extraArguments().length > 0) {
            builder.withExtraArguments(Arrays.asList(config.extraArguments()));
        }

        if (config.ignoreHeadersValues().length > 0) {
            builder.withIgnoreHeadersValues(Arrays.asList(config.ignoreHeadersValues()));
        }

        if (config.mirroring()) {
            builder.withMirroring(config.mirroring());
        }

        if (config.forcePlainText()) {
            builder.withForcePlainText(config.forcePlainText());
        }

        if (config.returnResult()) {
            builder.withReturnResult(config.returnResult());
        }

        return builder.build();
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
        throws ParameterResolutionException {
        return Diferencia.class.isAssignableFrom(parameterContext.getParameter().getType());
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
        throws ParameterResolutionException {
        return this.diferencia;
    }
}
