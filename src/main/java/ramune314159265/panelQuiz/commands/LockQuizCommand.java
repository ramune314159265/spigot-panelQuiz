package ramune314159265.panelQuiz.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import ramune314159265.panelQuiz.PanelQuiz;
import ramune314159265.panelQuiz.State;

public class LockQuizCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!PanelQuiz.getInstance().isQuizProcessing()) {
			sender.sendMessage(ChatColor.RED + "現在クイズが進行していません");
			return true;
		}
		if (!PanelQuiz.getInstance().processingQuiz.isAnswerable()) {
			sender.sendMessage(ChatColor.RED + "すでにクイズに回答できません");
			return true;
		}

		PanelQuiz.getInstance().processingQuiz.state = State.LOCKED;
		sender.sendMessage("ロックしました");
		return true;
	}
}
