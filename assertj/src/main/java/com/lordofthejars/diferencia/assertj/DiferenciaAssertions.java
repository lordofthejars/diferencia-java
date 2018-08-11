package com.lordofthejars.diferencia.assertj;

import com.lordofthejars.diferencia.core.Diferencia;
import com.lordofthejars.diferencia.gateway.DiferenciaAdminClient;
import org.assertj.core.api.Assertions;

public class DiferenciaAssertions extends Assertions {

    private DiferenciaAssertions() {
    }

    public static DiferenciaErrorCheckerAssert assertThat(Diferencia diferencia) {
        return new DiferenciaErrorCheckerAssert(diferencia.getDiferenciaAdminClient());
    }

    public static DiferenciaErrorCheckerAssert assertThat(DiferenciaAdminClient diferenciaAdminClient) {
        return new DiferenciaErrorCheckerAssert(diferenciaAdminClient);
    }
}
