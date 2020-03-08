package udaan.beone.mrpoint.activity;
/**
 * Author Vaibhav
 * Date 15/06/15
 */

import android.os.Bundle;

import udaan.beone.mrpoint.data.DataManager;
import udaan.beone.mrpoint.http.model.StockItemL;
import udaan.beone.mrpoint.http.model.StockTransfer;
import udaan.beone.mrpoint.http.util.HTTPConst;
import udaan.beone.mrpoint.http.util.HTTPTask;
import udaan.beone.mrpoint.util.AndroidUtil;
import udaan.beone.mrpoint.util.EnumConst;


public class DirectSaleStockActivity extends ActionStockActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        bunddle = StockItemL.buildMrStockDisplay(DataManager.instance().getAuthStock());

        // Main Activity Init
        super.onCreate(savedInstanceState);

        gsActionTitle.setText("Direct Sale");
        gsActionBn.setText("Sale");
    }

    protected void doAction() {

        if(DataManager.instance().getLoginMemberAcNo() == 0l) {
            AndroidUtil.logout(this);
            return;
        }

        DataManager.instance().setDataDirty();

        StockTransfer stockTransfer = new StockTransfer();
        stockTransfer.setStockItems(adapter.getItems());
        stockTransfer.setOwnerAcNo(DataManager.instance().getLoginMemberAcNo());
        stockTransfer.setStockTxType(EnumConst.StockTxType_Sold);

        HTTPTask task = new HTTPTask(new ActionStockCaller(this, "Direct Sold Stock"),
                StockTransfer.JSONRepo.buildJSONString(stockTransfer),
                HTTPConst.VISIT_DIRECT_SALE, null);
        task.execute();
    }

    protected void doPostAction() {
        DataManager.instance().setMemberVisitAcDirty();
        AndroidUtil.launchActivity(this, HomeActivity.class);
    }
}
