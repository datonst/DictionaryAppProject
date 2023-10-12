package com.app.dictionaryproject.Models;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Translate {
    private CloseableHttpClient httpClient;

    public Translate() {
        httpClient = HttpClients.createDefault(); // initialize httpClient
    }
    public String translateWord(String textToTranslate, String sourceLanguage, String targetLanguage) throws IOException {
        String encodedText = URLEncoder.encode(textToTranslate, StandardCharsets.UTF_8);
        String apiUrl = "https://translate.googleapis.com/translate_a/single?client=gtx&sl="
                + sourceLanguage + "&tl=" + targetLanguage + "&dt=t&text="
                + encodedText + "&op=translate"; // link request API google
//        System.out.println(apiUrl);
        HttpGet httpGet = new HttpGet(apiUrl);
        HttpResponse response = httpClient.execute(httpGet); // get response API google
        HttpEntity entity = response.getEntity(); // take entity
        if (entity != null) {
            String jsonResponse = EntityUtils.toString(entity);
            return parseTranslationResponse(jsonResponse);
        }
        return "Error: Not found response.";
    }

    public  String parseTranslationResponse(String response) {
        try {
            JSONArray jsonArray = new JSONArray(response); // create JSON
//            System.out.println(jsonArray.toString());
            JSONArray translationArray = jsonArray.getJSONArray(0); // take first element in JSONArray
            StringBuilder translatedTextBuilder = new StringBuilder();
            for (int i = 0; i < translationArray.length(); i++) {
                String translationSegment = translationArray.getJSONArray(i).getString(0); //getElement
                translatedTextBuilder.append(translationSegment);
            }
            return translatedTextBuilder.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "Error: NOT GET ELEMENT JSON RESPONSE.";
        }
    }
}
