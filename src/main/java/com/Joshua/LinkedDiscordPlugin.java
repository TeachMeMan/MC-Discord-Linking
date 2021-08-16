package com.Joshua;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class LinkedDiscordPlugin extends JavaPlugin {

    // Instance main class
    private static LinkedDiscordPlugin instance;
    public static LinkedDiscordPlugin getInstance(){
        return instance;
    }

    // Public discord hook instance
    public DiscordHook discordHook;

    public void onEnable(){
        instance = this;// Instance first to be able to call getInstance later

        getConfig().options().copyDefaults(true);
        this.saveDefaultConfig();

        loadData();
        loadCommands();
        discordHook = new DiscordHook();// Initialises the discord bot
    }

    public void onDisable(){
        saveData();// Save first
        instance = null;// Null last prevents memory leaks
    }

    // Loads commands
    private void loadCommands(){
        this.getCommand("link").setExecutor(new LinkCommand());
        this.getCommand("NitroClaim").setExecutor(new NitroClaimCommand());
    }

    // Save all linked account information and timer information
    private void saveData(){
        for(Long discordID : AccountManager.getInstance().getAccountList().keySet()){// For each discord account
            LinkedAccountDataManager.getInstance().getAccountManager().set("Accounts." + discordID,
                    AccountManager.getInstance().getAccountFromId(discordID).toString());// Save it

            if(LinkedAccountDataManager.getInstance().getAccountManager()
                    .getString("Timer." + discordID) == null){// If timer is null, save as 0 else save as time
                LinkedAccountDataManager.getInstance().getAccountManager().set("Timer." + discordID, 0);
            }
            LinkedAccountDataManager.getInstance().saveAccountsManager();
        }
    }

    // Load all linked account information and timer information
    private void loadData(){
        if(LinkedAccountDataManager.getInstance().getAccountManager().getString("Accounts") != null) {
            for (String discordID : LinkedAccountDataManager.getInstance().getAccountManager().getConfigurationSection("Accounts").getKeys(false)){
                Long userID = Long.valueOf(discordID);
                AccountManager.getInstance().addAccount(userID,
                        UUID.fromString(LinkedAccountDataManager.getInstance().getAccountManager().
                                getString("Accounts." + userID)));
            }
        }
    }

}