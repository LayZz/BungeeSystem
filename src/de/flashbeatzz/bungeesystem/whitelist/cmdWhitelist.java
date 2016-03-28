package de.flashbeatzz.bungeesystem.whitelist;

import de.flashbeatzz.bungeesystem.UUIDLibrary;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

import java.util.UUID;

public class cmdWhitelist extends Command {

    public cmdWhitelist() {
        super("whitelist");
    }

    @Override
    public void execute(CommandSender cs, String[] args) {
        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("activate") || args[0].equalsIgnoreCase("on") || args[0].equalsIgnoreCase("enable")) {
                if(Whitelist.enable()) {
                    cs.sendMessage(new TextComponent("You enabled the whitelist."));
                } else  {
                    cs.sendMessage(new TextComponent("whitelist is already enabled."));
                }
            } else if(args[0].equalsIgnoreCase("deactivate") || args[0].equalsIgnoreCase("off") || args[0].equalsIgnoreCase("disable")) {
                if(Whitelist.disable()) {
                    cs.sendMessage(new TextComponent("You disabled the whitelist."));
                } else  {
                    cs.sendMessage(new TextComponent("whitelist is not enabled."));
                }
            } else if(args[0].equalsIgnoreCase("status")) {
                String str = Whitelist.status() ? "enabled" : "disabled";
                cs.sendMessage(new TextComponent("whitelist: " + str));
            } else if(args[0].equalsIgnoreCase("list")) {
                String str = "";
                for(UUID uuid : Whitelist.getWhitelisted()) {
                    str += UUIDLibrary.getNameToUUID(uuid) + ", ";
                }
                str = str.substring(0, str.length() - 1);
                cs.sendMessage(new TextComponent("Whitelisted Players: \n" + str));
            } else {
                cs.sendMessage(new TextComponent("§cWrong usage: /whitelist <enable | disable | status | add [player] | remove [player]>"));
            }
        } else if(args.length >= 2) {
            if(args[0].equalsIgnoreCase("add")) {
                for(int i = 1; args[i] != null; i++) {
                    if (Whitelist.addPlayer(UUIDLibrary.getUUIDtoName(args[i]))) {
                        cs.sendMessage(new TextComponent("You added " + args[i] + " to the whitelist."));
                    } else {
                        cs.sendMessage(new TextComponent(args[i] + " is already on the whitelist."));
                    }
                }
            } else if(args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("rem")) {
                for(int i = 1; args[i] != null; i++) {
                    if (Whitelist.removePlayer(UUIDLibrary.getUUIDtoName(args[i]))) {
                        cs.sendMessage(new TextComponent("You removed " + args[i] + " to the whitelist."));
                    } else {
                        cs.sendMessage(new TextComponent(args[i] + " is not on the whitelist."));
                    }
                }
            } else {
                cs.sendMessage(new TextComponent("§cWrong usage: /whitelist <enable | disable | status | add [player] | remove [player]>"));
            }
        } else {
            cs.sendMessage(new TextComponent("§cWrong usage: /whitelist <enable | disable | status | add [player] | remove [player]>"));
        }
    }
}
