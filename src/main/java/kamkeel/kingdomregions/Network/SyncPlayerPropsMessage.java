package kamkeel.kingdomregions.Network;

import cpw.mods.fml.relauncher.Side;
import java.io.IOException;
import kamkeel.kingdomregions.NBT.KingdomPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;

public class SyncPlayerPropsMessage extends AbstractMessage.AbstractClientMessage<SyncPlayerPropsMessage> {
   private NBTTagCompound data2;

   public SyncPlayerPropsMessage() {
   }

   public SyncPlayerPropsMessage(EntityPlayer player) {
      this.data2 = new NBTTagCompound();
      KingdomPlayer.get(player).saveNBTData(this.data2);
   }

   protected void read(PacketBuffer buffer) throws IOException {
      this.data2 = buffer.readNBTTagCompoundFromBuffer();
   }

   protected void write(PacketBuffer buffer) throws IOException {
      buffer.writeNBTTagCompoundToBuffer(this.data2);
   }

   public void process(EntityPlayer player, Side side) {
      if (KingdomPlayer.get(player) == null) {
         KingdomPlayer.register(player);
      }

      KingdomPlayer.get(player).loadNBTData(this.data2);
   }
}
