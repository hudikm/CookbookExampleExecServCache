package fri.uniza.sk.cookbookexample.model;

import java.util.List;

/**
 * Created by Martin on 09.03.2017.
 */
public class Singleton {
    private static Singleton ourInstance = new Singleton();
    private List<Recipe> Recipes;

    private Singleton() {
    }

    public static Singleton getInstance() {
        return ourInstance;
    }

    public List<Recipe> getRecipes() {
        return Recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        Recipes = recipes;
    }
}
