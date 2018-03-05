package gmoo.com.gmudevelopers.edu.gmoo.helpers;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    private int space;
 
    public DividerItemDecoration(int space) {
        this.space = space;
    }
 
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;
 
 
        //for vertical scrolling
      outRect.bottom = space;
      outRect.top = space;} }