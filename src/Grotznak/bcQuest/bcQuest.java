package Grotznak.bcQuest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;

import java.util.Scanner;
import java.util.logging.Logger;


import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import org.bukkit.event.*;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;




public class bcQuest extends JavaPlugin{	
	bcqPlayerListener pListener = new bcqPlayerListener();
	bcqBlockListener bListener = new bcqBlockListener();
	bcqEntityListener eListener = new bcqEntityListener();
	bcqWorldListener wListener = new bcqWorldListener();
	//bcqInputListener keyListner = new bcqInputListener();
	public Logger log;
 //   public static PermissionHandler permissionHandler;
	public boolean debug;
  
	// config
    public Hashtable<String,String> CONFIG;
	//default config text file 
	private static final String defaultConfig = 
		"admin-tool=12" + '\n' +
	 	"debug=true" + '\n' +
	 	"guiLang=english" + '\n' +	
	 	"allowBuild=false" + '\n' +
	 	"forceInventory=false" + '\n' +	
	 	"worlds=world" + '\n' +	
	 	""
		;

	//language config
    public Hashtable<String,String> LANG;
	//default lang text file
	private static final String defaultLang = 		
	     "VOTING_COMMANDS_HEAD=Commands:"  + '\n' +
	     "VOTING_COMMANDS_VOTE_DESC_DAY=-- Vote for daylight"  + '\n' +
	     "VOTING_COMMANDS_VOTE_DESC_NIGHT=-- Vote for nightfall"  + '\n' +
	     "VOTING_COMMANDS_VOTE_DESC_SUN=-- Vote for sunshine"  + '\n' +
	     "VOTING_COMMANDS_VOTE_DESC_RAIN=-- Vote for rain"  + '\n' +
	     "VOTING_COMMANDS_VOTE_DESC_UNDO=-- undo (reset) all you votings to default (no)"  + '\n' +
	     "VOTE_BROADCAST_PERMISSION=Someone has started a vote for %vote%. Currently there are %yes% YES-votes and %no% NO-votes." + '\n' +
	     "" 
		;

	public static Hashtable<String,Hashtable<String, String>> NPCList = new Hashtable<String,Hashtable<String, String>>();
   
	
	@Override
	public void onDisable() {
		
		
	}

	@Override
	public void onEnable() {
		// TODO Auto-generated method stub
		log = Logger.getLogger("Minecraft");
		printlog("BlockCraftQuest loaded");
	
		
		loadConfig();		
		loadLanguageFile();	
//		setupPermissions();
				
		if (CONFIG.get("debug").equals("true")) {
				debug = true; 		
		}  else {
			    debug = false; 		
		} 
			    
		pListener.config(CONFIG,LANG,debug,log);
		bListener.config(CONFIG,LANG,debug,log);
		eListener.config(CONFIG,LANG,debug,log);		
		 
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_MOVE, pListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_INTERACT, pListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_INTERACT_ENTITY, pListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_JOIN, pListener, Priority.Normal, this);
	    pm.registerEvent(Event.Type.PLAYER_QUIT, pListener, Priority.Normal, this);
		pm.registerEvent(Event.Type.BLOCK_BREAK, bListener, Priority.Normal, this);
	    pm.registerEvent(Event.Type.BLOCK_PLACE, bListener, Priority.Normal, this);
		pm.registerEvent(Event.Type.BLOCK_DAMAGE, bListener, Priority.Normal, this);
	    pm.registerEvent(Event.Type.ENTITY_TARGET, eListener, Priority.Normal, this);
	    pm.registerEvent(Event.Type.WORLD_LOAD, wListener, Priority.Normal, this);


	    this.getServer().broadcastMessage("bcQuest reloaded for worlds: "+ CONFIG.get("worlds"));
	    
	    bcqNPCInteractionListener pl = new bcqNPCInteractionListener(this);
        pm.registerEvent(Event.Type.PLAYER_INTERACT, pl, Event.Priority.Normal, this);
	    
	}
	
	@Override
    public boolean onCommand(CommandSender sender, Command command,
    		String label, String[] args) {
    	    return pListener.onPlayerCommand(sender, command, label, args);
    }
	
	
	
	public void printlog(String message) {
		PluginDescriptionFile pdfFile = getDescription();
		log.info("[" + pdfFile.getName() + " Version: " + pdfFile.getVersion() + "] " + message);
	}
	

	
	private void loadConfig(){ 
		//check for configurations files or create them
		 File folder = new File("plugins" + File.separator + "bcQuest");
	        if (!folder.exists()) {
	            folder.mkdir();
	        }
	        
	        File ConfigFile = new File(folder.getAbsolutePath() + File.separator + "config.yml");
	        if (ConfigFile.exists()){
	        	printlog("loading config file: done" );
	        	Scanner sc = null;
	        	try {
					sc = new Scanner(ConfigFile);
					CONFIG = bcQuestConfig.langGenerate(sc);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
	        }else{
	        	printlog("creating config file: done");
	        	BufferedWriter out = null;
	        	try {
					ConfigFile.createNewFile();
					out = new BufferedWriter(new FileWriter(ConfigFile));
					out.write(defaultConfig);
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				loadConfig();	
	        }
	}
	
	
	private void loadLanguageFile(){
		//check for configurations files or create them
		 File folder = new File("plugins" + File.separator + "bcQuest");
	        if (!folder.exists()) {
	            folder.mkdir();
	        }
	        
	        File langFile = new File(folder.getAbsolutePath() + File.separator + CONFIG.get("guiLang") + ".lang");
	        if (langFile.exists()){
	        	printlog("loading language file: " + CONFIG.get("guiLang"));
	        	Scanner sc = null;
	        	try {
					sc = new Scanner(langFile);
					LANG = bcQuestConfig.langGenerate(sc);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
	        }else{
	        	printlog("creating language file: " + CONFIG.get("guiLang"));
	        	BufferedWriter out = null;
	        	try {
					langFile.createNewFile();
					out = new BufferedWriter(new FileWriter(langFile));
					out.write(defaultLang);
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				loadLanguageFile();	
	        }
		
	}
	
	
	public static void addnpc(String id, Hashtable<String, String> NPCTable){
		NPCList.put(id,NPCTable);
	}

	
	
}