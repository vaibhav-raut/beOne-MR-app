package udaan.beone.mrpoint.activity;
/**
 * Author Vaibhav
 * Date 15/06/16
 */

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import udaan.beone.mrpoint.R;
import udaan.beone.mrpoint.http.model.StockItem;
import udaan.beone.mrpoint.http.util.HTTPCaller;
import udaan.beone.mrpoint.http.util.HTTPConst;
import udaan.beone.mrpoint.http.util.HTTPTask;
import udaan.beone.mrpoint.util.DataUtil;


public class ItemInfoActivity extends AppCompatActivity implements OnClickListener {

    protected Button gsClearBn;
    protected Button gsDoneBn;
    protected Button gsLoadItemBn;

    protected EditText gsStockItemId;

    protected ProgressBar progressBar;

    protected LinearLayout itemDataTableLL;

    protected TextView stockItemIdTV;
    protected TextView ownerAcNoTV;
    protected TextView ownerNameTV;
//    protected TextView stockItemNameTV;
    protected TextView designNoTV;
    protected TextView stockTypeNameTV;
    protected TextView brandNameTV;
    protected TextView itemStatusTV;
    protected TextView itemConditionTV;
    protected TextView mrPriceTV;
    protected TextView disMrPriceTV;
    protected TextView mrpPriceTV;
    protected TextView disMrpPriceTV;
    protected TextView soldPriceTV;
    protected TextView mrSoldPriceTV;
    protected TextView discountAmTV;
    protected TextView vatAmTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Main Activity Init
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);

        gsClearBn = (Button) findViewById(R.id.gs_clear_bn);
        gsDoneBn = (Button) findViewById(R.id.gs_done_bn);
        gsLoadItemBn = (Button) findViewById(R.id.gs_load_item_bn);

        gsClearBn.setOnClickListener(this);
        gsDoneBn.setOnClickListener(this);
        gsLoadItemBn.setOnClickListener(this);

        gsStockItemId = (EditText) findViewById(R.id.gs_stock_item_id);

        itemDataTableLL = ((LinearLayout) findViewById(R.id.item_data_table));

        stockItemIdTV = ((TextView) findViewById(R.id.stock_item_id_v));
        ownerAcNoTV = ((TextView) findViewById(R.id.owner_ac_no_v));
        ownerNameTV = ((TextView) findViewById(R.id.owner_name_v));
//        stockItemNameTV = ((TextView) findViewById(R.id.stock_item_name_v));
        designNoTV = ((TextView) findViewById(R.id.design_no_v));
        stockTypeNameTV = ((TextView) findViewById(R.id.stock_type_name_v));
        brandNameTV = ((TextView) findViewById(R.id.brand_name_v));
        itemStatusTV = ((TextView) findViewById(R.id.item_status_v));
        itemConditionTV = ((TextView) findViewById(R.id.item_condition_v));
        mrPriceTV = ((TextView) findViewById(R.id.mr_price_v));
        disMrPriceTV = ((TextView) findViewById(R.id.dis_mr_price_v));
        mrpPriceTV = ((TextView) findViewById(R.id.mrp_price_v));
        disMrpPriceTV = ((TextView) findViewById(R.id.dis_mrp_price_v));
        soldPriceTV = ((TextView) findViewById(R.id.sold_price_v));
        mrSoldPriceTV = ((TextView) findViewById(R.id.mr_sold_price_v));
        discountAmTV = ((TextView) findViewById(R.id.discount_am_v));
        vatAmTV = ((TextView) findViewById(R.id.vat_am_v));
    }

    @Override
    public void onClick(View var1) {

        switch(var1.getId()) {

            case R.id.gs_clear_bn: {
                itemDataTableLL.setVisibility(View.GONE);
                break;
            }

            case R.id.gs_done_bn: {

                super.onBackPressed();
                break;
            }

            case R.id.gs_load_item_bn: {

                String stockItemStr = gsStockItemId.getText().toString();
                if (stockItemStr == null || stockItemStr.isEmpty()) {
                    Toast.makeText(this, "Please enter Stock Item Id before Load!", Toast.LENGTH_SHORT).show();
                    return;
                }

                long stockItemId = Long.parseLong(stockItemStr);
                gsStockItemId.setText("");

                if (stockItemId <= 0) {
                    Toast.makeText(this, "Invalid Stock Item Id!", Toast.LENGTH_LONG).show();
                    return;
                }
                gsStockItemId.setText("");

                gsClearBn.setEnabled(false);
                gsDoneBn.setEnabled(false);
                gsLoadItemBn.setEnabled(false);

                progressBar = (ProgressBar) this.findViewById(R.id.progress_bar);
                progressBar.setVisibility(View.VISIBLE);

                List<String> urlArgs1 = new ArrayList<>(1);
                urlArgs1.add(Long.toString(stockItemId));

                // Load MrStock
                HTTPTask task1 = new HTTPTask(new LoadItemCaller(this, HTTPConst.VISIT_GET_STOCK_ITEM), null,
                        HTTPConst.VISIT_GET_STOCK_ITEM, urlArgs1);
                task1.execute();

                break;
            }
        }
    }

    @Override
    protected void onPostResume () {
        super.onPostResume();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    protected void displayItemInfo(StockItem item) {
        if(item != null) {
            itemDataTableLL.setVisibility(View.VISIBLE);

            stockItemIdTV.setText(DataUtil.toString(item.getStockItemId()));
            ownerAcNoTV.setText(item.getOwnerAcNoDisplay());
            ownerNameTV.setText(item.getOwnerName());
//            stockItemNameTV.setText(item.getStockItemName());
            designNoTV.setText(item.getDesignNo());
            stockTypeNameTV.setText(item.getStockTypeName());
            brandNameTV.setText(item.getBrandName());
            itemStatusTV.setText(item.getItemStatus());
            itemConditionTV.setText(item.getItemCondition());
            mrPriceTV.setText(DataUtil.toString(item.getMrPrice()));
            disMrPriceTV.setText(DataUtil.toString(item.getDisMrPrice()));
            mrpPriceTV.setText(DataUtil.toString(item.getMrpPrice()));
            disMrpPriceTV.setText(DataUtil.toString(item.getDisMrpPrice()));
            soldPriceTV.setText(DataUtil.toString(item.getSoldPrice()));
            mrSoldPriceTV.setText(DataUtil.toString(item.getMrSoldPrice()));
            discountAmTV.setText(DataUtil.toString(item.getDiscountAm()));
            vatAmTV.setText(DataUtil.toString(item.getVatAm()));
        }
        else {
            itemDataTableLL.setVisibility(View.GONE);
        }
    }

    public class LoadItemCaller extends HTTPCaller {
        private ItemInfoActivity activity;

        public LoadItemCaller(ItemInfoActivity activity, String call) {
            super(activity, call);
            this.activity = activity;
        }
        public void httpSuccessCallback(String jsonResponce){

            if(jsonResponce != null && !jsonResponce.isEmpty()) {
                StockItem item = (StockItem) StockItem.JSONRepo.buildObject(jsonResponce);
                if(item != null) {
                    displayItemInfo(item);
                    Toast.makeText(activity, call + " is Successful!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(activity, "Item Data Not Available!", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(activity, "Item Data Not Available!", Toast.LENGTH_SHORT).show();
            }

            progressBar.setVisibility(View.GONE);

            gsClearBn.setEnabled(true);
            gsDoneBn.setEnabled(true);
            gsLoadItemBn.setEnabled(true);
        }
        public void httpFailureCallback(String message) {
            progressBar.setVisibility(View.GONE);

            gsClearBn.setEnabled(true);
            gsDoneBn.setEnabled(true);
            gsLoadItemBn.setEnabled(true);

            Log.e("Udaan", "Call: " + call + " is Failed!");
            Toast.makeText(activity, "Call: " + call + " is Failed! Please try Again!", Toast.LENGTH_SHORT).show();
        }
        public void httpErrorCallback(String message) {
            progressBar.setVisibility(View.GONE);

            gsClearBn.setEnabled(true);
            gsDoneBn.setEnabled(true);
            gsLoadItemBn.setEnabled(true);

            Log.e("Udaan", "Call: " + call + " is Failed!");
            Toast.makeText(activity, "Call: " + call + " is Failed! Please try Again!", Toast.LENGTH_SHORT).show();
        }
    }
}
