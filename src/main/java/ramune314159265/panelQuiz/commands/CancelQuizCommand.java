package ramune314159265.panelQuiz.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import ramune314159265.panelQuiz.PanelQuiz;

public class CancelQuizCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!PanelQuiz.getInstance().isQuizProcessing()) {
			sender.sendMessage(ChatColor.RED + "現在クイズが進行していません");
			return true;
		}
		sender.sendMessage("クイズ「" + PanelQuiz.getInstance().processingQuiz.question + ChatColor.RESET + "」を中止しました");
		PanelQuiz.getInstance().cancelQuiz();
		return true;
	}
}
