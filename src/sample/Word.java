package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Word {
    private StringProperty english;
    private StringProperty korean;

    public Word() {
    }

    public Word(String english, String korean) {
        this.english = new SimpleStringProperty(english);
        this.korean = new SimpleStringProperty(korean);
    }

    public String getEnglish() {
        return english.get();
    }

    public StringProperty englishProperty() {
        return english;
    }

    public void setEnglish(String english) {
        this.english.set(english);
    }

    public String getKorean() {
        return korean.get();
    }

    public StringProperty koreanProperty() {
        return korean;
    }

    public void setKorean(String korean) {
        this.korean.set(korean);
    }
}
