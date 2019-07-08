package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private ObservableList<Word> finalResult = FXCollections.observableArrayList();

    // 기본 생성자
    public Controller() throws IOException {
    }

    // FXML파일과 연결
    @FXML
    public TableView<Word> myTableView;
    @FXML
    public TableColumn<Word, String> englishCol;
    @FXML
    public TableColumn<Word, String> koreanCol;
    @FXML
    public TextField inputWord;
    @FXML
    public Label prevWord;
    @FXML
    public Button inputButton;
    @FXML
    public Label answerLabel;
    @FXML
    public Button addButton;
    @FXML
    public TextField englishAdd;
    @FXML
    public TextField koreanAdd;
    @FXML
    public TextField englishRemove;
    @FXML
    public Button removeButton;
    @FXML
    public Label managementLabel;
    @FXML
    public Label prevWordMean;

    // initialize 메소드, 웹 크롤링 및 단어 리스트 출력
    @FXML
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Document doc = null;
        try {
            doc = Jsoup.connect("https://quizlet.com/393334132/%EC%9B%8C%EB%93%9C%EB%A7%88%EC%8A%A4%ED%84%B0-%EC%A0%84%EC%B2%B4-flash-cards/").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements englishElements = doc.select("[class=TermText notranslate lang-en]");
        List<String> englishKeywords = englishElements.eachText();

        Elements koreanElements = doc.select("[class=TermText notranslate lang-ko]");
        List<String> koreanKeywords = koreanElements.eachText();

        myTableView.setItems(finalResult);

        for (int i = 0; i < koreanKeywords.size(); i++) {
            finalResult.add(new Word(englishKeywords.get(i), koreanKeywords.get(i)));
        }

        englishCol.setCellValueFactory(cellData -> cellData.getValue().englishProperty());
        koreanCol.setCellValueFactory(cellData -> cellData.getValue().koreanProperty());
    }

    // 끝말잇기 부분
    public void textInput(ActionEvent event) {
        boolean a = false;
        String result = inputWord.getText();
        String prev = prevWord.getText().substring(prevWord.getText().length() - 1);
        if (result.startsWith(prev)) {
            for (int i = 0; i < finalResult.size(); i++) {
                if (myTableView.getItems().get(i).getEnglish().equals(result)) {
                    inputWord.setText("");
                    prevWord.setText(result);
                    prevWordMean.setText(myTableView.getItems().get(i).getKorean());
                    answerLabel.setText("성공하셨습니다");
                    a = true;
                    break;
                }
            }
        } else if (!result.startsWith(prev)) {
            answerLabel.setText("이건 끝말잇기입니다ㅎㅎ");
            inputWord.setText("");
        }
        if (result.startsWith(prev) && !a) {
            answerLabel.setText("리스트에 없는 단어입니다");
            inputWord.setText("");
        }
    }

    // 단어 관리 중 추가 부분
    public void wordAdd(ActionEvent actionEvent) {
        int i;
        for (i = 0; i < finalResult.size(); i++) {
            if (myTableView.getItems().get(i).getEnglish().equals(englishAdd.getText()))
                break;
        }

        if (i < finalResult.size()) {
            managementLabel.setText("이미 있는 단어입니다");
            englishAdd.setText("");
            koreanAdd.setText("");
        } else {
            finalResult.add(new Word(englishAdd.getText(), koreanAdd.getText()));
            englishAdd.setText("");
            koreanAdd.setText("");
            managementLabel.setText("추가되었습니다");
        }
    }

    // 단어 관리 중 삭제 부분
    public void wordRemove(ActionEvent actionEvent) {
        int i;
        for (i = 0; i < finalResult.size(); i++) {
            if (myTableView.getItems().get(i).getEnglish().equals(englishRemove.getText())) {
                break;
            }
        }
        if (i >= finalResult.size()) {
            managementLabel.setText("목록에 없는 단어입니다");
            englishRemove.setText("");
        } else {
            finalResult.remove(i);
            englishRemove.setText("");
            managementLabel.setText("");
        }
    }
}
