package ramune314159265.panelQuiz.quiztypes;

import org.bukkit.command.CommandExecutor;
import ramune314159265.panelQuiz.AnswerData;
import ramune314159265.panelQuiz.State;

import java.util.HashMap;
import java.util.Map;

public class Quiz {
	public Map<Integer, AnswerData> answers;
	public String question;
	public String quizColumn;
	public State state;
	public Integer maximumIndex;

	public Quiz(String question, String quizColumn) {
		this.answers = new HashMap<>();
		this.question = question;
		this.quizColumn = quizColumn;
		this.state = State.ANSWERING;
		this.maximumIndex = 0;
	}

	public boolean answerQuestion(Integer index, String answererName, String answer) {
		this.answers.put(index, new AnswerData(answer, answererName));
		this.maximumIndex = Math.max(index, this.maximumIndex);
		return true;
	}

	public boolean isAnswerable() {
		return this.state == State.ANSWERING;
	}

	public void setIsCorrect(Integer index, Boolean isCorrect) {
		if (!this.answers.containsKey(index)) {
			return;
		}
		this.answers.get(index).setIsCorrect(isCorrect);
	}

	//public boolean handleStartCommand(CommandExecutor sender, String[] args){
//
	//}
}
