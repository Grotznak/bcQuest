package Grotznak.bcQuest;

import java.util.Hashtable;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;


public class bcqBlockListener extends BlockListener{
	public Logger log;
	private boolean debug;
	Player p;
	Server s;
	World w;
	String name;
	
	private Hashtable<String, String> CONFIG;
	private Hashtable<String, String> LANG;
	
	public void config(Hashtable<String, String> CONFIG,Hashtable<String, String> LANG,boolean debug,Logger log){
    	this.CONFIG = CONFIG;
        this.LANG = LANG;
        this.debug = debug;
        this.log = log;
    }
	


	public void onBlockBreak(BlockBreakEvent e)  {
	   if (CONFIG.get("allowBuild").equals("false")){
			   e.setCancelled(true);
	   }
	   
	   Player p = e.getPlayer();
	   bcqPlayer myPlayer = new bcqPlayer(p);
	   if (!myPlayer.hasActiveQuests()) return;
	   if (!myPlayer.isInQuestZone()) return;
	   

	   
	}
	
	public void onBlockDamage(BlockDamageEvent e)  {

		  
		   Player p = e.getPlayer();
		   bcqPlayer myPlayer = new bcqPlayer(p);
		   if (!myPlayer.hasActiveQuests()) return;
		   if (!myPlayer.isInQuestZone()) return;
		   int bid = e.getBlock().getTypeId();
		   int tool = e.getItemInHand().getTypeId();
		   for (String item : myPlayer.activeQuests){
			  // if (debug) p.sendMessage("Checking Quest:" + item + " Target: " + bid + " Tool:" + tool);
			   bcqQuest myQuest = new bcqQuest(item);
	
			   if (myQuest.isIn(p.getLocation())){
				  if( myQuest.isAllowedTool(tool)){
					  if( myQuest.isAllowedTarget(bid)){
						 // if (debug) p.sendMessage("Proccessing Quest:" + item + " Target: " + bid + " Tool:" + tool);
						  String base = myQuest.getBaseSkill();

						  int skill = Integer.parseInt(myPlayer.getSkill(base));
						 // p.sendMessage(myQuest.getSkillDif(base));
						  int dif =  Integer.parseInt(myQuest.getSkillDif(base));
						  double d =( (skill+1) / dif);
						  d = Math.random() *d;
						 // p.sendMessage(skill+ " "+ dif + " " +d);
					      if (d> 0.95) {
					    	  ItemStack i = new ItemStack(bid,1);
					    	  p.getInventory().addItem(i);
					    	  myPlayer.SkillIncrease(base,dif);
					      }
					  } 
				  }
			   } 
		   }
		  
		   if (CONFIG.get("allowBuild").equals("false")){
			   e.setCancelled(true);
		   }
		}
	
	public void onBlockPlace(BlockPlaceEvent event)  {
		
	}
}
