package at.instamsg.persistent;

import at.instamsg.main.Main;
import com.google.gson.Gson;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

public class PlayerDataManager {

    private static PlayerDataManager MANAGER;
    static final Gson GSON = new Gson();

    private final HashMap<String, PlayerData> playerData = new HashMap<>();

    private PlayerDataManager() {
        // Add TurtleDeadException
    }


    public static PlayerDataManager getPlayerDataManager() {
        if(MANAGER == null) {
            MANAGER = new PlayerDataManager();
        }
        return MANAGER;
    }


    public PlayerData getPlayerData(ProxiedPlayer player) {
        if(player == null) return null;
        PlayerData data = playerData.get(player.getUniqueId().toString());
        if(data == null) {
            data = loadPlayerDataFromFile(player);
            playerData.put(player.getUniqueId().toString(), data);
        }
        return data;
    }


    public void unloadPlayerData(ProxiedPlayer player) {
        String uuid = player.getUniqueId().toString();
        playerData.remove(uuid);
    }


    private PlayerData loadPlayerDataFromFile(ProxiedPlayer player) {
        String uuid = player.getUniqueId().toString();
        File file = new File(ProxyServer.getInstance().getPluginsFolder().getPath() + File.separator
                + "InstaMessage" + File.separator + "players" + File.separator + uuid + ".json");
        file.getParentFile().mkdirs();

        PlayerData data = null;
        try {
            if (file.exists()) {
                String json = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
                data = GSON.fromJson(json, PlayerData.class);
                data.setUuid(uuid);
                return data;
            }
        }catch (IOException | NullPointerException e) {
            e.printStackTrace();
            ProxyServer.getInstance().getLogger().log(Level.SEVERE, "Something strange happened. " +
                    "Unable to read players data file. Using default. " +
                    "Will be overwritten with default when the player changes his msg preferences.");
        }

        return new PlayerData(player.getUniqueId().toString());
    }
}
