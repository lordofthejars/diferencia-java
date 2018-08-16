package com.lordofthejars.diferencia.arquillian;

import com.lordofthejars.diferencia.arquillian.api.DiferenciaUrl;
import com.lordofthejars.diferencia.core.Diferencia;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.test.spi.TestEnricher;

public class DiferenciaUrlTestEnricher implements TestEnricher {

    @Inject
    Instance<Diferencia> diferenciaInject;

    @Override
    public void enrich(Object testCase) {

        final Diferencia diferencia = diferenciaInject.get();
        if (diferencia != null) {
            final List<Field> fieldsWithAnnotation =
                ReflectionUtil.getFieldsWithAnnotation(testCase.getClass(), DiferenciaUrl.class);
            for (Field diferenciaUrlField : fieldsWithAnnotation) {

                if (!diferenciaUrlField.isAccessible()) {
                    diferenciaUrlField.setAccessible(true);
                }

                if (URL.class.isAssignableFrom(diferenciaUrlField.getType())) {
                    try {
                        diferenciaUrlField.set(testCase, new URL(diferencia.getDiferenciaUrl()));
                    } catch (IllegalAccessException | MalformedURLException e) {
                        throw new IllegalArgumentException(e);
                    }
                }
            }
        }
    }

    @Override
    public Object[] resolve(Method method) {

        Object[] values = new Object[method.getParameterTypes().length];
        final Diferencia diferencia = diferenciaInject.get();

        if (diferencia != null) {
            Integer[] annotatedParameters = annotatedParameters(method);
            Class<?>[] parameterTypes = method.getParameterTypes();
            for (Integer i : annotatedParameters) {
                if (URL.class.isAssignableFrom(parameterTypes[i])) {
                    values[i] = diferencia.getDiferenciaUrl();
                }
            }
        }

        return values;
    }

    private Integer[] annotatedParameters(Method method) {
        List<Integer> parametersWithAnnotations = new ArrayList<>();
        final Annotation[][] paramAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < paramAnnotations.length; i++) {
            for (Annotation a : paramAnnotations[i]) {
                if (a instanceof DiferenciaUrl) {
                    parametersWithAnnotations.add(i);
                }
            }
        }
        return parametersWithAnnotations.toArray(new Integer[parametersWithAnnotations.size()]);
    }
}
