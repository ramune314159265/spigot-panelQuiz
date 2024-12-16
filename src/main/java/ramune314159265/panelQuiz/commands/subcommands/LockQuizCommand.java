package ramune314159265.panelQuiz.commands.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import ramune314159265.panelQuiz.PanelQuiz;
import ramune314159265.panelQuiz.State;

import java.util.List;

public class LockQuizCommand extends SubCommand {
	@Override
	public String getName() {
		return "lock";
	}

	@Override
	public void onCommand(CommandSender sender, List<String> args) {
		if (!PanelQuiz.getInstance().isQuizProcessing()) {
			sender.sendMessage(ChatColor.RED + "現在クイズが進行していません");
			return;
		}
		if (!PanelQuiz.getInstance().processingQuiz.isAnswerable()) {
			sender.sendMessage(ChatColor.RED + "すでにクイズに回答できません");
			return;
		}

		PanelQuiz.getInstance().processingQuiz.state = State.LOCKED;
		sender.sendMessage("ロックしました");
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, List<String> args) {
		return null;
	}

	@Override
	public boolean isAvailable() {
		return PanelQuiz.getInstance().isQuizProcessing() && PanelQuiz.getInstance().processingQuiz.isAnswerable();
	}
}
