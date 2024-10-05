package kamkeel.kingdomregions.NBT;

import java.util.ArrayList;
import kamkeel.kingdomregions.client.player.InventoryKingdomPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class KingdomPlayer implements IExtendedEntityProperties {
   public static final String PROP_NAME_REGIONS = "kingdom_regions";
   private final EntityPlayer player;
   private int timer;
   private int maxtime = 20;
   public ArrayList<String> DiscoverdBiomeList = new ArrayList();
   public static int MapCooldown = 0;
   public ArrayList<ChunkCoordinates> DiscoverdCordsList = new ArrayList();
   public ArrayList<String> DiscoverdNameList = new ArrayList();
   public static ArrayList<String> clientStrList;
   public InventoryKingdomPlayer inventory = new InventoryKingdomPlayer();

   public KingdomPlayer(EntityPlayer player) {
      this.player = player;
   }

   public static final void register(EntityPlayer player) {
      player.registerExtendedProperties("kingdom_regions", new KingdomPlayer(player));
   }

   public static final KingdomPlayer get(EntityPlayer player) {
      return (KingdomPlayer)player.getExtendedProperties("kingdom_regions");
   }

   public void copy(KingdomPlayer props) {
      this.DiscoverdCordsList = props.DiscoverdCordsList;
      this.DiscoverdNameList = props.DiscoverdNameList;
   }

   public void saveNBTData(NBTTagCompound compound) {
      NBTTagCompound properties = new NBTTagCompound();
      properties.setInteger("size", this.DiscoverdCordsList.size());

      int i;
      for(i = 0; i < this.DiscoverdCordsList.size(); ++i) {
         ChunkCoordinates cords = (ChunkCoordinates)this.DiscoverdCordsList.get(i);
         properties.setInteger("position_" + i + "_posX", cords.posX);
         properties.setInteger("position_" + i + "_posY", cords.posY);
         properties.setInteger("position_" + i + "_posZ", cords.posZ);
         properties.setString("position_" + i + "_Name", (String)this.DiscoverdNameList.get(i));
      }

      properties.setInteger("size_biomename", this.DiscoverdBiomeList.size());

      for(i = 0; i < this.DiscoverdBiomeList.size(); ++i) {
         properties.setString("biomename" + i + "_Name", (String)this.DiscoverdBiomeList.get(i));
      }

      this.inventory.writeToNBT(properties);
      properties.setInteger("MapCooldown", MapCooldown);
      compound.setTag("kingdom_regions", properties);
   }

   public void loadNBTData(NBTTagCompound compound) {
      NBTTagCompound properties = (NBTTagCompound)compound.getTag("kingdom_regions");
      this.DiscoverdCordsList = new ArrayList();
      int tempsize = properties.getInteger("size");
      this.DiscoverdNameList = new ArrayList();

      int tempsize2;
      int j;
      for(tempsize2 = 0; tempsize2 < tempsize; ++tempsize2) {
         j = properties.getInteger("position_" + tempsize2 + "_posX");
         int tempy = properties.getInteger("position_" + tempsize2 + "_posY");
         int tempz = properties.getInteger("position_" + tempsize2 + "_posZ");
         if (j != 0 && tempy != 0 && tempz != 0) {
            ChunkCoordinates cords = new ChunkCoordinates(j, tempy, tempz);
            this.DiscoverdCordsList.add(cords);
            this.DiscoverdNameList.add(properties.getString("position_" + tempsize2 + "_Name"));
         }
      }

      this.DiscoverdBiomeList = new ArrayList();
      tempsize2 = properties.getInteger("size_biomename");

      for(j = 0; j < tempsize2; ++j) {
         this.DiscoverdBiomeList.add(properties.getString("biomename" + j + "_Name"));
      }

      clientStrList = this.DiscoverdNameList;
      this.inventory.readFromNBT(properties);
      MapCooldown = properties.getInteger("MapCooldown");
   }

   public void init(Entity entity, World world) {
      this.DiscoverdCordsList = new ArrayList();
      this.DiscoverdNameList = new ArrayList();
      this.inventory = new InventoryKingdomPlayer();
      clientStrList = this.DiscoverdNameList;
   }

   public void addTeleportCords(ChunkCoordinates cords, String str) {
      if (!this.DiscoverdNameList.contains(str)) {
         this.DiscoverdCordsList.add(cords);
         this.DiscoverdNameList.add(str);
      }

   }

   public ArrayList<ChunkCoordinates> getDiscoverdCordsList() {
      return this.DiscoverdCordsList;
   }

   public void setDiscoverdCordsList(ArrayList<ChunkCoordinates> discoverdCordsList) {
      this.DiscoverdCordsList = discoverdCordsList;
   }

   public ArrayList<String> getDiscoverdNameList() {
      return this.DiscoverdNameList;
   }

   public void setDiscoverdNameList(ArrayList<String> discoverdNameList) {
      this.DiscoverdNameList = discoverdNameList;
   }
}
