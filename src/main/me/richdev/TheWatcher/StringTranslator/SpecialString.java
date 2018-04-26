package me.richdev.TheWatcher.StringTranslator;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.istack.internal.NotNull;
import me.richdev.TheWatcher.Utils.Validate;

import java.io.*;

/**
 * @since 1.0 BETA
 * @version 1.0
 */
public class SpecialString {

    /**
     *
     * Get the translation of a requried MSG.
     *
     * @param language The language you require.
     * @param msgID The ID of the require msg.
     * @return The translated message.
     */
    public static String getTranslation(@NotNull Language language, @NotNull String msgID) {
        Validate.isEmpty(msgID, "The ID of the message can't be null.");

        String[] ob = msgID.split("\\.");
        JsonObject jsonObject = null; //new JsonParser().parse("{\"name\": {\"last\": \"ROd\"}}").getAsJsonObject();

        InputStream in = SpecialString.class.getResourceAsStream("Files/" + language.getFileString());
        try (Reader reader = new FileReader(new File("C:/Users/7766/Documents/es.json"))) {
            jsonObject = new JsonParser().parse(reader).getAsJsonObject();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JsonObject found = jsonObject;

        //System.out.println(jsonObject.get("name").getAsJsonObject().get("last").getAsString());

        int i = 1;
        for (String s : ob) {
            if(found == null || found.get(s) == null)
                throw new NullPointerException("We couldn't found that section of the translation config");

            if(i != ob.length) {
                //System.out.println(found);
                if(!(found.get(s) instanceof JsonObject))
                    throw new NullPointerException("We couldn't found that translation.");
                found = found.get(s).getAsJsonObject();
            } else {
                return found.get(s).getAsString();
            }
            i++;
        }

        return "";
    }

}
