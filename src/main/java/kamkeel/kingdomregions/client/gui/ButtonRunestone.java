package kamkeel.kingdomregions.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class ButtonRunestone extends GuiButton {
   public ResourceLocation buttonTexture = new ResourceLocation("kingdomregions:textures/gui/gui_runestone.png");
   private int ID = 0;

   public ButtonRunestone(int id, int width, int height, String displayString) {
      super(id, width, height, displayString);
      this.width = 16;
      this.height = 16;
   }

   public void drawButton(Minecraft minecraft, int xCoord, int yCoord) {
      if (this.visible) {
         FontRenderer fontrenderer = minecraft.fontRenderer;
         minecraft.getTextureManager().bindTexture(this.buttonTexture);
         this.field_146123_n = xCoord >= this.xPosition && yCoord >= this.yPosition && xCoord < this.xPosition + this.width && yCoord < this.yPosition + this.height;
         int k = this.getHoverState(this.field_146123_n);
         GL11.glEnable(3042);
         OpenGlHelper.glBlendFunc(770, 771, 1, 0);
         GL11.glBlendFunc(770, 771);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         if (k == 1) {
            this.ID = 0;
         } else if (k == 2) {
            this.ID = 64;
         }

         this.drawButton(this.xPosition, this.yPosition);
         this.mouseDragged(minecraft, xCoord, yCoord);
         int l = 14737632;
         if (this.packedFGColour != 0) {
            l = this.packedFGColour;
         } else if (!this.enabled) {
            l = 10526880;
         } else if (this.field_146123_n) {
            l = 16777120;
         }

         if (k == 2) {
            int x2 = 12;
            int x1 = (int)((double)this.displayString.length() * 3.5D);
            drawRect(this.xPosition - x1 + x2, this.yPosition + 5, this.xPosition + x1 + x2, this.yPosition + 17, Integer.MIN_VALUE);
            this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + x2, this.yPosition + 7, l);
         }
      }

   }

   public void drawButton(int x, int y) {
      float f = 0.00390625F;
      float f1 = 0.00390625F;
      Tessellator tessellator = Tessellator.instance;
      tessellator.startDrawingQuads();
      tessellator.addVertexWithUV((double)x, (double)(y + 16), (double)this.zLevel, (double)((float)(this.ID + 0) * f), (double)(64.0F * f1));
      tessellator.addVertexWithUV((double)(x + 16), (double)(y + 16), (double)this.zLevel, (double)((float)(this.ID + 64) * f), (double)(64.0F * f1));
      tessellator.addVertexWithUV((double)(x + 16), (double)(y + 0), (double)this.zLevel, (double)((float)(this.ID + 64) * f), (double)(0.0F * f1));
      tessellator.addVertexWithUV((double)(x + 0), (double)(y + 0), (double)this.zLevel, (double)((float)(this.ID + 0) * f), (double)(0.0F * f1));
      tessellator.draw();
   }
}
