package com.app.dictionaryproject.Repository;


import com.app.dictionaryproject.Models.Word;
import com.app.dictionaryproject.Models.WordShort;
import com.app.dictionaryproject.WordForm.WordRoot;

import java.util.ArrayList;

public interface DictDAO <T extends WordRoot>  {


    void insertWord(T wordToInsert);

    T searchWord(String wordToSearch);

    ArrayList<String> searchListWord(String wordToSearch);

    void deleteWord(String wordToDelete);


    void updateWord(T wordToUpdate);

    boolean existWord(String word);
}
