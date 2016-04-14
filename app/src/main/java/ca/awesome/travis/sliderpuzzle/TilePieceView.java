package ca.awesome.travis.sliderpuzzle;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by tco on 16-04-13.
 */
public class TilePieceView extends ImageView {

    private int id;
    private int row;
    private int column;
    private boolean empty = false;
    private int homeX;
    private int homeY;

    public TilePieceView(Context context, int id, int row, int column, boolean empty) {
        super(context);
        this.id = id;
        this.row = row;
        this.column = column;
        this.empty = empty;
    }

    public int getId(){
        return id;
    }

    public int getRow(){
        return row;
    }

    public void setRow(int row){
        this.row = row;
    }

    public int getColumn(){
        return column;
    }

    public void setColumn(int column){
        this.column = column;
    }

    public int getHomeX(){
        return homeX;
    }

    public void setHomeX(int homeX){
        this.homeX = homeX;
    }

    public int getHomeY(){
        return homeY;
    }

    public void setHomeY(int homeY){
        this.homeY = homeY;
    }

    public boolean isEmpty(){
        return empty;
    }
}
