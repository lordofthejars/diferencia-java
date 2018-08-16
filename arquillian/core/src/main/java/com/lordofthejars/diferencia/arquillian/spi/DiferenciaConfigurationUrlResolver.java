package com.lordofthejars.diferencia.arquillian.spi;

import java.net.URL;

public interface DiferenciaConfigurationUrlResolver {

    boolean canBeResolved(String url);
    URL resolve(String name);

}
