package kamkeel.kingdomregions;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

class DummyModInteropProxy implements ModInteropProxy {
   public void load() {
   }

   public NBTTagCompound getOrMakeVNInfo(World world, int posX, int posY, int posZ) {
      return null;
   }
}
