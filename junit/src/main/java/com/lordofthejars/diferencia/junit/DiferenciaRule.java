package com.lordofthejars.diferencia.junit;

import com.lordofthejars.diferencia.api.DiferenciaConfiguration;
import com.lordofthejars.diferencia.core.Diferencia;
import com.lordofthejars.diferencia.gateway.DiferenciaAdminClient;
import com.lordofthejars.diferencia.gateway.DiferenciaClient;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Rule;
import org.junit.rules.ExternalResource;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class DiferenciaRule extends ExternalResource {

    private static final Logger logger= Logger.getLogger(DiferenciaRule.class.getName());

    private Diferencia diferencia;

    public DiferenciaRule(DiferenciaConfiguration diferenciaConfiguration) {
        diferencia = new Diferencia(diferenciaConfiguration);
    }

    public DiferenciaRule(DiferenciaConfiguration.Builder diferenciaConfigurationBuilder) {
        this(diferenciaConfigurationBuilder.build());
    }

    public DiferenciaRule(String primary, String candidate) {
        this(new DiferenciaConfiguration.Builder(primary, candidate));
    }

    /**
     * Creates Diferencia configuration with primary, candidate and secondary and noise detection to true.
     * @param primary
     * @param candidate
     * @param secondary
     */
    public DiferenciaRule(String primary, String candidate, String secondary) {
        this(new DiferenciaConfiguration.Builder(primary, candidate).withSecondary(secondary).withNoiseDetection(true));
    }

    @Override
    public Statement apply(Statement base, Description description) {
        if (isAnnotatedWithRule(description)) {
            logger.log(Level.WARNING, "It is recommended to use HoverflyRule with @ClassRule to get better performance in your tests.");
        }
        return super.apply(base, description);
    }

    @Override
    protected void before() throws Throwable {
        diferencia.start();
    }

    @Override
    protected void after() {
        if (this.diferencia != null) {
            diferencia.close();
        }
    }

    public DiferenciaClient getDiferenciaClient() {
        return diferencia.getDiferenciaClient();
    }

    public String getDiferenciaUrl() {
        return this.diferencia.getDiferenciaUrl();
    }

    public DiferenciaAdminClient getDiferenciaAdminClient() {
        return this.diferencia.getDiferenciaAdminClient();
    }

    static boolean isAnnotatedWithRule(Description description) {
        boolean isRule = false;
        Field[] fields = description.getTestClass().getFields();
        for (Field field : fields) {
            if (field.getType().isAssignableFrom(DiferenciaRule.class) && field.getAnnotation(Rule.class) != null) {
                isRule = true;
                break;
            }
        }
        return isRule;
    }

}
