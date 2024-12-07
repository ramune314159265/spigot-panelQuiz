package ramune314159265.panelQuiz;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PluginListener implements Listener {
	@EventHandler
	public void onButtonPress(PlayerInteractEvent e) {
		if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && Tag.BUTTONS.isTagged(e.getClickedBlock().getType())) {
			PanelQuiz.getInstance().config.buttonPoses.forEach((Integer index, Location location) ->{
				if(!location.equals(e.getClickedBlock().getLocation())){
					return;
				}
				e.getPlayer().performCommand("answerquestion " + index);
			});
		}
	}
}
