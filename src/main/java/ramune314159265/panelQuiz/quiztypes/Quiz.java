package ramune314159265.panelQuiz.quiztypes;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ramune314159265.panelQuiz.AnswerData;
import ramune314159265.panelQuiz.PanelDisplay;
import ramune314159265.panelQuiz.PanelQuiz;
import ramune314159265.panelQuiz.State;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Quiz {
	public Map<Integer, AnswerData> answers;
	public String question;
	public String quizColumn;
	public State state;
	public Integer maximumIndex;
	public BossBar questionBossBar;

	public Quiz(String question, String quizColumn) {
		this.answers = new HashMap<>();
		this.question = question;
		this.quizColumn = quizColumn;
		this.state = State.ANSWERING;
		this.maximumIndex = 0;
		this.questionBossBar = Bukkit.createBossBar(
				ChatColor.GREEN.toString() + ChatColor.BOLD + this.question,
				BarColor.RED,
				BarStyle.SOLID
		);
		this.questionBossBar.setProgress(1);
	}

	public final boolean isAnswerable() {
		return this.state == State.ANSWERING;
	}

	public final void setIsCorrect(Integer index, Boolean isCorrect) {
		if (!this.answers.containsKey(index)) {
			return;
		}
		this.answers.get(index).setIsCorrect(isCorrect);
	}

	public final void answerQuestion(Integer index, String answererName, String answer) {
		this.answers.put(index, new AnswerData(answer, answererName));
		this.maximumIndex = Math.max(index, this.maximumIndex);
	}

	public boolean isValidAnswer(String answer) {
		return true;
	}

	public void handleAnnounceCommand(CommandSender sender, List<String> args) {
		String preTitle = args.get(1);

		for (Player p : PanelQuiz.getInstance().getServer().getOnlinePlayers()) {
			p.sendTitle(preTitle, "", (int) (0.5 * 20), 3 * 20, (int) (0.5 * 20));
		}

		ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
		exec.schedule(() -> {
			for (Player p : PanelQuiz.getInstance().getServer().getOnlinePlayers()) {
				p.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + PanelQuiz.getInstance().processingQuiz.question + "\n" + ChatColor.RESET + PanelQuiz.getInstance().processingQuiz.quizColumn);
				this.questionBossBar.addPlayer(p);
			}
			exec.shutdown();
		}, 3, TimeUnit.SECONDS);
	}

	public void handleJudgeCommand(CommandSender sender, List<String> args) {
		if (args.size() == 3) {
			try {
				int index = Math.abs(Integer.parseInt(args.get(1)));
				PanelQuiz.getInstance().processingQuiz.setIsCorrect(index, Boolean.valueOf(args.get(2)));
			} catch (NumberFormatException e) {
				sender.sendMessage(net.md_5.bungee.api.ChatColor.RED + "無効な数値です");
			}
		}
		if (PanelQuiz.getInstance().processingQuiz.isAnswerable() && args.size() == 1) {
			BaseComponent[] warnMessageComponent = new ComponentBuilder("回答がロックされていません。回答をロックするには").color(net.md_5.bungee.api.ChatColor.YELLOW)
					.append(" /panelquiz lock ").color(net.md_5.bungee.api.ChatColor.YELLOW).underlined(true).event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/panelquiz lock"))
					.append("を実行してください。").underlined(false).color(net.md_5.bungee.api.ChatColor.YELLOW).create();
			sender.spigot().sendMessage(warnMessageComponent);
		}

		ComponentBuilder component = new ComponentBuilder("クリックして正解不正解を変更\n");

		for (int i = 0; i <= PanelQuiz.getInstance().processingQuiz.maximumIndex; i++) {
			AnswerData answerData = PanelQuiz.getInstance().processingQuiz.answers.get(i);
			if (Objects.isNull(answerData)) {
				component.append("空")
						.color(net.md_5.bungee.api.ChatColor.GRAY)
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
					.color(answerData.isCorrect ? net.md_5.bungee.api.ChatColor.RED : net.md_5.bungee.api.ChatColor.AQUA)
					.bold(answerData.isCorrect)
					.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(String.valueOf(i)).append("番目\n")
							.append("内容: " + answerData.content + "\n")
							.append("回答者: " + answerData.answererName + "\n")
							.append("現在の状態: " + (answerData.isCorrect ? "正解" : "不正解") + "\n")
							.append("クリックして" + (answerData.isCorrect ? "不正解" : "正解") + "に変更")
							.create()
					))
					.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/panelquiz judge " + i + " " + (answerData.isCorrect ? "false" : "true")))
					.append(", ").color(net.md_5.bungee.api.ChatColor.WHITE).bold(false);
		}

		sender.spigot().sendMessage(component.create());
	}

	public void handleOpenCommand(CommandSender sender, List<String> args) {
		for (int i = 0; i <= PanelQuiz.getInstance().processingQuiz.maximumIndex; i++) {
			AnswerData answerData = PanelQuiz.getInstance().processingQuiz.answers.get(i);
			if (Objects.isNull(answerData)) {
				continue;
			}
			PanelDisplay panelDisplay = PanelDisplay.list.get(i);
			if (Objects.isNull(panelDisplay)) {
				continue;
			}
			panelDisplay.setPanelText(answerData.content);
			panelDisplay.fillPanelBlock(answerData.isCorrect ? Material.RED_CONCRETE : Material.BLUE_CONCRETE);
		}

		for (Player p : PanelQuiz.getInstance().getServer().getOnlinePlayers()) {
			this.questionBossBar.removePlayer(p);
		}

		PanelQuiz.getInstance().processingQuiz.state = State.OPENED;
	}

	public void handleAnswerCommand(CommandSender sender, List<String> args) {
		try {
			int index = Math.abs(Integer.parseInt(args.get(0)));
			String defaultMessage = index + "番目の答え";
			new AnvilGUI.Builder()
					.onClick((slot, stateSnapshot) -> {
						if (slot != AnvilGUI.Slot.OUTPUT) {
							return Collections.emptyList();
						}
						if (Objects.equals(stateSnapshot.getText(), defaultMessage)) {
							return Collections.emptyList();
						}

						if (!PanelQuiz.getInstance().processingQuiz.isValidAnswer(stateSnapshot.getText())) {
							sender.sendMessage(ChatColor.RED + "正しく入力されていません");
							return List.of(AnvilGUI.ResponseAction.close());
						}

						PanelQuiz.getInstance().processingQuiz.answerQuestion(index, sender.getName(), stateSnapshot.getText());
						sender.sendMessage(ChatColor.GREEN + "「" + stateSnapshot.getText() + "」と回答しました");

						return List.of(AnvilGUI.ResponseAction.close());
					})
					.text(defaultMessage)
					.title(PanelQuiz.getInstance().processingQuiz.question)
					.plugin(PanelQuiz.getInstance())
					.open((Player) sender);
		} catch (NumberFormatException e) {
			sender.sendMessage(ChatColor.RED + "無効な数値です");
		}
	}
}
