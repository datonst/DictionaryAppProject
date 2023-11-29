package com.app.dictionaryproject.DictionaryCmd;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

class DictionaryCommandLine extends DictionaryManagement {
    void dictionaryBasic() {
        insertFromCommandline();
        showAllWords();
    }
    void showAllWords() {
        if(size != 0) {
            System.out.println("No   | English             | Vietnamese          ");
        }else {
            System.out.println("There are no words in the dictionary");
        }


        for(int i = 0; i < size; i++) {
            System.out.printf("%-5d| ", i+1);
            words[i].printWord(true);
        }
    }

    //hàm tìm kiếm gợi ý
    void dictionarySearcher() {
        System.out.println("Enter the word you want to search: ");
        String wordToSearch = sc.nextLine().toLowerCase();
        List<String> matchingWords = new ArrayList<>();

        for(int i = 0; i < size; i++) {
            String word_target = words[i].getWord_target().toLowerCase();
            if(word_target.startsWith(wordToSearch)) {
                matchingWords.add(word_target);
            }
        }
        if(matchingWords.isEmpty()) {
            System.out.println("No matching words found");
        }else {
            for(String word : matchingWords) {
                System.out.println(word);
            }
        }
    }



    //hàm chạy
    void dictionaryAdvanced() {
        while (true) {
            System.out.println("\nWelcome to My Application!");
            System.out.println("[0] Exit");
            System.out.println("[1] Add");
            System.out.println("[2] Remove");
            System.out.println("[3] Update");
            System.out.println("[4] Display");
            System.out.println("[5] Lookup");
            System.out.println("[6] Search");
            System.out.println("[7] Game");
            System.out.println("[8] Import from file");
            System.out.println("[9] Export to file");

            System.out.print("Your action: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "0":
                    System.out.println("Exiting the application.");
                    return;
                case "1":
                    insertFromCommandline();
                    break;
                case "2":
                    removeWord();
                    break;
                case "3":
                    fixWord();
                    break;
                case "4":
                    showAllWords();
                    break;
                case "5":
                    dictionaryLookup();
                    break;
                case "6":
                    dictionarySearcher();
                    break;
                case "7":
                    if (size < 10) {
                        String path = System.getProperty("user.dir")
                                + "\\src\\main\\resources\\data\\dictionary.txt";
                        insertFromFile(path);
                    }
                    //System.out.println(size);
                    System.out.println("----Rules of game----");
                    System.out.println("Get 1000 scores or more to win.");
                    System.out.println("If you make a mistake 5 times in a row, you lose.\n");
                    int score = 0;
                    int cnt = 0;
                    while(true){

                        int index = randomIndex();
                        System.out.println("What is meaning of [" + words[index].getWord_target() + "] ?" );
                        System.out.print("Input your answer of English word: " );
                        String mean = sc.nextLine();
                        if(findMeaning(mean, index)){
                            score += 200;
                            cnt = 0;
                            System.out.println("Correctly!!!");
                            System.out.println("Your score: " + score + "\n");
                        }else {
                            cnt++;
                            System.out.println("Incorrectly!!!");
                            System.out.println("You made a mistake " + cnt + " times.");
                            System.out.print("Correct answer is: " );
                            int count = 1;
                            for (String explain : words[index].getWord_explains()) {
                                System.out.print(explain);
                                if (count < words[index].getWord_explains().size()) {
                                    System.out.print(" or ");
                                    count ++;
                                }
                                System.out.println();
                            }
                            System.out.println("Your score: " + score + "\n");

                        }
                        if(cnt >= 5){
                            System.out.println("You loseeeeee!");
                            System.out.println("Your score: " + score);
                            System.out.println("I think you should learn that words by heart again ! ");
                            break;
                        }
                        if(score >= 1000) {
                            System.out.println("Your Winnnn!!!!! ");
                            break;
                        }
                    }
                    break;
                case "8":
                    // thay đổi theo từng máy của mọi người
                    //String path = "C:\\Users\\Admin\\Documents\\GitHub\\DictionaryAppProject\\DictionaryAppProject\\src\\main\\resources\\data\\dictionary.txt";
                    String path = System.getProperty("user.dir")
                            + "\\src\\main\\resources\\data\\dictionary.txt";
                    insertFromFile(path);
                    System.out.println("imported successfully!");

                    break;
                case "9":
                    System.out.print("Input file path: ");
                    String filePath = sc.nextLine();
                    exportToFile(filePath);
                    break;
                default:
                    System.out.println("Action not supported.");
                    break;
            }
        }
    }


    //test main
    public static void main(String[] args) {
        DictionaryCommandLine dic = new DictionaryCommandLine();
        dic.dictionaryAdvanced();
    }
}
