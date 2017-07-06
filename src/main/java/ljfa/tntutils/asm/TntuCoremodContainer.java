package ljfa.tntutils.asm;

import com.google.common.eventbus.EventBus;

import ljfa.tntutils.Reference;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;

public class TntuCoremodContainer extends DummyModContainer {
    public TntuCoremodContainer() {
        super(new ModMetadata());
        ModMetadata meta = getMetadata();
        meta.modId = "tnt_utilities_core";
        meta.name = "TNTUtils Core";
        meta.version = Reference.VERSION;
        meta.authorList.add("ljfa");
        meta.url = "https://minecraft.curseforge.com/projects/tntutils";
        meta.description = "The core mod belonging to TNTUtils";
        meta.parent = "tnt_utilities";
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
        return true;
    }
}
