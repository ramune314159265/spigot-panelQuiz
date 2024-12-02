package ramune314159265.panelQuiz.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ramune314159265.panelQuiz.PanelQuiz;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AnnounceQuizCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length != 1) {
			return false;
		}
		if (!PanelQuiz.getInstance().isQuizProcessing()) {
			sender.sendMessage(ChatColor.RED + "現在クイズが進行していません");
			return true;
		}

		String preTitle = args[0];
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
		return true;
	}
}
