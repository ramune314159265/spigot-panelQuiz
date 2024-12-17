package ramune314159265.panelQuiz.commands;

import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ramune314159265.panelQuiz.PanelQuiz;
import ramune314159265.panelQuiz.State;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class AnswerQuestionCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "プレイヤーからのみ実行可能です");
			return true;
		}
		if (!PanelQuiz.getInstance().isQuizProcessing()) {
			sender.sendMessage(ChatColor.RED + "現在クイズが進行していません");
			return true;
		}
		if (PanelQuiz.getInstance().processingQuiz.state == State.LOCKED) {
			sender.sendMessage(ChatColor.RED + "現在回答がロックされています");
			return true;
		}
		if (args.length != 1) {
			return false;
		}

		PanelQuiz.getInstance().processingQuiz.handleAnswerCommand(sender, Arrays.asList(args));
		return true;
	}
}
