package eu.gir.guilib.ecs;

import net.minecraftforge.fml.common.network.FMLEventChannel;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public final class UIInit {

    private UIInit() {
    }

    private static FMLEventChannel channel;

    public static FMLEventChannel getChannel() {
        return channel;
    }

    public static final String CHANNELNAME = "gir|guisyncnet";

    public static void initCommon(final String modid) {
        channel = NetworkRegistry.INSTANCE.newEventDrivenChannel(CHANNELNAME);
        channel.register(new GuiSyncNetwork());
        NetworkRegistry.INSTANCE.registerGuiHandler(modid, GuiHandler.getInstance(modid));
        GuiSyncNetwork.start();
    }

}
