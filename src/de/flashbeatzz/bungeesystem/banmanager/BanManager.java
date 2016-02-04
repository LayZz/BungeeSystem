package de.flashbeatzz.bungeesystem.banmanager;

import de.flashbeatzz.bungeesystem.Data;
import de.flashbeatzz.bungeesystem.MySQL;
import de.flashbeatzz.bungeesystem.uuid_lib.UUIDLibrary;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class BanManager {

    public BanManager() {
        MySQL.createTable("banmanager_bans", "`id` int(11) NOT NULL AUTO_INCREMENT",
                "  `banned_uuid` varchar(100) NOT NULL",
                "  `banner_uuid` varchar(100) NOT NULL",
                "  `reason` varchar(50) NOT NULL",
                "  `ban_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP",
                "  `ban_expires` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00'",
                "  `permanent` tinyint(1) NOT NULL",
                "  PRIMARY KEY (`id`)");
        MySQL.createTable("banmanager_warns", "`id` int(11) NOT NULL AUTO_INCREMENT",
                "  `warned_uuid` varchar(100) NOT NULL",
                "  `warner_uuid` varchar(100) NOT NULL",
                "  `reason` varchar(50) NOT NULL",
                "  PRIMARY KEY (`id`)");
    }

    public static void warnPlayer(UUID uuid, UUID warner, String reason) {
        ProxiedPlayer p = BungeeCord.getInstance().getPlayer(uuid);
        if(Objects.equals(warner.toString(), new UUID(0L, 0L).toString())) {
            if(UUIDLibrary.isRegistred(uuid)) {
                MySQL.update("INSERT INTO `banmanager_warns` (`warned_uuid`, `warner_uuid`, `reason`) VALUES ('" + uuid.toString() + "', '" + warner.toString() + "', '" + reason + "');");
                p.sendMessage(ChatMessageType.CHAT, new TextComponent(
                        "§8[§4WARN§8] §cDu wurdest von §6SERVER§c gewarnt! \n" +
                        "§8[§4WARN§8] §cGrund: §6" + reason));
                Data.console.info("§8[§2WARN§8] §aDu hast §6" + p.getName() + "§a gewarnt!");
                return;
            }
            Data.console.warning("§8[§4WARN§8] §cDer Spieler §6" + p.getName() + "§c existiert nicht!");
        } else {
            ProxiedPlayer p1 = BungeeCord.getInstance().getPlayer(warner);
            if(UUIDLibrary.isRegistred(uuid)) {
                MySQL.update("INSERT INTO `banmanager_warns` (`warned_uuid`, `warner_uuid`, `reason`) VALUES ('" + uuid.toString() + "', '" + warner.toString() + "', '" + reason + "');");
                p.sendMessage(ChatMessageType.CHAT, new TextComponent(
                        "§8[§4WARN§8] §cDu wurdest von §6" + p1.getName() + "§c gewarnt! \n" +
                        "§8[§4WARN§8] §cGrund: §6" + reason));
                p1.sendMessage(new TextComponent("§8[§2WARN§8] §aDu hast §6" + p.getName() + "§a gewarnt!"));
                return;
            }
            p1.sendMessage(new TextComponent("§8[§4WARN§8] §cDer Spieler §6" + p.getName() + "§c existiert nicht!"));
        }
    }

    public static void kickPlayer(UUID player, UUID kicker, String reason) {
        ProxiedPlayer p = BungeeCord.getInstance().getPlayer(player);
        if(kicker.toString().equals(new UUID(0L, 0L).toString())) {
            if (BungeeCord.getInstance().getPlayers().contains(p)) {
                p.disconnect(new TextComponent(
                        "§cDu wurdest von §6SERVER§c vom Server gekickt! \n" +
                                "§cGrund: §6" + reason));
                Data.console.info("§7[§2KICK§7] §aDu hast §6" + p.getName() + "§a gekickt!");
                return;
            }
            Data.console.info("§7[§4KICK§7] §cDer Spieler §6" + p.getName() + " §cist nicht online!");
            return;
        }
        ProxiedPlayer p1 = BungeeCord.getInstance().getPlayer(kicker);
        if (BungeeCord.getInstance().getPlayers().contains(p)) {
            p.disconnect(new TextComponent(
                    "§cDu wurdest von §6" + p1.getName() + " §c vom Server gekickt! \n" +
                            "§cGrund: §6" + reason));
            p1.sendMessage(new TextComponent("§7[§2KICK§7] §aDu hast §6" + p.getName() + "§a gekickt!"));
            return;
        }
        p1.sendMessage(new TextComponent("§7[§4KICK§7] §cDer Spieler §6" + p.getName() + " §cist nicht online!"));

    }

    public static void tempBanPlayer(UUID player, UUID banner, String reason, String time) {
        ProxiedPlayer p = BungeeCord.getInstance().getPlayer(player);
        if(UUIDLibrary.isRegistred(player)) {
            if (Objects.equals(banner.toString(), new UUID(0L, 0L).toString())) {
                if (getBanType(player) == BanType.NOT_BANNED) {
                    MySQL.update("INSERT INTO `banmanager_bans` (`banned_uuid`, `banner_uuid`, `reason`, `ban_expires`, `permanent`) VALUES ('" + player.toString() + "', '" + banner.toString() + "', '" + reason + "', '" + new Timestamp(new Date().getTime() + parse(time)) + "', '0');");
                    if (BungeeCord.getInstance().getPlayers().contains(p)) {
                        p.disconnect(new TextComponent(
                                "§cDu wurdest vom §6SERVER§c für §6" + time + " §cgebannt! \n" +
                                        "§cGrund: §6" + reason));
                    }
                    Data.console.info("§8[§2BAN§8] §aDu hast den Spieler §6" + p.getName() + "§a für §6" + time + "§a gebannt!");
                } else {
                    Data.console.warning("§8[§4BAN§8] §cDer Spieler §6" + p.getName() + " §cist bereits gebannt!");
                }
                return;
            }
            Data.console.warning("§8[§4BAN§8] §cDer Spieler §6" + p.getName() + "§c existiert nicht!");
        }
        ProxiedPlayer p1 = BungeeCord.getInstance().getPlayer(banner);
        if(UUIDLibrary.isRegistred(player)) {
            if (getBanType(player) == BanType.NOT_BANNED) {
                MySQL.update("INSERT INTO `banmanager_bans` (`banned_uuid`, `banner_uuid`, `reason`, `ban_expires`, `permanent`) VALUES ('" + player.toString() + "', '" + banner.toString() + "', '" + reason + "', '" + new Timestamp(new Date().getTime() + parse(time)) + "', '0');");
                if (BungeeCord.getInstance().getPlayers().contains(p)) {
                    p.disconnect(new TextComponent(
                            "§cDu wurdest von §6" + p1.getName() + "§c für §6" + time + " §cgebannt! \n" +
                            "§cGrund: §6" + reason));
                }
                p1.sendMessage(new TextComponent("§8[§2BAN§8] §aDu hast den Spieler §6" + p.getName() + "§a für §6" + time + "§a gebannt!"));
            } else {
                p1.sendMessage(new TextComponent("§8[§4BAN§8] §cDer Spieler §6" + p.getName() + " §cist bereits gebannt!"));
                return;
            }
            p1.sendMessage(new TextComponent("§8[§4BAN§8] §cDer Spieler §6" + p.getName() + " §cexistiert nicht!"));
        }
    }

    public static void banPlayer(UUID player, UUID banner, String reason) {
        ProxiedPlayer p = BungeeCord.getInstance().getPlayer(player);
        if(banner.toString().equals(new UUID(0L, 0L).toString())) {
            if(UUIDLibrary.isRegistred(player)) {
                if(getBanType(player) == BanType.NOT_BANNED) {
                    MySQL.update("INSERT INTO `banmanager_bans` (`banned_uuid`, `banner_uuid`, `reason`, `ban_expires`, `permanent`) VALUES ('" + player.toString() + "', '" + banner.toString() + "', '" + reason + "', '" + new Timestamp(0) + "', '1');");
                    if(BungeeCord.getInstance().getPlayers().contains(p)) {
                        p.disconnect(new TextComponent(
                                "§cDu wurdest von §6SERVER §cpermanent gebannt! \n" +
                                "§cGrund: §6" + reason));
                    }
                    Data.console.info("§8[§2BAN§8] §aDu hast den Spieler §6" + p.getName() + "§a gebannt!");
                    return;
                }
                Data.console.warning("§8[§4BAN§8] §cDer Spieler §6" + p.getName() + "§c ist bereits gebannt!");
                return;
            }
            Data.console.warning("§8[§4BAN§8] §cDer Spieler §6" + p.getName() + "§c existiert nicht!");
        } else {
            ProxiedPlayer p1 = BungeeCord.getInstance().getPlayer(banner);
            if(UUIDLibrary.isRegistred(player)) {
                if(getBanType(player) == BanType.NOT_BANNED) {
                    MySQL.update("INSERT INTO `banmanager_bans` (`banned_uuid`, `banner_uuid`, `reason`, `ban_expires`, `permanent`) VALUES ('" + player.toString() + "', '" + banner.toString() + "', '" + reason + "', '" + new Timestamp(0) + "', '1');");
                    if(BungeeCord.getInstance().getPlayers().contains(p)) {
                        p.disconnect(new TextComponent(
                                "§cDu wurdest von §6" + p1.getName() + " §cpermanent gebannt! \n" +
                                "§cGrund: §6" + reason));
                    }
                    p1.sendMessage(new TextComponent("§8[§2BAN§8] §aDu hast den Spieler §6" + p.getName() + " §agebannt!"));
                    return;
                }
                p1.sendMessage(new TextComponent("§8[§4BAN§8] §cDer Spieler §6" + p.getName() + "§c ist bereits gebannt!"));
                return;
            }
            p1.sendMessage(new TextComponent("§8[§4BAN§8] §cDer Spieler §6" + p.getName() + "§c existiert nicht!"));
        }
    }

    public static BanType getBanType(UUID player) {
        ResultSet rs = MySQL.query("SELECT * FROM `banmanager_bans` WHERE `banned_uuid`='" + player.toString() + "';");
        try {
            if(rs != null && rs.next()) {
                if(rs.getBoolean("permanent")) {
                    return BanType.PERMA_BANNED;
                } else if(rs.getTimestamp("ban_expires").getTime() > new Date().getTime()) {
                    return BanType.TEMP_BANNED;
                }
            }
            return BanType.NOT_BANNED;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Integer getWarns(UUID player) {
        ResultSet rs = MySQL.query("SELECT COUNT(*) FROM `banmanager_warns` WHERE `warned_uuid`='" + player.toString() + "';");
        try {
            return rs != null ? rs.getInt("COUNT(*)") : 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public enum BanType {
        NOT_BANNED,
        PERMA_BANNED,
        TEMP_BANNED,
    }

    private static long parse(String input) {
        long result = 0;
        String number = "";
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (Character.isDigit(c)) {
                number += c;
            } else if (Character.isLetter(c) && !number.isEmpty()) {
                result += convert(Integer.parseInt(number), c);
                number = "";
            }
        }
        return result;
    }

    private static long convert(int value, char unit) {
        switch(unit) {
            case 'd' : return value * 1000*60*60*24;
            case 'h' : return value * 1000*60*60;
            case 'm' : return value * 1000*60;
            case 's' : return value * 1000;
        }
        return 0;
    }

}