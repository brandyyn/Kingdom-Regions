package kamkeel.kingdomregions;

import java.util.ArrayList;
import java.util.List;
import kamkeel.kingdomregions.Network.PacketDispatcher;
import kamkeel.kingdomregions.Network.sendTextPacket;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class CommandDisplayText implements ICommand {
   public int compareTo(Object arg0) {
      return 0;
   }

   public String getCommandName() {
      return "displaytext";
   }

   public String getCommandUsage(ICommandSender s) {
      return "/displaytext <playername> <text> Displays a certain Text for the Player";
   }

   public List getCommandAliases() {
      List<String> commandAliases = new ArrayList();
      commandAliases.add("displaytext");
      return commandAliases;
   }

   public void processCommand(ICommandSender s, String[] args) {
      if (!s.canCommandSenderUseCommand(4, this.getCommandName())) {
         this.sendMsg(s, "Command for operators only");
      } else if (args[1] == null) {
         this.sendMsg(s, "Format Error: /displaytext getPlayerEntityByName");
      } else {
         EntityPlayer p = s.getEntityWorld().getPlayerEntityByName(args[0]);
         if (p == null) {
            this.sendMsg(s, "Player not found");
         } else {
            PacketDispatcher.sendTo(new sendTextPacket(args[1]), (EntityPlayerMP)p);
         }
      }
   }

   private void sendMsg(ICommandSender s, String msg) {
      s.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + msg));
   }

   public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_) {
      return true;
   }

   public List addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_) {
      return null;
   }

   public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
      return false;
   }
}
