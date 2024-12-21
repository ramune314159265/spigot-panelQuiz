package ramune314159265.panelQuiz.quiztypes;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import ramune314159265.panelQuiz.AnswerData;
import ramune314159265.panelQuiz.PanelDisplay;
import ramune314159265.panelQuiz.PanelQuiz;
import ramune314159265.panelQuiz.State;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class AllMatch extends Quiz {
	public boolean isAllMatched;

	public AllMatch(String question) {
		super(question, "");
		this.isAllMatched = false;
	}

	@Override
	public void handleAnnounceCommand(CommandSender sender, List<String> args) {
		String preTitle = args.get(1);

		for (Player p : PanelQuiz.getInstance().getServer().getOnlinePlayers()) {
			p.sendTitle(ChatColor.AQUA + preTitle, ChatColor.GRAY + "全員一致するまで終われまテン", (int) (0.5 * 20), 3 * 20, 0);
		}

		ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
		exec.schedule(() -> {
			for (Player p : PanelQuiz.getInstance().getServer().getOnlinePlayers()) {
				p.sendMessage(ChatColor.GREEN + "お題: " + ChatColor.BOLD + PanelQuiz.getInstance().processingQuiz.question);
				this.questionBossBar.addPlayer(p);
			}
			exec.shutdown();
		}, 3, TimeUnit.SECONDS);
	}

	@Override
	public void handleJudgeCommand(CommandSender sender, List<String> args) {
		if (args.size() == 3) {
			this.isAllMatched = Boolean.parseBoolean(args.get(2));
			for (int i = 0; i <= PanelQuiz.getInstance().processingQuiz.maximumIndex; i++) {
				AnswerData answerData = PanelQuiz.getInstance().processingQuiz.answers.get(i);
				if (Objects.isNull(answerData)) {
					continue;
				}
				answerData.setIsCorrect(Boolean.parseBoolean(args.get(2)));
			}
		}
		if (PanelQuiz.getInstance().processingQuiz.isAnswerable() && args.size() == 1) {
			BaseComponent[] warnMessageComponent = new ComponentBuilder("回答がロックされていません。回答をロックするには").color(net.md_5.bungee.api.ChatColor.YELLOW)
					.append(" /panelquiz lock ").color(net.md_5.bungee.api.ChatColor.YELLOW).underlined(true).event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/panelquiz lock"))
					.append("を実行してください。").underlined(false).color(net.md_5.bungee.api.ChatColor.YELLOW).create();
			sender.spigot().sendMessage(warnMessageComponent);
		}

		ComponentBuilder component = new ComponentBuilder("状態: ")
				.append("[一致]")
				.color(this.isAllMatched ? net.md_5.bungee.api.ChatColor.RED : net.md_5.bungee.api.ChatColor.GRAY)
				.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/panelquiz judge 0 true"))
				.append(" ")
				.reset()
				.append("[不一致]\n")
				.color(this.isAllMatched ? net.md_5.bungee.api.ChatColor.GRAY : net.md_5.bungee.api.ChatColor.AQUA)
				.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/panelquiz judge 0 false"));

		for (int i = 0; i <= PanelQuiz.getInstance().processingQuiz.maximumIndex; i++) {
			AnswerData answerData = PanelQuiz.getInstance().processingQuiz.answers.get(i);
			if (Objects.isNull(answerData)) {
				continue;
			}

			component.append(answerData.content)
					.reset()
					.color(answerData.isCorrect ? net.md_5.bungee.api.ChatColor.RED : net.md_5.bungee.api.ChatColor.AQUA)
					.bold(answerData.isCorrect)
					.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(String.valueOf(i)).append("番目\n")
							.append("内容: " + answerData.content + "\n")
							.append("回答者: " + answerData.answererName + "\n")
							.create()
					))
					.append(", ").color(net.md_5.bungee.api.ChatColor.WHITE).bold(false);
		}

		sender.spigot().sendMessage(component.create());
	}

	@Override
	public void handleOpenCommand(CommandSender sender, List<String> args) {
		for (Player p : PanelQuiz.getInstance().getServer().getOnlinePlayers()) {
			this.questionBossBar.removePlayer(p);
		}

		for (Player p : PanelQuiz.getInstance().getServer().getOnlinePlayers()) {
			p.sendTitle(ChatColor.AQUA + "結果発表", "", 40, 60, 20);
		}

		BukkitScheduler exec = Bukkit.getScheduler();
		exec.runTaskLater(PanelQuiz.getInstance(), () -> {
			AtomicInteger i = new AtomicInteger(0);
			List<String> answerStrings = new ArrayList<>();

			BukkitScheduler openExec = Bukkit.getScheduler();
			openExec.runTaskTimer(PanelQuiz.getInstance(), (BukkitTask task) -> {
				if (this.maximumIndex < i.get()) {
					Bukkit.broadcastMessage(ChatColor.GREEN + "お題: " + ChatColor.BOLD + PanelQuiz.getInstance().processingQuiz.question + "\n" + ChatColor.RESET +
							ChatColor.GREEN + "結果:" + "\n" + String.join("\n", answerStrings) + "\n\n" + ChatColor.RESET +
							(this.isAllMatched ? ChatColor.RED + "全員一致！" : ChatColor.AQUA + "一致ならず..."));
					openExec.cancelTask(task.getTaskId());
					return;
				}

				AnswerData answerData = this.answers.get(i.get());
				if (Objects.isNull(answerData)) {
					i.getAndIncrement();
					return;
				}

				answerStrings.add(" " + answerData.content + " (" + answerData.answererName + ")");
				for (Player p : PanelQuiz.getInstance().getServer().getOnlinePlayers()) {
					p.sendTitle(answerData.content, ChatColor.ITALIC + "- " + answerData.answererName, 20, 200, 0);
				}

				PanelDisplay panelDisplay = PanelDisplay.list.get(i.get());
				if (Objects.isNull(panelDisplay)) {
					i.getAndIncrement();
					return;
				}
				panelDisplay.setPanelText(answerData.content);
				panelDisplay.fillPanelBlock(answerData.isCorrect ? Material.RED_CONCRETE : Material.BLUE_CONCRETE);

				i.getAndIncrement();
			}, 0, 60);
		}, 100);

		PanelQuiz.getInstance().processingQuiz.state = State.OPENED;
	}
}
