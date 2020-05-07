package at.instamsg.listener;

import at.instamsg.persistent.PlayerDataManager;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayerQuitEvent implements Listener {

    @EventHandler
    public void onQuitEvent(PlayerDisconnectEvent event) {
        PlayerDataManager.getPlayerDataManager().unloadPlayerData(event.getPlayer());
    }
}
