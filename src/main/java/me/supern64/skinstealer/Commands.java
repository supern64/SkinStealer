package me.supern64.skinstealer;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.Arrays;

public class Commands extends CommandBase {
    @Override
    public String getCommandName() {
        return "skinstealer";
    }
    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("ss", "ssmod");
    }
    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "Base command for SkinStealer.";
    }
    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    public static void sendHelp(ICommandSender sender) {
        IChatComponent component = new ChatComponentText(
            SkinStealer.prefix + EnumChatFormatting.GREEN + "Usage: /skinstealer <command>\n" +
            SkinStealer.prefix + EnumChatFormatting.GREEN + "Commands:\n" +
            SkinStealer.prefix + EnumChatFormatting.GREEN + "  toggle - Toggles the skin stealer on and off.\n" +
            SkinStealer.prefix + EnumChatFormatting.GREEN + "  help - Displays this help message.");
        sender.addChatMessage(component); 
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 1) sendHelp(sender);
        if (args[0].equals("toggle")) {
            toggleCommand(sender);
        } else if (args[0].equals("copy") && args.length == 2){
            copyCommand(sender, args[1]);
        } else {
            sendHelp(sender);
        } 
    }

    // command implementation
    private void toggleCommand(ICommandSender sender) {
        SkinStealer.isToggled = !SkinStealer.isToggled;
        sender.addChatMessage(new ChatComponentText(SkinStealer.prefix + EnumChatFormatting.GREEN + "Steal " + (SkinStealer.isToggled ? "enabled." : "disabled.")));
    }

    private void copyCommand(ICommandSender sender, String type) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection selection;
        switch (type) {
            case "skin":
                if (PlayerStealer.lastSkinURL != null) {
                    selection = new StringSelection(PlayerStealer.lastSkinURL);
                    clipboard.setContents(selection, selection);
                } else {
                    sender.addChatMessage(new ChatComponentText(SkinStealer.prefix + EnumChatFormatting.RED + "No skin to be copied."));
                    return;
                }
                break;
            case "heldskull":
                if (PlayerStealer.lastHeldSkullURL != null) {
                    selection = new StringSelection(PlayerStealer.lastHeldSkullURL);
                    clipboard.setContents(selection, selection);
                } else {
                    sender.addChatMessage(new ChatComponentText(SkinStealer.prefix + EnumChatFormatting.RED + "No held skull to be copied."));
                    return;
                }
                break;
            case "headskull":
                if (PlayerStealer.lastHeadSkullURL != null) {
                    selection = new StringSelection(PlayerStealer.lastHeadSkullURL);
                    clipboard.setContents(selection, selection);
                } else {
                    sender.addChatMessage(new ChatComponentText(SkinStealer.prefix + EnumChatFormatting.RED + "No head skull to be copied."));
                    return;
                }
                break;
            default:
                sendHelp(sender);
                return;
        }
        sender.addChatMessage(new ChatComponentText(SkinStealer.prefix + EnumChatFormatting.GREEN + "Copied URL to clipboard!"));
    }
}