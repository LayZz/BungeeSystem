package de.flashbeatzz.bungeesystem.whitelist;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class WhitelistListener implements Listener {

    @EventHandler
    public void onLogin(PostLoginEvent e) {
        if(Whitelist.status()) {
            if(!Whitelist.getWhitelisted().contains(e.getPlayer().getUniqueId())) {
                e.getPlayer().disconnect(new TextComponent(Whitelist.getDisconnectMessage()));
            }
        }
    }

}
