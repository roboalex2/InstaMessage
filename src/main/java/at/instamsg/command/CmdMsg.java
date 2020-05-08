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
import java.util.Arrays;

public class CmdMsg extends Command implements TabExecutor {

    private PlayerDataManager manager;

    public CmdMsg() {
        super("msg", "instacube.msg", "tell", "w");
        this.manager = PlayerDataManager.getPlayerDataManager();
    }


    public void execute(CommandSender sender, String[] args)
    {

        String senderName = "§cConsole";
        ProxiedPlayer player = null;
        if(sender instanceof ProxiedPlayer) {
            player = (ProxiedPlayer)sender;
            senderName = player.getDisplayName();
        }

        if(args.length < 2) {
            sender.sendMessage(new TextComponent(TextComponent.fromLegacyText(
                    "§7[§e!§7] §7Bitte beachte die Syntax: §c/msg <Spieler> <Nachricht>")));
            return;
        }

        ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
        if(target == null) {
            sender.sendMessage(new TextComponent(TextComponent.fromLegacyText(
                    "§7[§e!§7] §cDer Spieler ist nicht online.")));
            return;
        }

        if(target == player) {
            sender.sendMessage(new TextComponent(TextComponent.fromLegacyText(
                    "§7[§e!§7] §cDu kannst dir selbst keine Nachricht schicken.")));
            return;
        }

        PlayerData targetData = manager.getPlayerData(target);
        if(!targetData.canReceiveFrom(player)) {
            sender.sendMessage(new TextComponent(TextComponent.fromLegacyText(
                    "§7[§e!§7] §cDer Spieler hat Privatnachrichten abgeschalten oder dich geblockt.")));
            return;
        }


        StringBuilder messageBuilder = new StringBuilder();
        int size = args.length;
        for(int i = 1; i < size; i++) {
            messageBuilder.append(args[i]).append(" ");
        }
        String message = messageBuilder.substring(0, messageBuilder.length() - 1).replace("§", "&");

        if(sender.hasPermission("instacube.chatcolormsg"))
            message = ChatColor.translateAlternateColorCodes('&', message);

        target.sendMessage(new TextComponent(TextComponent.fromLegacyText(
                String.format("§e[§6%s §e» §cmir§e]§7 %s", senderName, message))));
        sender.sendMessage(new TextComponent(TextComponent.fromLegacyText(
                String.format("§e[§cich §e» §6%s§e]§7 %s", target.getDisplayName(), message))));

        if(player != null){
            targetData.setLastChatPartner(player.getUniqueId());

            PlayerData playerData = manager.getPlayerData(player);
            playerData.setLastChatPartner(target.getUniqueId());
        }
    }


    public Iterable<String> onTabComplete(CommandSender arg0, String[] arg1)
    {
        if(arg1.length < 2) return new ArrayList<>();
        ArrayList<String> players = new ArrayList<>();
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            players.add(player.getName());
        }
        return players;
    }
}
