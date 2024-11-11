package kamkeel.kingdomregions;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import kamkeel.kingdomregions.Network.PacketDispatcher;
import kamkeel.kingdomregions.client.RegionEventHandler;
import kamkeel.kingdomregions.client.gui.InterfaceGUI;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

@Mod(
   modid = "kingdomregions",
   name = "Kingdom | Regions",
   version = "5.0"
)
public class KingdomRegions {
   public static final String MODID = "kingdomregions";
   public static final String NAME = "Regions";
   public static final String VERSION = "5.9";
   public static ModInteropProxy modInterop;
   public static KingdomRegions instance;
   public static Configuration config;
   @SidedProxy(
      clientSide = "kamkeel.kingdomregions.client.ClientProxy",
      serverSide = "kamkeel.kingdomregions.CommonProxy"
   )
   public static CommonProxy proxy;

   public KingdomRegions() {
      instance = this;
   }

   @EventHandler
   public void preInit(FMLPreInitializationEvent preEvent) {
      config = new Configuration(preEvent.getSuggestedConfigurationFile());
      ConfigurationMoD.loadConfig();
      if (Loader.isModLoaded("VillageNames") && ConfigurationMoD.EnableVillageNamesSupport) {
         try {
            modInterop = (ModInteropProxy)Class.forName("kamkeel.kingdomregions.ActiveModInteropProxy").asSubclass(ModInteropProxy.class).newInstance();
         } catch (InstantiationException var3) {
            var3.printStackTrace();
         } catch (IllegalAccessException var4) {
            var4.printStackTrace();
         } catch (ClassNotFoundException var5) {
            var5.printStackTrace();
         }
      } else {
         modInterop = new DummyModInteropProxy();
      }

      NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
   }

   @EventHandler
   public void init(FMLInitializationEvent event) {
      new ItemRegistry();
      PacketDispatcher.registerPackets();
      RegionEventHandler events = new RegionEventHandler();
      FMLCommonHandler.instance().bus().register(events);
      MinecraftForge.EVENT_BUS.register(events);
   }

   @EventHandler
   public void postInit(FMLPostInitializationEvent postEvent) {
      if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
         InterfaceGUI event = new InterfaceGUI(Minecraft.getMinecraft());
         MinecraftForge.EVENT_BUS.register(event);
         FMLCommonHandler.instance().bus().register(event);
      }

   }

   @EventHandler
   public void serverLoad(FMLServerStartingEvent event) {
      event.registerServerCommand(new CommandDisplayText());
      event.registerServerCommand(new CommandSetDisplayPoint());
   }
}
