package udaan.beone.mrpoint.activity;
/**
 * Author Vaibhav
 * Date 15/06/15
 */

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import udaan.beone.mrpoint.R;
import udaan.beone.mrpoint.activity.adapter.SelectedStockAdapter;
import udaan.beone.mrpoint.data.DataManager;
import udaan.beone.mrpoint.http.model.StockItemL;
import udaan.beone.mrpoint.http.util.HTTPCaller;
import udaan.beone.mrpoint.http.util.HTTPConst;
import udaan.beone.mrpoint.http.util.HTTPTask;
import udaan.beone.mrpoint.util.AndroidUtil;
import udaan.beone.mrpoint.util.BDUtil;
import udaan.beone.mrpoint.util.DataUtil;


public abstract class ActionStockActivity extends AppCompatActivity implements OnClickListener, SelectedStockAdapter.OnSelectStockListener{

    protected TextView gsActionTitle;

    protected Button gsClearAllBn;
    protected Button gsCancelBn;
    protected Button gsActionBn;
    protected Button gsSelectItemBn;

    protected EditText gsStockItemId;

    protected TextView gsTotalStockAm;
    protected TextView gsTotalStockNo;
    protected TextView gsSelectedStockAm;
    protected TextView gsSelectedStockNo;

    protected RecyclerView mRecyclerView;
    protected SelectedStockAdapter adapter;

    protected ProgressBar progressBar;

    protected StockItemL.StockItemsBunddle bunddle;
    protected BigDecimal selectedStockAm = DataUtil.ZERO_BIG_DECIMAL;
    protected int selectedStockNo = DataUtil.ZERO_INTEGER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Main Activity Init
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_stock);

        gsActionTitle = (TextView) findViewById(R.id.gs_action_title);

        gsClearAllBn = (Button) findViewById(R.id.gs_clear_all_bn);
        gsCancelBn = (Button) findViewById(R.id.gs_cancel_bn);
        gsActionBn = (Button) findViewById(R.id.gs_action_bn);
        gsSelectItemBn = (Button) findViewById(R.id.gs_select_item_bn);

        gsStockItemId = (EditText) findViewById(R.id.gs_stock_item_id);

        gsTotalStockAm = (TextView) findViewById(R.id.gs_total_stock_am);
        gsTotalStockNo = (TextView) findViewById(R.id.gs_total_stock_no);
        gsSelectedStockAm = (TextView) findViewById(R.id.gs_selected_stock_am);
        gsSelectedStockNo = (TextView) findViewById(R.id.gs_selected_stock_no);

        // Initialize recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.selected_stock_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new SelectedStockAdapter(this);
        mRecyclerView.setAdapter(adapter);

        gsClearAllBn.setOnClickListener(this);
        gsCancelBn.setOnClickListener(this);
        gsActionBn.setOnClickListener(this);
        gsSelectItemBn.setOnClickListener(this);

        gsTotalStockAm.setText(DataUtil.roundHalfUp(bunddle.getTotalStockMrAm(), 0).toString());
        gsTotalStockNo.setText(Integer.toString(bunddle.getTotalStockNo()));
        gsSelectedStockAm.setText(DataUtil.roundHalfUp(selectedStockAm, 0).toString());
        gsSelectedStockNo.setText(Integer.toString(selectedStockNo));
    }

    protected abstract void doAction();

    protected abstract void doPostAction();

    @Override
    public void onClick(View var1) {

        switch(var1.getId()) {

            case R.id.gs_clear_all_bn: {

                selectedStockAm = DataUtil.ZERO_BIG_DECIMAL;
                selectedStockNo = 0;
                gsSelectedStockAm.setText(DataUtil.roundHalfUp(selectedStockAm, 0).toString());
                gsSelectedStockNo.setText(Integer.toString(selectedStockNo));

                if(adapter != null) {
                    adapter.clearAllItems();
                }

                break;
            }

            case R.id.gs_cancel_bn: {

                clear();
                doPostAction();
                break;
            }

            case R.id.gs_action_bn: {

                if(adapter != null && adapter.getItems() != null && adapter.getItems().size() > 0) {

                    gsClearAllBn.setEnabled(false);
                    gsCancelBn.setEnabled(false);
                    gsActionBn.setEnabled(false);
                    gsSelectItemBn.setEnabled(false);

                    progressBar = (ProgressBar) this.findViewById(R.id.progress_bar);
                    progressBar.setVisibility(View.VISIBLE);

                    doAction();
                }
                else {
                    Toast.makeText(this, "Please Select some Items to Give!", Toast.LENGTH_SHORT).show();
                }

                break;
            }

            case R.id.gs_select_item_bn: {

                String stockItemStr = gsStockItemId.getText().toString();
                if (stockItemStr == null || stockItemStr.isEmpty()) {
                    Toast.makeText(this, "Please enter Stock Item Id before Select!", Toast.LENGTH_SHORT).show();
                    return;
                }

                long stockItemId = Long.parseLong(stockItemStr);
                gsStockItemId.setText("");

                if (stockItemId <= 0) {
                    Toast.makeText(this, "Invalid Stock Item Id!", Toast.LENGTH_LONG).show();
                    gsStockItemId.setText("");
                    return;
                }
                if (!bunddle.getStockItemMap().containsKey(stockItemId)) {
                    Toast.makeText(this, "Stock Item Don't belong to Auth!", Toast.LENGTH_LONG).show();
                    return;
                }
                gsStockItemId.setText("");

                if (adapter.addItem(bunddle.getStockItemMap().get(stockItemId))) {

                    selectedStockAm = BDUtil.add(selectedStockAm, bunddle.getStockItemMap().get(stockItemId).getDisMrPrice());
                    selectedStockNo++;
                    gsSelectedStockAm.setText(DataUtil.roundHalfUp(selectedStockAm, 0).toString());
                    gsSelectedStockNo.setText(Integer.toString(selectedStockNo));
                }
                else {
                    Toast.makeText(this, "Stock Item Id is Already Selected!", Toast.LENGTH_SHORT).show();
                }

                break;
            }
        }
    }

    @Override
    public void onSelectStockDelete(StockItemL item) {

        selectedStockAm = BDUtil.sub(selectedStockAm, item.getDisMrPrice());
        selectedStockNo--;
        gsSelectedStockAm.setText(DataUtil.roundHalfUp(selectedStockAm, 0).toString());
        gsSelectedStockNo.setText(Integer.toString(selectedStockNo));
    }

    @Override
    protected void onPostResume () {
        super.onPostResume();
    }
    @Override
    public void onBackPressed() {
    }

    private void clear() {
        if(bunddle != null) {
            bunddle.clear();
        }
        if(adapter != null) {
            adapter.clearAllItems();
        }

        gsClearAllBn = null;
        gsCancelBn = null;
        gsActionBn = null;
        gsSelectItemBn = null;
        gsStockItemId = null;
        gsTotalStockAm = null;
        gsTotalStockNo = null;
        gsSelectedStockAm = null;
        gsSelectedStockNo = null;
        mRecyclerView = null;
        adapter = null;
    }

    public class ActionStockCaller extends HTTPCaller {
        private ActionStockActivity activity;

        public ActionStockCaller(ActionStockActivity activity, String call) {
            super(activity, call);
            this.activity = activity;
        }
        public void httpSuccessCallback(String jsonResponce){

            if(DataManager.instance().isAuthStockDirty()) {

                if(DataManager.instance().getLoginMemberAcNo() == 0l) {
                    AndroidUtil.logout(activity);
                    return;
                }
                List<String> urlArgs1 = new ArrayList<>(1);
                urlArgs1.add(Long.toString(DataManager.instance().getLoginMemberAcNo()));

                // Load MrStock
                HTTPTask task1 = new HTTPTask(new HTTPCaller.PassiveDummy(activity, HTTPConst.ACCOUNT_GET_AUTH_MR_STOCK), null,
                        HTTPConst.ACCOUNT_GET_AUTH_MR_STOCK, urlArgs1);
                task1.execute();
            }

            if(adapter != null) {
                adapter.clearAllItems();
            }

            progressBar.setVisibility(View.GONE);
            Toast.makeText(activity, gsActionTitle.getText().toString() + " is Successful!", Toast.LENGTH_SHORT).show();

            gsClearAllBn.setEnabled(true);
            gsCancelBn.setEnabled(true);
            gsActionBn.setEnabled(true);
            gsSelectItemBn.setEnabled(true);
        }
        public void httpFailureCallback(String message) {
            progressBar.setVisibility(View.GONE);

            gsClearAllBn.setEnabled(true);
            gsCancelBn.setEnabled(true);
            gsActionBn.setEnabled(true);
            gsSelectItemBn.setEnabled(true);

            Log.e("Udaan", "Call: " + call + " is Failed!");
            Toast.makeText(activity, "Call: " + call + " is Failed! Please try Again!", Toast.LENGTH_SHORT).show();
        }
        public void httpErrorCallback(String message) {
            progressBar.setVisibility(View.GONE);

            gsClearAllBn.setEnabled(true);
            gsCancelBn.setEnabled(true);
            gsActionBn.setEnabled(true);
            gsSelectItemBn.setEnabled(true);

            Log.e("Udaan", "Call: " + call + " is Failed!");
            Toast.makeText(activity, "Call: " + call + " is Failed! Please try Again!", Toast.LENGTH_SHORT).show();
        }
    }
}
