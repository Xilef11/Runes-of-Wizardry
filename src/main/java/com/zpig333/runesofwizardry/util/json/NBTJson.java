package com.zpig333.runesofwizardry.util.json;

import java.lang.reflect.Type;
import java.util.Map;

import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagEnd;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
/**
 * Converts a NBTTagCompound to and from a JsonObject
 * @author Xilef11
 *
 */
public class NBTJson implements JsonDeserializer<NBTTagCompound>, JsonSerializer<NBTTagCompound> {
	@Override
	public JsonElement serialize(NBTTagCompound src, Type typeOfSrc,JsonSerializationContext context) {
		JsonObject object = new JsonObject();
		JsonObject tagMap = new JsonObject();
		Map map = ReflectionHelper.getPrivateValue(NBTTagCompound.class,src,"tagMap");
		for(Object o: map.entrySet()){
			if(o instanceof Map.Entry){
				Map.Entry e = (Map.Entry)o;
				//key is String, value is NBTBase
				JsonObject tag = new JsonObject();
				String key = (String)e.getKey();
				NBTBase val = (NBTBase)e.getValue();
				byte type = val.getId();
				tag.addProperty("type", type);
				JsonElement value = context.serialize(val);
				tag.add("tag",value);
				tagMap.add(key, tag);
			}
		}
		object.add("tagMap", tagMap);
		return object;
	}
	@Override
	public NBTTagCompound deserialize(JsonElement json, Type typeOfT,JsonDeserializationContext context) throws JsonParseException {
		NBTTagCompound tagCompound = new NBTTagCompound();
		JsonObject map = json.getAsJsonObject().get("tagMap").getAsJsonObject();
		for(Map.Entry<String,JsonElement> e : map.entrySet()){
			String key = e.getKey();
			JsonObject tag = e.getValue().getAsJsonObject();
			byte type = tag.get("type").getAsByte();
			JsonElement val = tag.get("tag");
			NBTBase value = deserializeType(type, val, context);
			tagCompound.setTag(key, value);
		}
		return tagCompound;
	}

	private NBTBase deserializeType(byte type, JsonElement tag, JsonDeserializationContext context){
		NBTBase value;
		//there may be a better way
		switch(type){
		case 0:
            value= context.deserialize(tag, NBTTagEnd.class);
            break;
        case 1:
            value = context.deserialize(tag, NBTTagByte.class);
            break;
        case 2:
        	value = context.deserialize(tag, NBTTagShort.class);
        	break;
        case 3:
        	value = context.deserialize(tag, NBTTagInt.class);
        	break;
        case 4:
        	value = context.deserialize(tag, NBTTagLong.class);
        	break;
        case 5:
        	value = context.deserialize(tag, NBTTagFloat.class);
        	break;
        case 6:
        	value = context.deserialize(tag, NBTTagDouble.class);
        	break;
        case 7:
        	value = context.deserialize(tag, NBTTagByteArray.class);
        	break;
        case 8:
        	value = context.deserialize(tag, NBTTagString.class);
        	break;
        case 9:
        	value = context.deserialize(tag, NBTTagList.class);
        	break;
        case 10:
        	value = context.deserialize(tag, NBTTagCompound.class);
        	break;
        case 11:
        	value = context.deserialize(tag, NBTTagIntArray.class);
        	break;
        default: value = context.deserialize(tag, NBTBase.class);
		}
		return value;
	}
}
