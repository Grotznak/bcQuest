package Grotznak.bcQuest;

import java.io.BufferedWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;


import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.player.SpoutPlayer;
import org.martin.bukkit.npclib.NPCEntity;




public class bcqPlayer {
	
	public int id; 
	Player p;
	public String name; 
	public String[] keys = new String[9];
	
	
	public List<String> activeQuests = new ArrayList<String>();
	public List<String> doneQuests = new ArrayList<String>();
	public List<String> achievements = new ArrayList<String>();
	
	 Hashtable<String,String> PLAYER;
	 private String defaultPlayer = 
		 "name=12" + '\n' +
		 "activequests=" + '\n' +
		 "donequests=" + '\n' +		 
		 "achievements=" + '\n' +
		 "skill-lumberjack=0" + '\n' +
		 ""
		 ;
	 
	 Hashtable<String,String> ACHIEVEMENTS;
	 private String defaultACH = 
		 "QUEST_DONE=APPLE,QUEST_DONE,QUEST_DONE_DESC" + '\n' +
		 "QUEST_RECIEVED=APPLE,QUEST_QUEST_RECIEVED,QUEST_RECIEVED_DESC" + '\n' +		 
		 "TALK_TO_NPC=APPLE,TALK_TO_NPC,TALK_TO_NPC_DESC" + '\n' +		 
		 ""
		 ;
	
	public bcqPlayer(Player p){
		checkPlayerExists();
		this.p = p;
		this.name = p.getName();
		this.activeQuests = getActiveQuests();
		this.doneQuests = getDoneQuests();
		this.achievements = getAchievements();
	}

 
	private List<String> getAchievements() {
		List<String> l = new ArrayList<String>();
		String s;
		if ((s = this.PLAYER.get("achievements")) != null){	
			if (s.contains(",")){
				String[] contents = s.split(",");
				for (String item: contents){		
					l.add(item);
				}	
			} else {
				l.add(s);
			}
		} else {
			l.add("0");
			return l;
		}	
		return l;
	}
	
	private boolean checkAchievements(String a){
		List<String> l = this.getAchievements();
		if (l.contains(a)) {
			return true;		
		}		
		return false;	
	}

	public void recieveAchievement( String a) {
		// TODO Auto-generated method stub
		
		if (!checkAchievements(a)){
			loadAchievementFile();	
			this.achievements.add(a);
			String al;
			if (!getAchievements().contains("0")) {
				al = this.PLAYER.get("achievements");
				al = al +","+ a;	
				p.sendMessage("achfound" + al );
			}	else {
				al = a;
				p.sendMessage("achfound NOT"  + al );
			}	
			this.PLAYER.put("achievements",al);
			storePlayerFile(this.name);	
			SpoutPlayer sPlayer = (SpoutPlayer) p;
			sPlayer.sendNotification(a, "Achievement", Material.APPLE );
		}	
		p.sendMessage("aallready ");
	}


	private void loadAchievementFile() {
		// TODO Auto-generated method stub
		 File folder = new File("plugins" + File.separator + "bcQuest" );
	     if (!folder.exists()) {
	         folder.mkdir();
	     }
	     
	     File PlayerFile = new File(folder.getAbsolutePath() + File.separator + "achievements" + ".dat");
	     if (PlayerFile.exists()){
	     	//printlog("loading config file: done" );
	     	Scanner sc = null;
	     	try {
					sc = new Scanner(PlayerFile);
					this.ACHIEVEMENTS = bcQuestConfig.langGenerate(sc);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
	     }else{
	     //	printlog("creating config file: done");
	     	BufferedWriter out = null;
	     	try {
					PlayerFile.createNewFile();
					out = new BufferedWriter(new FileWriter(PlayerFile));
					out.write(defaultACH);
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				loadAchievementFile();	
	     }
	}

	private List<String> getActiveQuests() {
		List<String> l = new ArrayList<String>();
		String s;
		if ((s = this.PLAYER.get("activequests")) != null){	
			String[] contents = s.split(",");
			for (String item: contents){		
				l.add(item);
			}
		} else {
			l.add("0");
			return l;
		}	
		return l;
	}

	private List<String> getDoneQuests() {
		 List<String> l = new ArrayList<String>();
			String s;
			if ((s = this.PLAYER.get("donequests")) != null){	
				String[] contents = s.split(",");
				for (String item: contents){		
					l.add(item);
				}
			} else {
				l.add("0");
				return l;
			}	
			return l;
	}

	public boolean activateQuest(String q){
	if (!isActiveQuest(q) && !isDoneQuest(q)){
		this.activeQuests.add(q);
		String ql = PLAYER.get("activequests");
		ql = ql +","+ q;
		PLAYER.put("activequests",ql);
		storePlayerFile(this.name);
		return true;
	}
	return false;
}

	public boolean storeFirstSelection(int  x, int y, int z){
	 if (!checkPlayerExists()) { return false;}
	
	 if (PLAYER.contains("clip_first_x")){
		 PLAYER.remove("clip_first_x");
		 PLAYER.put("clip_first_x", x+"");
	 } else {
		 PLAYER.put("clip_first_x", x+"");
	 }
	 
	 if (PLAYER.contains("clip_first_y")){
		 PLAYER.remove("clip_first_y");
		 PLAYER.put("clip_first_y", y+"");
	 } else {
		 PLAYER.put("clip_first_y", y+"");
	 }
	 
	 if (PLAYER.contains("clip_first_z")){
		 PLAYER.remove("clip_first_z");
		 PLAYER.put("clip_first_z", z+"");
	 } else {
		 PLAYER.put("clip_first_z", z+"");
	 }
	 
	 storePlayerFile(p.getName());
	 return true;
 }
 
	public boolean storeSecondarySelection(int  x, int y, int z){
	 if (!checkPlayerExists()) { return false;}
	
	 if (PLAYER.containsKey("clip_second_x")){
		 PLAYER.remove("clip_second_x");
		 PLAYER.put("clip_second_x", x+"");
	 } else {
		 PLAYER.put("clip_second_x", x+"");
	 }
	 
	 if (PLAYER.containsKey("clip_second_y")){
		 PLAYER.remove("clip_second_y");
		 PLAYER.put("clip_second_y", y+"");
	 } else {
		 PLAYER.put("clip_second_y", y+"");
	 }
	 
	 if (PLAYER.containsKey("clip_second_z")){
		 PLAYER.remove("clip_second_z");
		 PLAYER.put("clip_second_z", z+"");
	 } else {
		 PLAYER.put("clip_second_z", z+"");
	 }
	 
	 storePlayerFile(p.getName());
	 return true;
 }
 
 	public boolean tryStoreArea(String[] command){
	if (!checkPlayerExists()) { return false;}
	//if (true) {p.sendMessage("Area:"+  PLAYER.containsKey(key)("clip_first_x"));} 
	
	//if(this.PLAYER.containsKey("cord_first_x")) {
		//if(this.PLAYER.containsKey("cord_second_x")) {
			if ((command.length == 2 )){
				 p.sendMessage("Questarea saved by name "+ command[1] + " in world " + p.getWorld().getName());
				 File folder = new File("plugins" + File.separator + "bcQuest" + File.separator + "areas");
			     if (!folder.exists()) {
			         folder.mkdir();
			     }
			     File PlayerFile = new File(folder.getAbsolutePath() + File.separator + command[1] + ".area");
			     BufferedWriter out = null;
			     	try {
							PlayerFile.createNewFile();
							out = new BufferedWriter(new FileWriter(PlayerFile));
							out.write( "world="+ p.getWorld().getName() + '\n' +
									 "fx=" + PLAYER.get("clip_first_x") + '\n' +
									 "fy=" + PLAYER.get("clip_first_y") + '\n' +
									 "fz=" + PLAYER.get("clip_first_z") + '\n' +
									 "sx=" + PLAYER.get("clip_second_x") + '\n' +
									 "sy=" + PLAYER.get("clip_second_y") + '\n' +
									 "sz=" + PLAYER.get("clip_second_z") + '\n' +
									 ""
									 );
							out.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
				return true;
			} else { p.sendMessage("No name given: /quest definearea [name]");}
		//} else { p.sendMessage("No secondary selection");}
	//} else { p.sendMessage("No primary selection");}
    return false;	
}

	public  boolean tryCreateNpc(String[] command){
	
   if (!checkPlayerExists()) { return false;}	
			if ((command.length == 2 )){
				
				 p.sendMessage("NPC saved by name "+ command[1] + " in world " + p.getWorld().getName());
				 
				 NPCEntity npc = bcqNPCInteractionListener.spawnNPC(command[1], p.getLocation());
				 
					
				 File folder = new File("plugins" + File.separator + "bcQuest" + File.separator + "npc");
			     if (!folder.exists()) {
			         folder.mkdir();
			     }
			     File PlayerFile = new File(folder.getAbsolutePath() + File.separator + command[1] + ".txt");
			     BufferedWriter out = null;
			     	try {
							PlayerFile.createNewFile();
							out = new BufferedWriter(new FileWriter(PlayerFile));
							out.write( "world="+ p.getWorld().getName() + '\n' +
									 "name=" + command[1] + '\n' +
									 "x=" + p.getLocation().getX() + '\n' +
									 "y=" +  p.getLocation().getY() + '\n' +
									 "z=" +  p.getLocation().getZ()  + '\n' +
									 "id=" + npc.id  + '\n' +
									 ""
									 );
							out.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					//LivingEntity mynpc = null;
					

				return true;
			} else { p.sendMessage("No name given: /quest createnpc [name]");}
     
    return false; 	
}
 
	public boolean checkPlayerExists(){
	 File folder = new File("plugins" + File.separator + "bcQuest" + File.separator + "players");
     if (!folder.exists()) {
         folder.mkdir();
     }
     
     File PlayerFile = new File(folder.getAbsolutePath() + File.separator + p.getDisplayName() + ".dat");
     if (PlayerFile.exists()){
     	//printlog("loading config file: done" );
     	Scanner sc = null;
     	try {
				sc = new Scanner(PlayerFile);
				this.PLAYER = bcQuestConfig.langGenerate(sc);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
     }else{
     //	printlog("creating config file: done");
     	BufferedWriter out = null;
     	try {
				PlayerFile.createNewFile();
				out = new BufferedWriter(new FileWriter(PlayerFile));
				out.write(defaultPlayer);
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			checkPlayerExists();	
     }
     
	return true; 
 }
 
 	public boolean storePlayerFile(String name){
	 File folder = new File("plugins" + File.separator + "bcQuest" + File.separator + "players");
	 File PlayerFile = new File(folder.getAbsolutePath() + File.separator + name + ".dat");
	 BufferedWriter out = null;
	 try {
			PlayerFile.createNewFile();
			out = new BufferedWriter(new FileWriter(PlayerFile));
			out.write(bcQuestConfig.getPlayerString(this.PLAYER));
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	 return true;
 }

	public boolean isAvailableQuest(String q) {	
		if (isDoneQuest(q)) {
			return false;
		}
		if (isActiveQuest(q)) {
			return false;
		}
		return true;
	}
	
	private boolean isActiveQuest(String q) {
	    if (this.activeQuests.contains(q)) {
	    	return true;
	    }
		return false;
	}

	private boolean isDoneQuest(String q) {
		 if (this.doneQuests.contains(q)) {
		    	return true;
		    }
		return false;
	}

	public void setKey(int i, String qitem) {
		// TODO Auto-generated method stub
		keys[i]=qitem;
	}


	public boolean hasActiveQuests() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isInQuestZone() {
		// TODO Auto-generated method stub
		List<String> l = this.getActiveQuests();
		boolean isIn = false;
		for (String item: l){
			if (isInQuestZone(item)) isIn = true;
		}
		return isIn;
	}
	
	public boolean isInQuestZone(String quest) {
		// TODO Auto-generated method stub
		return true;
	}




	public String getSkill(String string) {
		// TODO Auto-generated method stub
		//return "1";
		return PLAYER.get("skill-"+string);
	}




	public void SkillIncrease(String base, int dif) {
		// TODO Auto-generated method stub
		
		int i = 1+ Integer.parseInt(PLAYER.get("skill-"+base));
		
		double a = (dif/(Math.pow((i+1.0),3)));
		double b = Math.random();
		//p.sendMessage(a +" > "+ b + "Skill" + i +"difficultiy" + dif);
		if (a>b) {
			this.PLAYER.put("skill-"+base,dif+i+"");
			storePlayerFile(this.name);	
			p.sendMessage("Skill Increased:" + base + " to lvl:" + dif+i );
		}

	}






}
