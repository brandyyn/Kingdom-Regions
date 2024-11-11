package kamkeel.kingdomregions;

import java.util.ArrayList;
import java.util.List;

import kamkeel.kingdomregions.Network.PacketDispatcher;
import kamkeel.kingdomregions.Network.sendTextPacket;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class CommandDisplayText implements ICommand {
   public int compareTo(Object arg0) {
      return 0;
   }

   public String getCommandName() {
      return "displaytext";
   }

   public String getCommandUsage(ICommandSender sender) {
      return "/displaytext <selector|playername> <text> Displays a certain Text for the Player(s)";
   }

   public List getCommandAliases() {
      List<String> commandAliases = new ArrayList<>();
      commandAliases.add("displaytext");
      return commandAliases;
   }

   public void processCommand(ICommandSender sender, String[] args) {
      if (!sender.canCommandSenderUseCommand(4, this.getCommandName())) {
         this.sendMsg(sender, "Command for operators only");
         return;
      }
      if (args.length < 2) {
         this.sendMsg(sender, "Format Error: /displaytext <selector|playername> <text>");
         return;
      }

      String target = args[0];
      String text = args[1];

      // Resolve players based on selectors or name
      List<EntityPlayerMP> players = resolvePlayers(sender, target);

      if (players.isEmpty()) {
         this.sendMsg(sender, "No players found");
         return;
      }

      // Send the packet to all matched players
      for (EntityPlayerMP player : players) {
         PacketDispatcher.sendTo(new sendTextPacket(text), player);
      }
   }

   private List<EntityPlayerMP> resolvePlayers(ICommandSender sender, String target) {
      List<EntityPlayerMP> players = new ArrayList<>();
      MinecraftServer server = MinecraftServer.getServer();

      if (target.equalsIgnoreCase("@a")) {
         // All players
         for (Object obj : server.getConfigurationManager().playerEntityList) {
            if (obj instanceof EntityPlayerMP) {
               players.add((EntityPlayerMP) obj);
            }
         }
      } else if (target.equalsIgnoreCase("@p")) {
         // Closest player to the sender (simplified to the sender themselves)
         if (sender instanceof EntityPlayerMP) {
            players.add((EntityPlayerMP) sender);
         }
      } else if (target.equalsIgnoreCase("@r")) {
         // Random player
         List<EntityPlayerMP> allPlayers = new ArrayList<>();
         for (Object obj : server.getConfigurationManager().playerEntityList) {
            if (obj instanceof EntityPlayerMP) {
               allPlayers.add((EntityPlayerMP) obj);
            }
         }
         if (!allPlayers.isEmpty()) {
            players.add(allPlayers.get(server.getEntityWorld().rand.nextInt(allPlayers.size())));
         }
      } else {
         // Try to find a player by exact name
         EntityPlayerMP player = server.getConfigurationManager().func_152612_a(target); // getPlayerByUsername in 1.7.10
         if (player != null) {
            players.add(player);
         }
      }

      return players;
   }

   private void sendMsg(ICommandSender sender, String msg) {
      sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + msg));
   }

   public boolean canCommandSenderUseCommand(ICommandSender sender) {
      return true;
   }

   public List addTabCompletionOptions(ICommandSender sender, String[] args) {
      if (args.length == 1) {
         List<String> suggestions = new ArrayList<>();
         suggestions.add("@a");
         suggestions.add("@p");
         suggestions.add("@r");

         for (Object obj : MinecraftServer.getServer().getConfigurationManager().playerEntityList) {
            if (obj instanceof EntityPlayerMP) {
               EntityPlayerMP player = (EntityPlayerMP) obj;
               suggestions.add(player.getCommandSenderName());
            }
         }

         return suggestions;
      }
      return null;
   }

   public boolean isUsernameIndex(String[] args, int index) {
      return index == 0;
   }
}
