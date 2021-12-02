package me.supern64.skinstealer;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSkull;
import net.minecraft.event.ClickEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;

public class ArmorStandStealer {
    public static String lastSkullURL = null;

    @SubscribeEvent
    public void onEntityHit(EntityInteractEvent event) {
        if (event.target instanceof EntityArmorStand) {
            EntityArmorStand target = (EntityArmorStand) event.target;
            if (SkinStealer.isToggled && event.entityPlayer.getName().equals(Minecraft.getMinecraft().thePlayer.getName())) {
                event.setCanceled(true);
                ItemStack headItem = target.getEquipmentInSlot(4);
                if (headItem != null && headItem.getItem() instanceof ItemSkull) {
                    lastSkullURL = SkinStealer.getSkullURL(headItem);
                    if (lastSkullURL != null) {
                        IChatComponent message = new ChatComponentText(SkinStealer.prefix + EnumChatFormatting.GREEN + "Click to copy URL!");
                        IChatComponent click = new ChatComponentText(EnumChatFormatting.AQUA + "" + EnumChatFormatting.BOLD + "SKULL");
                        click.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/skinstealer copy armorstandskull"));
                        message.appendText(" ").appendSibling(click);
                    }
                }
            }
        }
    }
}