package ljfa.tntutils.coremod;

import java.util.Arrays;

import ljfa.tntutils.Reference;

import com.google.common.eventbus.EventBus;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;

public class TntuModContainer extends DummyModContainer {
    public TntuModContainer() {
        super(new ModMetadata());
        ModMetadata meta = getMetadata();
        meta.modId = "tnt_utilities_core";
        meta.name = "TNTUtils Core";
        meta.version = Reference.VERSION;
        meta.authorList.add("ljfa");
        meta.url = "http://minecraft.curseforge.com/mc-mods/227449-tntutils";
        meta.description = "The core mod belonging to TNTUtils";
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
        return true;
    }
}
