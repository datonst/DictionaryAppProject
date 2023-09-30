package com.app.dictionaryproject.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.util.List;
import java.util.Vector;

public class TextToSQL {
    public String getPronounce(String pronounce) {
        String check = pronounce.substring(2);
        if (check.contains("/")) {
            int endPronounce = check.indexOf("/");
            pronounce = pronounce.substring(0,endPronounce+3);
        }
        return pronounce;
    }
    public List<String> replaceElement(String eWord, String pronounce, String def) {
        eWord = eWord.replace("'", "`");
        pronounce = pronounce.replace("'", "`");
        def = def.replace("-", "\n-");
        def = def.replace("'", "`");

        eWord = eWord.replace("=", "\n=");
        eWord = eWord.replace("*", "\nLoai tu: ");
        //res = res.replace("-", "\n-");
        eWord = eWord.replace("+", "\n+");

        pronounce = pronounce.replace("=", "\n=");
        pronounce = pronounce.replace("*", "\nLoai tu: ");
        //res = res.replace("-", "\n-");
        pronounce = pronounce.replace("+", "\n+");

        def = def.replace("=", "\n=");
        def = def.replace("*", "\nLoai tu: ");
        //res = res.replace("-", "\n-");
        def = def.replace("+", "\n+");
        return List.of(eWord,pronounce,def);
    }

    public void convert(DBRepository dbRepository) {
        Path path = Path.of("src/main/resources/data/EnglishData.txt");
        try {
            List<String> word_list = Files.readAllLines(path);
            StringBuilder definition= new StringBuilder();
            String eWord = new String(""), pronounce= "adsad";
            for (String line : word_list) {
                if (!line.isEmpty()) {
                    if (line.charAt(0) == '@') {
                        if (!eWord.isEmpty()) {
                            String def = definition.toString();
                            List<String> list = replaceElement(eWord,pronounce,def);
                            dbRepository.insertWord(list.get(0),list.get(1),list.get(2));
                            definition.setLength(0);
                        }
                        //add vào đây
                        if (line.contains(" /")) {
                            int startPronounce = line.indexOf(" /");
                            eWord = line.substring(1, startPronounce);
//                            System.out.println(line.substring(startPronounce));
                            pronounce = getPronounce(line.substring(startPronounce));
                        } else {
                            eWord = line.substring(1);
                            pronounce = "none";
                        }
                        eWord = eWord.trim();
                    }
                    else {
                        definition.append(line);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
