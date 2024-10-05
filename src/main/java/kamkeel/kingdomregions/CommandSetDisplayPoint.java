package kamkeel.kingdomregions;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class CommandSetDisplayPoint implements ICommand {
   public int compareTo(Object arg0) {
      return 0;
   }

   public String getCommandName() {
      return "adddisplayPosition";
   }

   public String getCommandUsage(ICommandSender s) {
      return "/adddisplayPosition <PosX> <PosY> <PosZ> <Radius> <Name> Displays a certain Text for the Player at a certain Position";
   }

   public List getCommandAliases() {
      List<String> commandAliases = new ArrayList();
      commandAliases.add("adddisplayPosition");
      return commandAliases;
   }

   public void processCommand(ICommandSender s, String[] args) {
      if (!s.canCommandSenderUseCommand(4, this.getCommandName())) {
         this.sendMsg(s, "Command for operators only");
      } else if (args.length != 5) {
         this.sendMsg(s, "Format Error: /adddisplayPosition <PosX> <PosY> <PosZ> <Radius> <Name>");
      } else {
         int i;
         for(i = 0; i < args.length; ++i) {
            if (args[i] == null) {
               this.sendMsg(s, "Format Error: /adddisplayPosition <PosX> <PosY> <PosZ> <Radius> <Name>");
               return;
            }
         }

         for(i = 1; i < 4; ++i) {
            if (!args[i].matches("[0-9.]+")) {
               this.sendMsg(s, "incorrect arg: " + args[i]);
               return;
            }
         }

         i = Integer.parseInt(args[0]);
         int y = Integer.parseInt(args[1]);
         int z = Integer.parseInt(args[2]);
         int radius = Integer.parseInt(args[3]);
         String Name = args[4];
         int[] templist = new int[]{i, y, z, radius};
         ConfigurationMoD.addPoint(templist, Name);
      }
   }

   private void sendMsg(ICommandSender s, String msg) {
      s.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + msg));
   }

   public boolean canCommandSenderUseCommand(ICommandSender s) {
      return true;
   }

   public List addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_) {
      return null;
   }

   public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
      return false;
   }
}
