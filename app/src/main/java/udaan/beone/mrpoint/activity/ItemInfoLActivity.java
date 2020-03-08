package udaan.beone.mrpoint.activity;
/**
 * Author Vaibhav
 * Date 18/06/16
 */

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import udaan.beone.mrpoint.R;
import udaan.beone.mrpoint.http.util.HTTPConst;
import udaan.beone.mrpoint.http.util.HTTPTask;
import udaan.beone.mrpoint.util.KeyConst;


public class ItemInfoLActivity extends ItemInfoActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Main Activity Init
        super.onCreate(savedInstanceState);

        ((LinearLayout) findViewById(R.id.gs_stock_item_ll_1)).setVisibility(View.GONE);
        ((LinearLayout) findViewById(R.id.gs_stock_item_ll_2)).setVisibility(View.GONE);

        long stockItemId = getIntent().getLongExtra(KeyConst.STOCK_ITEM_ID, 0l);
        if (stockItemId == 0) {
            super.onPostResume();
        }

        progressBar = (ProgressBar) this.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        List<String> urlArgs1 = new ArrayList<>(1);
        urlArgs1.add(Long.toString(stockItemId));

        // Load MrStock
        HTTPTask task1 = new HTTPTask(new LoadItemCaller(this, HTTPConst.VISIT_GET_STOCK_ITEM), null,
                HTTPConst.VISIT_GET_STOCK_ITEM, urlArgs1);
        task1.execute();
    }
}
