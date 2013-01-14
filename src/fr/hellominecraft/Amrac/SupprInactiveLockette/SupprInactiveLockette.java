package fr.hellominecraft.Amrac.SupprInactiveLockette;

import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

public class SupprInactiveLockette extends JavaPlugin {
	
	Logger log;
	
    public void onEnable(){ 
    	log = this.getLogger();
    	
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
        
        getServer().getPluginManager().registerEvents(new Listeners(this), this);
		log.info("SupprInactiveLockette had been enabled."+this.getConfig().getInt("DaysLimit"));
    }
     
    public void onDisable(){ 
    	log.info("SupprInactiveLockette has been disabled.");
    }


}
