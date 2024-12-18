package ramune314159265.panelQuiz.quiztypes;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ramune314159265.panelQuiz.AnswerData;
import ramune314159265.panelQuiz.PanelQuiz;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FiveLeague extends Quiz {
	public FiveLeague(String question, String quizColumn) {
		super(question, quizColumn);
	}

	@Override
	public boolean isValidAnswer(String answer) {
		return answer.length() == 1;
	}

	@Override
	public void handleAnnounceCommand(CommandSender sender, List<String> args){
		String preTitle = args.get(1);

		for (Player p : PanelQuiz.getInstance().getServer().getOnlinePlayers()) {
			p.sendTitle(ChatColor.RED + preTitle,  ChatColor.GRAY+ "ファイブリーグ", (int) (0.5 * 20), 3 * 20, 0);
		}

		ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
		exec.schedule(() -> {
			for (Player p : PanelQuiz.getInstance().getServer().getOnlinePlayers()) {
				p.sendMessage(ChatColor.GREEN + "Q. " + ChatColor.BOLD + PanelQuiz.getInstance().processingQuiz.question + "\n" + ChatColor.RESET +
						" ".repeat(50) + ChatColor.AQUA + "A. " + PanelQuiz.getInstance().processingQuiz.quizColumn);
			}
			exec.shutdown();
		}, 3, TimeUnit.SECONDS);
	}
}
