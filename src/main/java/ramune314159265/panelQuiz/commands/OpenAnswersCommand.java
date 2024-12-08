package ramune314159265.panelQuiz.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import ramune314159265.panelQuiz.AnswerData;
import ramune314159265.panelQuiz.PanelDisplay;
import ramune314159265.panelQuiz.PanelQuiz;
import ramune314159265.panelQuiz.State;

import java.util.Objects;

public class OpenAnswersCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		for (int i = 0; i <= PanelQuiz.getInstance().processingQuiz.maximumIndex; i++) {
			AnswerData answerData = PanelQuiz.getInstance().processingQuiz.answers.get(i);
			if (Objects.isNull(answerData)) {
				continue;
			}
			PanelDisplay panelDisplay = PanelDisplay.list.get(i);
			panelDisplay.setPanelText(answerData.content);
			panelDisplay.fillPanelBlock(answerData.isCorrect ? Material.RED_CONCRETE : Material.BLUE_CONCRETE);
		}

		PanelQuiz.getInstance().processingQuiz.state = State.OPENED;

		return true;
	}
}
