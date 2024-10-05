package kamkeel.kingdomregions.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import kamkeel.kingdomregions.ConfigurationMoD;
import kamkeel.kingdomregions.NBT.KingdomPlayer;
import kamkeel.kingdomregions.Network.PacketDispatcher;
import kamkeel.kingdomregions.Network.PacketGuiTeleport;
import kamkeel.kingdomregions.client.player.InventoryKingdomPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RuneStoneGUI extends GuiContainer {
   private float xSize_lo;
   private float ySize_lo;
   public static final int GUI_ID = 55;
   private int ImageWidth = 256;
   private int ImageHeight = 256;
   private InventoryPlayer inventoryPlayer;
   private GuiTextField text;
   private final InventoryKingdomPlayer inventory;
   private KingdomPlayer rinp;

   public RuneStoneGUI(EntityPlayer player, InventoryPlayer inventoryPlayer, InventoryKingdomPlayer inventoryCustom) {
      super(new InventoryContainerEmpty(player, inventoryPlayer, inventoryCustom));
      this.inventory = inventoryCustom;
      this.inventoryPlayer = inventoryPlayer;
   }

   protected void mouseClicked(int x, int y, int btn) {
      super.mouseClicked(x, y, btn);
   }

   public void initGui() {
      EntityClientPlayerMP entityClientPlayerMP = Minecraft.getMinecraft().thePlayer;
      KingdomPlayer rins = KingdomPlayer.get(entityClientPlayerMP);
      int k = this.width / 2;
      int l = this.height / 2;
      int tempk = k + 29;
      Keyboard.enableRepeatEvents(true);
      this.buttonList.clear();
      if (!KingdomPlayer.clientStrList.isEmpty()) {
         int n = KingdomPlayer.clientStrList.size();
         float alpha = (float)(360 / n);
         float p = (float)n - 1.0F;

         for(int i = 0; i < n; ++i) {
            float alpha2 = alpha * (float)i;
            float x = (float)Math.cos(Math.toRadians((double)alpha2) * (double)p) * 70.0F;
            float z = (float)Math.sin(Math.toRadians((double)alpha2) * (double)p) * 70.0F;
            this.buttonList.add(new ButtonRunestone(i, (int)((float)k + x - 8.0F), (int)((float)l + z - 8.0F), I18n.format((String)KingdomPlayer.clientStrList.get(i), new Object[0])));
         }
      }

      super.initGui();
   }

   public void drawScreen(int par1, int par2, float par3) {
      super.drawScreen(par1, par2, par3);
      this.xSize_lo = (float)par1;
      this.ySize_lo = (float)par2;
   }

   public void updateScreen() {
      super.updateScreen();
   }

   protected void drawGuiContainerForegroundLayer(int par1, int par2) {
   }

   protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
      FontRenderer fontRender = this.mc.fontRenderer;
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glEnable(3042);
      OpenGlHelper.glBlendFunc(770, 771, 1, 0);
      GL11.glBlendFunc(770, 771);
      this.mc.getTextureManager().bindTexture(new ResourceLocation("kingdomregions:textures/gui/gui_runestone_1.png"));
      int k = this.width / 2 - 128;
      int l = this.height / 2 - 125;
      this.drawTexturedModalRect(k, l, 0, 0, this.ImageWidth, this.ImageHeight);
      if (ConfigurationMoD.useMapCooldown) {
         k = this.width / 2;
         l = this.height / 2;
         this.drawCenteredString(fontRender, "Cooldown: " + KingdomPlayer.MapCooldown / 40, k, l - 3, 3423421);
      }

   }

   protected void actionPerformed(GuiButton button) {
      if (button.enabled) {
         if (ConfigurationMoD.useMapCooldown) {
            if (KingdomPlayer.MapCooldown == 0) {
               PacketDispatcher.sendToServer(new PacketGuiTeleport(button.id));
               KingdomPlayer.MapCooldown = ConfigurationMoD.TeleportCooldown;
            }
         } else {
            PacketDispatcher.sendToServer(new PacketGuiTeleport(button.id));
         }

      }
   }

   public void drawGuiBar(int x, int y, int u, int v, int width, int height, int texturewidth) {
      float f = 0.00390625F;
      float f1 = 0.00390625F;
      Tessellator tessellator = Tessellator.instance;
      tessellator.startDrawingQuads();
      tessellator.addVertexWithUV((double)(x + 0), (double)(y + height), (double)this.zLevel, (double)((float)(u + 0) * f), (double)((float)(v + height) * f1));
      tessellator.addVertexWithUV((double)(x + texturewidth), (double)(y + height), (double)this.zLevel, (double)((float)(u + width) * f), (double)((float)(v + height) * f1));
      tessellator.addVertexWithUV((double)(x + texturewidth), (double)(y + 0), (double)this.zLevel, (double)((float)(u + width) * f), (double)((float)(v + 0) * f1));
      tessellator.addVertexWithUV((double)(x + 0), (double)(y + 0), (double)this.zLevel, (double)((float)(u + 0) * f), (double)((float)(v + 0) * f1));
      tessellator.draw();
   }
}
