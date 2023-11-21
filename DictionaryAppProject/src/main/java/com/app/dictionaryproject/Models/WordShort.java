package com.app.dictionaryproject.Models;

public class WordShort {
    private String word;
    private String TextDescription;
    private String HTMLDescription;

    public WordShort(String word, String textDescription, String HTMLDescription) {
        this.word = word;
        this.TextDescription = textDescription;
        this.HTMLDescription = HTMLDescription;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getTextDescription() {
        return TextDescription;
    }

    public void setTextDescription(String textDescription) {
        TextDescription = textDescription;
    }

    public String getHTMLDescription() {
        return HTMLDescription;
    }

    public void setHTMLDescription(String HTMLDescription) {
        this.HTMLDescription = HTMLDescription;
    }
}
