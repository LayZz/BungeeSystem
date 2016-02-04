package de.flashbeatzz.bungeesystem;

import de.flashbeatzz.bungeesystem.banmanager.BanManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.UUID;

public class SocketReadThread implements Runnable {

    HashMap<String, Socket> connectedServers = new HashMap<>();

    @Override
    public void run() {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(19888);

            while (true) {
                try {
                    Socket socket = serverSocket.accept();

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    // targets
                    // - ALL
                    // - SPIGOT
                    // - BUNGEECORD TODO
                    // - specific name
                    String target = (String) bufferedReader.lines().toArray()[0];
                    String header = (String) bufferedReader.lines().toArray()[1];
                    String message = (String) bufferedReader.lines().toArray()[2];

                    bufferedReader.close();

                    switch (target) {
                        case "BUNGEECORD":
                            switch (header) {
                                case "SYSTEM":
                                    if (message.startsWith("CONNECT")) {
                                        // server connected
                                        String name = message.split(" ")[1];

                                        connectedServers.put(name, socket);
                                    } else if (message.startsWith("DISCONNECT")) {
                                        // server disconnected
                                        String name = message.split(" ")[1];

                                        connectedServers.remove(name);
                                    }
                                    break;
                                case "BAN": {
                                    UUID banner = UUID.fromString(message.split("<>")[0]);
                                    UUID banned = UUID.fromString(message.split("<>")[1]);
                                    String reason = message.split("<>")[2];

                                    BanManager.banPlayer(banned, banner, reason);
                                    break;
                                }
                                case "TEMPBAN": {
                                    UUID banner = UUID.fromString(message.split("<>")[0]);
                                    UUID banned = UUID.fromString(message.split("<>")[1]);
                                    String time = message.split("<>")[2];
                                    String reason = message.split("<>")[3];

                                    BanManager.tempBanPlayer(banned, banner, reason, time);
                                    break;
                                }
                                case "KICK": {
                                    UUID kicker = UUID.fromString(message.split("<>")[0]);
                                    UUID kicked = UUID.fromString(message.split("<>")[1]);
                                    String reason = message.split("<>")[2];

                                    BanManager.kickPlayer(kicked, kicker, reason);
                                    break;
                                }
                                case "WARN": {
                                    UUID warner = UUID.fromString(message.split("<>")[0]);
                                    UUID warned = UUID.fromString(message.split("<>")[1]);
                                    String reason = message.split("<>")[2];

                                    BanManager.warnPlayer(warned, warner, reason);
                                    break;
                                }
                                default:
                                    // TODO send to bungeecord (call event)
                                    break;
                            }
                            break;
                        case "ALL":
                            // TODO send to bungeecord (call event)

                            for (Socket server : connectedServers.values()) {
                                PrintWriter out = new PrintWriter(server.getOutputStream(), true);
                                out.print(header + ";" + message);
                                out.flush();
                                out.close();
                            }
                            break;
                        case "SPIGOT":
                            break;
                        default:
                            if (connectedServers.containsKey(target)) {
                                PrintWriter out = new PrintWriter(connectedServers.get(target).getOutputStream(), true);
                                out.print(header + ";" + message);
                                out.flush();
                                out.close();
                            }
                            break;
                    }
                } catch (Exception e) {

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
