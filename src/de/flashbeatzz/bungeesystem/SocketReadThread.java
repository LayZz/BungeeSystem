package de.flashbeatzz.bungeesystem;

import net.md_5.bungee.BungeeCord;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class SocketReadThread implements Runnable {

    @Override
    public void run() {
        Socket socket = BungeeSystem.getInstance().socket;
        try {
            Scanner scanner = new Scanner(socket.getInputStream());
            while (true) {
                if(scanner.hasNext()) {
                    String[] array = scanner.nextLine().split("/§§/");
                    String header = array[0];
                    String message = array[1];
                    BungeeCord.getInstance().getPluginManager().callEvent(new MessageEvent(header, message));
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
