package de.flashbeatzz.bungeesystem;

import net.md_5.bungee.api.plugin.Event;

public class MessageEvent extends Event {

    private String header;
    private String message;

    public MessageEvent(String header, String message) {
        this.header = header;
        this.message = message;
    }

    public String getHeader() {
        return header;
    }

    public String getMessage() {
        return message;
    }
}
