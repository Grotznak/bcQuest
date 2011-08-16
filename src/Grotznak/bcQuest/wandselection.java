package Grotznak.bcQuest;

import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;


// deprecated
public class wandselection {
	public int firstX;
	public int firstY;
	public int firstZ;
	public int secondX;
	public int secondY;
	public int secondZ;
	//private bcqPlayer pHandler = new bcqPlayer();
 
	public void doSelect(Player p,Action type,int x, int y, int z){
		if (type== Action.RIGHT_CLICK_BLOCK ){
			this.firstX =x;
			this.firstY =y;
			this.firstZ =z;
			//pHandler.storeFirstSelection(p, x, y, z);
		}
		if (type== Action.LEFT_CLICK_BLOCK ){
			this.secondX =x;
			this.secondY =y;
			this.secondZ =z;
			//pHandler.storeSecondarySelection(p, x, y, z);
		}		
	}
	
}
