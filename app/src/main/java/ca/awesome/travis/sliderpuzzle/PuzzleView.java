package ca.awesome.travis.sliderpuzzle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 * Created by tco on 16-04-13.
 */
public class PuzzleView extends RelativeLayout implements View.OnTouchListener {

    private static final float BACKGROUND_ALPHA = 0.2f;
    private static final int ROWS = 4;
    private static final int COLUMNS = 4;


    private Context context;
    private ImageView backgroundImageView;
    private boolean viewInitialized = false;

    private ArrayList<TilePieceView> tilePieceViews = new ArrayList<>();

    public PuzzleView(Context context) {
        super(context);
        this.context = context;
    }

    public PuzzleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (!viewInitialized) {
//            initializeBackground();
            initializeTiles();

            viewInitialized = true;
        }
    }

    private void initializeBackground() {

        backgroundImageView = new ImageView(context);
        backgroundImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.globe));
        backgroundImageView.setAlpha(BACKGROUND_ALPHA);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(getWidth(), getHeight());
        params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        addView(backgroundImageView, params);
    }

    private void initializeTiles() {
        ArrayList<Bitmap> puzzleTiles = ImageSlicer.sliceImage(ContextCompat.getDrawable(context, R.drawable.globe), ROWS, COLUMNS);
        TilePieceView tilePiece;
        for (int row = 0; row < ROWS; row ++) {
            for (int column = 0; column < COLUMNS; column++) {
//                IF this is the bottom right tilePiece leave it empty
                if (row == ROWS -1 && column == COLUMNS -1) {
                    tilePiece = new TilePieceView(context, column, row, true);
                } else {
                    tilePiece = new TilePieceView(context, column, row, false);
                    tilePiece.setImageBitmap(puzzleTiles.get(0));
                    placeTilePiece(tilePiece);
                }
                tilePieceViews.add(tilePiece);
                puzzleTiles.remove(0);
            }
        }
    }

    private void placeTilePiece(TilePieceView tilePiece) {
        int width = getWidth() / COLUMNS;
        int height = getWidth() / ROWS;
        int left = (int) tilePiece.getColumn() * width;
        int top = (int) tilePiece.getRow() * height;
        int right = left + width;
        int bottom = top + height;
        Rect containerRect = new Rect(left, top, right, bottom);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
        params.topMargin = containerRect.top;
        params.leftMargin = containerRect.left;
        addView(tilePiece, params);
        tilePiece.setOnTouchListener(this);

    }



    @Override
    public boolean onTouch(View v, MotionEvent event) {
        TilePieceView tilePiece = (TilePieceView) v;
        if (!tilePiece.isEmpty()) {
            switch (event.getActionMasked()){
                case MotionEvent.ACTION_DOWN:
                    tilePiece.setAlpha(0.1f);
                    break;
                case MotionEvent.ACTION_UP:
                    tilePiece.setAlpha(1f);
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
            }
        }
        return true;
    }


}
