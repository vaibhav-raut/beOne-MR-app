package udaan.beone.mrpoint.activity;
/**
 * Author Vaibhav
 * Date 15/06/15
 */

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import udaan.beone.mrpoint.data.DataManager;
import udaan.beone.mrpoint.http.model.StockItemL;
import udaan.beone.mrpoint.http.model.StockTransfer;
import udaan.beone.mrpoint.http.util.HTTPConst;
import udaan.beone.mrpoint.http.util.HTTPTask;
import udaan.beone.mrpoint.util.AndroidUtil;
import udaan.beone.mrpoint.util.BDUtil;
import udaan.beone.mrpoint.util.DataUtil;
import udaan.beone.mrpoint.util.EnumConst;


public class SoldAllStockActivity extends ActionStockActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        bunddle = StockItemL.buildMrStockDisplay(DataManager.instance().getOwnerStock());

        // Main Activity Init
        super.onCreate(savedInstanceState);

        gsActionTitle.setText("Sold Stock");
        gsActionBn.setText("Sold");

        List<StockItemL> stockItems = new ArrayList<StockItemL>();
        for(StockItemL item : bunddle.getStockItemMap().values()) {
            if(item.getCheckFlag() != EnumConst.CheckFlag_Checked) {
                stockItems.add(item);
            }
        }
        Collections.sort(stockItems, new Comparator<StockItemL>(){
            public int compare(StockItemL o1, StockItemL o2){
                return (int)(o1.getStockItemId() - o2.getStockItemId());
            }
        });
        for(StockItemL stockItem : stockItems) {
            if (adapter.addItem(stockItem)) {

                selectedStockAm = BDUtil.add(selectedStockAm, stockItem.getDisMrPrice());
                selectedStockNo++;
                gsSelectedStockAm.setText(DataUtil.roundHalfUp(selectedStockAm, 0).toString());
                gsSelectedStockNo.setText(Integer.toString(selectedStockNo));
            }
        }
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
