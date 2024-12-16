package ramune314159265.panelQuiz.commands.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import ramune314159265.panelQuiz.PanelQuiz;

import java.util.List;

public class CancelQuizCommand extends SubCommand {
	@Override
	public String getName() {
		return "cancel";
	}

	@Override
	public void onCommand(CommandSender sender, List<String> args) {
		if (!PanelQuiz.getInstance().isQuizProcessing()) {
			sender.sendMessage(ChatColor.RED + "現在クイズが進行していません");
			return;
		}
		sender.sendMessage("クイズ「" + PanelQuiz.getInstance().processingQuiz.question + ChatColor.RESET + "」を中止しました");
		PanelQuiz.getInstance().cancelQuiz();
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
