package udaan.beone.mrpoint.activity.fragment;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

import udaan.beone.mrpoint.data.DataManager;
import udaan.beone.mrpoint.http.util.HTTPConst;
import udaan.beone.mrpoint.http.util.HTTPTask;
import udaan.beone.mrpoint.util.AndroidUtil;
import udaan.beone.mrpoint.util.DataUtil;

public class OwnerStockFragment extends StockFragment {

    public static OwnerStockFragment newInstance() {

        return new OwnerStockFragment();
    }

    public void initRefreshData() {
        if(DataManager.instance().isOwnerStockDirty() ||
                DataManager.instance().getOwnerStockDisplay() == null ||
                DataManager.instance().getOwnerStockDisplay().getDisplayItems() == null) {

            if(DataManager.instance().getSelectedGroupAcNo() == 0l) {
                AndroidUtil.logout(getActivity());
                return;
            }

            List<String> urlArgs = new ArrayList<String>(1);
            urlArgs.add(Long.toString(DataManager.instance().getSelectedMemberAcNo()));

            HTTPTask task = new HTTPTask(new MrStockCaller(getActivity(), HTTPConst.ACCOUNT_GET_OWNER_MR_STOCK),
                    null, HTTPConst.ACCOUNT_GET_OWNER_MR_STOCK, urlArgs);
            task.execute();

        } else {
            reloadData();
        }
    }

    protected void reloadData() {
        // Set already Loaded Data Directly
        if(DataManager.instance().getOwnerStockDisplay() != null &&
                DataManager.instance().getOwnerStockDisplay().getDisplayItems() != null) {

            if(adapter == null) {
                adapter = new MyRecyclerAdapter(getActivity(), DataManager.instance().getOwnerStockDisplay().getDisplayItems());
                mRecyclerView.setAdapter(adapter);
            }
            else {
                adapter.setDisplayItems(DataManager.instance().getOwnerStockDisplay().getDisplayItems());
                adapter.notifyDataSetChanged();
            }

            mTotalNo.setText(Integer.toString(DataManager.instance().getOwnerStockDisplay().getCumNoStock()));
            mTotalWSP.setText(DataUtil.roundHalfUp(DataManager.instance().getOwnerStockDisplay().getCumMrPrice(), 0).toString());
        }

        progressBar.setVisibility(View.GONE);
    }

}
