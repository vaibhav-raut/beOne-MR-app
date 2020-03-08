package udaan.beone.mrpoint.activity.fragment;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

import udaan.beone.mrpoint.data.DataManager;
import udaan.beone.mrpoint.http.util.HTTPConst;
import udaan.beone.mrpoint.http.util.HTTPTask;
import udaan.beone.mrpoint.util.AndroidUtil;

public class OwnerMrAccountFragment extends MrAccountFragment {
    private static OwnerMrAccountFragment instance;

    public static OwnerMrAccountFragment newInstance() {

//        if(instance == null) {
            instance = new OwnerMrAccountFragment();
//        }
        return instance;
    }

    public void initRefreshData() {
        if(DataManager.instance().isOwnerAccountDirty() || DataManager.instance().getOwnerAccount() == null) {

            if(DataManager.instance().getSelectedGroupAcNo() == 0l) {
                AndroidUtil.logout(getActivity());
                return;
            }

            List<String> urlArgs = new ArrayList<String>(1);
            urlArgs.add(Long.toString(DataManager.instance().getSelectedMemberAcNo()));

            HTTPTask task = new HTTPTask(new MrStockCaller(getActivity(), HTTPConst.ACCOUNT_GET_OWNER_MR_ACCOUNT),
                    null, HTTPConst.ACCOUNT_GET_OWNER_MR_ACCOUNT, urlArgs);
            task.execute();

        } else {
            reloadData();
        }
    }

    protected void reloadData() {
        // Set already Loaded Data Directly
        if(DataManager.instance().getOwnerAccount() != null) {

            if(adapter == null) {
                adapter = new MyRecyclerAdapter(getActivity(), DataManager.instance().getOwnerAccount());
                mRecyclerView.setAdapter(adapter);
            }
            else {
                adapter.setMrAccount(DataManager.instance().getOwnerAccount());
                adapter.notifyDataSetChanged();
//                mRecyclerView.invalidate();
            }
        }

        progressBar.setVisibility(View.GONE);
    }


}
