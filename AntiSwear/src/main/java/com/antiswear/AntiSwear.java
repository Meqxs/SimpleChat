package com.antiswear;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AntiSwear extends JavaPlugin implements Listener {
    private Set<String> swearWords;
    private File logFile;
    private FileConfiguration logConfig;
    private Logger logger;

    @Override
    public void onEnable() {
        try {
            // Initialize logger
            logger = getLogger();
            
            // Register event listener
            getServer().getPluginManager().registerEvents(this, this);
            
            // Create config if it doesn't exist
            saveDefaultConfig();
            
            // Load swear words from config
            loadSwearWords();
            
            // Initialize log file
            initializeLogFile();
            
            logger.info("AntiSwear has been enabled!");
        } catch (Exception e) {
            logger.severe("Failed to enable AntiSwear: " + e.getMessage());
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        logger.info("AntiSwear has been disabled!");
    }

    private void loadSwearWords() {
        swearWords = new HashSet<>();
        FileConfiguration config = getConfig();
        
        // Add default swear words if none exist
        if (!config.contains("swear-words")) {
            config.set("swear-words", new String[]{
                "fuck", "shit", "bitch", "ass", "damn", "crap", "piss", "dick", "cock", "pussy",
                "fucker", "motherfucker", "bullshit", "bastard", "cunt", "whore", "slut"
            });
            saveConfig();
        }
        
        // Load swear words from config
        swearWords.addAll(config.getStringList("swear-words"));
    }

    private void initializeLogFile() {
        logFile = new File(getDataFolder(), "swear_logs.yml");
        if (!logFile.exists()) {
            try {
                logFile.getParentFile().mkdirs();
                logFile.createNewFile();
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Could not create log file!", e);
            }
        }
        logConfig = YamlConfiguration.loadConfiguration(logFile);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled()) return;
        
        String message = event.getMessage().toLowerCase();
        
        // Check for swear words
        for (String swear : swearWords) {
            if (message.contains(swear)) {
                // Cancel the message
                event.setCancelled(true);
                
                // Log the violation
                logViolation(event.getPlayer().getName(), message);
                
                // Notify the player with the specific word
                event.getPlayer().sendMessage("§cYou cannot say the word '" + swear + "' in chat!");
                return;
            }
        }
    }

    private void logViolation(String playerName, String message) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        logConfig.set(timestamp + ".player", playerName);
        logConfig.set(timestamp + ".message", message);
        
        try {
            logConfig.save(logFile);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Could not save to log file!", e);
        }
    }
} 