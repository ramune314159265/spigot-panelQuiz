package ramune314159265.panelQuiz.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import ramune314159265.panelQuiz.PanelQuiz;

public class ReloadCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		PanelQuiz.getInstance().config.load();
		sender.sendMessage("設定ファイルを再読み込みしました");
		return true;
	}
}
