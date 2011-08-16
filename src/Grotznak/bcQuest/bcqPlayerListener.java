package Grotznak.bcQuest;

import java.util.Hashtable;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.ChatColor;

import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import org.bukkit.entity.Entity;

import org.bukkit.entity.Player;

import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import org.getspout.spoutapi.player.SpoutPlayer;


public class bcqPlayerListener extends PlayerListener{
	
	public Logger log;
	private Hashtable<String, String> CONFIG;
	private Hashtable<String, String> LANG;
	private boolean debug;
	private wandselection clip = new wandselection();
	//private bcqPlayer pHandler = new bcqPlayer();
		
	Player p;
	Server s;
	World w;
	String name;
	
	public void config(Hashtable<String, String> CONFIG,Hashtable<String, String> LANG, boolean debug,Logger log){
    	this.CONFIG = CONFIG;
        this.LANG = LANG;
        this.debug = debug;
        this.log = log;
    }

	public boolean onPlayerCommand(CommandSender sender, Command command, String label, String[] args){
		if (sender instanceof Player) {
			p = (Player) sender;

			sender.sendMessage("BlockCraft Quest Plugin:");
			w = p.getWorld();
		} else {
			//bcQuest.printlog("onPlayerCommand - sender is not a player, skipping commands.");
			return false;
		}
		
		String[] split = args;
		if (!( label.equalsIgnoreCase("q") || label.equalsIgnoreCase("quest") || label.equalsIgnoreCase("bcquest") )) return false;
		bcqPlayer myPlayer = new bcqPlayer(p);
		
		if (split.length == 0 || (split.length == 1 && split[0].equalsIgnoreCase("help"))){
			p.sendMessage("Hilfe");
			return true;
		}
		
		if(split[0].equalsIgnoreCase("definearea")){
			sender.sendMessage(ChatColor.AQUA + "Trying to  create questarea");
			myPlayer.tryStoreArea(split);
			return true;
		}
		
		
		if(split[0].equalsIgnoreCase("createnpc")){
			sender.sendMessage(ChatColor.AQUA + "Trying to create NPC");
			//sender.sendMessage(ChatColor.AQUA + " " + clip.firstX);	
			myPlayer.tryCreateNpc(split);
			return true;
		}
		
		if(split[0].equalsIgnoreCase("reloadnpc") || split[0].equalsIgnoreCase("rw")){
			sender.sendMessage(ChatColor.AQUA + "Reloading Npc");
			int count = bcqNPCHandler.loadNPCFromFiles(p.getWorld());
			sender.sendMessage(ChatColor.AQUA + "Loaded: " + count + "in world: ("+ p.getWorld().getName()+")");
		}
		
		if(split[0].equalsIgnoreCase("select")){
			sender.sendMessage(ChatColor.AQUA + "Try to activate Quest" + split[1]);
			if (split[1]!=null) {
				if (myPlayer.activateQuest(split[1])) {
					sender.sendMessage(ChatColor.AQUA + "Successfull" + split[1]);
					myPlayer.recieveAchievement("QUEST_RECIEVED");
				} 	else {
					sender.sendMessage(ChatColor.AQUA + "Thats a negatory" + split[1]);
				}			
			}
			else {
				List<String> l = myPlayer.activeQuests;
				for (String item: l) {
					sender.sendMessage(ChatColor.AQUA + "Active-quest:" + item);						
				}
			}
		}
		
		return false;		
	}

	public void onPlayerJoin(PlayerJoinEvent e){
		p = e.getPlayer();
		p.getServer().broadcastMessage(ChatColor.AQUA + "Hallo " +e.getPlayer().getDisplayName() + " auf dem Server 1 (bcQuest)");
		bcqPlayer myPlayer = new bcqPlayer(e.getPlayer());
		List<String> l = myPlayer.activeQuests;
		String msg = "bcQuest: your active Quests:";
			for (String q: l){
				msg = msg + " "+ q;
			}
		p.getServer().broadcastMessage(msg);
	}
	public void onPlayerQuit(PlayerQuitEvent e){
		e.getPlayer().getServer().broadcastMessage(ChatColor.AQUA + "Left " +e.getPlayer().getDisplayName() + " auf dem Server (bcQuest)");
	}
	
	public void onPlayerInteract(PlayerInteractEvent e) {
		p = e.getPlayer();
		s = p.getServer();
		w = p.getWorld();
		ItemStack istack = null;
		int itemId;
		int configAdminTool = 0;
		int CordX = 0;
		int CordY = 0;	
		int CordZ = 0;
		
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK) {
			CordX = e.getClickedBlock().getX();
			CordY = e.getClickedBlock().getY();
			CordZ = e.getClickedBlock().getZ();
		}
		
		//check if hands empty
		if (e.hasItem()) {
			istack =  e.getItem();
			itemId = istack.getTypeId();
		} else {
			istack =  null;
			itemId = 0;
		}
        
		
		//read config admin tool
		try {
		   configAdminTool = new Integer(CONFIG.get("admin-tool")).intValue();
		} catch (Exception E) {
			configAdminTool = 270;
			p.sendMessage("Admin tool not found in config falling back to 270 (woodenpickaxe)");
		}
				
		if (itemId == configAdminTool) {
			if (debug) { s.broadcastMessage(ChatColor.AQUA + "DEBUG: Admin tool usage " + itemId + w.getName().toString() + " with: " +e.getAction() ); }
			if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK) {
				s.broadcastMessage(ChatColor.AQUA +  e.getAction().toString() + "First coordinate set " + " (x:" + CordX + " y:" + CordY +  " z:" +CordZ + ")" );
				clip.doSelect(p,e.getAction(), CordX, CordY, CordZ);
			}
			
		} else {
			// if (debug) { s.broadcastMessage(ChatColor.AQUA + "NO Admin tool usage " + itemId );  }
		}
	}
	
	
	public void onPlayerInteractEntity(PlayerInteractEntityEvent e)  {
		Player p = e.getPlayer();
		SpoutPlayer sPlayer = (SpoutPlayer) p;
		Entity en = e.getRightClicked();
		//p.sendMessage(en.getEntityId() +"");
		bcqPlayer myPlayer = new bcqPlayer(e.getPlayer());
		
		if (en instanceof Player) {
		
			Hashtable<String, String> entable = bcQuest.NPCList.get(en.getEntityId() +"");
			if (entable!= null) {
				//p.sendMessage(en.getEntityId() +" tried to move to " + p.getDisplayName());
				//sPlayer.setCompassTarget(en.getLocation());
				//sPlayer.sendNotification("Title", "Npc angesprochen ", Material.APPLE );
				Player se = (Player) en;
				//SpoutManager.getAppearanceManager().setGlobalTitle(SpoutManager.getPlayer(se), "test 1234 blub");
				//sPlayer.getMainScreen().attachWidget(Widget.);
				
				bcqNPCInteractionListener.NpcManager.moveNPC(en.getEntityId()+"", p.getLocation());
				List<String> qlist;
				if ((qlist = bcqNPCHandler.getQuestList(entable)) != null){
					myPlayer.recieveAchievement("TALK_TO_NPC");
					p.sendMessage(en.getEntityId() +" Has following quests:");
					for (String item: qlist){
						//p.sendMessage(en.getEntityId() +" Questline: " + item);	
						List<String> ql = bcQuestConfig.getQuestList(item);					
						for (String qitem: ql){
							if (myPlayer.isAvailableQuest(qitem)) {								
								bcqQuest myQuest = new bcqQuest(qitem);
								
								p.sendMessage(ChatColor.AQUA + " Quest: " + ChatColor.WHITE + qitem + " | " + myQuest.name + ChatColor.AQUA  + "  avaiable (/q select <id>)");									
							}
						}
					}
				} else  {
					p.sendMessage(en.getEntityId() +" has no quests in config file ");
					
				}
				
			} else{
				p.sendMessage("ERROR Entity NIL:" + en.getEntityId() + " ... removing");
				en.remove();
			}
		}	
	}

	
	
}
