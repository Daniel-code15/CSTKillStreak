package me.craftstation.cstkillstreak;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class TopKillsCmd implements CommandExecutor {
    private static final T_Config dados = new T_Config(CSTKillStreak.getPlugin(CSTKillStreak.class), "dados.yml");
    private static final T_Config config = new T_Config(CSTKillStreak.getPlugin(CSTKillStreak.class), "config.yml");
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String lb, String[] args) {
        
        Player p = (Player)sender;
        dados.reloadConfig();
        config.reloadConfig();
        
        String name = config.getConfig().getString("menutopkills.title");
        int size = config.getConfig().getInt("menutopkills.size");
            
            ConfigurationSection section = dados.getConfig().getConfigurationSection("players");
            List<Float> valores = new ArrayList();
            
            List<String> lore = new ArrayList();
            String currentkiller = "";
           
            
            for(String key : section.getKeys(false)) {
                int kills = section.getInt(key + ".kills");
                int deaths = section.getInt(key + ".deaths");
                
                float kdr = kdr(kills, deaths);
                
                
                valores.add(kdr);
            }
            Collections.sort(valores);

            Inventory inv = Bukkit.createInventory(null, 27, color(name));
            
            for(String nicks : section.getKeys(false)) {
                int kills = dados.getConfig().getInt("players."+nicks+".kills");
                int deaths = dados.getConfig().getInt("players."+nicks+".deaths");
                float kdr = kdr(kills, deaths);
                
                for(int i = 1 ; i < 6 ; ++i) {
                    int ii = i+1;
                    if(kdr == valores.get(valores.size() - i)) {
                        
                       //p.sendMessage("§"+i+"#"+i+" - §c"+nicks+" §5KDR: "+kdr);
                       lore.clear();
                       lore.add(" ");
                       lore.add("§e§lTop - "+i+": §"+ii+""+nicks);
                       lore.add(" ");
                       lore.add("§c§lKills: §6"+kills);
                       lore.add("§e§lMortes: §4"+deaths);
                       lore.add("§5§lKDR: §3"+kdr);
                       lore.add(" ");
                       
                       
                       ItemStack si = nameItemi(new ItemStack(Material.SKULL_ITEM),color("§"+i+"§l#"+i+" - §c§l"+nicks), lore, nicks);
                       inv.setItem(i+10, si);
                       if(dados.getConfig().getBoolean("players."+nicks+".killer")) {
                           currentkiller = nicks;
                       }
                           
                       
                       if(nicks == p.getName()) {
                           if(kdr >= valores.get(valores.size() - 5)) {
                                for(Player allp : Bukkit.getOnlinePlayers()) {
                                    allp.sendMessage("§e§l"+p.getName()+"§c§l Acaba de Se Tornar o novo: §4§lKiller");
                                }
                                dados.getConfig().set("players." + currentkiller + ".killer", false);
                                dados.getConfig().set("players." + p.getName() + ".killer", true);
                                inv.setItem(5, si);
                                dados.saveConfig();
                                dados.reloadConfig();
                           }
                       }
                    }
                }
                
            }
            
            ItemStack panel_1 = nameItem(new ItemStack(Material.STAINED_GLASS_PANE), " ");
            inv.setItem(0, panel_1);
            inv.setItem(8, panel_1);
            inv.setItem(26, panel_1);
            inv.setItem(18, panel_1);
            inv.setItem(10, panel_1);
            inv.setItem(16, panel_1);
            inv.setItem(2, panel_1);
            inv.setItem(4, panel_1);
            inv.setItem(6, panel_1);
            inv.setItem(24, panel_1);
            inv.setItem(20, panel_1);
            inv.setItem(22, panel_1);
            p.playSound(p.getLocation(), Sound.ENTITY_FIREWORK_LAUNCH, 100, 100);
            p.openInventory(inv);
            p.sendMessage("§cAbrindo TopKills");
            
            
            if(p.getInventory().contains(new ItemStack(Material.DIAMOND), ItemStack(Material.DIAMOND).getAmount())) {
            
            }
        
        return false;
    }
    
    
    private ItemStack nameItem(ItemStack item, String name) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.addEnchant(Enchantment.DURABILITY, 0, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        return item;
    }
    private ItemStack nameItemi(ItemStack item, String name, List lore, String owner) {
        SkullMeta sm = (SkullMeta)item.getItemMeta();
        sm.setOwner(owner);
        sm.setLore(lore);
        sm.setDisplayName(name);
        
        item.setDurability((short)3);
        item.setItemMeta(sm);
        
        return item;
    }
    public String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
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
}