package udaan.beone.mrpoint.activity.fragment;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

import udaan.beone.mrpoint.data.DataManager;
import udaan.beone.mrpoint.http.util.HTTPConst;
import udaan.beone.mrpoint.http.util.HTTPTask;
import udaan.beone.mrpoint.util.AndroidUtil;
import udaan.beone.mrpoint.util.DataUtil;

public class AuthStockFragment extends StockFragment {

    public static AuthStockFragment newInstance() {

        return new AuthStockFragment();
    }

    public void initRefreshData() {
        if(DataManager.instance().isAuthStockDirty() ||
                DataManager.instance().getAuthStockDisplay() == null ||
                DataManager.instance().getAuthStockDisplay().getDisplayItems() == null) {

            if(DataManager.instance().getSelectedGroupAcNo() == 0l) {
                AndroidUtil.logout(getActivity());
                return;
            }

            List<String> urlArgs = new ArrayList<String>(1);
            urlArgs.add(Long.toString(DataManager.instance().getSelectedMemberAcNo()));

            HTTPTask task = new HTTPTask(new MrStockCaller(getActivity(), HTTPConst.ACCOUNT_GET_AUTH_MR_ACCOUNT),
                    null, HTTPConst.ACCOUNT_GET_AUTH_MR_ACCOUNT, urlArgs);
            task.execute();

        } else {
            reloadData();
        }
    }

    protected void reloadData() {
        // Set already Loaded Data Directly
        if(DataManager.instance().getAuthStockDisplay() != null &&
                DataManager.instance().getAuthStockDisplay().getDisplayItems() != null) {

            if(adapter == null) {
                adapter = new MyRecyclerAdapter(getActivity(), DataManager.instance().getAuthStockDisplay().getDisplayItems());
                mRecyclerView.setAdapter(adapter);
            }
            else {
                adapter.setDisplayItems(DataManager.instance().getAuthStockDisplay().getDisplayItems());
                adapter.notifyDataSetChanged();
            }

            mTotalNo.setText(Integer.toString(DataManager.instance().getAuthStockDisplay().getCumNoStock()));
            mTotalWSP.setText(DataUtil.roundHalfUp(DataManager.instance().getAuthStockDisplay().getCumMrPrice(), 0).toString());
        }
        else {
            mTotalNo.setText("0");
            mTotalWSP.setText("0");
        }

        progressBar.setVisibility(View.GONE);
    }

}
