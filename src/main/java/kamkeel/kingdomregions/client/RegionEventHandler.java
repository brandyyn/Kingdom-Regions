package kamkeel.kingdomregions.client;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;
import java.util.List;
import kamkeel.kingdomregions.ConfigurationMoD;
import kamkeel.kingdomregions.KingdomRegions;
import kamkeel.kingdomregions.NBT.KingdomPlayer;
import kamkeel.kingdomregions.Network.PacketDispatcher;
import kamkeel.kingdomregions.Network.SyncPlayerPropsRegions;
import kamkeel.kingdomregions.Network.sendTextpopRegions;
import kamkeel.kingdomregions.Network.sendVillagePacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.village.Village;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.player.PlayerEvent.Clone;

public class RegionEventHandler {
   private static int timer = 0;

   @SubscribeEvent
   public void onClientTick(ClientTickEvent event) {
      if (KingdomPlayer.MapCooldown > 0) {
         --KingdomPlayer.MapCooldown;
      }

   }

   @SubscribeEvent
   public void onEntityConstructing(EntityConstructing event) {
      if (event.entity instanceof EntityPlayer) {
         if (KingdomPlayer.get((EntityPlayer)event.entity) == null) {
            KingdomPlayer.register((EntityPlayer)event.entity);
         } else {
            PacketDispatcher.sendTo(new SyncPlayerPropsRegions((EntityPlayer)event.entity), (EntityPlayerMP)event.entity);
         }
      }

   }

   @SubscribeEvent
   public void onPlayerLogIn(PlayerLoggedInEvent event) {
      if (event.player instanceof EntityPlayer) {
         if (KingdomPlayer.get(event.player) == null) {
            KingdomPlayer.register(event.player);
         } else {
            PacketDispatcher.sendTo(new SyncPlayerPropsRegions(event.player), (EntityPlayerMP)event.player);
         }

         PacketDispatcher.sendTo(new sendTextpopRegions("none"), (EntityPlayerMP)event.player);
      }

   }

   @SubscribeEvent
   public void onPlayerLogOut(PlayerLoggedOutEvent event) {
      if (event.player instanceof EntityPlayer) {
         PacketDispatcher.sendTo(new SyncPlayerPropsRegions(event.player), (EntityPlayerMP)event.player);
      }

   }

   @SubscribeEvent
   public void onJoinWorld(EntityJoinWorldEvent event) {
      if (event.entity instanceof EntityPlayerMP && KingdomPlayer.get((EntityPlayer)event.entity) == null) {
         KingdomPlayer.register((EntityPlayer)event.entity);
      }

   }

   @SubscribeEvent
   public void onClonePlayer(Clone event) {
      KingdomPlayer.get(event.entityPlayer).copy(KingdomPlayer.get(event.original));
      PacketDispatcher.sendTo(new SyncPlayerPropsRegions(event.entityPlayer), (EntityPlayerMP)event.entityPlayer);
   }

   @SubscribeEvent
   public void onClientTick(WorldTickEvent event) {
      if (event.side.isServer() && this.updatetime() && ConfigurationMoD.EnableVillageNamesSupport) {
         List<Village> allVillages = event.world.villageCollectionObj.getVillageList();
         int i;
         if (!allVillages.isEmpty()) {
            int a = allVillages.size();

            for(i = 0; i < a; ++i) {
               ChunkCoordinates cords = ((Village)allVillages.get(i)).getCenter();
               List<EntityPlayer> list = event.world.playerEntities;
               if (!list.isEmpty()) {
                  int o = list.size();

                  for(int k = 0; k < o; ++k) {
                     EntityPlayer player = (EntityPlayer)list.get(k);
                     if (player != null) {
                        boolean api = false;
                        NBTTagCompound nbt = KingdomRegions.modInterop.getOrMakeVNInfo(event.world, cords.posX, cords.posY, cords.posZ);
                        if (nbt != null) {
                           cords = new ChunkCoordinates(nbt.getInteger("signX"), nbt.getInteger("signY"), nbt.getInteger("signZ"));
                           api = true;
                        }

                        if (player.getDistance((double)cords.posX, (double)cords.posY, (double)cords.posZ) < 50.0D) {
                           int tempID = i;

                           String str;
                           for(str = "Empty String"; tempID > ConfigurationMoD.TownNames.length; tempID -= ConfigurationMoD.TownNames.length + 1) {
                           }

                           if (tempID < ConfigurationMoD.TownNames.length) {
                              str = ConfigurationMoD.TownNames[tempID];
                           }

                           if (api) {
                              str = nbt.getString("namePrefix") + " " + nbt.getString("nameRoot") + " " + nbt.getString("nameSuffix").trim();
                           }

                           if (ConfigurationMoD.canTeleportToVillages) {
                              KingdomPlayer.get(player).addTeleportCords(cords, str);
                           }

                           PacketDispatcher.sendTo(new sendVillagePacket(str, cords, 120), (EntityPlayerMP)player);
                        }
                     }
                  }
               }
            }
         }

         List<EntityPlayer> playerlist = event.world.playerEntities;
         if (!playerlist.isEmpty()) {
            i = playerlist.size();

            for(int k = 0; k < i; ++k) {
               EntityPlayer player = (EntityPlayer)playerlist.get(k);
               if (player != null) {
                  ConfigurationMoD.checkPlayerPos(player);
               }
            }
         }
      }

   }

   private boolean updatetime() {
      ++timer;
      if (timer >= 100) {
         timer = 0;
         return true;
      } else {
         return false;
      }
   }
}
