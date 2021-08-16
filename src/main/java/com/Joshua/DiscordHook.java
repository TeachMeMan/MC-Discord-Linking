package com.Joshua;

import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import java.awt.*;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;

public class DiscordHook extends ListenerAdapter {

    public JDA discordBot;

    public DiscordHook(){
        try { // Try and load the discord bot
            discordBot = new JDABuilder(AccountType.BOT).createDefault(
                    LinkedDiscordPlugin.getInstance().getConfig().getString("DiscordToken"))
                    .setMemberCachePolicy(MemberCachePolicy.ALL)
                    .enableIntents(GatewayIntent.GUILD_MEMBERS)
                    .setChunkingFilter(ChunkingFilter.ALL)
                    .build();
            discordBot.awaitReady();
        }catch (Exception e){
            LinkedDiscordPlugin.getInstance().getLogger().log(Level.WARNING, "Unable to load discord bot!");
        }

        discordBot.addEventListener(this);
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message message = event.getMessage();// Get message

        if(message.getContentRaw() == null
                || event.getAuthor().isBot() || event.getChannel().getId() == null) return;// Doesnt fit criteria

        String content = message.getContentRaw();// Get Mesage

        if (event.getChannel().getId()
                .equalsIgnoreCase(LinkedDiscordPlugin.getInstance().getConfig().getString("DiscordChannelID"))) {
            if (content.toLowerCase()
                    .equals(LinkedDiscordPlugin.getInstance().getConfig()
                            .getString("DiscordCommand").toLowerCase())) {
                if (AccountManager.getInstance().
                        getAccountFromId(event.getAuthor().getIdLong()) == null) {// Fits criteria then
                    event.getAuthor().openPrivateChannel().complete().sendMessage(// Send message with code to put
                            getMessageWithString("DiscordDM", "%CODE%",// In game
                                    String.valueOf(AccountManager.getInstance().addNewCode(event.getAuthor().getIdLong())))).queue();
                } else {// Else they are already linked
                    event.getAuthor().openPrivateChannel().complete().
                            sendMessage(getMessageWithString("AlreadyLinked")).queue();
                }
                event.getChannel().deleteMessageById(message.getId()).queue();// Delete message
            }
        }

        if (event.getChannel().getId().equalsIgnoreCase(// If its the bot channel
                LinkedDiscordPlugin.getInstance().getConfig().getString("BotCommandsID"))) {
            if (content.split(" ")[0].toLowerCase().equals(LinkedDiscordPlugin.// Lookup command
                    getInstance().getConfig().getString("DiscordCommand-Lookup").toLowerCase())) {
                try {// Try searching up the user to find
                    event.getChannel().sendMessage(getLookupMessage(content.split(" ")[1],
                            AccountManager.getInstance().getAccountFromId(
                                    Long.valueOf(content.split(" ")[1])))).queue();
                }catch (NumberFormatException e){// Else cant find user
                    event.getChannel().sendMessage(getLookupMessage(content.split(" ")[1],
                            null)).queue();
                }
                event.getChannel().deleteMessageById(message.getId()).queue();// Delete
            }
        }

    }

    /*

        Bunch of discord embed messages
        TODO: Clean up later

     */

    public MessageEmbed getLookupMessage(String ID, UUID uuid){
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Lookup for user - " + ID);
        eb.setColor(new Color(LinkedDiscordPlugin.getInstance().getConfig().getInt("Color.R"),
                LinkedDiscordPlugin.getInstance().getConfig().getInt("Color.G"),
                LinkedDiscordPlugin.getInstance().getConfig().getInt("Color.B")));
        eb.setDescription("Lookup results for discord ID: " + ID + "\n UUID Found: `" + uuid + "`");
        eb.setTimestamp(new Date().toInstant());
        eb.setFooter("Made by TeachMe#2924");
        return eb.build();
    }

    public MessageEmbed getMessageWithString(String configMsg){
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(LinkedDiscordPlugin.getInstance().getConfig().getString("Title." + configMsg));
        eb.setColor(new Color(LinkedDiscordPlugin.getInstance().getConfig().getInt("Color.R"),
                LinkedDiscordPlugin.getInstance().getConfig().getInt("Color.G"),
                LinkedDiscordPlugin.getInstance().getConfig().getInt("Color.B")));
        eb.setDescription(LinkedDiscordPlugin.getInstance().getConfig().getString("Message." + configMsg));
        eb.setTimestamp(new Date().toInstant());
        eb.setFooter("Made by TeachMe#2924");
        return eb.build();
    }

    public MessageEmbed getMessageWithString(String configMsg, String replace, String with){
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(LinkedDiscordPlugin.getInstance().getConfig().getString("Title." + configMsg));
        eb.setColor(new Color(LinkedDiscordPlugin.getInstance().getConfig().getInt("Color.R"),
                LinkedDiscordPlugin.getInstance().getConfig().getInt("Color.G"),
                LinkedDiscordPlugin.getInstance().getConfig().getInt("Color.B")));
        eb.setDescription(LinkedDiscordPlugin.getInstance().getConfig().getString("Message." + configMsg).replace(replace, with));
        eb.setTimestamp(new Date().toInstant());
        eb.setFooter("Made by TeachMe#2924");
        return eb.build();
    }

}
