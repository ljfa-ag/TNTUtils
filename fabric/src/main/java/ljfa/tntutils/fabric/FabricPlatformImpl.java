package ljfa.tntutils.fabric;

import ljfa.tntutils.PlatformAbstractions;
import ljfa.tntutils.TNTUtilsConfig;

public class FabricPlatformImpl implements PlatformAbstractions {
	@Override
	public TNTUtilsConfig config() {
		return FiberTNTUtilsConfig.COMMON;
	}
}
