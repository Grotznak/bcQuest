package Grotznak.bcQuest;



import java.io.File;
import java.io.FileNotFoundException;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

public class bcQuestConfig {
	public static Hashtable<String,String> langGenerate(Scanner sc)
	   {
	      Hashtable<String,String> h = new Hashtable<String,String>();

			while(sc.hasNext()){
				String thisline = sc.nextLine();
				String[] contents = thisline.split("=");
				if (contents.length > 1){
					h.put(contents[0],contents[1]);
				}
			}	     
	      return h;
	   }
	
	public static Hashtable<String,String> parsePlayer(Scanner sc)
	   {
	      Hashtable<String,String> h = new Hashtable<String,String>();

			while(sc.hasNext()){
				String thisline = sc.nextLine();
				String[] contents = thisline.split("=");
				if (contents.length > 1){
					h.put(contents[0],contents[1]);
				}
			}	     
	      return h;
	   }
	
	public static String getPlayerString(Hashtable<String,String> tb)
	   {
	      String h = "";
			while(!tb.isEmpty()){
				String key = tb.keys().nextElement();
				h += key + "=" + tb.get(key) + '\n' ;
				tb.remove(key);
			}	     
	      return h;
	   }
	
    public static List<String> getQuestList(String id) {
		 List<String> l = new ArrayList<String>();
		 File folder = new File("plugins" + File.separator + "bcQuest" + File.separator + "quests");
	        if (!folder.exists()) {
	            folder.mkdir();
	        }
	        
	        File qFile = new File(folder.getAbsolutePath() + File.separator + "questlists" + ".dat");
	        if (qFile.exists()){
	        	//printlog("loading Ques file: " + CONFIG.get("guiLang"));
	        	Scanner sc = null;
	        	try {
					sc = new Scanner(qFile);
					while(sc.hasNext()){
						String thisline = sc.nextLine();
						String[] contents = thisline.split("=");
						if (contents.length > 1){
							if (contents[0].equals(id)) {
								String[] ql = contents[1].split(",");
								for (String item: ql){
									l.add(item);
								}
							}
							//l.put(contents[0],contents[1]);
						}
					}	     
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
	        }else{
	        //	printlog("creating language file: " + CONFIG.get("guiLang"));
	        return null;
	        }			
	      return l;
	   }
}


