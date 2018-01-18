package me.badbones69.crazyenchantments.multisupport.nms;

import net.minecraft.server.v1_9_R2.NBTTagCompound;
import net.minecraft.server.v1_9_R2.NBTTagList;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_9_R2.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public class NMS_v1_9_R2 {

	public static ItemStack addGlow(ItemStack item) {
		if(item.hasItemMeta()) {
			if(item.getItemMeta().hasEnchants()) {
				return item;
			}
		}
		net.minecraft.server.v1_9_R2.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
		NBTTagCompound tag = null;
		if(!nmsStack.hasTag()) {
			tag = new NBTTagCompound();
			nmsStack.setTag(tag);
		}
		if(tag == null) {
			tag = nmsStack.getTag();
		}
		NBTTagList ench = new NBTTagList();
		tag.set("ench", ench);
		nmsStack.setTag(tag);
		return CraftItemStack.asCraftMirror(nmsStack);
	}

	@SuppressWarnings("deprecation")
	public static ItemStack getSpawnEgg(EntityType type, int amount) {
		ItemStack item = new ItemStack(Material.MONSTER_EGG, amount);
		net.minecraft.server.v1_9_R2.ItemStack stack = CraftItemStack.asNMSCopy(item);
		NBTTagCompound tagCompound = stack.getTag();
		if(tagCompound == null) {
			tagCompound = new NBTTagCompound();
		}
		NBTTagCompound id = new NBTTagCompound();
		id.setString("id", type.getName());
		tagCompound.set("EntityTag", id);
		stack.setTag(tagCompound);
		return CraftItemStack.asBukkitCopy(stack);
	}

}