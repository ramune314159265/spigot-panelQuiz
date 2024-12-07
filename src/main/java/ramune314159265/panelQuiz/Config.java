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
	public Map<Integer, Location> buttonPoses;
	File dataFolder;

	public Config(File dataFolder) {
		this.dataFolder = dataFolder;
		this.buttonPoses = new HashMap<>();
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

		configToml.getTables("buttonPoses").forEach((Toml buttonPosData) -> {
			this.buttonPoses.put(
					Math.toIntExact(buttonPosData.getLong("index")),
					new Location(
							Bukkit.getWorld(buttonPosData.getString("world")),
							buttonPosData.getLong("x"),
							buttonPosData.getLong("y"),
							buttonPosData.getLong("z"))
			);
		});
	}
}
