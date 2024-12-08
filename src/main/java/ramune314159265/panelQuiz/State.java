package ramune314159265.panelQuiz;

import org.bukkit.ChatColor;

public enum State {
	ANSWERING(ChatColor.GREEN + "回答受付中"),
	LOCKED(ChatColor.RED + "回答ロック中"),
	OPENED(ChatColor.LIGHT_PURPLE + "正誤発表後");

	final String string;

	State(String string) {
		this.string = string;
	}

	public String toString() {
		return this.string;
	}
}
