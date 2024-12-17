package ramune314159265.panelQuiz.commands.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import ramune314159265.panelQuiz.PanelQuiz;
import ramune314159265.panelQuiz.State;

import java.util.List;

public class UnlockQuizCommand extends SubCommand {
	@Override
	public String getName() {
		return "unlock";
	}

	@Override
	public void onCommand(CommandSender sender, List<String> args) {
		if (!PanelQuiz.getInstance().isQuizProcessing()) {
			sender.sendMessage(ChatColor.RED + "現在クイズが進行していません");
			return;
		}
		if (PanelQuiz.getInstance().processingQuiz.state != State.LOCKED) {
			sender.sendMessage(ChatColor.RED + "ロックされていません");
			return;
		}

		PanelQuiz.getInstance().processingQuiz.state = State.ANSWERING;
		sender.sendMessage("ロックを解除しました");
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, List<String> args) {
		return null;
	}

	@Override
	public boolean isAvailable() {
		return PanelQuiz.getInstance().isQuizProcessing() && PanelQuiz.getInstance().processingQuiz.state == State.LOCKED;
	}
}
