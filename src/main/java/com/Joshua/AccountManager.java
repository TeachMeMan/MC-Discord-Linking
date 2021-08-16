package com.Joshua;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AccountManager {

    // Instance class
    private static AccountManager instance = new AccountManager();
    public static AccountManager getInstance(){
        return instance;
    }

    // DiscordID, Random UUID for the code
    private HashMap<Long, UUID> activeCodes = new HashMap<Long, UUID>();

    // Add a code, simple random uuid
    public UUID addNewCode(Long id) {
        UUID uuid = UUID.randomUUID();
        activeCodes.put(id, uuid);
        return uuid;
    }

    // Returns discord ID from a uuid which would be randomly generated
    // No discord id returns -1
    // Random codes list
    public Long getDiscordIDFromRandomCode(UUID code){
        for (Map.Entry<Long, UUID> entry : activeCodes.entrySet()) {
            if (entry.getValue().equals(code)) {
                return entry.getKey();
            }
        }
        return Long.valueOf(-1);
    }

    // Get all codes
    public HashMap<Long, UUID> getActiveCodes(){
        return activeCodes;
    }

    //  Accounts
    public HashMap<Long, UUID> accounts = new HashMap<Long, UUID>();

    // Simple add account
    public void addAccount(Long id, UUID uuid) {
        accounts.put(id, uuid);
    }
    // Simple get account from id
    public UUID getAccountFromId(Long id) {
        return accounts.get(id);
    }

    // Get discord ID from accounts stored list
    public Long getDiscordIDFromUUIDAccounts(UUID uuid){
        for (Map.Entry<Long, UUID> entry : accounts.entrySet()) {
            if (entry.getValue().equals(uuid)) {
                return entry.getKey();
            }
        }
        return Long.parseLong("-1");
    }

    // Gets list of accounts
    public HashMap<Long, UUID> getAccountList(){
        return accounts;
    }

}
