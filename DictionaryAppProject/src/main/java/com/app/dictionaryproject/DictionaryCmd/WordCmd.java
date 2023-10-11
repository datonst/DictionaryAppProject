package com.app.dictionaryproject.DictionaryCmd;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class WordCmd {
    private String word_target;
    private Set<String> word_explains;

    WordCmd() {
        word_target = "";
        word_explains = new TreeSet<>();
    }

    WordCmd(String word_target, Set<String> word_explains) {
        this.word_target = word_target;
        this.word_explains = new TreeSet<>(word_explains);
    }

    public String getWord_target() {
        return word_target;
    }

    public Set<String> getWord_explains() {
        return word_explains;
    }

    public void setWord_target(String word_target) {
        this.word_target = word_target;
    }

    public void addWord_explain(String word_explain) {
        if(!containsWord_explain(word_explain)) {
            word_explains.add(word_explain);
        }
    }

    public void removeWord_explain(String word_explain) {
        if(containsWord_explain(word_explain)) {
            word_explains.remove(word_explain);
        }
    }

    public boolean containsWord_explain(String word_explain) {
        return word_explains.contains(word_explain);
    }

    public String getWord_exp() {
        // Lấy phần tử đầu tiên của word_explains;
        Iterator<String> iterator = word_explains.iterator();
        String firstElement = "";
        if (iterator.hasNext()) {
            firstElement = iterator.next();
        }
        return firstElement;
    }

    //in từ
    void printWord(boolean showAll) {
        System.out.printf("%-20s| ", word_target);
        boolean firstItem = true;
        for (String meaning : word_explains) {
            if (!firstItem) {
                if(showAll) {
                    System.out.printf("%-5s| %-20s| %-20s", " ",word_target, meaning );
                    System.out.println("");
                }else {
                    System.out.println("                    | " + meaning);
                }
            } else
                System.out.println(meaning);
            firstItem = false;
        }
    }
}

