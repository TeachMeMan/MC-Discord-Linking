package com.Joshua;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class NitroClaimCommand implements CommandExecutor {

    public void sendWrongUsage(CommandSender s){// Send wrong command usage
        s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                LinkedDiscordPlugin.getInstance().getConfig().getString("WrongNitroCMD")));
    }

    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        if(!(sender instanceof Player)) {// Not a player
            sendWrongUsage(sender);
            return true;
        }
        if(AccountManager.getInstance().getDiscordIDFromUUIDAccounts(((Player) sender).getUniqueId())
                == Long.parseLong("-1")) {// If the UUID doesnt exist in accounts
            sendWrongUsage(sender);
            return true;
        }
        if(!hasNitroRole(AccountManager.getInstance().getDiscordIDFromUUIDAccounts(
                ((Player) sender).getUniqueId()))) {// User doesnt have nitro role
            sendWrongUsage(sender);
            return true;
        }
        if((LinkedAccountDataManager.getInstance().getAccountManager().getLong("Timer." +
                AccountManager.getInstance().getDiscordIDFromUUIDAccounts(((Player) sender).getUniqueId())))
                + 604800000 < System.currentTimeMillis()) {// If the timer has ran out
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                    LinkedDiscordPlugin.getInstance().getConfig().
                            getString("NitroCommand").replace("%USER%", sender.getName()));
            LinkedAccountDataManager.getInstance().getAccountManager().set("Timer." +
                    AccountManager.getInstance().getDiscordIDFromUUIDAccounts// Update timer in config
                            (((Player) sender).getUniqueId()), System.currentTimeMillis());
        }else{
            long millis = ((LinkedAccountDataManager.getInstance().getAccountManager().getLong("Timer." +
                    AccountManager.getInstance().getDiscordIDFromUUIDAccounts(((Player) sender).getUniqueId()))
                    + 604800000)-System.currentTimeMillis());// Get millisecond duration from last time command ran
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',// Send the message with time left
                    LinkedDiscordPlugin.getInstance().getConfig().getString("WaitForCommand")).
                    replace("%TIME%", getDurationBreakdown(millis)));
        }
        return true;
    }


    // Returns true if user has role name from config
    private boolean hasNitroRole(Long userId){
        for(Member m : LinkedDiscordPlugin.getInstance().
                discordHook.discordBot.
                getGuildById(LinkedDiscordPlugin.getInstance().getConfig().getString("DiscordGuildID")).getMembers()){
            if (m.getIdLong() == userId) {
                for(Role r : m.getRoles()){
                    if(r.getName().equalsIgnoreCase(LinkedDiscordPlugin.getInstance().getConfig().getString("NitroRole"))){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // Changes milliseconds to D H M S
    private String getDurationBreakdown(long millis) {
        if(millis < 0) {
            throw new IllegalArgumentException("Duration must be greater than zero!");
        }

        // Gets each value required for days, hours, minutes, seconds
        long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        // Returns the value as a string
        return new StringBuilder()
                        .append(days).append("D ")
                        .append(hours).append("H ")
                        .append(minutes).append("M ")
                        .append(seconds).append("S ")
                        .toString();
    }

}
