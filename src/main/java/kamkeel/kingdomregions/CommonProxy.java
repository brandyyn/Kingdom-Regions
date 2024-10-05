package kamkeel.kingdomregions;

import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayer;

public class CommonProxy {
   public EntityPlayer getPlayerEntity(MessageContext ctx) {
      return ctx.getServerHandler().playerEntity;
   }
}
