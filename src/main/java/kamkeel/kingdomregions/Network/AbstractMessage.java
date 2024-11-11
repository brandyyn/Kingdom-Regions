package kamkeel.kingdomregions.Network;

import com.google.common.base.Throwables;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import kamkeel.kingdomregions.KingdomRegions;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;

public abstract class AbstractMessage<T extends AbstractMessage<T>> implements IMessage, IMessageHandler<T, IMessage> {
   protected abstract void read(PacketBuffer var1) throws IOException;

   protected abstract void write(PacketBuffer var1) throws IOException;

   public abstract void process(EntityPlayer var1, Side var2);

   protected boolean isValidOnSide(Side side) {
      return true;
   }

   protected boolean requiresMainThread() {
      return true;
   }

   public void fromBytes(ByteBuf buffer) {
      try {
         this.read(new PacketBuffer(buffer));
      } catch (IOException var3) {
         throw Throwables.propagate(var3);
      }
   }

   public void toBytes(ByteBuf buffer) {
      try {
         this.write(new PacketBuffer(buffer));
      } catch (IOException var3) {
         throw Throwables.propagate(var3);
      }
   }

   public final IMessage onMessage(T msg, MessageContext ctx) {
      if (ctx.side == Side.CLIENT) {
         msg.process(KingdomRegions.proxy.getPlayerEntity(ctx), ctx.side);
      } else if (ctx.side == Side.SERVER) {
      }
      return null;
   }

   private void checkThreadAndEnqueue(T msg, MessageContext ctx) {
   }

   public abstract static class AbstractServerMessage<T extends AbstractMessage<T>> extends AbstractMessage<T> {
      protected final boolean isValidOnSide(Side side) {
         return side.isServer();
      }
   }

   public abstract static class AbstractClientMessage<T extends AbstractMessage<T>> extends AbstractMessage<T> {
      protected final boolean isValidOnSide(Side side) {
         return side.isClient();
      }
   }
}
