package at.instamsg.main;

import at.instamsg.command.*;
import at.instamsg.listener.PlayerQuitEvent;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.logging.Level;

public class Main extends Plugin {

    public static Plugin MAIN;

    @Override
    public void onEnable() {
        Main.MAIN = this;
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new CmdMsg());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new CmdR());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new CmdMsgtoggle());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new CmdMsgblock());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new CmdMsgunblock());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new PlayerQuitEvent());

        ProxyServer.getInstance().getLogger().log(Level.INFO, "§3InstaMessage gestartet.");
    }



}
