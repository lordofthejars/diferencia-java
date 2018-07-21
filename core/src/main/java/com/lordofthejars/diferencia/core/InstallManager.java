package com.lordofthejars.diferencia.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class InstallManager {

    private static final String PACKAGE_LOCATION = "binaries";

    private Path copyToTemp;
    OperativeSystemConfig operativeSystemConfig;

    InstallManager() {
        this.operativeSystemConfig = new OperativeSystemConfig(System.getProperty("os.name"));
    }

    Path install() throws IOException {

        final String diferenciaBinary = this.operativeSystemConfig.resolveDiferenciaBinary();

        final String location = PACKAGE_LOCATION + "/" + diferenciaBinary;

        final FileManager fileManager = new FileManager(location);
        copyToTemp = fileManager.copyToTemp();

        FilePermission.execPermission(copyToTemp);

        return copyToTemp;
    }

    void uninstall() throws IOException {
        if (Files.exists(copyToTemp)) {
            Files.delete(copyToTemp);
        }
    }

}
