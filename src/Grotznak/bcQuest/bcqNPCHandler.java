package Grotznak.bcQuest;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Creature;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.martin.bukkit.npclib.NPCEntity;

public class bcqNPCHandler {
	
	public static int loadNPCFromFiles (World w) {
		 if (unloadNPC(w)){
		 Hashtable<String,Hashtable<String,String>> worldlist = new Hashtable<String,Hashtable<String,String>>();
		 int count =0;
		 File folder = new File("plugins" + File.separator + "bcQuest" + File.separator + "npc");
	     if (!folder.exists()) {
	         folder.mkdir();
	     }
	     File[] npcfiles = folder.listFiles();
	     for (int i = 0; i < npcfiles.length; i++) {
	    	 if (spawnNPC(w,npcfiles[i]))
	    	 {
	    		 count++;
	    	 }
		 }	
	    return count;
		 }
		return 0;
	}

	
	public static boolean spawnNPC(World w, File npcFile){
		Hashtable<String,String> NPCTable =null;
		Scanner sc = null;
    	try {
			sc = new Scanner(npcFile);
			 NPCTable = bcQuestConfig.langGenerate(sc);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
       		
		
		Location l = w.getSpawnLocation() ;
//		l.setWorld(w);
		//l.setPitch(0);
		//l.setYaw(0);
		l.setX((Double.valueOf( NPCTable.get("x"))));
		l.setY((Double.valueOf( NPCTable.get("y"))));
		l.setZ((Double.valueOf( NPCTable.get("z"))));
		
		if (w.getName().equals(NPCTable.get("world"))) {
			NPCEntity newnpc = bcqNPCInteractionListener.spawnNPC(NPCTable.get("name"), l);
			bcQuest.addnpc(newnpc.id+"", NPCTable);
			
			return true;
		
		} else {
			return false;
		}
		
	}
	
	public static boolean unloadNPC(World w){
        bcqNPCInteractionListener.NpcManager.despawnAll();
		return true;
	}


	public static List<String> getQuestList(Hashtable<String, String> npc) {
		List<String> l = new ArrayList<String>();
		String qstring;
		if ((qstring = npc.get("questlists")) != null){
		
			String[] contents = qstring.split(",");
			for (String item: contents) {
				   l.add(item);
			}
			return l;
		} 
			
			return null;			
		
	}
	
}
