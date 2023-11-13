package com.app.dictionaryproject.Models;

public class Word {
    private String word_target;
    private String phonetic;
    private String wordType;
    private String synonym;
    private String antonym;
    private String definitionWord;

    public String getWord_target() {
        return word_target;
    }

    public void setWord_target(String word_target) {
        this.word_target = word_target;
    }

    public String getPhonetic() {
        return phonetic;
    }

    public void setPhonetic(String phonetic) {
        this.phonetic = phonetic;
    }

    public String getWordType() {
        return wordType;
    }

    public void setWordType(String wordType) {
        this.wordType = wordType;
    }

    public String getSynonym() {
        return synonym;
    }

    public void setSynonym(String synonym) {
        this.synonym = synonym;
    }

    public String getAntonym() {
        return antonym;
    }

    public void setAntonym(String antonym) {
        this.antonym = antonym;
    }

    public String getDefinitionWord() {
        return definitionWord;
    }

    public void setDefinitionWord(String definitionWord) {
        this.definitionWord = definitionWord;
    }
    public Word(String word_target, String phonetic,
                 String definitionWord) {
        this.word_target = word_target;
        this.phonetic = phonetic;
        this.definitionWord = definitionWord;
    }
    public Word(String word_target, String phonetic, String wordType, String definitionWord, String synonym, String antonym) {
        this.word_target = word_target;
        this.phonetic = phonetic;
        this.wordType = wordType;
        this.antonym = antonym;
        this.synonym = synonym;
        this.definitionWord = definitionWord;
    }

    Word() {
        word_target = "";
        wordType = "";
        antonym = "";
        synonym = "";
        phonetic = "";
        definitionWord = "";
    }


}
