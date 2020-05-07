package at.instamsg.command;

import at.instamsg.persistent.PlayerData;
import at.instamsg.persistent.PlayerDataManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;

public class CmdMsgunblock extends Command implements TabExecutor {

    private PlayerDataManager manager;

    public CmdMsgunblock() {
        super("msgunblock", "instacube.msg", "msgunignore", "msgpardon");
        this.manager = PlayerDataManager.getPlayerDataManager();
    }


    public void execute(CommandSender sender, String[] args) {

        ProxiedPlayer player = null;
        if (sender instanceof ProxiedPlayer) {
            player = (ProxiedPlayer) sender;
        } else {
            sender.sendMessage(new TextComponent("§7[§e!§7] §cNur ingame möglich."));
            return;
        }

        if(args.length < 1) {
            sender.sendMessage(new TextComponent("§7[§e!§7] §cBitte beachte die Syntax: /msgunblock <spieler>"));
            return;
        }

        ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
        if(target == null) {
            sender.sendMessage(new TextComponent("§7[§e!§7] §cDer Spieler ist nicht online."));
            return;
        }

        if(target == player) {
            sender.sendMessage(new TextComponent("§7[§e!§7] §cDu kannst dich selbst nicht entsperren."));
            return;
        }

        PlayerData playerData = manager.getPlayerData(player);
        playerData.removeBlockedPlayer(target);
        sender.sendMessage(new TextComponent("§7[§e!§7] §7Der Spieler ist nun §eentsperrt§7."));

        return;
    }


    public Iterable<String> onTabComplete(CommandSender arg0, String[] arg1)
    {
        ArrayList<String> players = new ArrayList();
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            players.add(player.getName());
        }
        return players;
    }
}