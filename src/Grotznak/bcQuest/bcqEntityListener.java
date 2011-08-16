package Grotznak.bcQuest;

import java.util.Hashtable;
import java.util.logging.Logger;

import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.util.Vector;

public class bcqEntityListener extends EntityListener{
	public Logger log;
	private Hashtable<String, String> CONFIG;
	private Hashtable<String, String> LANG;
	private boolean debug;
	
	public void config(Hashtable<String, String> CONFIG,Hashtable<String, String> LANG, boolean debug,Logger log){
    	this.CONFIG = CONFIG;
        this.LANG = LANG;
        this.debug = debug;
        this.log = log;
    }
	
	public void onEntityTarget(EntityTargetEvent e) {

	}
}
