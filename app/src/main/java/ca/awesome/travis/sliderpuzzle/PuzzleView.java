package ca.awesome.travis.sliderpuzzle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by tco on 16-04-13.
 */
public class PuzzleView extends RelativeLayout implements View.OnTouchListener {

    private static final int ROWS = 4;
    private static final int COLUMNS = 4;
    private static final int EMPTY_TILE_ID = -1;

    private Context context;
    private boolean viewInitialized = false;

    private ArrayList<TilePieceView> tilePieceViews = new ArrayList<>();
    private ArrayList<TilePieceView> movingTilePieceViews = new ArrayList<>();
    private GridCalculations.Direction movingDirection = GridCalculations.Direction.NONE;

    private TilePieceView emptyTilePieceView;
    private TilePieceView selectedTilePieceView;

    private GridCalculations gridCalc;
    private int tileWidth, tileHight;
    private float initialTouchX, initialTouchY;


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
            setMotionEventSplittingEnabled(false);

            tileWidth = getWidth() / COLUMNS;
            tileHight = getWidth() / ROWS;
            gridCalc = new GridCalculations(tileWidth, tileHight);

            initializeTiles();
            viewInitialized = true;
        }
    }

    private void initializeTiles() {
        //Initialize images, remove bottom right corner and shuffle
        ArrayList<Bitmap> puzzleTiles = ImageSlicer.sliceImage(ContextCompat.getDrawable(context, R.drawable.globe), ROWS, COLUMNS);
        puzzleTiles.remove(puzzleTiles.size() - 1);

        int currentId = 0;
        TilePieceView tilePiece;
        for (int row = 0; row < ROWS; row ++) {
            for (int column = 0; column < COLUMNS; column++) {
//              If this is the bottom right tilePiece leave it empty
                if (row == ROWS -1 && column == COLUMNS -1) {
                    tilePiece = new TilePieceView(context, currentId, column, row, true);
                    emptyTilePieceView = tilePiece;
                } else {
                    tilePiece = new TilePieceView(context, currentId, column, row, false);
                    tilePiece.setImageBitmap(puzzleTiles.get(0));
                    placeTilePiece(tilePiece);

                    puzzleTiles.remove(0);
                }
                tilePieceViews.add(tilePiece);
                currentId++;
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
        tilePiece.setHomeX(left);
        tilePiece.setHomeY(top);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        TilePieceView touchedTilePiece = (TilePieceView) v;
        if (!touchedTilePiece.isEmpty() && gridCalc.tilePieceInSameRowOrColumnToEmptyTile(touchedTilePiece, emptyTilePieceView)) {

            selectedTilePieceView = touchedTilePiece;
            switch (event.getActionMasked()){
                case MotionEvent.ACTION_DOWN:
                    movingTilePieceViews.clear();

                    initialTouchX = event.getRawX();
                    initialTouchY = event.getRawY();
                    movingTilePieceViews = gridCalc.tilesPiecesThatMove(touchedTilePiece, emptyTilePieceView, tilePieceViews);
                    movingDirection = gridCalc.calculateDirection(touchedTilePiece, emptyTilePieceView);
                    break;

                case MotionEvent.ACTION_MOVE:
                    float deltaX = event.getRawX() - initialTouchX;
                    float deltaY = event.getRawY() - initialTouchY;
                    float[] constrainedTemporaryDeltas = gridCalc.constrainTemporaryMovement(movingDirection, deltaX, deltaY);
                    temporarilyMoveTiles(constrainedTemporaryDeltas[0], constrainedTemporaryDeltas[1]);
                    break;

                case MotionEvent.ACTION_UP:
                    float finalDeltaX = event.getRawX() - initialTouchX;
                    float finalDeltaY = event.getRawY() - initialTouchY;
                    float[] constrainedFinalDeltas = gridCalc.constrainTemporaryMovement(movingDirection, finalDeltaX, finalDeltaY);

                    float[] finalDeltas = gridCalc.snapFinalMovement(movingDirection, constrainedFinalDeltas[0], constrainedFinalDeltas[1]);
                    if (GridCalculations.movementTriggeredTileUpdate(finalDeltas[0], finalDeltas[1])) {
                        moveTilesToFinalPosition(finalDeltas[0], finalDeltas[1]);
                        emptyTilePieceView = new TilePieceView(context, EMPTY_TILE_ID, selectedTilePieceView.getRow(), selectedTilePieceView.getColumn(), true);
                        updateTilePieceViewsRowAndColumn(finalDeltas[0], finalDeltas[1]);
                    } else {
                        moveTilesToFinalPosition(finalDeltas[0], finalDeltas[1]);
                    }

                    movingDirection = GridCalculations.Direction.NONE;
                    break;
            }
        }
        return true;
    }


    public void temporarilyMoveTiles(float deltaX, float deltaY) {
        for (TilePieceView tilePieceView: movingTilePieceViews){
            tilePieceView.setX(tilePieceView.getHomeX() + deltaX);
            tilePieceView.setY(tilePieceView.getHomeY() + deltaY);
        }
    }

    public void moveTilesToFinalPosition(float deltaX, float deltaY) {

        for (TilePieceView tilePieceView: movingTilePieceViews){
            tilePieceView.setX(tilePieceView.getHomeX() + deltaX);
            tilePieceView.setY(tilePieceView.getHomeY() + deltaY);
            tilePieceView.setHomeX(tilePieceView.getHomeX() + (int) deltaX);
            tilePieceView.setHomeY(tilePieceView.getHomeY() + (int) deltaY);
        }
    }

    public void updateTilePieceViewsRowAndColumn(float deltaX, float deltaY) {

        for (TilePieceView tilePieceView: movingTilePieceViews){
            if (deltaX < 0 ) {
                tilePieceView.setColumn(tilePieceView.getColumn() - 1);
            } else if (deltaX > 0 ) {
                tilePieceView.setColumn(tilePieceView.getColumn() + 1);
            }

            if (deltaY < 0 ) {
                tilePieceView.setRow(tilePieceView.getRow() - 1);
            } else if (deltaY > 0 ){
                tilePieceView.setRow(tilePieceView.getRow() + 1);
            }
        }
    }
}
