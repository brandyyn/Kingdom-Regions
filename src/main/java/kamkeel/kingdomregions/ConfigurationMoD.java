package kamkeel.kingdomregions;

import cpw.mods.fml.common.FMLCommonHandler;
import kamkeel.kingdomregions.Network.PacketDispatcher;
import kamkeel.kingdomregions.Network.sendVillagePacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.biome.BiomeGenBase;

public class ConfigurationMoD {
   public static int textX = 0;
   public static int textY = 0;
   public static int HTML_COLOR = 16119039;
   public static int DisplayTime = 400;
   public static String[] valuesB;
   public static String[] valuesW;
   public static boolean useWhitelist;
   public static boolean displayerBiomeAgain;
   public static boolean EnableVillageNamesSupport;
   public static boolean EnableWandermap;
   public static String[] TownNames;
   public static double ScaleSize = 1.0D;
   public static boolean canTeleportToVillages;
   public static boolean MapDisapearAfterTeleport;
   public static boolean useMapCooldown;
   public static int TeleportCooldown;
   public static int teleportBlockprice;
   private static int Size;
   private static int[] ValueList;
   static final String CATEGORY_2 = "general.CustomCords (sended by Server, do NOT Edit that)";

   public static void loadConfig() {
      FMLCommonHandler.instance().bus().register(KingdomRegions.instance);
      String CATEGORY_1 = "general.Kingdom | Regions";
      KingdomRegions.config.addCustomCategoryComment(CATEGORY_1, "General");
      String CATEGORY_3 = "general.Kingdom | Regions (Interface)";
      KingdomRegions.config.addCustomCategoryComment(CATEGORY_3, "General");
      String CATEGORY_2 = "general.Kingdom | Regions (Teleport)";
      KingdomRegions.config.addCustomCategoryComment(CATEGORY_2, "General");
      EnableVillageNamesSupport = KingdomRegions.config.getBoolean("EnableVillageNamesSupport", CATEGORY_1, true, "Set to true to enable Village Names support.");
      EnableWandermap = KingdomRegions.config.getBoolean("EnableWandermap", CATEGORY_1, true, "Set to true to enable the Wandermap item.");
      MapDisapearAfterTeleport = KingdomRegions.config.getBoolean("MapDisapearAfterTeleport", CATEGORY_2, false, "Enable this to let the Map disappear after use.");
      useMapCooldown = KingdomRegions.config.getBoolean("useMapCooldown", CATEGORY_2, true, "Enable this to use the cooldown system for teleports.");
      canTeleportToVillages = KingdomRegions.config.getBoolean("canTeleportToVillages", CATEGORY_2, true, "Enable to let the player use Runestones.");
      TeleportCooldown = KingdomRegions.config.get(CATEGORY_2, "Teleport cooldown in secounds", 10).getInt() * 40;
      textX = KingdomRegions.config.get(CATEGORY_3, "X offset of the text", 0).getInt();
      textY = KingdomRegions.config.get(CATEGORY_3, "Y offset of the text", 0).getInt();
      HTML_COLOR = KingdomRegions.config.get(CATEGORY_3, "Color code for the Display Text ", 16119039).getInt();
      DisplayTime = KingdomRegions.config.get(CATEGORY_3, "the Time in Seconds which the Text is shown", 4).getInt() * 40;
      ScaleSize = KingdomRegions.config.get(CATEGORY_3, "Scale the Text be Factor", 5).getDouble();
      valuesB = KingdomRegions.config.getStringList("BiomeBlackList", "Biome", new String[]{BiomeGenBase.getBiomeGenArray()[7].biomeName, BiomeGenBase.getBiomeGenArray()[11].biomeName, BiomeGenBase.getBiomeGenArray()[16].biomeName, BiomeGenBase.getBiomeGenArray()[3].biomeName, BiomeGenBase.getBiomeGenArray()[25].biomeName, BiomeGenBase.getBiomeGenArray()[26].biomeName}, "Blacklisted Strings");
      valuesW = KingdomRegions.config.getStringList("BiomeWhiteList", "Biome", new String[]{BiomeGenBase.getBiomeGenArray()[7].biomeName, BiomeGenBase.getBiomeGenArray()[11].biomeName, BiomeGenBase.getBiomeGenArray()[16].biomeName, BiomeGenBase.getBiomeGenArray()[3].biomeName, BiomeGenBase.getBiomeGenArray()[25].biomeName, BiomeGenBase.getBiomeGenArray()[26].biomeName}, "Whitlisted Strings (only if Enabled!)");
      useWhitelist = KingdomRegions.config.getBoolean("Whitelisting", CATEGORY_1, false, "Enable Whitelist for the Names shown on the Screen");
      displayerBiomeAgain = KingdomRegions.config.getBoolean("displayerBiomeAgain", CATEGORY_1, false, "Enable this to let the Biome display again and again.");
      TownNames = KingdomRegions.config.getStringList("TownNames", "List", new String[]{"Demonsummit", "Fallhold", "Ebonholde", "Lakeville", "Nevermoor", "Goldcrest", "Highbury", "Sleetdale", "Brittletide", "Sleekborn", "Swiftspell", "Silkbay", "Doghall", "Glimmerharbor", "Muddrift", "Ghostgrasp", "Roseharbor", "Sleetstar", "Deadwallow", "Shimmervault", "Mageshore", "Mistville", "Snowgrasp", "Feardenn", "Sleekborn", "Mistcoast", "Baygulch", "Oxkeep", "Stagcrest", "Whitspire", "Sunfalls", "Mutehaven", "Northburgh", "Newgarde", "Quickhold", "Stillfall", "Whitcross", "Silveracre", "Whiteview", "Grimefair", "Nightburgh", "Kilgrove", "Highfield", "Lakeharbor", "Coldhallow", "Swampshield", "Deepstrand", "Whiteshire", "Whitereach", "Rockhand", "Deadstorm", "Sleetholde", "Quickwind", "Nightfield", "Scorchwood", "Rosehaven", "Southbay"}, "Standard Names for Villages.");
      Size = KingdomRegions.config.get(CATEGORY_2, "CurrentPoints", 0).getInt();
      if (KingdomRegions.config.hasChanged()) {
         KingdomRegions.config.save();
      }

   }

   public static void addPoint(int[] intlist, String string) {
      Size = KingdomRegions.config.get("general.CustomCords (sended by Server, do NOT Edit that)", "CurrentPoints", 0).getInt();
      ++Size;
      KingdomRegions.config.get("general.CustomCords (sended by Server, do NOT Edit that)", "cords_" + Size, intlist).getIntList();
      KingdomRegions.config.get("general.CustomCords (sended by Server, do NOT Edit that)", "cords_" + Size + "name", string).getString();
      KingdomRegions.config.get("general.CustomCords (sended by Server, do NOT Edit that)", "CurrentPoints", 0).set(Size);
      if (KingdomRegions.config.hasChanged()) {
         KingdomRegions.config.save();
      }

   }

   public static void checkPlayerPos(EntityPlayer player) {
      Size = KingdomRegions.config.get("general.CustomCords (sended by Server, do NOT Edit that)", "CurrentPoints", 0).getInt();

      for(int i = 1; i <= Size; ++i) {
         ValueList = KingdomRegions.config.get("general.CustomCords (sended by Server, do NOT Edit that)", "cords_" + i, new int[]{1, 12, 2, 2}).getIntList();
         if (player.getDistance((double)ValueList[0], (double)ValueList[1], (double)ValueList[2]) <= (double)ValueList[3]) {
            PacketDispatcher.sendTo(new sendVillagePacket(KingdomRegions.config.get("general.CustomCords (sended by Server, do NOT Edit that)", "cords_" + i + "name", "none").getString(), new ChunkCoordinates(ValueList[0], ValueList[1], ValueList[2]), ValueList[3]), (EntityPlayerMP)player);
         }
      }

      if (KingdomRegions.config.hasChanged()) {
         KingdomRegions.config.save();
      }

   }
}
