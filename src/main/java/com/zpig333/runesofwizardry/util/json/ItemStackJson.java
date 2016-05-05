/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2016-01-06
 */
package com.zpig333.runesofwizardry.util.json;

import java.lang.reflect.Type;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/** Converts an ItemStack to and from a JsonObject
 * @author Xilef11
 *
 */
public class ItemStackJson implements JsonDeserializer<ItemStack>,JsonSerializer<ItemStack> {

	@Override
	public JsonElement serialize(ItemStack src, Type typeOfSrc,	JsonSerializationContext context) {
		JsonObject object = new JsonObject();
		int stackSize = src.stackSize;
		String item = ((ResourceLocation)Item.REGISTRY.getNameForObject(src.getItem())).toString();
		JsonElement nbt = context.serialize(src.getTagCompound());
		int meta = src.getItemDamage();
		//not dealing with ItemFrames
		object.addProperty("stackSize", stackSize);
		object.addProperty("item", item);
		object.add("stackTagCompound",nbt);
		object.addProperty("itemDamage", meta);
		return object;
	}

	@Override
	public ItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject object = json.getAsJsonObject();
		int size = object.get("stackSize").getAsInt();
		String itemName = object.get("item").getAsString();
		Item item = Item.getByNameOrId(itemName);
		NBTTagCompound nbt = context.deserialize(object.get("stackTagCompound"),NBTTagCompound.class);
		int meta = object.get("itemDamage").getAsInt();
		ItemStack stack = new ItemStack(item);
		stack.stackSize=size;
		stack.setTagCompound(nbt);
		stack.setItemDamage(meta);
		return stack;
	}

}
