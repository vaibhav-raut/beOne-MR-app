package udaan.beone.mrpoint.activity;
/**
 * Author Vaibhav
 * Date 15/06/15
 */

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import udaan.beone.mrpoint.R;
import udaan.beone.mrpoint.data.DataManager;
import udaan.beone.mrpoint.http.model.MemberName;
import udaan.beone.mrpoint.http.util.HTTPCaller;
import udaan.beone.mrpoint.http.util.HTTPConst;
import udaan.beone.mrpoint.http.util.HTTPTask;
import udaan.beone.mrpoint.util.AndroidUtil;
import udaan.beone.mrpoint.util.DataUtil;


public class SelectMemberActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MyRecyclerAdapter adapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Main Activity Init
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_member);

        // Initialize recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.member_list_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        loadMemberNames();
    }

    private void loadMemberNames() {
        if(DataManager.instance().getAllSGMembers() == null) {

            if(DataManager.instance().getSelectedGroupAcNo() == 0l) {
                AndroidUtil.logout(this);
            }

            List<String> urlArgs = new ArrayList<String>(3);
            urlArgs.add("English");
            urlArgs.add(Long.toString(DataManager.instance().getSelectedGroupAcNo()));
            urlArgs.add("a");

            HTTPTask task = new HTTPTask(new AllMemberNameCaller(this, HTTPConst.MEMBER_GET_ALL_MEMBER_NAMES),
                    null, HTTPConst.MEMBER_GET_ALL_MEMBER_NAMES, urlArgs);
            task.execute();

        } else {
            // Set already Loaded Data Directly
            adapter = new SelectMemberActivity.MyRecyclerAdapter(this, DataManager.instance().getAllSGMembers().getMemberNames());
            mRecyclerView.setAdapter(adapter);

            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onPostResume () {
        super.onPostResume();
    }

    public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.CustomViewHolder> {
        private List<MemberName> memberNames;
        private Activity mContext;

        public MyRecyclerAdapter(Activity context, List<MemberName> memberNames) {
            this.memberNames = memberNames;
            this.mContext = context;
        }

        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rc_row_member_name, null);

            CustomViewHolder viewHolder = new CustomViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
            MemberName memberName = memberNames.get(i);

            //Setting text view title
            customViewHolder.memberAcView.setText(Html.fromHtml(
                    DataUtil.getDisplayMemberAcNo(DataManager.instance().getLoginInfoREST().getGroupDistrictCode(), memberName.getMemberAcNo())));
            customViewHolder.memberNameView.setText(Html.fromHtml(memberName.getMemberName()));
            customViewHolder.memberStatusView.setText(Html.fromHtml(memberName.getStatus()));

            // Add OnClickListener for the Card List
            customViewHolder.memberAcView.setOnClickListener(clickListener);
            customViewHolder.memberNameView.setOnClickListener(clickListener);
            customViewHolder.memberStatusView.setOnClickListener(clickListener);

            customViewHolder.memberAcView.setTag(customViewHolder);
            customViewHolder.memberNameView.setTag(customViewHolder);
            customViewHolder.memberStatusView.setTag(customViewHolder);
        }

        @Override
        public int getItemCount() {
            return (null != memberNames ? memberNames.size() : 0);
        }

        public class CustomViewHolder extends RecyclerView.ViewHolder {
            protected TextView memberAcView;
            protected TextView memberNameView;
            protected TextView memberStatusView;

            public CustomViewHolder(View view) {
                super(view);
                this.memberAcView = (TextView) view.findViewById(R.id.member_ac_no);
                this.memberNameView = (TextView) view.findViewById(R.id.member_name);
                this.memberStatusView = (TextView) view.findViewById(R.id.member_status);
            }
        }

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CustomViewHolder holder = (CustomViewHolder) view.getTag();
                int position = holder.getAdapterPosition();

                MemberName memberName = memberNames.get(position);
                Toast.makeText(mContext, "Selected : " + memberName.getMemberAcNo() + " : " + memberName.getMemberName(), Toast.LENGTH_SHORT).show();

                DataManager.instance().setOwnerDataDirty();
                DataManager.instance().setSelectedMemberAcNo(memberName.getMemberAcNo());

                if(DataManager.instance().getSelectedGroupAcNo() == 0l) {
                    AndroidUtil.logout(mContext);
                }
                AndroidUtil.launchActivity(mContext, HomeActivity.class);

                List<String> urlArgs = new ArrayList<>(1);
                urlArgs.add(Long.toString(DataManager.instance().getSelectedMemberAcNo()));
                urlArgs.add(Long.toString(DataManager.instance().getLoginMemberAcNo()));

//                // Load MrStock
//                HTTPTask task1 = new HTTPTask(new HTTPCaller.Dummy(), null, HTTPConst.ACCOUNT_GET_OWNER_MR_STOCK, urlArgs);
//                task1.execute();
//
//                // Load MrAccount
//                HTTPTask task2 = new HTTPTask(new HTTPCaller.Dummy(), null, HTTPConst.ACCOUNT_GET_OWNER_MR_ACCOUNT, urlArgs);
//                task2.execute();

                // Load MrAccount
                HTTPTask task3 = new HTTPTask(new HTTPCaller.PassiveDummy(mContext, HTTPConst.VISIT_GET_ACTIVE_VISITS_FOR_OWNER),
                        null, HTTPConst.VISIT_GET_ACTIVE_VISITS_FOR_OWNER, urlArgs);
                task3.execute();

            }
        };
    }

    protected class AllMemberNameCaller extends HTTPCaller {
        public AllMemberNameCaller(Activity activity, String call) {
            super(activity, call);
        }

        @Override
        public void httpSuccessCallback(String jsonResponce) {
            // Download complete. Let us update UI
            progressBar.setVisibility(View.GONE);

            if(DataManager.instance().getAllSGMembers() != null) {
                adapter = new SelectMemberActivity.MyRecyclerAdapter(activity, DataManager.instance().getAllSGMembers().getMemberNames());
                mRecyclerView.setAdapter(adapter);
            } else {
                Toast.makeText(activity, "Member List Not Loaded!", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void httpFailureCallback(String message) {
            Toast.makeText(activity, "Loading Select Member is Failed!", Toast.LENGTH_SHORT).show();
            AndroidUtil.launchActivity(activity, HomeActivity.class);
        }

        @Override
        public void httpErrorCallback(String message) {
            Toast.makeText(activity, "Loading Select Member is Failed!", Toast.LENGTH_SHORT).show();
            AndroidUtil.launchActivity(activity, HomeActivity.class);
        }
    }
}
