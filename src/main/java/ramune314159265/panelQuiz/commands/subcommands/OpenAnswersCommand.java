package ramune314159265.panelQuiz.commands.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import ramune314159265.panelQuiz.AnswerData;
import ramune314159265.panelQuiz.PanelDisplay;
import ramune314159265.panelQuiz.PanelQuiz;
import ramune314159265.panelQuiz.State;

import java.util.List;
import java.util.Objects;

public class OpenAnswersCommand extends SubCommand {
	@Override
	public String getName() {
		return "open";
	}

	@Override
	public void onCommand(CommandSender sender, List<String> args) {
		if (!PanelQuiz.getInstance().isQuizProcessing()) {
			sender.sendMessage(ChatColor.RED + "現在クイズが進行していません");
			return;
		}
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
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, List<String> args) {
		return null;
	}

	@Override
	public boolean isAvailable() {
		return PanelQuiz.getInstance().isQuizProcessing();
	}
}
