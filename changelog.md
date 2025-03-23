First prerelease for Minecraft 1.21.1. Due to the large difference from Minecraft 1.12 to 1.21, the mod has been
rewritten from scratch, supporting both NeoForge and Fabric.

**Please note that TNTUtils is still work in progress, not all features of the 1.12 version have been implemented yet.**
See the config file for the features that are available.
Please report any issues you encounter to our [issue tracker](https://github.com/ljfa-ag/TNTUtils/issues).

Some notable differences from the 1.12 version:
- The "/explosion" command has been renamed to "/explode" and now supports an additional argument specifying how the
  explosion interacts with blocks
- The "dropChanceIncrease" option is now called "dropChanceMultiplier" and simply multiplies the drop chance by the given factor.
  Note that you can use the vanilla game rules "mobExplosionDropDecay", "tntExplosionDropDecay" and "blockExplosionDropDecay"
  to force the block drop chance to 100% depending on the explosion source.
- Added an option "alwaysAffectAE2Singularities", which makes sure that Applied Energistics 2 Singularities will always
  be affected by explosions, so they can be entangled
