package ramune314159265.panelQuiz.commands.subcommands;

import org.bukkit.command.CommandSender;

import java.util.List;

abstract public class SubCommand {
	public abstract String getName();

	public abstract void onCommand(CommandSender sender, List<String> args);

	public abstract List<String> onTabComplete(CommandSender sender, List<String> args);

	public abstract boolean isAvailable();
}
