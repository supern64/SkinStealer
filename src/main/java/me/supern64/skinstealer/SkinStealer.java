package me.supern64.skinstealer;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.client.ClientCommandHandler;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Base64;

@Mod(modid = SkinStealer.MODID, version = SkinStealer.VERSION)
public class SkinStealer
{
    public static final String MODID = "skinstealer";
    public static final String VERSION = "1.1.0";

    public static final String prefix = EnumChatFormatting.BLUE + "[SkinStealer] ";
    public static final String baseTextureURL = "https://textures.minecraft.net/texture/";

    public static boolean isToggled = false;

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new PlayerStealer());
        ClientCommandHandler.instance.registerCommand(new Commands());

    }

    public static String getSkullURL(ItemStack skull) {
        if (skull.hasTagCompound() && skull.getTagCompound().hasKey("SkullOwner")) {
            NBTTagCompound tags = skull.getTagCompound();
            NBTBase skullTags = tags.getTag("SkullOwner");
            if (skullTags instanceof NBTTagString) { // playername
                return "name:" + ((NBTTagString)skullTags).getString();
            } else if (skullTags instanceof NBTTagCompound) { // custom
                NBTTagCompound skullTagsCompound = (NBTTagCompound) skullTags;
                System.out.println(skullTags.toString());
                String textureString = skullTagsCompound.getCompoundTag("Properties").getTagList("textures", Constants.NBT.TAG_COMPOUND).getCompoundTagAt(0).getString("Value");
                String decoded = new String(Base64.getDecoder().decode(textureString));
                JsonObject json = new JsonParser().parse(decoded).getAsJsonObject();
                return json.getAsJsonObject("textures").getAsJsonObject("SKIN").getAsJsonPrimitive("url").getAsString();
            } else { return null; }
        }
        return null;
    }

    
}
