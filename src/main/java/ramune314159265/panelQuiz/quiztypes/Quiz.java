package ramune314159265.panelQuiz.quiztypes;

import ramune314159265.panelQuiz.State;

import java.util.Map;

abstract public class Quiz {
	public Map<Integer, String> answers;
	public String question;
	public String quizColumn;
	public State state;
	public Integer maximumIndex;

	abstract public boolean answerQuestion(Integer index, String answererName, String answer);

	abstract public boolean isAnswerable();
}
