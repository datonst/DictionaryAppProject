package com.app.dictionaryproject.DictionaryCmd;

import java.util.*;

public class Dictionary {
    protected WordCmd[] words = new WordCmd[1000];
    protected int size = 0;
    private static final int INITIAL_CAPACITY = 1000;

    WordCmd[] getWords() {
        return words;
    }

    // Phương thức để thêm từ vào danh sách từ điển
    boolean addWord(WordCmd word) {
        //lấy word_target và nghĩa muốn thêm vào
        String word_target = word.getWord_target();
        String word_explain = word.getWord_exp();

        if(findDuplicate(word_target, word_explain)) {
            return false;//không thêm vào do trùng lặp
        }
        if(size >= words.length) {
            wordsExpand();//mở rộng mảng nếu đầy
        }
        words[size++] = word;
        return true;
    }

    //phương thức kiểm tra từ trùng lặp
    boolean findDuplicate(String word_target, String word_explain) {
        for(int i = 0; i < size; i++) {
            if(word_target.equals(words[i].getWord_target())) {
                if(!(words[i].getWord_explains()).contains(word_explain)) {
                    words[i].addWord_explain(word_explain);
                }
                return true;
            }
        }
        return false;
    }

    //phương thức mở rộng mảng word
    void wordsExpand() {
        words = Arrays.copyOf(words, words.length + INITIAL_CAPACITY);
    }

    //phương thức sắp xếp theo thứ tự chữ cái
    void sortDictionary() {
        Arrays.sort(words, 0, size, new Comparator<WordCmd>() {
            @Override
            public int compare(WordCmd word1, WordCmd word2) {
                return word1.getWord_target().compareToIgnoreCase(word2.getWord_target());
            }
        });
    }

    //tìm kiếm word_target
    int binarySearch(String word_target) {
        int left = 0;
        int right = size - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            int comparisonResult = word_target.compareToIgnoreCase(words[mid].getWord_target());

            if (comparisonResult == 0) {
                // Tìm thấy từ cần tìm kiếm, trả về chỉ mục mid
                return mid;
            } else if (comparisonResult < 0) {
                // Từ cần tìm kiếm nằm ở bên trái của từ ở vị trí mid
                right = mid - 1;
            } else {
                // Từ cần tìm kiếm nằm ở bên phải của từ ở vị trí mid
                left = mid + 1;
            }
        }

        // Không tìm thấy từ cần tìm kiếm, trả về -1 để chỉ ra rằng không tồn tại
        return -1;
    }


}