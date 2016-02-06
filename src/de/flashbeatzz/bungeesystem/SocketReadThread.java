package de.flashbeatzz.bungeesystem;

import de.flashbeatzz.bungeesystem.banmanager.BanManager;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;
import java.util.UUID;

public class SocketReadThread implements Runnable {

    HashMap<String, SocketRunnable> connectedServers = new HashMap<>();

    @Override
    public void run() {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(19888, 0, InetAddress.getByName("109.230.231.247"));
            while (true) {
                try {
                    Data.console.info("successful flushed");
                    Socket socket = serverSocket.accept();
                    Data.console.info("1");

                    new Thread(new SocketRunnable(socket)).start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public class SocketRunnable implements Runnable {

        private Socket socket;
        private PrintWriter printWriter;

        public SocketRunnable(Socket socket) {
            this.socket = socket;
            try {
                this.printWriter = new PrintWriter(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public Socket getSocket() {
            return this.socket;
        }

        public PrintWriter getPrintWriter() {
            return this.printWriter;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Scanner scanner = new Scanner(socket.getInputStream());

                    if(scanner.hasNext()) {
                        String[] msg = scanner.nextLine().split("/§§/");
                        String target = msg[0];
                        String header = msg[1];
                        String message = msg[2];

                        switch (target) {
                            case "BUNGEECORD":
                                switch (header) {
                                    case "SYSTEM":
                                        if (message.startsWith("CONNECT")) {
                                            connectedServers.put(message, SocketRunnable.this);
                                            Data.console.info("SOCKET FROM SERVER " + message + " CONNECTED");
                                        } else if (message.startsWith("DISCONNECT")) {
                                            Thread.currentThread().interrupt();
                                            Data.console.info("SOCKET FROM SERVER " + message + " DISCONNECTED");
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
                                        UUID kicker = UUID.fromString(message.split("/§§/")[0]);
                                        UUID kicked = UUID.fromString(message.split("/§§/")[1]);
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
                                    case "UNBAN": {
                                        UUID banned = UUID.fromString(message.split("<>")[0]);
                                        UUID requester = UUID.fromString(message.split("<>")[1]);

                                        BanManager.unbanPlayer(banned, requester);
                                        break;
                                    }
                                    case "CHECK": {
                                        UUID checked = UUID.fromString(message.split("<>")[0]);
                                        UUID checker = UUID.fromString(message.split("<>")[1]);

                                        BanManager.check(checked, checker);
                                        break;
                                    }
                                    default:
                                        BungeeSystem.getInstance().getProxy().getPluginManager().callEvent(new MessageEvent(header, message));
                                        break;
                                }
                                break;
                            case "ALL":
                                BungeeSystem.getInstance().getProxy().getPluginManager().callEvent(new MessageEvent(header, message));
                                for (SocketRunnable socketRunnable : connectedServers.values()) {
                                    socketRunnable.getPrintWriter().print(header + "/§§/" + message);
                                    socketRunnable.getPrintWriter().flush();
                                }
                                break;
                            case "SPIGOT":
                                for (SocketRunnable socketRunnable : connectedServers.values()) {
                                    socketRunnable.getPrintWriter().print(header + "/§§/" + message);
                                    socketRunnable.getPrintWriter().flush();
                                }
                                break;
                            default:
                                if (connectedServers.containsKey(target)) {
                                    SocketRunnable socketRunnable = connectedServers.get(target);
                                    socketRunnable.getPrintWriter().print(header + "/§§/" + message);
                                    socketRunnable.getPrintWriter().flush();
                                }
                                break;
                        }
                    }
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
