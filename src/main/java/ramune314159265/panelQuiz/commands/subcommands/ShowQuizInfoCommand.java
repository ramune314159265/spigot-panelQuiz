package ramune314159265.panelQuiz.commands.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import ramune314159265.panelQuiz.AnswerData;
import ramune314159265.panelQuiz.PanelQuiz;

import java.util.List;
import java.util.Objects;

public class ShowQuizInfoCommand extends SubCommand {
	@Override
	public String getName() {
		return "info";
	}

	@Override
	public void onCommand(CommandSender sender, List<String> args) {
		if (!PanelQuiz.getInstance().isQuizProcessing()) {
			sender.sendMessage(ChatColor.RED + "現在クイズが進行していません");
			return;
		}
		sender.sendMessage(
				"----------進行中のクイズ情報----------",
				"クイズ名: " + PanelQuiz.getInstance().processingQuiz.question,
				"クイズ入力枠: " + PanelQuiz.getInstance().processingQuiz.quizColumn,
				"状態: " + PanelQuiz.getInstance().processingQuiz.state,
				"回答状況: "
		);
		for (int i = 0; i <= PanelQuiz.getInstance().processingQuiz.maximumIndex; i++) {
			AnswerData answerData = PanelQuiz.getInstance().processingQuiz.answers.get(i);
			if (Objects.isNull(answerData)) {
				sender.sendMessage(" " + i + ": " + ChatColor.GRAY + "回答なし");
				continue;
			}
			sender.sendMessage(" " + i + ": " + (answerData.isCorrect ?
							ChatColor.RED + ChatColor.BOLD.toString() :
							ChatColor.AQUA
					) + answerData.content + ChatColor.RESET + " (" + answerData.answererName + ")"
			);
		}
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
