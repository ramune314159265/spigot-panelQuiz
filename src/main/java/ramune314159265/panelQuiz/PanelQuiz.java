package ramune314159265.panelQuiz;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TextDisplay;
import org.bukkit.plugin.java.JavaPlugin;
import ramune314159265.panelQuiz.commands.*;
import ramune314159265.panelQuiz.quiztypes.Quiz;

import java.util.Objects;

public final class PanelQuiz extends JavaPlugin {
	private static PanelQuiz INSTANCE;
	public Quiz processingQuiz;
	public Config config;

	public static PanelQuiz getInstance() {
		return INSTANCE;
	}

	@Override
	public void onEnable() {
		INSTANCE = this;

		this.config = new Config(getDataFolder());
		this.config.load();

		this.getServer().getPluginManager().registerEvents(new PluginListener(), this);
		this.getCommand("panelquiz").setExecutor(new panelQuizCommand());
		this.getCommand("startquiz").setExecutor(new StartQuizCommand());
		this.getCommand("answerquestion").setExecutor(new AnswerQuestionCommand());
		this.getCommand("lockquiz").setExecutor(new LockQuizCommand());
		this.getCommand("showquizinfo").setExecutor(new ShowQuizInfoCommand());
		this.getCommand("judgeanswer").setExecutor(new JudgeAnswersCommand());
		this.getCommand("announcequiz").setExecutor(new AnnounceQuizCommand());
		this.getCommand("cancelquiz").setExecutor(new CancelQuizCommand());
		this.getCommand("reloadpanelquizplugin").setExecutor(new ReloadCommand());
		this.getCommand("openanswers").setExecutor(new OpenAnswersCommand());
	}

	@Override
	public void onDisable() {
		Bukkit.getWorlds().forEach((World world) -> {
			world.getEntitiesByClasses(TextDisplay.class).forEach((Entity textDisplay) -> {
				if (!textDisplay.hasMetadata("panelQuiz.index")) {
					return;
				}
				textDisplay.remove();
			});
		});
	}

	public void startQuiz(Quiz quizInstance) {
		this.processingQuiz = quizInstance;
	}

	public void cancelQuiz() {
		this.processingQuiz = null;
	}

	public boolean isQuizProcessing() {
		if (Objects.isNull(this.processingQuiz)) {
			return false;
		}
		if (this.processingQuiz.state == State.OPENED) {
			return false;
		}
		return true;
	}
}
