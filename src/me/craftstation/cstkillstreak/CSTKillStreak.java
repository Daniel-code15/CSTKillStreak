package me.craftstation.cstkillstreak;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class CSTKillStreak extends JavaPlugin {
    public static CSTKillStreak plugin;
    @Override
    public void onEnable() {
        plugin = this;
        loadcmds();
        Bukkit.getConsoleSender().sendMessage("§cCarregando Configuração...");
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        Bukkit.getConsoleSender().sendMessage("§cRegistrando Eventos...");
        Bukkit.getPluginManager().registerEvents(new Events(), this);
        Bukkit.getConsoleSender().sendMessage("§cSucesso! Registros Ativados!");
    }
    @Override
    public void onDisable() {
        
    }

    private void loadcmds() {
        getCommand("kills").setExecutor(new KillsCmd());
        getCommand("topkills").setExecutor(new TopKillsCmd());
        getCommand("status").setExecutor(new StatusCmd());
    }
}