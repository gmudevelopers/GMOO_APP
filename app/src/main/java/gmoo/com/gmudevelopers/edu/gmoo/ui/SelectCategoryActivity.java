package gmoo.com.gmudevelopers.edu.gmoo.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import gmoo.com.gmudevelopers.edu.gmoo.R;
import gmoo.com.gmudevelopers.edu.gmoo.adapters.SelectCategory_Adapter;
import gmoo.com.gmudevelopers.edu.gmoo.helpers.RecyclerItemClickListener;
import gmoo.com.gmudevelopers.edu.gmoo.helpers.RecyclerViewClickListener;
import gmoo.com.gmudevelopers.edu.gmoo.model.SelectCategoryItem;

public class SelectCategoryActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private SelectCategory_Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_category_diaglog);

        mRecyclerView = (RecyclerView) findViewById(R.id.category);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)

        RecyclerViewClickListener listener = (view, position) -> {
            Toast.makeText(this, "["+position+"]",Toast.LENGTH_SHORT).show();
        };
        mAdapter = new SelectCategory_Adapter(items());
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Toast.makeText(SelectCategoryActivity.this, "["+position+"]",Toast.LENGTH_SHORT).show();
                    }
                })
        );



    }

    public ArrayList<SelectCategoryItem> items() {

        ArrayList<SelectCategoryItem> select_category = new ArrayList<SelectCategoryItem>();
        select_category.add(new SelectCategoryItem("Books",getResources().getDrawable(R.drawable.books)));
        select_category.add(new SelectCategoryItem("Electronics",getResources().getDrawable(R.drawable.electronics)));
        select_category.add(new SelectCategoryItem("Furniture",getResources().getDrawable(R.drawable.furniture)));
        select_category.add(new SelectCategoryItem("Sports and Outdoors",getResources().getDrawable(R.drawable.sports_and_doors)));
        select_category.add(new SelectCategoryItem("Fashion and Clothing",getResources().getDrawable(R.drawable.fashion_clothing)));
        select_category.add(new SelectCategoryItem("Services and Gigs",getResources().getDrawable(R.drawable.services)));
        select_category.add(new SelectCategoryItem("Other",getResources().getDrawable(R.drawable.other)));
        return select_category;
    }

    public void dismiss(View view) {
       // isDismissing = true;
        setResult(Activity.RESULT_CANCELED);
        finishAfterTransition();
    }

}
