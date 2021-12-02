package me.supern64.skinstealer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSkull;
import net.minecraft.event.ClickEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;

public class PlayerStealer {
    public static String lastSkinURL = null;
    public static String lastHeldSkullURL = null;
    public static String lastHeadSkullURL = null;

    @SubscribeEvent
    public void onEntityHit(AttackEntityEvent event) {
        if (event.target instanceof EntityOtherPlayerMP) {
            EntityOtherPlayerMP target = (EntityOtherPlayerMP) event.target;
            if (SkinStealer.isToggled && event.entityPlayer.getName().equals(Minecraft.getMinecraft().thePlayer.getName())) {
                boolean hasSkin = false;
                boolean hasHeldSkull = false;
                boolean hasHeadSkull = false;

                // player skin
                String skinID = target.getLocationSkin().toString().split("/")[1];
                if (skinID.equals("entity")) {
                    hasSkin = false;
                    lastSkinURL = null;
                } else {
                    hasSkin = true;
                    lastSkinURL = SkinStealer.baseTextureURL + skinID;
                }
                
                // held skull
                ItemStack heldItem = target.getHeldItem();
                if (heldItem != null && (heldItem.getItem() instanceof ItemSkull)) {
                    lastHeldSkullURL = SkinStealer.getSkullURL(heldItem);
                    if (lastHeldSkullURL != null) {
                        hasHeldSkull = true;
                    }
                }

                // head skull
                ItemStack headItem = target.getEquipmentInSlot(4);
                if (headItem != null && (headItem.getItem() instanceof ItemSkull)) {
                    lastHeadSkullURL = SkinStealer.getSkullURL(headItem);
                    if (lastHeadSkullURL != null) {
                        hasHeadSkull = true;
                    }
                }

                if (!(hasSkin || hasHeldSkull || hasHeadSkull)) {
                    event.entityPlayer.addChatMessage(new ChatComponentText(SkinStealer.prefix + EnumChatFormatting.RED + "No custom skin is loaded. Wait if they have one."));
                    return;
                } else {
                    // skin message
                    IChatComponent message = new ChatComponentText(SkinStealer.prefix + EnumChatFormatting.GREEN + "Click to copy URL!");

                    if (hasSkin) {
                        IChatComponent skinClick = new ChatComponentText(EnumChatFormatting.AQUA + "" + EnumChatFormatting.BOLD + "SKIN");
                        skinClick.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/skinstealer copy skin"));
                        message.appendText(" ").appendSibling(skinClick);
                    }

                    if (hasHeldSkull) {
                        IChatComponent skinClick = new ChatComponentText(EnumChatFormatting.YELLOW + "" + EnumChatFormatting.BOLD + "HELD SKULL");
                        skinClick.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/skinstealer copy heldskull"));
                        message.appendText(" ").appendSibling(skinClick);
                    }

                    if (hasHeadSkull) {
                        IChatComponent skinClick = new ChatComponentText(EnumChatFormatting.RED + "" + EnumChatFormatting.BOLD + "HEAD SKULL");
                        skinClick.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/skinstealer copy headskull"));
                        message.appendText(" ").appendSibling(skinClick);
                    }

                    event.entityPlayer.addChatMessage(message);
                }
            }
        }
    }
}