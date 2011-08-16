package Grotznak.bcQuest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class bcqQuest {

	public int id; 
	public String name; 
	
	

	 Hashtable<String,String> QUEST;
	 private String defaultQuest = 
		 "name=default quest" + '\n' +
		 "allowedTool=0" + '\n' +
		 "allowedTarget=0" + '\n' +
		 "base-skill=lumberjack" + '\n' +
		 "skill-min-lumberjack=0" + '\n' +
		 "skill-dif-lumberjack=1" + '\n' +
		 "skill-gain-lumberjack=0" + '\n' +
		 "archivements=QUEST_COMPLETED" + '\n' +
		 ""
		 ;
	
	public bcqQuest(String id){
		checkQuestExists(id);
		this.name = QUEST.get("name");
		//this.activeQuests = getActiveQuests();
		//this.doneQuests = getDoneQuests();
		//this.achievements = getAchievements();
	}
	public boolean checkQuestExists(String id){	
		 File folder = new File("plugins" + File.separator + "bcQuest" + File.separator + "quests");
	     if (!folder.exists()) {
	         folder.mkdir();
	     }
	     
	     File PlayerFile = new File(folder.getAbsolutePath() + File.separator + id + ".quest");
	     if (PlayerFile.exists()){
	     	//printlog("loading config file: done" );
	     	Scanner sc = null;
	     	try {
					sc = new Scanner(PlayerFile);
					this.QUEST = bcQuestConfig.langGenerate(sc);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
	     }else{
	     //	printlog("creating config file: done");
	     	BufferedWriter out = null;
	     	try {
					PlayerFile.createNewFile();
					out = new BufferedWriter(new FileWriter(PlayerFile));
					out.write(defaultQuest);
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				checkQuestExists(id);	
	     }
	     
		return true; 
	}

	public boolean isIn(Location l){
		return true;
	}
	public boolean isAllowedTool(int tool) {
		// TODO Auto-generated method stub
		if (getAllowedTools().contains(""+tool)) return true;
		return false;
	}
	private List<String> getAllowedTools() {
		// TODO Auto-generated method stub
		List<String> l = new ArrayList<String>();
		String s;
		if ((s = this.QUEST.get("allowedTool")) != null){	
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
	
	public boolean isAllowedTarget(int bid) {
		if (getAllowedTarget().contains(""+bid)) return true;
		return false;
	}
	
	private List<String> getAllowedTarget() {
		// TODO Auto-generated method stub
		List<String> l = new ArrayList<String>();
		String s;
		if ((s = this.QUEST.get("allowedTarget")) != null){	
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
	public String getBaseSkill() {
		// TODO Auto-generated method stub
		return this.QUEST.get("base-skill");
	}
	public String getSkillDif(String base) {
		// TODO Auto-generated method stub
		return this.QUEST.get("skill-dif-"+base);
	}
}
