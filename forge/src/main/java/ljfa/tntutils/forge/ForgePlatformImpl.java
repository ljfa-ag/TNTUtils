package ljfa.tntutils.forge;

import ljfa.tntutils.PlatformAbstractions;
import ljfa.tntutils.TNTUtilsConfigAccess;

public class ForgePlatformImpl implements PlatformAbstractions {
    @Override
    public TNTUtilsConfigAccess config() {
        return ForgeTNTUtilsConfig.COMMON;
    }
}
