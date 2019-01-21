package com.lordofthejars.diferencia.api;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import java.lang.reflect.Field;

public class DiferenciaConfigurationUpdate {

    private String serviceName;
    private String primary;
    private String secondary;
    private String candidate;
    private String noiseDetection;
    private String mode;
    private String returnResult;

    private DiferenciaConfigurationUpdate() {
    }

    public String toJson() {
        final JsonObject object = Json.object();

        Field[] fields = DiferenciaConfigurationUpdate.class.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            try {
                final Object o = field.get(this);

                if (o != null) {
                    object.add(field.getName(), (String) o);
                }

            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        }

        return object.toString();

    }

    public static class Builder {

        private DiferenciaConfigurationUpdate diferenciaConfigurationUpdate;

        public Builder() {
            this.diferenciaConfigurationUpdate = new DiferenciaConfigurationUpdate();
        }

        public Builder withServicename(String serviceName) {
            this.diferenciaConfigurationUpdate.serviceName = serviceName;
            return this;
        }

        public Builder withPrimary(String primary) {
            this.diferenciaConfigurationUpdate.primary = primary;
            return this;
        }

        public Builder withCandidate(String candidate) {
            this.diferenciaConfigurationUpdate.candidate = candidate;
            return this;
        }

        public Builder withSecondary(String secondary) {
            this.diferenciaConfigurationUpdate.secondary = secondary;
            return this;
        }

        public Builder withNoiseDetection(boolean noiseDetection) {
            this.diferenciaConfigurationUpdate.noiseDetection = String.valueOf(noiseDetection);
            return this;
        }

        public Builder withMode(DiferenciaMode diferenciaMode) {
            this.diferenciaConfigurationUpdate.mode = diferenciaMode.getName();
            return this;
        }

        public Builder withReturnResult(boolean returnResult) {
            this.diferenciaConfigurationUpdate.returnResult = String.valueOf(returnResult);
            return this;
        }

        public DiferenciaConfigurationUpdate build() {
            return this.diferenciaConfigurationUpdate;
        }

    }

}
