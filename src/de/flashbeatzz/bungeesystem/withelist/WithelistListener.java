package de.flashbeatzz.bungeesystem.withelist;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class WithelistListener implements Listener {

    @EventHandler
    public void onLogin(PostLoginEvent e) {
        if(Withelist.status()) {
            if(!Withelist.getWithelisted().contains(e.getPlayer().getUniqueId())) {
                e.getPlayer().disconnect(new TextComponent(Withelist.getDisconnectMessage()));
            }
        }
    }

}
