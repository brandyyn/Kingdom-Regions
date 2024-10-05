package kamkeel.kingdomregions;

import cpw.mods.fml.common.network.IGuiHandler;
import kamkeel.kingdomregions.client.gui.InventoryContainerEmpty;
import kamkeel.kingdomregions.client.gui.RuneStoneGUI;
import kamkeel.kingdomregions.client.player.InventoryKingdomPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler {
   public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
      return ID == 55 ? new InventoryContainerEmpty(player, player.inventory, new InventoryKingdomPlayer()) : null;
   }

   public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
      return ID == 55 ? new RuneStoneGUI(player, player.inventory, new InventoryKingdomPlayer()) : null;
   }
}
