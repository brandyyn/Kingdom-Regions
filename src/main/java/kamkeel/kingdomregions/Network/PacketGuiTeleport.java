package kamkeel.kingdomregions.Network;

import cpw.mods.fml.relauncher.Side;
import java.io.IOException;
import kamkeel.kingdomregions.ConfigurationMoD;
import kamkeel.kingdomregions.ItemRegistry;
import kamkeel.kingdomregions.NBT.KingdomPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ChunkCoordinates;

public class PacketGuiTeleport extends AbstractMessage.AbstractServerMessage<PacketGuiTeleport> {
   public int x;

   public PacketGuiTeleport() {
   }

   public PacketGuiTeleport(int ID) {
      this.x = ID;
   }

   protected void read(PacketBuffer buffer) throws IOException {
      this.x = buffer.readInt();
   }

   protected void write(PacketBuffer buffer) throws IOException {
      buffer.writeInt(this.x);
   }

   public void process(EntityPlayer player, Side side) {
      if (ConfigurationMoD.canTeleportToVillages) {
         KingdomPlayer rins = KingdomPlayer.get(player);
         if (rins.getDiscoverdNameList().size() >= this.x && player.getHeldItem().getItem() == ItemRegistry.Runestone) {
            if (ConfigurationMoD.MapDisapearAfterTeleport) {
               player.setCurrentItemOrArmor(0, (ItemStack)null);
            }

            ChunkCoordinates cords = (ChunkCoordinates)rins.DiscoverdCordsList.get(this.x);
            player.setPositionAndUpdate((double)cords.posX, (double)cords.posY, (double)cords.posZ);
         }
      }

   }
}
