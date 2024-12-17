package ramune314159265.panelQuiz.commands.subcommands;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import ramune314159265.panelQuiz.AnswerData;
import ramune314159265.panelQuiz.PanelQuiz;
import ramune314159265.panelQuiz.State;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class JudgeAnswersCommand extends SubCommand {
	@Override
	public String getName() {
		return "judge";
	}

	@Override
	public void onCommand(CommandSender sender, List<String> args) {
		if (!PanelQuiz.getInstance().isQuizProcessing()) {
			sender.sendMessage(ChatColor.RED + "現在クイズが進行していません");
			return;
		}
		if (PanelQuiz.getInstance().processingQuiz.state == State.OPENED) {
			sender.sendMessage(ChatColor.RED + "すでにクイズは終了しています");
			return;
		}

		PanelQuiz.getInstance().processingQuiz.handleJudgeCommand(sender, args);
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, List<String> args) {
		if (args.size() == 2) {
			if (!PanelQuiz.getInstance().isQuizProcessing()) {
				return null;
			}
			List<String> list = new ArrayList<>();
			for (int i = 0; i <= PanelQuiz.getInstance().processingQuiz.maximumIndex; i++) {
				AnswerData answerData = PanelQuiz.getInstance().processingQuiz.answers.get(i);
				if (!Objects.isNull(answerData)) {
					list.add(String.valueOf(i));
				}
			}
			return list;
		}
		if (args.size() == 3) {
			return Arrays.asList("true", "false");
		}
		return null;
	}

	@Override
	public boolean isAvailable() {
		return PanelQuiz.getInstance().isQuizProcessing();
	}
}
