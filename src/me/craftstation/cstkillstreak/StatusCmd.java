package me.craftstation.cstkillstreak;

import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class StatusCmd implements CommandExecutor {
    private static final T_Config dados = new T_Config(CSTKillStreak.getPlugin(CSTKillStreak.class), "dados.yml");
    private static final T_Config config = new T_Config(CSTKillStreak.getPlugin(CSTKillStreak.class), "config.yml");
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String lb, String[] args) {
        dados.reloadConfig();
        config.reloadConfig();
        
        Player p = (Player) sender;  
        
        if(args.length == 0) {
            p.sendMessage("§eUse §c/status <player> §epara ver os status de alguém");
            return true;
        }
        int kills = dados.getConfig().getInt("players." + args[0] + ".kills");
        int deaths = dados.getConfig().getInt("players." + args[0] + ".deaths");
        float kdr = kdr(kills, deaths);
        List<String> status = config.getConfig().getStringList("status.message");
        for (int i = 0; i < status.size(); ++i) {
            status.set(i, status.get(i).replaceAll("%username%", args[0]));
            status.set(i, status.get(i).replaceAll("%kills%", String.valueOf(kills)));
            status.set(i, status.get(i).replaceAll("%deaths%", String.valueOf(deaths)));
            status.set(i, status.get(i).replaceAll("%kdr%", String.valueOf(kdr)));
            status.set(i, status.get(i).replaceAll("&", "§"));
        }
        
        for(int i = 0 ; i < status.size() ; ++i ) {
            p.sendMessage(status.get(i));
        }
        
        
    return false;
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