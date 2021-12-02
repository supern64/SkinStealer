package me.supern64.skinstealer;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.BlockPos;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSkull;
import net.minecraft.event.ClickEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Base64;

public class BlockSkullStealer {
    public static String lastSkullURL = null;

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            BlockPos pos = event.pos;
            Block block = event.world.getBlockState(pos).getBlock();
            TileEntity te = event.world.getTileEntity(pos);
            if (SkinStealer.isToggled && event.entityPlayer.getName().equals(Minecraft.getMinecraft().thePlayer.getName())) {
                if (block instanceof BlockSkull && te instanceof TileEntitySkull) {
                    TileEntitySkull skull = (TileEntitySkull) te;
                    if (skull.getSkullType() == 3 && skull.getPlayerProfile() != null) {
                        event.setCanceled(true);
                        GameProfile prof = skull.getPlayerProfile();
                        Property property = prof.getProperties().get("textures").iterator().next();
                        String decoded = new String(Base64.getDecoder().decode(property.getValue()));
                        JsonObject json = new JsonParser().parse(decoded).getAsJsonObject();
                        lastSkullURL = json.getAsJsonObject("textures").getAsJsonObject("SKIN").getAsJsonPrimitive("url").getAsString();
                        IChatComponent message = new ChatComponentText(SkinStealer.prefix + EnumChatFormatting.GREEN + "Click to copy URL!");
                        IChatComponent click = new ChatComponentText(EnumChatFormatting.AQUA + "" + EnumChatFormatting.BOLD + "SKULL");
                        click.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/skinstealer copy blockskull"));
                        message.appendText(" ").appendSibling(click);
                    }
                }
            }
        }
    }
}