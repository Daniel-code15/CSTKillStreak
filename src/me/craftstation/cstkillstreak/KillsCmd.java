package me.craftstation.cstkillstreak;

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class KillsCmd implements CommandExecutor {
    private static final T_Config dados = new T_Config(CSTKillStreak.getPlugin(CSTKillStreak.class), "dados.yml");
    private static final T_Config config = new T_Config(CSTKillStreak.getPlugin(CSTKillStreak.class), "config.yml");
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String lb, String[] args) {
        Player p = (Player)sender;
        
        dados.reloadConfig();
        config.reloadConfig();
        
        int kills = dados.getConfig().getInt("players." + p.getName() + ".kills");
        int deaths = dados.getConfig().getInt("players." + p.getName() + ".deaths");
        
        float kdr = kdr(kills, deaths);
        
        // Menu
        int size = config.getConfig().getInt("menukillstatus.size");
        String name = config.getConfig().getString("menukillstatus.title");
        
        // -=-=-=-==-=-=-
        
        
        //  Status Item
        int sslot = config.getConfig().getInt("menukillstatus.Items.status.slot");
        String sitem = config.getConfig().getString("menukillstatus.Items.status.item");
        String sname = config.getConfig().getString("menukillstatus.Items.status.name").replaceAll("%displayname%", p.getDisplayName()).replaceAll("%username%", p.getName()).replaceAll("%kills%", String.valueOf(kills)).replaceAll("%deaths%", String.valueOf(deaths)).replaceAll("%kdr%", String.valueOf(kdr));
        List<String> slore = config.getConfig().getStringList("menukillstatus.Items.status.lore");
        
        for (int i = 0; i < slore.size(); ++i) {
            slore.set(i, slore.get(i).replaceAll("%username%", p.getName()));
            slore.set(i, slore.get(i).replaceAll("%displayname%", p.getDisplayName()));
            slore.set(i, slore.get(i).replaceAll("%kills%", String.valueOf(kills)));
            slore.set(i, slore.get(i).replaceAll("%deaths%", String.valueOf(deaths)));
            slore.set(i, slore.get(i).replaceAll("%kdr%", String.valueOf(kdr)));
            slore.set(i, slore.get(i).replaceAll("&", "§"));
        }
        // -=-=-=-=-=-=-=-
        
        
        
        // Info Item
        int islot = config.getConfig().getInt("menukillstatus.Items.info.slot");
        String iitem = config.getConfig().getString("menukillstatus.Items.info.item");
        String iname = config.getConfig().getString("menukillstatus.Items.info.name").replaceAll("%displayname%", p.getDisplayName()).replaceAll("%username%", p.getName()).replaceAll("%kills%", String.valueOf(kills)).replaceAll("%deaths%", String.valueOf(deaths)).replaceAll("%kdr%", String.valueOf(kdr));
        List<String> ilore = config.getConfig().getStringList("menukillstatus.Items.info.lore");
        
        for (int i = 0; i < slore.size(); ++i) {
            ilore.set(i, ilore.get(i).replaceAll("%username%", p.getName()));
            ilore.set(i, ilore.get(i).replaceAll("%displayname%", p.getDisplayName()));
            ilore.set(i, ilore.get(i).replaceAll("%kills%", String.valueOf(kills)));
            ilore.set(i, ilore.get(i).replaceAll("%deaths%", String.valueOf(deaths)));
            ilore.set(i, ilore.get(i).replaceAll("%kdr%", String.valueOf(kdr)));
            ilore.set(i, ilore.get(i).replaceAll("&", "§"));
        }
        // -=-=-=-=-=-=-=-
        
        
        if(sitem.equals("%head%")) {
            Inventory i = Bukkit.createInventory(null, size, color(name));
            ItemStack si = nameItemi(new ItemStack(Material.SKULL_ITEM),color(sname), slore, p);
            ItemStack ii = nameItem(new ItemStack(Material.getMaterial(iitem)),color(iname), ilore);
        
            i.setItem(sslot, si);
            i.setItem(islot, ii);
        
            p.openInventory(i);      
        }else {
            Inventory i = Bukkit.createInventory(null, size, color(name));
            ItemStack si = nameItem(new ItemStack(Material.getMaterial(sitem)),color(sname), slore);
            ItemStack ii = nameItem(new ItemStack(Material.getMaterial(iitem)),color(iname), ilore);
        
            
            i.setItem(sslot, si);
            i.setItem(islot, ii);
        
            p.openInventory(i);   
        }
        return false;
    }
    private ItemStack nameItem(ItemStack item, String name, List lore) {
        ItemMeta meta = item.getItemMeta();
        meta.setLore(lore);
        meta.setDisplayName(name);
        meta.addEnchant(Enchantment.DURABILITY, 0, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        return item;
    }
    private ItemStack nameItemi(ItemStack item, String name, List lore, Player p) {
        SkullMeta sm = (SkullMeta)item.getItemMeta();
        sm.setOwner(p.getName());
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