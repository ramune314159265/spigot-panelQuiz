package ramune314159265.panelQuiz.commands;

import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ramune314159265.panelQuiz.PanelQuiz;

import java.util.Arrays;
import java.util.Collections;
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
		if (!PanelQuiz.getInstance().processingQuiz.isAnswerable()) {
			sender.sendMessage(ChatColor.RED + "現在クイズに回答できません");
			return true;
		}
		if (args.length != 1) {
			return false;
		}
		try {
			int index = Math.abs(Integer.parseInt(args[0]));
			String defaultMessage = index + "番目の答え";
			new AnvilGUI.Builder()
					.onClick((slot, stateSnapshot) -> {
						if (slot != AnvilGUI.Slot.OUTPUT) {
							return Collections.emptyList();
						}
						if (Objects.equals(stateSnapshot.getText(), defaultMessage)) {
							return Collections.emptyList();
						}

						boolean isSucceeded = PanelQuiz.getInstance().processingQuiz.answerQuestion(index, sender.getName(), stateSnapshot.getText());
						if (isSucceeded) {
							sender.sendMessage(ChatColor.GREEN + "「" + stateSnapshot.getText() + "」と回答しました");
						} else {
							sender.sendMessage(ChatColor.RED + "正しく入力されていません");
						}
						return Arrays.asList(AnvilGUI.ResponseAction.close());
					})
					.text(defaultMessage)
					.title(PanelQuiz.getInstance().processingQuiz.question)
					.plugin(PanelQuiz.getInstance())
					.open((Player) sender);
			return true;
		} catch (NumberFormatException e) {
			sender.sendMessage(ChatColor.RED + "無効な数値です");
			return true;
		}
	}
}
