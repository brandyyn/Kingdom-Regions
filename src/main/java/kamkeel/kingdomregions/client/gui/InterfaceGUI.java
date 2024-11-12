package kamkeel.kingdomregions.client.gui;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import java.util.ArrayList;

import cpw.mods.fml.common.gameevent.TickEvent;
import kamkeel.kingdomregions.ConfigurationMoD;
import kamkeel.kingdomregions.NBT.KingdomPlayer;
import kamkeel.kingdomregions.Network.PacketDispatcher;
import kamkeel.kingdomregions.Network.sendTextpopRegions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Chat;
import org.lwjgl.opengl.GL11;

public class InterfaceGUI extends GuiScreen {
   private Minecraft mc;
   private static boolean inVillage = false;
   private static ArrayList<String> DISPLAYSTRING = new ArrayList();
   private static String BIOMEOLD = "none";
   private static String DISPLAYSTRINGVILLAGEOLD = "none";
   private static int time;
   private static int f1 = 0;
   private static int maxtime;
   private static int VillagePosX;
   private static int VillagePosY;
   private static int VillagePosZ;
   private static int radius;
   public static int playtime;
   private static final double SCALE;

   public InterfaceGUI(Minecraft mc) {
      this.mc = mc;
   }


   @SubscribeEvent(
           priority = EventPriority.HIGHEST
   )
   public void onRenderExperienceBar(Chat event) {
      if (playtime > 0) {
         --playtime;
      } else {
         EntityClientPlayerMP player = this.mc.thePlayer;
         if (DISPLAYSTRING.size() > 0) {
            if (this.updateTime()) {
               DISPLAYSTRING.remove(0);
            } else {
               String name = (String)DISPLAYSTRING.get(0);
               int i;
               if (!name.toString().contains(" ")) {
                  StringBuilder newName = new StringBuilder((String)DISPLAYSTRING.get(0));

                  for(i = 1; i < name.length() - 1; ++i) {
                     if (!Character.isUpperCase(name.charAt(i - 1)) && Character.isUpperCase(name.charAt(i)) && !Character.isUpperCase(name.charAt(i + 1))) {
                        newName.insert(i, ' ');
                        ++i;
                     }
                  }

                  name = newName.toString();
               }

               ScaledResolution scaledresolution = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
               i = scaledresolution.getScaledWidth();
               int height = scaledresolution.getScaledHeight();
               FontRenderer fontRender = this.mc.fontRenderer;
               GL11.glPushMatrix();
               GL11.glScaled(SCALE, SCALE, 1.0D);
               int scaledTextWidth = (int)((double)fontRender.getStringWidth(name) * SCALE);
               int scaledTextHeight = (int)((double)fontRender.FONT_HEIGHT * SCALE);
               int x = (i - scaledTextWidth) / 2;
               int y = (height - scaledTextHeight) / 2;
               x = ConfigurationMoD.textX < 0 ? x - ConfigurationMoD.textX * -1 : x + ConfigurationMoD.textX;
               y = ConfigurationMoD.textY < 0 ? y - ConfigurationMoD.textY * -1 : y + ConfigurationMoD.textY;
               int startend = maxtime / 3;
               int endstart = maxtime - startend;
               double dd;
               if (time < startend) {
                  dd = (double)(255 * time / startend);
                  f1 = (int)dd;
               } else if (time > endstart) {
                  dd = (double)(255 * (time - endstart) / startend);
                  f1 = (int)(255.0D - dd);
               } else {
                  f1 = 255;
               }

               if (f1 >= 15) {
                  fontRender.drawStringWithShadow(name, (int)((double)x / SCALE), (int)((double)y / SCALE), ConfigurationMoD.HTML_COLOR + (f1 << 24));
               }

               GL11.glPopMatrix();
            }
         } else if (inVillage && player.getDistance((double)VillagePosX, (double)VillagePosY, (double)VillagePosZ) > 50.0D) {
            inVillage = false;
         } else {
            this.updateBiome(player);
         }

      }
   }

   private void updateBiome(EntityPlayer player) {
      // Fetch the original biome name
      String originalBiomeName = player.worldObj.getBiomeGenForCoords((int) player.posX, (int) player.posZ).biomeName;

      // Get the renamed biome name for display purposes
      String displayName = ConfigurationMoD.getRenamedBiome(originalBiomeName);

      // Check if the biome has changed
      if (!BIOMEOLD.equals(originalBiomeName)) {
         boolean isValidBiome = true; // Assume valid unless blacklisted or fails whitelist

         // Check blacklist
         if (ConfigurationMoD.valuesB != null && ConfigurationMoD.valuesB.length > 0) {
            for (String blacklisted : ConfigurationMoD.valuesB) {
               if (blacklisted.equals(originalBiomeName)) {
                  isValidBiome = false; // Mark invalid if blacklisted
                  break;
               }
            }
         }

         // Check whitelist
         if (ConfigurationMoD.useWhitelist && ConfigurationMoD.valuesW != null && ConfigurationMoD.valuesW.length > 0) {
            isValidBiome = false; // Default to false, allow only if matched in whitelist
            for (String whitelisted : ConfigurationMoD.valuesW) {
               if (whitelisted.equals(originalBiomeName)) {
                  isValidBiome = true;
                  break;
               }
            }
         }

         // Handle entering a valid biome
         if (isValidBiome) {
            if (!KingdomPlayer.get(player).DiscoverdBiomeList.contains(originalBiomeName)) {
               // Add to DiscoverdBiomeList and display for new biomes
               KingdomPlayer.get(player).DiscoverdBiomeList.add(originalBiomeName);
               DISPLAYSTRING.add(displayName);
               PacketDispatcher.sendToServer(new sendTextpopRegions(displayName));
            } else if (ConfigurationMoD.displayerBiomeAgain) {
               // Redisplay for valid biomes if redisplay is enabled
               DISPLAYSTRING.add(displayName);
               PacketDispatcher.sendToServer(new sendTextpopRegions(displayName));
            }

            BIOMEOLD = originalBiomeName; // Update BIOMEOLD to the valid biome
         } else {
            // Handle entering an invalid biome
            if (ConfigurationMoD.displayerBiomeAgain) {
               BIOMEOLD = ""; // Reset BIOMEOLD to allow redisplay when reentering a valid biome
            }
         }
      }
   }


   @SubscribeEvent
   public void onClientTick(TickEvent.ClientTickEvent event) {
      if (DISPLAYSTRING.size() > 0) {
         ++time;
      }
   }

   private boolean updateTime() {
      if (time >= maxtime) {
         f1 = 0;
         time = 0;
         return true;
      } else {
         return false;
      }
   }

   public static void setDISPLAYSTRING(String dISPLAYSTRING) {
      DISPLAYSTRING.add(dISPLAYSTRING);
   }

   public static void setDISPLAYSTRINGVILLAGE(String str, int x, int y, int z, int rad) {
      if (!inVillage && !DISPLAYSTRINGVILLAGEOLD.equals(str)) {
         DISPLAYSTRING.add(str);
         DISPLAYSTRINGVILLAGEOLD = str;
         inVillage = true;
         VillagePosX = x;
         VillagePosY = y;
         VillagePosZ = z;
         radius = rad;
      }
   }

   static {
      maxtime = ConfigurationMoD.DisplayTime * 20;
      playtime = 200;
      SCALE = ConfigurationMoD.ScaleSize;
   }
}