package ca.awesome.travis.sliderpuzzle;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by tco on 16-04-13.
 */
public class GridCalculations {

    private float tileWidth;
    private float tileHight;


    public GridCalculations(float tileWidth, float tileHight){
        this.tileWidth = tileWidth;
        this.tileHight = tileHight;
    }

    public enum Direction {
        LEFT, UP, RIGHT, DOWN, NONE
    }

    public boolean tilePieceInSameRowOrColumnToEmptyTile(TilePieceView tilePieceView, TilePieceView emptyTilePieceView) {
        if (tilePieceView.getRow() == emptyTilePieceView.getRow() || tilePieceView.getColumn() == emptyTilePieceView.getColumn() ) {
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<TilePieceView> tilesPiecesThatMove(TilePieceView touchedTilePiece, TilePieceView emptySpaceTilePiece, ArrayList<TilePieceView> tilePieces ){
        ArrayList<TilePieceView> touchedTilePieces = new ArrayList<>();
        touchedTilePieces.add(touchedTilePiece);


        if (touchedTilePiece.getColumn() == emptySpaceTilePiece.getColumn()) {
            for (TilePieceView tempTile : tilePieces) {
                if (tempTile.getColumn() == touchedTilePiece.getColumn()) {
                    if (touchedTilePiece.getRow() < emptySpaceTilePiece.getRow()) {
                        if (tempTile.getRow() < emptySpaceTilePiece.getRow() &&
                                tempTile.getRow() > touchedTilePiece.getRow()) {
                            touchedTilePieces.add(tempTile);
                        }

                    } else {
                        if (tempTile.getRow() > emptySpaceTilePiece.getRow() &&
                                tempTile.getRow() < touchedTilePiece.getRow()) {
                            touchedTilePieces.add(tempTile);
                        }
                    }

                }
            }

        } else if (touchedTilePiece.getRow() == emptySpaceTilePiece.getRow()) {
            Log.d("", "They are in the same row");
            for (TilePieceView tempTile : tilePieces) {
                if (tempTile.getRow() == touchedTilePiece.getRow()) {
                    if (touchedTilePiece.getColumn() < emptySpaceTilePiece.getColumn()) {
                        if (tempTile.getColumn() < emptySpaceTilePiece.getColumn() &&
                                tempTile.getColumn() > touchedTilePiece.getColumn()) {
                            touchedTilePieces.add(tempTile);
                            Log.d("Slider", "added row: " + tempTile.getRow() + " column: " + tempTile.getColumn());
                        }
                    } else {
                        if (tempTile.getColumn() > emptySpaceTilePiece.getColumn() &&
                                tempTile.getColumn() < touchedTilePiece.getColumn()) {
                            touchedTilePieces.add(tempTile);
                            Log.d("Slider", "added row: " + tempTile.getRow() + " column: " + tempTile.getColumn());
                        }
                    }
                }
            }
        }

        return touchedTilePieces;
    }

    public Direction calculateDirection(TilePieceView touchedTilePiece, TilePieceView emptySpaceTilePiece) {

        if (touchedTilePiece.getColumn() == emptySpaceTilePiece.getColumn()) {
            if (touchedTilePiece.getRow() < emptySpaceTilePiece.getRow()) {
                return Direction.DOWN;
            } else {
                return Direction.UP;
            }
        } else if (touchedTilePiece.getRow() == emptySpaceTilePiece.getRow()) {
            if (touchedTilePiece.getColumn() < emptySpaceTilePiece.getColumn()) {
                return Direction.RIGHT;
            } else {
                return Direction.LEFT;
            }
        }
        return Direction.NONE;
    }

    public float[] constrainTemporaryMovement(GridCalculations.Direction moveDirection, float deltaX, float deltaY) {
        float[] constrainedDeltas = new float[2];
        switch (moveDirection){
            case DOWN:
                deltaX = 0;
                deltaY = Math.min(Math.max(0, deltaY), tileHight);
                break;
            case UP:
                deltaX = 0;
                deltaY = -Math.min(Math.max(0, -deltaY), tileHight);
                break;
            case LEFT:
                deltaX = -Math.min(Math.max(0, -deltaX), tileWidth);
                deltaY = 0;
                break;
            case RIGHT:
                deltaX = Math.min(Math.max(0, deltaX), tileWidth);
                deltaY = 0;
                break;
        }
        constrainedDeltas[0] = deltaX;
        constrainedDeltas[1] = deltaY;
        return constrainedDeltas;
    }

    public float[] snapFinalMovement(GridCalculations.Direction moveDirection, float deltaX, float deltaY) {
        float[] constrainedFinalDeltas = new float[2];

        switch (moveDirection){
            case DOWN:
                if (deltaY > (tileHight / 2)) {
                    deltaY = tileHight;
                } else {
                    deltaY = 0;
                }
                break;
            case UP:
                if (Math.abs(deltaY) > (tileHight / 2)) {
                    deltaY = -tileHight;
                } else {
                    deltaY = 0;
                }
                break;
            case LEFT:
                if (Math.abs(deltaX) > (tileWidth / 2)) {
                    deltaX = -tileWidth;
                } else {
                    deltaX = 0;
                }
                break;
            case RIGHT:
                if (deltaX > (tileWidth / 2)) {
                    deltaX = tileWidth;
                } else {
                    deltaX = 0;
                }
                break;
        }

        constrainedFinalDeltas[0] = deltaX;
        constrainedFinalDeltas[1] = deltaY;
        return constrainedFinalDeltas;
    }

    public float[] snapTapActionFinalMovement(GridCalculations.Direction moveDirection, float deltaX, float deltaY) {
        float[] constrainedFinalDeltas = new float[2];

        switch (moveDirection){
            case DOWN:
                deltaY = tileHight;
                break;
            case UP:
                deltaY = -tileHight;
                break;
            case LEFT:
                deltaX = -tileWidth;
                break;
            case RIGHT:
                deltaX = tileWidth;
                break;
        }

        constrainedFinalDeltas[0] = deltaX;
        constrainedFinalDeltas[1] = deltaY;
        return constrainedFinalDeltas;
    }



    public boolean isTouchAction(float maxDrag, float deltaX, float deltaY) {
        if (Math.sqrt((deltaX*deltaX) + (deltaY*deltaY)) < maxDrag) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean movementTriggeredTileUpdate(float deltaX, float deltaY) {
        if (deltaX != 0 || deltaY != 0){
            return true;
        }
        return false;
    }


}
