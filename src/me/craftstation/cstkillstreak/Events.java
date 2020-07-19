package me.craftstation.cstkillstreak;

import br.com.devpaulo.legendchat.api.events.ChatMessageEvent;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;



public class Events implements Listener {
    private static final T_Config dados = new T_Config(CSTKillStreak.getPlugin(CSTKillStreak.class), "dados.yml");
    private static final T_Config config = new T_Config(CSTKillStreak.getPlugin(CSTKillStreak.class), "config.yml");
    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        
        int deaths = dados.getConfig().getInt("players." + p.getName() + ".deaths");
        
        if (e.getEntity().getKiller() instanceof Player && e.getEntity() instanceof Player) {
            Player k = p.getKiller();
            int kills = dados.getConfig().getInt("players." + k.getName() + ".kills");
            dados.reloadConfig();
            dados.set("players." + k.getName() + ".kills", kills + 1); //assassino
            dados.set("players." + p.getName() + ".deaths", deaths + 1); //vitima
            dados.saveConfig();
            dados.reloadConfig();
        }else {
            dados.set("players."+p.getName()+".deaths", deaths+1); //vitima
            dados.saveConfig();
            dados.reloadConfig();
        }
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
    Player p = e.getPlayer();
    if (!dados.getConfig().contains("players." + p.getName())) {
      dados.saveConfig();
      dados.getConfig().set("players." + p.getName() + ".kills", 0);
      dados.getConfig().set("players." + p.getName() + ".deaths", 0);
      dados.getConfig().set("players." + p.getName() + ".killer", false);
      dados.saveConfig();
      dados.reloadConfig();
    }
    }
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        Inventory inventory = e.getInventory();
        int kills = dados.getConfig().getInt("players." + p.getName() + ".kills");
        int deaths = dados.getConfig().getInt("players." + p.getName() + ".deaths");
        float kdr = kdr(kills, deaths);
        
        String namek = config.getConfig().getString("menukillstatus.title");
        String namet = config.getConfig().getString("menutopkills.title");
        
        if(inventory.getName().equals(color(namek))){
            e.setCancelled(true);
        }
        if(inventory.getName().equals(color(namet))){
            e.setCancelled(true);
        }
        
    }
    public void onMessage(ChatMessageEvent e) {
        Player p = e.getSender();
        if(dados.getConfig().getBoolean("players." + p.getName() + ".killer")) {
            if(e.getTags().contains("CST-Killer")) {
                e.setTagValue("CST-Killer","&4&l[Killer]&r");
            }
        }
    }
    public float kdr(int kills, int deaths) {
        if (kills == 0) {
            if (deaths == 0)
                return 0.0F; 
            return -deaths;
        } 
        if (deaths == 0) {
            if (kills == 0)
                return 0.0F; 
            return kills;
        } 
        return (kills / deaths);
    }
    public String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}
