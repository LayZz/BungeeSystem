package de.flashbeatzz.bungeesystem.messages;

import java.util.HashMap;

public class Messages {

    public static HashMap<String, Message> messages = new HashMap<>();

    public static Message getMessage(String tag) {
        if(messages.containsKey(tag)) {
            return messages.get(tag);
        }
        return null;
    }

    public static String getMessage(String tag, String lang) {
        if(messages.containsKey(tag)) {
            Message message = messages.get(tag);
            return (lang.equals("de") ? message.getGerman() : message.getEnglish());
        }
        return "Message with tag '" + tag + "' not found!";
    }

}
