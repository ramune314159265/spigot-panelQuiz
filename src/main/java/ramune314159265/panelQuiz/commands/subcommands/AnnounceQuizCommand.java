package ramune314159265.panelQuiz.commands.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ramune314159265.panelQuiz.PanelQuiz;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AnnounceQuizCommand extends SubCommand {
	@Override
	public String getName() {
		return "announce";
	}

	@Override
	public void onCommand(CommandSender sender, List<String> args) {
		if (args.size() != 2) {
			return;
		}
		if (!PanelQuiz.getInstance().isQuizProcessing()) {
			sender.sendMessage(ChatColor.RED + "現在クイズが進行していません");
			return;
		}

		String preTitle = args.get(1);
		for (Player p : PanelQuiz.getInstance().getServer().getOnlinePlayers()) {
			p.sendTitle(preTitle, "", (int) (0.5 * 20), 3 * 20, (int) (0.5 * 20));
		}

		ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
		exec.schedule(() -> {
			for (Player p : PanelQuiz.getInstance().getServer().getOnlinePlayers()) {
				p.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + PanelQuiz.getInstance().processingQuiz.question + "\n" + ChatColor.RESET + PanelQuiz.getInstance().processingQuiz.quizColumn);
			}
			exec.shutdown();
		}, 3, TimeUnit.SECONDS);
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, List<String> args) {
		return null;
	}

	@Override
	public boolean isAvailable() {
		return PanelQuiz.getInstance().isQuizProcessing();
	}
}
