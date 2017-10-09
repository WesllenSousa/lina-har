/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author Wesllen Sousa
 */
public class StringUtil {

    public static String formatJson(String json) {
        String newJson = "";
        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == '\\') {
                continue;
            } else if (c == '"' && json.charAt(i + 1) == '{') {
                continue;
            } else if (c == '"' && json.charAt(i - 1) == '}') {
                continue;
            } else if (c == '{') {
                newJson += c + "\n";
            } else if (c == ',' && json.charAt(i + 1) == '\"') {
                newJson += c + "\n";
            } else if (c == '}') {
                newJson += "\n" + c;
            } else {
                newJson += c;
            }
        }
        return newJson;
    }

}
