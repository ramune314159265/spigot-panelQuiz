package ramune314159265.panelQuiz.commands.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import ramune314159265.panelQuiz.PanelDisplay;
import ramune314159265.panelQuiz.PanelQuiz;
import ramune314159265.panelQuiz.quiztypes.AllMatch;
import ramune314159265.panelQuiz.quiztypes.FiveLeague;
import ramune314159265.panelQuiz.quiztypes.Quiz;

import java.util.List;

public class StartQuizCommand extends SubCommand {
	@Override
	public String getName() {
		return "start";
	}

	@Override
	public void onCommand(CommandSender sender, List<String> args) {
		if (args.size() != 4) {
			sender.sendMessage(ChatColor.RED + "/panelquiz start <type> <title> <column>");
			return;
		}
		if (PanelQuiz.getInstance().isQuizProcessing()) {
			sender.sendMessage(ChatColor.RED + "クイズ「" + PanelQuiz.getInstance().processingQuiz.question + "」が進行中です");
			return;
		}

		PanelDisplay.list.forEach((Integer index, PanelDisplay panelDisplay) -> {
			panelDisplay.fillPanelBlock(Material.WHITE_CONCRETE);
			panelDisplay.setPanelText("");
		});

		String quizType = args.get(1);
		String quizTitle = args.get(2);
		String quizColumn = args.get(3);

		switch (quizType) {
			case "fiveleague": {
				Quiz quizInstance = new FiveLeague(quizTitle, quizColumn);
				PanelQuiz.getInstance().startQuiz(quizInstance);
				break;
			}
			case "allmatch": {
				Quiz quizInstance = new AllMatch(quizTitle);
				PanelQuiz.getInstance().startQuiz(quizInstance);
				break;
			}
			default:
				sender.sendMessage(ChatColor.RED + "存在しないクイズタイプです");
				return;
		}
		sender.sendMessage("クイズ「" + quizTitle + ChatColor.RESET + "」を開始しました");
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, List<String> args) {
		if (args.size() == 2) {
			return List.of("fiveleague", "allmatch");
		}
		return null;
	}

	@Override
	public boolean isAvailable() {
		return !PanelQuiz.getInstance().isQuizProcessing();
	}
}
