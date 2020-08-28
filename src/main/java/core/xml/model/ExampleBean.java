package core.xml.model;

import java.beans.ConstructorProperties;

import org.springframework.beans.factory.InitializingBean;

public class ExampleBean implements InitializingBean{
	
	private String myString = "123";
	
	private int years;

	private String ultimateAnswer;

	@ConstructorProperties({ "years", "ultimateAnswer" })
	public ExampleBean(int years, String ultimateAnswer) {
		this.years = years;
		this.ultimateAnswer = ultimateAnswer;
		System.out.println("year = " + years + ", ultimateAnswer = "
				+ ultimateAnswer);
	}

	public void setBeanOne(AnotherBean anotherBean) {
		System.out.println(anotherBean);
	}

	public int getYears() {
		return years;
	}

	public void setYears(int years) {
		this.years = years;
	}

	public String getUltimateAnswer() {
		return ultimateAnswer;
	}

	public void setUltimateAnswer(String ultimateAnswer) {
		this.ultimateAnswer = ultimateAnswer;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (myString == null || myString.isEmpty()) {
			throw new IllegalStateException("My string must have text.");
		}
	}
}
