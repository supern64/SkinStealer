package me.supern64.skinstealer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ChatStyle;
import net.minecraft.event.ClickEvent;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = SkinStealer.MODID, version = SkinStealer.VERSION)
public class SkinStealer
{
    public static final String MODID = "skinstealer";
    public static final String VERSION = "1.0";
    public static boolean isToggled = false;

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(this);
        ClientCommandHandler.instance.registerCommand(new ToggleCommand());
    }

    public class ToggleCommand extends CommandBase {
        @Override
        public String getCommandName() {
            return "togglesteal";
        }
        @Override
        public String getCommandUsage(ICommandSender sender) {
            return "Toggles into steal mode.";
        }
        @Override
        public int getRequiredPermissionLevel() {
            return 0;
        }
        @Override
        public void processCommand(ICommandSender sender, String[] args) throws CommandException {
            isToggled = !isToggled;
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Steal toggled " + (isToggled ? "on." : "off.")));
        }
    }

    @SubscribeEvent
    public void onEntityHit(AttackEntityEvent event) {
        if (event.target instanceof EntityOtherPlayerMP) {
            EntityOtherPlayerMP target = (EntityOtherPlayerMP) event.target;
            if (isToggled && event.entityPlayer.getName().equals(Minecraft.getMinecraft().thePlayer.getName())) {
                String skinID = target.getLocationSkin().toString().split("/")[1];
                if (skinID.equals("entity")) {
                    event.entityPlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "This player has no custom skin. Please wait for it to load if they do."));
                    return;
                }
                String skinURL = "https://textures.minecraft.net/texture/" + skinID;
                ChatComponentText clickable = new ChatComponentText(EnumChatFormatting.GREEN + "URL: " + skinURL);
                ChatStyle linkClickStyle = new ChatStyle();
                linkClickStyle.setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, skinURL));
                clickable.setChatStyle(linkClickStyle);
                event.entityPlayer.addChatMessage(clickable);
            }
        }
    }
}
