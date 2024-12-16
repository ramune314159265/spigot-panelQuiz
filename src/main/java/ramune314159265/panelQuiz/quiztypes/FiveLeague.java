package ramune314159265.panelQuiz.quiztypes;

import ramune314159265.panelQuiz.AnswerData;

public class FiveLeague extends Quiz {
	public FiveLeague(String question, String quizColumn) {
		super(question, quizColumn);
	}

	@Override
	public boolean answerQuestion(Integer index, String answererName, String answer) {
		if (answer.length() != 1) {
			return false;
		}
		this.answers.put(index, new AnswerData(answer, answererName));
		this.maximumIndex = Math.max(index, this.maximumIndex);
		return true;
	}
}
