package ljfa.tntutils.fabric;

import ljfa.tntutils.PlatformAbstractions;
import ljfa.tntutils.TNTUtilsConfigAccess;

public class FabricPlatformImpl implements PlatformAbstractions {
	@Override
	public TNTUtilsConfigAccess config() {
		return FiberTNTUtilsConfig.COMMON;
	}
}
