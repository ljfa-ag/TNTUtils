package ljfa.tntutils;

import java.util.ServiceLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TNTUtils {
	public static final String MOD_ID = "tntutils";

	public static final Logger logger = LoggerFactory.getLogger(TNTUtils.class);
	public static final PlatformAbstractions platformAbstr = loadAbstractions();

	public static final TNTUtilsConfigAccess config() {
		return platformAbstr.config();
	}

	private static PlatformAbstractions loadAbstractions() {
		var providers = ServiceLoader.load(PlatformAbstractions.class).stream().toList();
		if(providers.isEmpty())
			throw new IllegalStateException("No service provider for TNTUtils' platform abstractions found");
		else if(providers.size() > 1)
			throw new IllegalStateException("More than one service provider for TNTUtils' platform abstractions found");
		return providers.get(0).get();
	}
}
