package ramune314159265.panelQuiz;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TextDisplay;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import java.util.HashMap;

public class PanelDisplay {
	public static HashMap<Integer, PanelDisplay> list = new HashMap<>();
	public int index;

	public PanelDisplay(int index) {
		this.index = index;
	}

	public static void setList() {
		PanelQuiz.getInstance().config.panelData.forEach((Integer index, PanelData panelData) -> {
			PanelDisplay.list.put(index, new PanelDisplay(index));
		});
	}

	public void fillPanelBlock(Material block) {
		if (!PanelQuiz.getInstance().config.panelData.containsKey(this.index)) {
			return;
		}
		PanelQuiz.getInstance().config.panelData.get(this.index).blockLocations.forEach((Location location) -> {
			location.getBlock().setType(block);
		});
	}

	public void setPanelText(String text) {
		PanelData panelData = PanelQuiz.getInstance().config.panelData.get(this.index);

		panelData.textLocation.getWorld().getEntitiesByClasses(TextDisplay.class)
				.forEach((Entity textDisplay) -> {
					if (!textDisplay.hasMetadata("panelQuiz.index")) {
						return;
					}
					if (textDisplay.getMetadata("panelQuiz.index").get(0).asInt() != this.index) {
						return;
					}
					textDisplay.remove();
				});
		float textSize = panelData.textSize * (2F / Math.max(text.length(), 2F));

		TextDisplay textDisplay = (TextDisplay) panelData.textLocation.getWorld().spawnEntity(panelData.textLocation, EntityType.TEXT_DISPLAY);
		textDisplay.setText(text);
		textDisplay.setBackgroundColor(org.bukkit.Color.fromARGB(0, 0, 0, 0));
		textDisplay.setBrightness(new Display.Brightness(15, 15));
		textDisplay.setTransformation(new Transformation(
				new Vector3f(),
				new AxisAngle4f(),
				new Vector3f(textSize, textSize, textSize),
				new AxisAngle4f()
		));
		textDisplay.setRotation(panelData.textRotation[0], panelData.textRotation[1]);
		textDisplay.setMetadata("panelQuiz.index", new FixedMetadataValue(PanelQuiz.getInstance(), this.index));
	}
}
