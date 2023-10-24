package com.app.dictionaryproject.Models;


import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class SpeechToText {
    // whisperAPI.com
    private static final String KEY_API = "XZSQBEZ4P4SSPIIG1AIFHAG2MMTTTDIZ";

    public String parseTranslationResponse(String response) {
        String result = "";
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (!jsonObject.isEmpty()) {
                try {
                    result = jsonObject.getString("text");
                } catch (JSONException e) {
                    result = "Error: Unable to parse the translation response.";
                }
            }

        } catch (JSONException e) {
            result = "Error: Unable to parse the translation response.";
        }
        return result;
    }

    public String translate(CloseableHttpClient httpClient, String sourceLanguage, String path) throws IOException {
        //Create file
        File file = new File(path);
        // Create a MultipartEntityBuilder object.
        final MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        // build multipart upload request
        HttpEntity data = builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                .addBinaryBody("file", file, ContentType.DEFAULT_BINARY, file.getName())
                .addTextBody("fileType", "wav")
                .addTextBody("diarization", "false")
                .addTextBody("numSpeakers", "1")
                .addTextBody("language", sourceLanguage)
                .addTextBody("task", "transcribe")
                .build();

        // build http request and assign multipart upload data
        HttpUriRequest request = RequestBuilder.post("https://transcribe.whisperapi.com")
                .setEntity(data)
                .setHeader("Authorization", "Bearer " + KEY_API)
                .build();

        //Create a custom response handler
        ResponseHandler<String> responseHandler = response -> {
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        };
        String responseBody = httpClient.execute(request, responseHandler);
        return parseTranslationResponse(responseBody);
    }
}
