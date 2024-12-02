package ramune314159265.panelQuiz.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import ramune314159265.panelQuiz.AnswerData;
import ramune314159265.panelQuiz.PanelQuiz;
import ramune314159265.panelQuiz.State;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class JudgeAnswersCommand implements CommandExecutor, TabCompleter {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!PanelQuiz.getInstance().isQuizProcessing()) {
			sender.sendMessage(ChatColor.RED + "現在クイズが進行していません");
			return true;
		}
		if (PanelQuiz.getInstance().processingQuiz.state == State.JUDGED) {
			sender.sendMessage(ChatColor.RED + "すでにクイズは終了しています");
			return true;
		}
		if(args.length == 2){
			try{
				int index = Math.abs(Integer.parseInt(args[0]));
				PanelQuiz.getInstance().processingQuiz.setIsCorrect(index, Boolean.valueOf(args[1]));
			} catch (NumberFormatException e) {
				sender.sendMessage(ChatColor.RED + "無効な数値です");
			}
		}
		if (PanelQuiz.getInstance().processingQuiz.isAnswerable() && args.length == 0) {
			BaseComponent[] warnMessageComponent = new ComponentBuilder("回答がロックされていません。回答をロックするには").color(ChatColor.YELLOW)
					.append(" /lockquiz ").color(ChatColor.YELLOW).underlined(true).event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/lockquiz"))
					.append("を実行してください。").underlined(false).color(ChatColor.YELLOW).create();
			sender.spigot().sendMessage(warnMessageComponent);
		}

		ComponentBuilder component = new ComponentBuilder("クリックして正解不正解を変更\n");

		for (int i = 0; i <= PanelQuiz.getInstance().processingQuiz.maximumIndex; i++) {
			AnswerData answerData = PanelQuiz.getInstance().processingQuiz.answers.get(i);
			if (Objects.isNull(answerData)) {
				component.append("空")
						.color(ChatColor.GRAY)
						.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(String.valueOf(i)).append("番目\n")
								.append("内容: なし\n")
								.append("回答者: なし\n")
								.append("現在の状態: なし")
								.create()
						))
						.append(", ");
				continue;
			}

			component.append(answerData.content)
					.color(answerData.isCorrect ? ChatColor.RED : ChatColor.AQUA)
					.bold(answerData.isCorrect)
					.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(String.valueOf(i)).append("番目\n")
							.append("内容: " + answerData.content + "\n")
							.append("回答者: " + answerData.answererName + "\n")
							.append("現在の状態: " + (answerData.isCorrect ? "正解" : "不正解") + "\n")
							.append("クリックして" + (answerData.isCorrect ? "不正解" : "正解") + "に変更")
							.create()
					))
					.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/judgeanswer " + i + " " + (answerData.isCorrect ? "false" : "true")))
					.append(", ").color(ChatColor.WHITE).bold(false);
		}

		sender.spigot().sendMessage(component.create());
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (!command.getName().equalsIgnoreCase("judgeanswer")) {
			return null;
		}
		if (args.length == 1) {
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
		if(args.length == 2){
			return Arrays.asList("true", "false");
		}
		return null;
	}
}
