package kamkeel.kingdomregions.Network;

import cpw.mods.fml.relauncher.Side;
import java.io.IOException;
import kamkeel.kingdomregions.NBT.KingdomPlayer;
import kamkeel.kingdomregions.client.gui.InterfaceGUI;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;

public class sendTextpop extends AbstractMessage.AbstractClientMessage<sendTextpop> {
   private String data2;

   public sendTextpop() {
   }

   public sendTextpop(String text) {
      this.data2 = text;
   }

   protected void read(PacketBuffer buffer) throws IOException {
      int length = buffer.readInt();
      this.data2 = buffer.readStringFromBuffer(length);
   }

   protected void write(PacketBuffer buffer) throws IOException {
      buffer.writeInt(this.data2.length());
      buffer.writeStringToBuffer(this.data2);
   }

   public void process(EntityPlayer player, Side side) {
      if (side.isServer()) {
         if (!KingdomPlayer.get(player).DiscoverdBiomeList.contains(this.data2)) {
            KingdomPlayer.get(player).DiscoverdBiomeList.add(this.data2);
         }

         PacketDispatcher.sendTo(new SyncPlayerPropsMessage(player), (EntityPlayerMP)player);
      } else {
         InterfaceGUI.playtime = 200;
      }

   }
}
