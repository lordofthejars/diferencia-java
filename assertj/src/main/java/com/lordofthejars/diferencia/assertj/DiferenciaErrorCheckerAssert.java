package com.lordofthejars.diferencia.assertj;

import com.lordofthejars.diferencia.api.Stat;
import com.lordofthejars.diferencia.api.Stats;
import com.lordofthejars.diferencia.gateway.DiferenciaAdminClient;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.AbstractAssert;

public class DiferenciaErrorCheckerAssert extends AbstractAssert<DiferenciaErrorCheckerAssert, DiferenciaAdminClient>  {

    private final List<Stat> filterStats = new ArrayList<>();

    public DiferenciaErrorCheckerAssert(DiferenciaAdminClient diferenciaAdminClient) {
        super(diferenciaAdminClient, DiferenciaErrorCheckerAssert.class);
    }

    public DiferenciaErrorCheckerAssert withFilter(String method, String path) {
        filterStats.add(new Stat(method, path));
        return this;
    }

    public DiferenciaErrorCheckerAssert hasNoErrors() {

        isNotNull();

        try {
            final Stats stats = this.actual.stats();
            if (filterStats.isEmpty() && !stats.isEmpty()) {
                failWithMessage("%nExpecting:%n no errors but%n next errors found %s", stats.getStats());
            } else {

                for (Stat stat : this.filterStats) {
                    if (stats.containsError(stat)) {
                        failWithMessage("%nExpecting:%n no errors on <%s> but%n it was found with errors", stat);
                    }
                }

            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        return this;
    }

}
