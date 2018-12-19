package com.lordofthejars.diferencia.junit.api;

import com.lordofthejars.diferencia.api.DiferenciaMode;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.ANNOTATION_TYPE)
public @interface DiferenciaConfig {

    int port() default 0;
    String serviceName() default  "";
    String secondary() default  "";
    String storeResults() default "";
    DiferenciaMode differenceMode() default DiferenciaMode.STRICT;
    boolean noiseDetection() default false;
    boolean allowUnsafeOperations() default false;
    boolean prometheus() default false;
    int prometheusPort() default 0;
    String logLevel() default "";
    boolean headers() default false;
    String[] ignoreHeadersValues() default {};
    String[] ignoreValues() default {};
    String ignoreValuesFile() default "";
    boolean insecureSkipVerify() default false;
    String caCert() default "";
    String clientCert() default "";
    String clientKey() default "";
    int adminPort() default 0;
    boolean forcePlainText() default false;
    boolean mirroring() default false;

    String[] extraArguments() default {};

}
