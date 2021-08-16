package com.Joshua;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class LinkCommand implements CommandExecutor {

    public void sendWrongUsage(CommandSender s){// Wrong command usage
        s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                LinkedDiscordPlugin.getInstance().getConfig().getString("WrongLinkCMD")));
    }

    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        if(args.length <= 0 || args.length > 1
                || !StringUtils.isNumeric(args[0])) {// Incorrect argument length or not numeric argument
            sendWrongUsage(sender);
            return true;
        }
        if(AccountManager.getInstance().getDiscordIDFromRandomCode(UUID.fromString(args[0]))
                == Long.parseLong("-1")) {// Cant find discord id from code
            sendWrongUsage(sender);
            return true;
        }

        AccountManager.getInstance().addAccount(AccountManager.getInstance()// Add account
                .getDiscordIDFromRandomCode(UUID.fromString(args[0])), ((Player) sender).getUniqueId());

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                LinkedDiscordPlugin.getInstance().getConfig().getString("LinkedDiscordCommand")).
                replace("%ID%", String.valueOf(AccountManager.getInstance().
                        getDiscordIDFromRandomCode(UUID.fromString(args[0])))));// Say account was linked

        AccountManager.getInstance().getActiveCodes()// Remove code thats been used
                .remove(AccountManager.getInstance().getDiscordIDFromRandomCode(UUID.fromString(args[0])));
        return true;
    }
}