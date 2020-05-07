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
import java.util.UUID;

public class CmdR extends Command implements TabExecutor {

    private PlayerDataManager manager;

    public CmdR() {
        super("r", "instacube.msg");
        this.manager = PlayerDataManager.getPlayerDataManager();
    }


    public void execute(CommandSender sender, String[] args)
    {

        ProxiedPlayer player = null;
        if(sender instanceof ProxiedPlayer) {
            player = (ProxiedPlayer)sender;
        } else {
            sender.sendMessage(new TextComponent("§7[§e!§7] §cNur ingame möglich."));
            return;
        }

        if(args.length < 1) {
            sender.sendMessage(new TextComponent("§7[§e!§7] §7Bitte beachte die Syntax: §c/r <Nachricht>"));
            return;
        }

        PlayerData playerData = manager.getPlayerData(player);
        UUID last = playerData.getLastChatPartner();

        if(last == null) {
            sender.sendMessage(new TextComponent("§7[§e!§7] §cDu hast noch keinen Chat partner! " +
                    "Beginne den Chat mit /msg <Spieler> <Nachricht>"));
            return;
        }

        ProxiedPlayer target = ProxyServer.getInstance().getPlayer(last);
        if(target == null) {
            sender.sendMessage(new TextComponent("§7[§e!§7] §cDer Spieler ist nicht mehr online."));
            return;
        }

        PlayerData targetData = manager.getPlayerData(target);
        if(!targetData.canReceiveFrom(player)) {
            sender.sendMessage(new TextComponent("§7[§e!§7] §cDer Spieler hat Privatnachrichten " +
                    "abgeschalten oder dich geblockt."));
            return;
        }


        StringBuilder messageBuilder = new StringBuilder();
        int size = args.length;
        for(int i = 1; i < size; i++) {
            messageBuilder.append(args[i] + " ");
        }
        String message = messageBuilder.substring(0, messageBuilder.length() - 1).replace("§", "&");

        if(sender.hasPermission("instacube.chatcolormsg"))
            message = ChatColor.translateAlternateColorCodes('&', message);

        target.sendMessage(new TextComponent(String.format("§e[§6%s §e» §cmir§e]§7 %s",
                player.getDisplayName(), message)));
        sender.sendMessage(new TextComponent(String.format("§e[§cich §e» §6%s§e]§7 %s",
                target.getDisplayName(), message)));

        targetData.setLastChatPartner(player.getUniqueId());
        return;
    }


    public Iterable<String> onTabComplete(CommandSender arg0, String[] arg1)
    {
        return new ArrayList<String>();
    }
}
