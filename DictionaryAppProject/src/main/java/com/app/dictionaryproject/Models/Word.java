package com.app.dictionaryproject.Models;

public class Word {
    private String word_target;
    private  String phonetic;
    private String definitionWord;
    public String getWordTarget() {return word_target;}
    public String getPhonetic() {return phonetic;}
    public String getDefinitionWord() {return definitionWord;}
    public void setWord_target (String word_target) {this.word_target = word_target;}
    public void setPhonetic (String phonetic) {this.phonetic = phonetic;}
    public void setDefinitionWord (String definitionWord) {this.definitionWord = definitionWord;}
    public  Word(String word_target, String phonetic, String definitionWord) {
        this.word_target = word_target;
        this.phonetic = phonetic;
        this.definitionWord = definitionWord;
    }
    Word() {
        word_target = "";
        phonetic = "";
        definitionWord = "";
    }
}
