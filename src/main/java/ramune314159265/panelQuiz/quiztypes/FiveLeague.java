package ramune314159265.panelQuiz.quiztypes;

import ramune314159265.panelQuiz.State;

public class FiveLeague extends Quiz {
	public FiveLeague(String question, String quizColumn) {
		this.question = question;
		this.quizColumn = quizColumn;
		this.state = State.ANSWERING;
	}

	@Override
	boolean anwerQuetion(Integer index, String answererName, String answer) {
		if (answererName.length() != 1) {
			return false;
		}
		this.answers.put(index, answer);
		return true;
	}
}
