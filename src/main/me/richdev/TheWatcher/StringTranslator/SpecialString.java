package me.richdev.TheWatcher.StringTranslator;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class SpecialString {

    public static String getTranslation(String ID) {
        String search = "commands.music.help.play";
        String[] ob = search.split("\\.");
        JsonObject jsonObject = null; //new JsonParser().parse("{\"name\": {\"last\": \"ROd\"}}").getAsJsonObject();

        try (Reader reader = new FileReader("C:/Users/7766/Documents/safe/es.json")) {
            jsonObject = new JsonParser().parse(reader).getAsJsonObject();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JsonObject found = jsonObject;

        //System.out.println(jsonObject.get("name").getAsJsonObject().get("last").getAsString());

        int i = 1;
        for (String s : ob) {
            if(i != ob.length) {
                //System.out.println(found);
                found = found.get(s).getAsJsonObject();
            } else {
                System.out.println(found.get(s).getAsString());
            }
            i++;
        }

        return "";
    }

}
