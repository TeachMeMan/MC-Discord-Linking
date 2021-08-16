package com.Joshua;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class LinkedAccountDataManager {

    // Instance class
    private static LinkedAccountDataManager instance = new LinkedAccountDataManager();
    public static LinkedAccountDataManager getInstance() {
        return instance;
    }

    // Generate file on instance creation if doesnt exist
    private LinkedAccountDataManager() {
        this.LinkedAccountManagerFile = new File(LinkedDiscordPlugin.getInstance().getDataFolder(), "Accounts.yml");
        if (!this.LinkedAccountManagerFile.exists()) {
            try {
                this.LinkedAccountManagerFile.getParentFile().mkdirs();
                this.LinkedAccountManagerFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.LinkedAccountManager = YamlConfiguration.loadConfiguration(this.LinkedAccountManagerFile);
    }

    private File LinkedAccountManagerFile;
    private FileConfiguration LinkedAccountManager;

    // Public methods to get from instance to write to class
    public FileConfiguration getAccountManager() {
        return LinkedAccountManager;
    }

    public void saveAccountsManager() {
        try {
            this.LinkedAccountManager.save(this.LinkedAccountManagerFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
