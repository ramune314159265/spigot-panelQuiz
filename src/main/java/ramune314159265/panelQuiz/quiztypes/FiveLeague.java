package ramune314159265.panelQuiz.quiztypes;

import ramune314159265.panelQuiz.State;

public class FiveLeague extends Quiz {
	public FiveLeague(String question, String quizColumn) {
		this.question = question;
		this.quizColumn = quizColumn;
		this.state = State.ANSWERING;
		this.maximumIndex = 0;
	}

	@Override
	public boolean answerQuestion(Integer index, String answererName, String answer) {
		if (answererName.length() != 1) {
			return false;
		}
		this.answers.put(index, answer);
		this.maximumIndex = Math.max(index, this.maximumIndex);
		return true;
	}

	@Override
	public boolean isAnswerable() {
		return this.state == State.ANSWERING;
	}
}
