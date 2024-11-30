package ramune314159265.panelQuiz;

public class AnswerData {
	public String content;
	public String answererName;
	public Boolean isCorrect;

	public AnswerData(String content, String answererName) {
		this.content = content;
		this.answererName = answererName;
		this.isCorrect = false;
	}

	public void setIsCorrect(Boolean isCorrect) {
		this.isCorrect = isCorrect;
	}
}
