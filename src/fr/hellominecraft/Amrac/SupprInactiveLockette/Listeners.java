package fr.hellominecraft.Amrac.SupprInactiveLockette;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Dispenser;
import org.bukkit.block.Furnace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.player.PlayerLoginEvent;

public class Listeners implements Listener {

	public SupprInactiveLockette plugin;
	
	public Listeners(SupprInactiveLockette plugin) {
           	this.plugin = plugin;
			plugin.getServer().getPluginManager().registerEvents(this, plugin);
        }
     
    @EventHandler
    public void normalLogin(PlayerLoginEvent event) {
            // Some code here
        }
     //(priority = EventPriority.HIGH)
    @EventHandler
     public void onPlayerInteract(BlockDamageEvent  event) {
    	
    	if (event.isCancelled()) return;

    		Block block = event.getBlock();
    		
    		if (block == null)
    		{
    			return;  // clicked in air, apparently
    		}

    		Material material = block.getType();
    		
    		if(Material.WALL_SIGN == material)//S'il s'agit d'une pancarte
    		{
    			Sign signe = (Sign) block.getState();
    			
    			if(signe.getLine(0).equalsIgnoreCase("[Private]") || signe.getLine(0).equalsIgnoreCase("[private]"))//C'est un lockette
    			{
    				Player player = event.getPlayer();
    				if(plugin.getConfig().getBoolean ("debug"))
    					player.sendMessage("[InactifLockette]Private detecté");
    				OfflinePlayer player1 = plugin.getServer().getOfflinePlayer(signe.getLine(1));
    		            if (player1 != null && player1.hasPlayedBefore() && (!player1.isOnline())) 
    		            {//Si on connait le joueur
    		            	long lastplayed = player1.getLastPlayed();
    		                
    		                Date maDateAvecFormat=new Date(lastplayed);
    		    			SimpleDateFormat dateStandard = new SimpleDateFormat("dd/MM/yyyy");
    		    			
    		    			if(plugin.getConfig().getBoolean ("debug"))
    		    				player.sendMessage(player1.getName()+" vu la dernier fois le "+dateStandard.format(maDateAvecFormat));
    		    			
    		    			
    		    			if(plugin.getConfig().getBoolean ("debug"))
    		    				player.sendMessage("il y a "+(System.currentTimeMillis()-lastplayed)/1000+" secondes");
    		    			
    		    			lastplayed = lastplayed/1000;
    		    			
    		    			//lastplayed= (long) 1325372400;
    		    			//lastplayed= (long) 1336749327;
    		    			long currentTime = System.currentTimeMillis()/1000-60*60*24*plugin.getConfig().getInt ("DaysLimit");
    		    			
    		    			if(lastplayed >currentTime)//15 jours - le joueur est actif, rien a faire
    		    			{
    		    				if(plugin.getConfig().getBoolean ("debug"))
    		    					player.sendMessage("Joueur actif ");//"+lastplayed+"="+currentTime
    		    			}
    		    			else 
    		    				{ // Joueur inactif - Lockette à virer 
    		    				if(plugin.getConfig().getBoolean ("debug"))
    		    					player.sendMessage("Joueur inactif"+lastplayed+"="+currentTime);
    		    				//On vide les coffres, on supprime le lockette
    		    				Block mur = block.getRelative( ((org.bukkit.material.Sign)(signe.getData())).getAttachedFace());
    		    			//	player.sendMessage("Mur: "+mur.toString());
    		    				if(mur.getType() == Material.CHEST  && plugin.getConfig().getBoolean ("clearAllItem"))
    		    				{
    		    					if(plugin.getConfig().getBoolean ("debug"))
    		    						player.sendMessage("[InactifLockette] Le coffre est vidé");
    		    					Chest coffre = (Chest) mur.getState();
    		    					coffre.getInventory().clear();
    		    					
    		    					BlockFace[] chestFaces = {BlockFace.EAST, BlockFace.NORTH, BlockFace.WEST, BlockFace.SOUTH};
    		    					for (BlockFace bf : chestFaces) {
    		    			            Block faceBlock = mur.getRelative(bf);
    		    			            if (faceBlock.getType() == Material.CHEST)
    		    			            {
    		    			            	((Chest) faceBlock.getState()).getInventory().clear();
    		    			            }
    		    			        }
    		    				}
    		    				else if(mur.getType() == Material.FURNACE  && plugin.getConfig().getBoolean ("clearAllItem"))
    		    				{
    		    					if(plugin.getConfig().getBoolean ("debug"))
    		    						player.sendMessage("[InactifLockette] Le coffre est vidé");
    		    					Furnace four = (Furnace) mur.getState();
    		    					four.getInventory().clear();
    		    				}
    		    				else if(mur.getType() == Material.DISPENSER && plugin.getConfig().getBoolean ("clearAllItem"))
    		    				{
    		    					if(plugin.getConfig().getBoolean ("debug"))
    		    						player.sendMessage(plugin.getConfig().getString("clearChest"));
    		    					Dispenser distributeur = (Dispenser) mur.getState();
    		    					distributeur.getInventory().clear();
    		    				}
    		    				//on supprime la lockette
    		    				block.setTypeId(0);
    		    				}
    		    			//player.sendMessage(""+lastplayed);
    		    			//player.sendMessage(""+currentTime);
    		    			//player.sendMessage(""+dateStandard.format(new Date(lastplayed)));
    		    			//player.sendMessage(""+dateStandard.format(new Date(currentTime)));
    		    			
    		            } else if(plugin.getConfig().getBoolean ("debug")) player.sendMessage("player1 null");
    			}
    			//else if(plugin.getConfig().getBoolean ("debug")) player.sendMessage("[InactifLockette]Private non detecté"+signe.getLine(0)+"|");
    		}//else if(plugin.getConfig().getBoolean ("debug")) player.sendMessage("[InactifLockette] pas de pancarte");
    		
    
    	}
    	
    	
  
    
}
