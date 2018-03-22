package fri.uniza.sk.cookbookexample.model;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "title",
        "ingredients",
        "detail",
        "image_url"
})
public class Recipe {

    @JsonProperty("title")
    public String title;
    @JsonProperty("ingredients")
    public List<String> ingredients = null;
    @JsonProperty("detail")
    public String detail;
    @JsonProperty("image_url")
    public String imageUrl;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    //    @JsonIgnore
//    public Bitmap getBitmapFromAsset(Context context) throws InterruptedException {
//        AssetManager assetManager = context.getAssets();
//
//
//        double random = Math.random();
//        Thread.sleep(500 + (int) (random * 1000));
//
//        InputStream istr;
//        Bitmap bitmap = null;
//        try {
//            istr = assetManager.open(imageUrl);
//            bitmap = BitmapFactory.decodeStream(istr);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return bitmap;
//    }
    @JsonIgnore
    public Bitmap getBitmapFromAsset(Context context) throws InterruptedException, IOException {
        AssetManager assetManager = context.getAssets();


        double random = Math.random();
        Thread.sleep(500 + (int) (random * 1000));

        URL url = null;
        try {
            url = new URL("https://fakeimg.pl/350x200/?text=IMG_" + imageUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Bitmap bitmap = null;

            bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());

        return bitmap;
    }


    @JsonIgnore
    public static Bitmap getBitmapOfSpinner(Context context) {
        AssetManager assetManager = context.getAssets();
        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open("spinner.gif");
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Recipe recipe = (Recipe) o;

        if (!title.equals(recipe.title)) return false;
        if (!ingredients.equals(recipe.ingredients)) return false;
        if (!detail.equals(recipe.detail)) return false;
        return imageUrl.equals(recipe.imageUrl);

    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + ingredients.hashCode();
        result = 31 * result + detail.hashCode();
        result = 31 * result + imageUrl.hashCode();
        return result;
    }
}