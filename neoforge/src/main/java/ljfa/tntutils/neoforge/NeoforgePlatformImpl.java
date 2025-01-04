package ljfa.tntutils.neoforge;

import ljfa.tntutils.PlatformAbstractions;
import ljfa.tntutils.TNTUtilsConfig;

public class NeoforgePlatformImpl implements PlatformAbstractions {
	@Override
	public TNTUtilsConfig config() {
		return NeoforgeTNTUtilsConfig.COMMON;
	}
}
