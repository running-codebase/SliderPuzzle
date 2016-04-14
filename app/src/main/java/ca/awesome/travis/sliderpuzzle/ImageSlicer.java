package ca.awesome.travis.sliderpuzzle;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;

/**
 * Created by tco on 16-04-13.
 */
public class ImageSlicer  {

    public static ArrayList<Bitmap> sliceImage(Drawable image,  int rowCount, int columnCount) {

        ArrayList<Bitmap> resultTiles = new ArrayList<>();
        Bitmap baseImage = ((BitmapDrawable) image).getBitmap();
        int tileWidth = baseImage.getWidth() / columnCount;
        int tileHeight = baseImage.getHeight() / rowCount;

        for (int row = 0; row < rowCount; row ++){
            for (int column = 0; column < columnCount; column ++){
                int x = row * tileWidth;
                int y = column * tileHeight;
                Bitmap tile = Bitmap.createBitmap(baseImage, x, y, tileWidth, tileHeight);
                resultTiles.add(tile);
            }
        }
        return resultTiles;
    }
}
