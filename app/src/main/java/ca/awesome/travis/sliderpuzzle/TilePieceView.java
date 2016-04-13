package ca.awesome.travis.sliderpuzzle;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by tco on 16-04-13.
 */
public class TilePieceView extends ImageView {

    private int row;
    private int column;
    private boolean empty = false;

    public TilePieceView(Context context, int row, int column, boolean empty) {
        super(context);
        this.row = row;
        this.column = column;
        this.empty = empty;
    }

    public int getRow(){
        return row;
    }

    public int getColumn(){
        return column;
    }

    public boolean isEmpty(){
        return empty;
    }

    public void setEmpty(boolean isEmpty){
        this.empty = isEmpty;
        if (empty){
            setBackground(null);
        }
    }

}
