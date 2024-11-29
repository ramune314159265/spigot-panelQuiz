package ramune314159265.panelQuiz;

public class AnswerData {
	String content;
	String answererName;
	Boolean isCorrect;

	public AnswerData(String content, String answererName) {
		this.content = content;
		this.answererName = answererName;
		this.isCorrect = false;
	}

	public void setIsCorrect(Boolean isCorrect) {
		this.isCorrect = isCorrect;
	}
}
