package ramune314159265.panelQuiz.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import ramune314159265.panelQuiz.commands.subcommands.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class PanelQuizCommand implements CommandExecutor, TabCompleter {
	static SubCommand[] commands = {
			new AnnounceQuizCommand(),
			new AnswerQuestionAsCommand(),
			new CancelQuizCommand(),
			new JudgeAnswersCommand(),
			new LockQuizCommand(),
			new OpenAnswersCommand(),
			new ReloadCommand(),
			new ShowQuizInfoCommand(),
			new StartQuizCommand(),
			new UnlockQuizCommand(),
	};

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		List<String> argsArray = Arrays.asList(args);
		if (argsArray.isEmpty()) {
			return false;
		}

		String subCommandName = argsArray.get(0);

		Optional<SubCommand> subCommandOptional = Arrays.stream(PanelQuizCommand.commands)
				.filter(c -> c.getName().equals(subCommandName))
				.findFirst();
		if (subCommandOptional.isEmpty()) {
			sender.sendMessage(ChatColor.RED + "サブコマンド: " + String.join(", ", Arrays.stream(PanelQuizCommand.commands).map(SubCommand::getName).toList()));
			return true;
		}

		subCommandOptional.get().onCommand(sender, argsArray);
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (!command.getName().equalsIgnoreCase("panelquiz")) {
			return null;
		}
		if (args.length == 1) {
			return Arrays.stream(PanelQuizCommand.commands)
					.filter(SubCommand::isAvailable)
					.map(SubCommand::getName).toList();
		}
		if (2 <= args.length) {
			return Arrays.stream(PanelQuizCommand.commands)
					.filter(c -> c.getName().equals(args[0]))
					.findFirst()
					.map(subCommand -> subCommand.onTabComplete(sender, Arrays.asList(args)))
					.orElse(List.of());
		}
		return null;
	}
}
