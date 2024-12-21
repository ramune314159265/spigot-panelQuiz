package ramune314159265.panelQuiz;

import com.moandjiezana.toml.Toml;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class Config {
	public Map<Integer, PanelData> panelData;
	File dataFolder;

	public Config(File dataFolder) {
		this.dataFolder = dataFolder;
		this.panelData = new HashMap<>();
	}

	public void load() {
		File configFile = new File(this.dataFolder, "conf.toml");
		if (!configFile.getParentFile().exists()) {
			configFile.getParentFile().mkdirs();
		}

		if (!configFile.exists()) {
			try (InputStream input = PanelQuiz.class.getResourceAsStream("/" + configFile.getName())) {
				if (input != null) {
					Files.copy(input, configFile.toPath());
				} else {
					configFile.createNewFile();
				}
			} catch (IOException e) {
				Bukkit.getLogger().warning(e.toString());
			}
		}

		Toml configToml = new Toml().read(configFile);

		configToml.getTables("panels").forEach((Toml panelToml) -> {
			PanelData panelData = new PanelData(Math.toIntExact(panelToml.getLong("index")));

			panelData.buttonLocation = new Location(
					Bukkit.getWorld(panelToml.getString("button.world")),
					panelToml.getLong("button.x"),
					panelToml.getLong("button.y"),
					panelToml.getLong("button.z")
			);

			panelData.textLocation = new Location(
					Bukkit.getWorld(panelToml.getString("text.world")),
					Double.parseDouble(panelToml.getString("text.x")),
					Double.parseDouble(panelToml.getString("text.y")),
					Double.parseDouble(panelToml.getString("text.z"))
			);
			panelData.textRotation[0] = Float.parseFloat(panelToml.getString("text.yaw"));
			panelData.textRotation[1] = Float.parseFloat(panelToml.getString("text.pitch"));
			panelData.textSize = Float.parseFloat(panelToml.getString("text.size"));

			panelToml.getTables("blocks").forEach((Toml blockToml) -> {
				panelData.blockLocations.add(new Location(
						Bukkit.getWorld(blockToml.getString("world")),
						blockToml.getLong("x"),
						blockToml.getLong("y"),
						blockToml.getLong("z")
				));
			});

			this.panelData.put(
					Math.toIntExact(panelToml.getLong("index")),
					panelData
			);
		});

		PanelDisplay.setList();
	}
}
