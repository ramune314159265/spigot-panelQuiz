package ramune314159265.panelQuiz.commands.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import ramune314159265.panelQuiz.PanelQuiz;

import java.util.List;

public class AnswerQuestionAsCommand extends SubCommand {
	@Override
	public String getName() {
		return "answeras";
	}

	@Override
	public void onCommand(CommandSender sender, List<String> args) {
		if (!PanelQuiz.getInstance().isQuizProcessing()) {
			sender.sendMessage(ChatColor.RED + "現在クイズが進行していません");
			return;
		}
		if (args.size() == 3) {
			sender.sendMessage(ChatColor.RED + "/panelquiz answeras <answererName> <index> <answer>");
			return;
		}

		String answererName = args.get(1);
		Integer index = Math.abs(Integer.parseInt(args.get(2)));
		String content = args.get(3);

		boolean isSucceed = PanelQuiz.getInstance().processingQuiz.answerQuestion(index, answererName, content);

		if (isSucceed) {
			sender.sendMessage(index + "番目に" + answererName + "として" + "「" + content + "」と回答しました");
		} else {
			sender.sendMessage(ChatColor.RED + "正しく入力されていません");
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
