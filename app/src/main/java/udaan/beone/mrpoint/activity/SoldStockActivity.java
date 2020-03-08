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
import udaan.beone.mrpoint.util.BDUtil;
import udaan.beone.mrpoint.util.DataUtil;
import udaan.beone.mrpoint.util.EnumConst;


public class SoldStockActivity extends ActionStockActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        bunddle = StockItemL.buildMrStockDisplay(DataManager.instance().getOwnerStock());

        // Main Activity Init
        super.onCreate(savedInstanceState);

        gsActionTitle.setText("Sold Stock");
        gsActionBn.setText("Sold");
    }

    protected void doAction() {

        if(DataManager.instance().getLoginMemberAcNo() == 0l ||
                DataManager.instance().getSelectedMemberAcNo() == 0l) {
            AndroidUtil.logout(this);
            return;
        }

        DataManager.instance().setOwnerDataDirty();

        StockTransfer stockTransfer = new StockTransfer();
        stockTransfer.setStockItems(adapter.getItems());
        stockTransfer.setAuthAcNo(DataManager.instance().getLoginMemberAcNo());
        stockTransfer.setOwnerAcNo(DataManager.instance().getSelectedMemberAcNo());
        stockTransfer.setMrVisitId(DataManager.instance().getActiveVisit().getMrVisitId());
        stockTransfer.setStockTxType(EnumConst.StockTxType_Sold);

        HTTPTask task = new HTTPTask(new ActionStockCaller(this, "Sold Stock"),
                StockTransfer.JSONRepo.buildJSONString(stockTransfer),
                HTTPConst.VISIT_TRANSFER_STOCK, null);
        task.execute();
    }

    protected void doPostAction() {
        AndroidUtil.launchActivity(this, VisitActivity.class);
    }
}
