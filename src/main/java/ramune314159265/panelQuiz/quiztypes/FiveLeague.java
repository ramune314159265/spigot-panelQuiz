package ramune314159265.panelQuiz.quiztypes;

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

public class FiveLeague extends Quiz {
	public FiveLeague(String question, String quizColumn) {
		super(question, quizColumn);
	}

	@Override
	public boolean isValidAnswer(String answer) {
		return answer.length() == 1;
	}

	@Override
	public void handleAnnounceCommand(CommandSender sender, List<String> args) {
		String preTitle = args.get(1);

		for (Player p : PanelQuiz.getInstance().getServer().getOnlinePlayers()) {
			p.sendTitle(ChatColor.RED + preTitle, ChatColor.GRAY + "ファイブリーグ", (int) (0.5 * 20), 3 * 20, 0);
		}

		ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
		exec.schedule(() -> {
			for (Player p : PanelQuiz.getInstance().getServer().getOnlinePlayers()) {
				p.sendMessage(ChatColor.GREEN + "Q. " + ChatColor.BOLD + PanelQuiz.getInstance().processingQuiz.question + "\n" + ChatColor.RESET +
						" ".repeat(50) + ChatColor.AQUA + "A. " + PanelQuiz.getInstance().processingQuiz.quizColumn);
				this.questionBossBar.addPlayer(p);
			}
			exec.shutdown();
		}, 3, TimeUnit.SECONDS);
	}

	@Override
	public void handleOpenCommand(CommandSender sender, List<String> args) {
		for (Player p : PanelQuiz.getInstance().getServer().getOnlinePlayers()) {
			this.questionBossBar.removePlayer(p);
		}

		for (Player p : PanelQuiz.getInstance().getServer().getOnlinePlayers()) {
			p.sendTitle(ChatColor.GREEN + "正解発表", "", 40, 60, 20);
		}

		BukkitScheduler exec = Bukkit.getScheduler();
		exec.runTaskLater(PanelQuiz.getInstance(), () -> {
			AtomicInteger i = new AtomicInteger(0);
			List<String> showAnswerStrings = new ArrayList<>();

			BukkitScheduler openExec = Bukkit.getScheduler();
			openExec.runTaskTimer(PanelQuiz.getInstance(), (BukkitTask task) -> {
				if (this.maximumIndex < i.get()) {
					Bukkit.broadcastMessage(ChatColor.GREEN + "問題: " + ChatColor.BOLD + PanelQuiz.getInstance().processingQuiz.question + "\n" + ChatColor.RESET +
							ChatColor.GREEN + "結果: " + String.join("", showAnswerStrings));
					openExec.cancelTask(task.getTaskId());
					return;
				}

				AnswerData answerData = this.answers.get(i.get());
				if (Objects.isNull(answerData)) {
					showAnswerStrings.add(ChatColor.GRAY + "空");
					for (Player p : PanelQuiz.getInstance().getServer().getOnlinePlayers()) {
						p.sendTitle(String.join("", showAnswerStrings), "", 0, 200, 20);
					}
					i.getAndIncrement();
					return;
				}

				showAnswerStrings.add((answerData.isCorrect ? ChatColor.RED : ChatColor.AQUA) + answerData.content);
				for (Player p : PanelQuiz.getInstance().getServer().getOnlinePlayers()) {
					p.sendTitle(String.join("", showAnswerStrings), "", 0, 200, 0);
				}

				PanelDisplay panelDisplay = PanelDisplay.list.get(i.get());
				if (Objects.isNull(panelDisplay)) {
					i.getAndIncrement();
					return;
				}
				panelDisplay.setPanelText(answerData.content);
				panelDisplay.fillPanelBlock(answerData.isCorrect ? Material.RED_CONCRETE : Material.BLUE_CONCRETE);

				i.getAndIncrement();
			}, 0, 20);
		}, 100);

		PanelQuiz.getInstance().processingQuiz.state = State.OPENED;
	}
}
