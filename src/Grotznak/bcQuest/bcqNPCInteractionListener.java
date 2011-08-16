package Grotznak.bcQuest;

import org.bukkit.Location;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.martin.bukkit.npclib.NPCEntity;
import org.martin.bukkit.npclib.NPCManager;

public class bcqNPCInteractionListener extends PlayerListener{
    public static bcQuest plugin;
    static NPCManager NpcManager;
    int id = 0;
    public bcqNPCInteractionListener(bcQuest instance){
        plugin = instance;
        NpcManager = new NPCManager(instance);
    }

    public void onPlayerInteract(PlayerInteractEvent event){
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK ){
          //  NpcManager.spawnNPC("Grotznak", event.getClickedBlock().getLocation().add(0.5, 1, 0.5), Integer.toString(id));
          //  id++;
        }
    }
    
    public static NPCEntity spawnNPC(String name, Location l) {
    	return  NpcManager.spawnNPC(name, l.add(0.5, 0.1, 0.5));   	
    }
    
    public static NPCEntity spawnNPC(String name, Location l, String id) {
    	return NpcManager.spawnNPC(name, l.add(0.5, 0.1, 0.5), id);   	
    }

}