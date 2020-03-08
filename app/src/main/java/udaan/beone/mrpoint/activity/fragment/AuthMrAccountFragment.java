package udaan.beone.mrpoint.activity.fragment;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

import udaan.beone.mrpoint.data.DataManager;
import udaan.beone.mrpoint.http.util.HTTPConst;
import udaan.beone.mrpoint.http.util.HTTPTask;
import udaan.beone.mrpoint.util.AndroidUtil;

public class AuthMrAccountFragment extends MrAccountFragment {

    public static AuthMrAccountFragment newInstance() {
        return new AuthMrAccountFragment();
    }

    public void initRefreshData() {
        if(DataManager.instance().isAuthAccountDirty() || DataManager.instance().getAuthAccount() == null) {

            if(DataManager.instance().getLoginMemberAcNo() == 0l) {
                AndroidUtil.logout(getActivity());
                return;
            }

            List<String> urlArgs = new ArrayList<String>(1);
            urlArgs.add(Long.toString(DataManager.instance().getLoginMemberAcNo()));

            HTTPTask task = new HTTPTask(new MrStockCaller(getActivity(), HTTPConst.ACCOUNT_GET_AUTH_MR_ACCOUNT),
                    null, HTTPConst.ACCOUNT_GET_AUTH_MR_ACCOUNT, urlArgs);
            task.execute();

        } else {
            reloadData();
        }
    }

    protected void reloadData() {
        // Set already Loaded Data Directly
        if(DataManager.instance().getAuthAccount() != null) {

            if(adapter == null) {
                adapter = new MyRecyclerAdapter(getActivity(), DataManager.instance().getAuthAccount());
                mRecyclerView.setAdapter(adapter);
            }
            else {
                adapter.setMrAccount(DataManager.instance().getAuthAccount());
                adapter.notifyDataSetChanged();
            }
        }

        progressBar.setVisibility(View.GONE);
    }

}
