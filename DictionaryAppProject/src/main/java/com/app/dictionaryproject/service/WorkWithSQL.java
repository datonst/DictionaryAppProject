package com.app.dictionaryproject.service;

import com.app.dictionaryproject.Models.WordShort;
import javafx.scene.control.Alert;

import java.sql.*;
import java.util.ArrayList;
public class WorkWithSQL {
    public class UpdateDict {
        public static String capFirstLetter(String word) {
            if (word == null || word.isEmpty()) {
                return word;
            }

            return word.substring(0, 1).toUpperCase() + word.substring(1);
        }

        private static String convertToHtml(String line) {
            if (line.startsWith("@")) {
                line = line.substring(1);
                int posOfPron = line.indexOf("/");
                String word = "";
                if (line.length() > 0) {
                    word = line.substring(0, posOfPron < 0 ? line.length() : posOfPron).trim();
                    word = capFirstLetter(word);
                }
                String pronOfWord = posOfPron < 0 ? "" : line.substring(posOfPron).trim();

                return "<h2 class=\"nameWord\">" + word + "</h2>\n"
                        + "<h3 class=\"pronounWord\">" + pronOfWord + "</h3>\n";
            } else if (line.startsWith("*")) {
                line = line.substring(1).trim();
                line = capFirstLetter(line);

                return "<h4 class = \"typeWord\">" + line + "</h4>\n";
            } else if (line.startsWith("-")) {
                line = line.substring(1).trim();
                line = capFirstLetter(line);

                return "<h5 class=\"meanWord\">" + line + "</h5>\n";
            } else if (line.startsWith("=")) {
                line = line.substring(1).trim();
                String[] words = line.split("\\+");
                String exSentence = words[0];
                exSentence = capFirstLetter(exSentence);
                String meanOfEx = "";
                if (words.length >= 2) {
                    meanOfEx = words[1].trim();
                    meanOfEx = capFirstLetter(meanOfEx);
                }

                return "<h6 class=\"exampleWord\">\n"
                        + "<p>" + exSentence + ": </p>\n"
                        + "<p>" + meanOfEx + "</p>\n"
                        + "</h6>\n";
            } else {
                return "";
            }
        }

        private static String getKeyWord(String line) {
            System.out.println(line);
            if (line.startsWith("@")) {
                line = line.substring(1);
                int posOfPron = line.indexOf("/");
                String word = "";
                if (line.length() > 0) {
                    word = line.substring(0, posOfPron < 0 ? line.length() : posOfPron).trim();
                }

                return word;
            } else {
                return "";
            }
        }

        private static String convertToText(String line) {
            if (line.startsWith("@")) {
                line = line.substring(1);
                int posOfPron = line.indexOf("/");
                String word = "";
                if (line.length() > 0) {
                    word = line.substring(0, posOfPron < 0 ? line.length() : posOfPron).trim();
                    word = capFirstLetter(word);
                }
                String pronOfWord = posOfPron < 0 ? "" : line.substring(posOfPron).trim();

                return "Từ: " + word + "\n"
                        + "Phát âm: " + pronOfWord + "\n";
            } else if (line.startsWith("*")) {
                line = line.substring(1).trim();
                line = capFirstLetter(line);

                return "Loại: " + line + "\n";
            } else if (line.startsWith("-")) {
                line = line.substring(1).trim();
                line = capFirstLetter(line);

                return "Nghĩa: " + line + "\n";
            } else if (line.startsWith("=")) {
                line = line.substring(1).trim();
                String[] words = line.split("\\+");
                String exSentence = words[0].trim();
                exSentence = capFirstLetter(exSentence);
                String meanOfEx = "";
                if (words.length >= 2) {
                    meanOfEx = words[1].trim();
                    meanOfEx = capFirstLetter(meanOfEx);
                }

                return "Ví dụ: " + exSentence + ". Dịch: " + meanOfEx + "\n";
            } else {
                return "";
            }
        }
    }
}
