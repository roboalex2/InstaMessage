package at.instamsg.command;

import at.instamsg.persistent.PlayerData;
import at.instamsg.persistent.PlayerDataManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;

public class CmdMsgtoggle extends Command implements TabExecutor {

    private PlayerDataManager manager;

    public CmdMsgtoggle() {
        super("msgtoggle", "instacube.msg", "msgswitch");
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

        PlayerData playerData = manager.getPlayerData(player);
        if(playerData.isMsgReceiveEnabled()) {
            playerData.setCanReceiveMessage(false);
            sender.sendMessage(new TextComponent("§7[§e!§7] §7Du erhältst nun §ekeine Privatnachrichten §7mehr."));
        } else {
            playerData.setCanReceiveMessage(true);
            sender.sendMessage(new TextComponent("§7[§e!§7] §7Du erhältst nun §ewieder Privatnachrichten§7."));
        }

        return;
    }


    public Iterable<String> onTabComplete(CommandSender arg0, String[] arg1) {
        return new ArrayList<String>();
    }
}