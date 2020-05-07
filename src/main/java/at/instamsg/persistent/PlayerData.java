package at.instamsg.persistent;

import at.instamsg.main.Main;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.UUID;
import java.util.logging.Level;

public class PlayerData {

    private String uuid;
    private boolean msgReceiveEnabled = true;
    private HashSet<String> blockedPlayers = new HashSet<>();
    private transient UUID lastChatPartner;

    PlayerData(String uuid) {
        this.uuid = uuid;
    }


    public boolean isMsgReceiveEnabled() {
        return msgReceiveEnabled;
    }


    public void setCanReceiveMessage(boolean msgReceiveEnabled) {
        this.msgReceiveEnabled = msgReceiveEnabled;
        save();
    }


    private HashSet<String> getBlockedPlayers() {
        return blockedPlayers;
    }


    public UUID getLastChatPartner() {
        return lastChatPartner;
    }


    public void setLastChatPartner(UUID lastChatPartner) {
        this.lastChatPartner = lastChatPartner;
    }


    void setUuid(String uuid) {
        this.uuid = uuid;
    }


    public boolean canReceiveFrom(ProxiedPlayer sender) {
        if(sender == null) return true;

        if(!isMsgReceiveEnabled()) return false;
        if(getBlockedPlayers().contains(sender.getUniqueId().toString())) return false;

        return true;
    }


    public void addBlockedPlayer(ProxiedPlayer player) {
        if(player == null) return;
        String uuidP = player.getUniqueId().toString();
        if(getBlockedPlayers().contains(uuidP)) return;
        getBlockedPlayers().add(uuidP);
        save();
    }


    public void removeBlockedPlayer(ProxiedPlayer player) {
        if(player == null) return;
        String uuidP = player.getUniqueId().toString();
        if(!getBlockedPlayers().contains(uuidP)) return;
        getBlockedPlayers().remove(uuidP);
        save();
    }


    private void save() {
        ProxyServer.getInstance().getScheduler().runAsync(Main.MAIN, new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    File file = new File(ProxyServer.getInstance().getPluginsFolder().getPath()
                            + File.separator + "InstaMessage" + File.separator + "players" + File.separator
                            + uuid + ".json");
                    file.getParentFile().mkdirs();

                    try {
                        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                        bw.write(PlayerDataManager.GSON.toJson(this));
                        bw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        ProxyServer.getInstance().getLogger().log(Level.SEVERE, "Unable to write players data file. " +
                                "Are your permissions set right?");
                    }
                }
            }
        });
    }
}
