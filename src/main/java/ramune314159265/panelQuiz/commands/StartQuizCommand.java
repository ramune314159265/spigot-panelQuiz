package ramune314159265.panelQuiz.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import ramune314159265.panelQuiz.PanelDisplay;
import ramune314159265.panelQuiz.PanelQuiz;
import ramune314159265.panelQuiz.quiztypes.FiveLeague;
import ramune314159265.panelQuiz.quiztypes.Quiz;

import java.util.Arrays;
import java.util.List;

public class StartQuizCommand implements CommandExecutor, TabCompleter {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length != 3) {
			return false;
		}
		if (PanelQuiz.getInstance().isQuizProcessing()) {
			sender.sendMessage(ChatColor.RED + "クイズ「" + PanelQuiz.getInstance().processingQuiz.question + "」が進行中です");
			return true;
		}

		PanelDisplay.list.forEach((Integer index, PanelDisplay panelDisplay) -> {
			panelDisplay.fillPanelBlock(Material.WHITE_CONCRETE);
			panelDisplay.setPanelText("");
		});

		String quizType = args[0];
		String quizTitle = args[1];
		String quizColumn = args[2];

		switch (quizType) {
			case "fiveleague":
				Quiz quizInstance = new FiveLeague(quizTitle, quizColumn);
				PanelQuiz.getInstance().startQuiz(quizInstance);
				break;
			default:
				return false;
		}
		sender.sendMessage("クイズ「" + quizTitle + ChatColor.RESET + "」を開始しました");
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (!command.getName().equalsIgnoreCase("startquiz")) {
			return null;
		}
		if (args.length == 1) {
			return Arrays.asList("fiveleague");
		}
		return null;
	}
}
