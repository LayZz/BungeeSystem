package de.flashbeatzz.bungeesystem.withelist;

import de.flashbeatzz.bungeesystem.UUIDLibrary;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

import java.util.UUID;

public class cmdWithelist extends Command {

    public cmdWithelist() {
        super("withelist");
    }

    @Override
    public void execute(CommandSender cs, String[] args) {
        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("activate") || args[0].equalsIgnoreCase("on") || args[0].equalsIgnoreCase("enable")) {
                if(Withelist.enable()) {
                    cs.sendMessage(new TextComponent("You enabled the withelist."));
                } else  {
                    cs.sendMessage(new TextComponent("Withelist is already enabled."));
                }
            } else if(args[0].equalsIgnoreCase("deactivate") || args[0].equalsIgnoreCase("off") || args[0].equalsIgnoreCase("disable")) {
                if(Withelist.disable()) {
                    cs.sendMessage(new TextComponent("You disabled the withelist."));
                } else  {
                    cs.sendMessage(new TextComponent("Withelist is not enabled."));
                }
            } else if(args[0].equalsIgnoreCase("status")) {
                String str = Withelist.status() ? "enabled" : "disabled";
                cs.sendMessage(new TextComponent("Withelist: " + str));
            } else if(args[0].equalsIgnoreCase("list")) {
                String str = "";
                for(UUID uuid : Withelist.getWithelisted()) {
                    str += UUIDLibrary.getNameToUUID(uuid) + ", ";
                }
                str = str.substring(0, str.length() - 1);
                cs.sendMessage(new TextComponent("Withelisted Players: \n" + str));
            } else {
                cs.sendMessage(new TextComponent("§cWrong usage: /withelist <enable | disable | status | add [player] | remove [player]>"));
            }
        } else if(args.length >= 2) {
            if(args[0].equalsIgnoreCase("add")) {
                if(Withelist.addPlayer(UUIDLibrary.getUUIDtoName(args[1]))) {
                    cs.sendMessage(new TextComponent("You added " + args[1] + " to the withelist."));
                } else {
                    cs.sendMessage(new TextComponent(args[1] + " is already on the withelist."));
                }
            } else if(args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("rem")) {
                if(Withelist.removePlayer(UUIDLibrary.getUUIDtoName(args[1]))) {
                    cs.sendMessage(new TextComponent("You removed " + args[1] + " to the withelist."));
                } else {
                    cs.sendMessage(new TextComponent(args[1] + " is not on the withelist."));
                }
            } else {
                cs.sendMessage(new TextComponent("§cWrong usage: /withelist <enable | disable | status | add [player] | remove [player]>"));
            }
        } else {
            cs.sendMessage(new TextComponent("§cWrong usage: /withelist <enable | disable | status | add [player] | remove [player]>"));
        }
    }
}
