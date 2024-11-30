package ramune314159265.panelQuiz.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import ramune314159265.panelQuiz.AnswerData;
import ramune314159265.panelQuiz.PanelQuiz;

import java.util.Objects;

public class ShowQuizInfoCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!PanelQuiz.getInstance().isQuizProcessing()) {
			sender.sendMessage(ChatColor.RED + "現在クイズが進行していません");
			return true;
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
		return true;
	}
}
