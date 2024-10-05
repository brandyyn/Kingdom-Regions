package kamkeel.kingdomregions;

import astrotibs.villagenames.village.StructureVillageVN;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

class ActiveModInteropProxy implements ModInteropProxy {
   public void load() {
   }

   public NBTTagCompound getOrMakeVNInfo(World world, int posX, int posY, int posZ) {
      return StructureVillageVN.getOrMakeVNInfo(world, posX, posY, posZ);
   }
}
