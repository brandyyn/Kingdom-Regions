package kamkeel.kingdomregions;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public interface ModInteropProxy {
   void load();

   NBTTagCompound getOrMakeVNInfo(World var1, int var2, int var3, int var4);
}
