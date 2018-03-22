package fri.uniza.sk.cookbookexample.utilities;

import android.os.Bundle;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;

import fri.uniza.sk.cookbookexample.model.Recipe;

/**
 * Created by Martin on 09.03.2017.
 */

public class LoadData {



    public static List<Recipe> loadRecepies(InputStream inputStream) {

        ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
        List<Recipe> recipe = null;
        try {
            recipe = mapper.readValue(inputStream, new TypeReference<List<Recipe>>() {
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
        return recipe;
    }

}
