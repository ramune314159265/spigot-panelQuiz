package ramune314159265.panelQuiz;

import org.bukkit.plugin.java.JavaPlugin;
import ramune314159265.panelQuiz.commands.AnswerQuestionCommand;
import ramune314159265.panelQuiz.commands.LockQuizCommand;
import ramune314159265.panelQuiz.commands.StartQuizCommand;
import ramune314159265.panelQuiz.quiztypes.Quiz;

import java.util.Objects;

public final class PanelQuiz extends JavaPlugin {
	private static PanelQuiz INSTANCE;
	public Quiz processingQuiz;

	public static PanelQuiz getInstance() {
		return INSTANCE;
	}

	@Override
	public void onEnable() {
		INSTANCE = this;
		this.getCommand("startquiz").setExecutor(new StartQuizCommand());
		this.getCommand("answerquestion").setExecutor(new AnswerQuestionCommand());
		this.getCommand("lockquiz").setExecutor(new LockQuizCommand());
	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}

	public void startQuiz(Quiz quizInstance) {
		this.processingQuiz = quizInstance;
	}

	public boolean isQuizProcessing() {
		if (Objects.isNull(this.processingQuiz)) {
			return false;
		}
		if (this.processingQuiz.state == State.JUDGED) {
			return false;
		}
		return true;
	}
}
