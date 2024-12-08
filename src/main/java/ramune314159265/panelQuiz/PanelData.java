package ramune314159265.panelQuiz;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class PanelData {
	public Integer index;
	public Location buttonLocation;
	public List<Location> blockLocations;
	public Location textLocation;
	public float[] textRotation;

	public PanelData(int index) {
		this.index = index;
		this.buttonLocation = null;
		this.textLocation = null;
		this.textRotation = new float[2];
		this.blockLocations = new ArrayList<>();
	}
}
