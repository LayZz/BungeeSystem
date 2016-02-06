package de.flashbeatzz.bungeesystem.banmanager;

import de.flashbeatzz.bungeesystem.MySQL;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

public class PostLoginListener implements Listener {

    @EventHandler
    public void onPostLogin(PostLoginEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();
        if(BanManager.getBanType(uuid) == BanManager.BanType.PERMA_BANNED) {
            String banner = "";
            String reason = "";
            ResultSet rs = MySQL.query("SELECT * FROM `banmanager_bans` WHERE `banned_uuid`='" + uuid.toString() + "';");
            try {
                if(rs != null && rs.next()) {
                    banner = BungeeCord.getInstance().getPlayer(UUID.fromString(rs.getString("banner_uuid"))).getName();
                    reason = rs.getString("reason");
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.getPlayer().disconnect(new TextComponent(
                    "§cDu wurdest von §6" + banner + " §cpermanent gebannt! \n" +
                    "§cGrund: §6" + reason));
        } else if(BanManager.getBanType(uuid) == BanManager.BanType.TEMP_BANNED) {
            String banner = "";
            String reason = "";
            long timeLeft = 0;
            ResultSet rs = MySQL.query("SELECT * FROM `banmanager_bans` WHERE `banned_uuid`='" + uuid.toString() + "';");
            try {
                if(rs != null && rs.next()) {
                    banner = BungeeCord.getInstance().getPlayer(UUID.fromString(rs.getString("banner_uuid"))).getName();
                    reason = rs.getString("reason");
                    timeLeft = rs.getTimestamp("ban_expires").getTime() - new Date().getTime();
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }

            int days = (int) (timeLeft / (3600 * 24));
            timeLeft -= days * 3600 * 24;
            int hours = (int) (timeLeft / 3600);
            timeLeft -= hours * 3600;
            int min = (int) (timeLeft / 60);
            timeLeft -= min * 60;
            int sec = (int) (timeLeft);

            String time = days + " Tag(e) " + hours + " Stunde(n) " + min + " Minute(n) und " + sec + " Sekunde(n)";

            e.getPlayer().disconnect(new TextComponent(
                    "§cDu wurdest von §6" + BungeeCord.getInstance().getPlayer(banner).getName() + "§c für §6" + time + " §cgebannt! \n" +
                    "§cGrund: §6" + reason)
            );
        }
    }

}
