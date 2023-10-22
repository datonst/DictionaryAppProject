package com.example.jademo;

import com.example.jademo.Model.SpeechToText;
import com.example.jademo.Model.TextToText;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;


public class TranslateService {

    private static final CloseableHttpClient httpClient = HttpClients.createDefault();
    private static final TextToText textToText = new TextToText();
    private static final SpeechToText speechToText = new SpeechToText();

    public String getVietnameseText(String englishWord) throws IOException {
        return textToText.translate(httpClient,englishWord,"en","vi");
    }

    public String getEnglishText(String vietnameseWord) throws IOException {
        return textToText.translate(httpClient,vietnameseWord,"vi","en");
    }

    public String getSpeechToText(String path, String sourceLanguage) throws IOException {
        return speechToText.translate(httpClient,sourceLanguage, path);
    }

//    public static void main(String[] args) throws IOException {
//        System.out.println(textToText("hello i love you very much", "en", "vi"));
//    }
}