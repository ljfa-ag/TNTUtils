package ljfa.tntutils;

/**
 * Provides access to functions that are implemented separately for each mod loader.
 * An instance of this interface is provided as a service by the loader subprojects.
 */
public interface PlatformAbstractions {
	TNTUtilsConfigAccess config();
}
