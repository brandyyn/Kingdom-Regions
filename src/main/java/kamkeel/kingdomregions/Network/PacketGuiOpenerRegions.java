package kamkeel.kingdomregions.Network;

import cpw.mods.fml.relauncher.Side;
import java.io.IOException;
import kamkeel.kingdomregions.KingdomRegions;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;

public class PacketGuiOpenerRegions extends AbstractMessage.AbstractServerMessage<PacketGuiOpenerRegions> {
   public int x;

   public PacketGuiOpenerRegions() {
   }

   public PacketGuiOpenerRegions(int ID) {
      this.x = ID;
   }

   protected void read(PacketBuffer buffer) throws IOException {
      this.x = buffer.readInt();
   }

   protected void write(PacketBuffer buffer) throws IOException {
      buffer.writeInt(this.x);
   }

   public void process(EntityPlayer player, Side side) {
      player.openGui(KingdomRegions.instance, this.x, player.worldObj, (int)player.posX, (int)player.posY, (int)player.posZ);
   }
}
