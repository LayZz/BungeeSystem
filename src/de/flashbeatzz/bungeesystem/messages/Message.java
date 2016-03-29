package de.flashbeatzz.bungeesystem.messages;

public class Message {

    private String tag;
    private String german, english;

    public Message(String tag, String german, String english) {
        this.tag = tag;
        this.german = german;
        this.english = english;
    }

    public String getTag() {
        return tag;
    }

    public String getGerman() {
        return german;
    }

    public String getEnglish() {
        return english;
    }
}
