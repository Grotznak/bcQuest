package Grotznak.bcQuest;



import org.bukkit.event.world.WorldListener;
import org.bukkit.event.world.WorldLoadEvent;

public class bcqWorldListener extends WorldListener {
	public void onWorldLoad(WorldLoadEvent event) {
	
    bcqNPCHandler.loadNPCFromFiles(event.getWorld());
	}
}
