package ramune314159265.panelQuiz.commands.subcommands;

import org.bukkit.command.CommandSender;
import ramune314159265.panelQuiz.PanelQuiz;

import java.util.List;

public class ReloadCommand extends SubCommand {
	@Override
	public String getName() {
		return "reload";
	}

	@Override
	public void onCommand(CommandSender sender, List<String> args) {
		PanelQuiz.getInstance().config.load();
		sender.sendMessage("設定ファイルを再読み込みしました");
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, List<String> args) {
		return null;
	}

	@Override
	public boolean isAvailable() {
		return true;
	}
}
