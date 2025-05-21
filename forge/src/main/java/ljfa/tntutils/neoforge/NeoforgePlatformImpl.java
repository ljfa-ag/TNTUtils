package ljfa.tntutils.neoforge;

import ljfa.tntutils.PlatformAbstractions;
import ljfa.tntutils.TNTUtilsConfigAccess;

public class NeoforgePlatformImpl implements PlatformAbstractions {
    @Override
    public TNTUtilsConfigAccess config() {
        return NeoforgeTNTUtilsConfig.COMMON;
    }
}
