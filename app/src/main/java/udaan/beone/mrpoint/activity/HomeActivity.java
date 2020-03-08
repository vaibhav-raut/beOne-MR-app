package udaan.beone.mrpoint.activity;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import udaan.beone.mrpoint.R;
import udaan.beone.mrpoint.activity.dialog.ActionDialog;
import udaan.beone.mrpoint.activity.dialog.OkDialog;
import udaan.beone.mrpoint.activity.fragment.CFragment;
import udaan.beone.mrpoint.activity.fragment.MrVisitAcFragment;
import udaan.beone.mrpoint.activity.fragment.OwnerMrAccountShortFragment;
import udaan.beone.mrpoint.activity.fragment.OwnerMrVisitAcFragment;
import udaan.beone.mrpoint.activity.fragment.OwnerStockFragment;
import udaan.beone.mrpoint.data.DataManager;
import udaan.beone.mrpoint.http.model.MrVisit;
import udaan.beone.mrpoint.http.model.s.MrVisitS;
import udaan.beone.mrpoint.http.util.HTTPCaller;
import udaan.beone.mrpoint.http.util.HTTPConst;
import udaan.beone.mrpoint.http.util.HTTPTask;
import udaan.beone.mrpoint.util.AndroidUtil;
import udaan.beone.mrpoint.util.EnumConst;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String SELECTED_ITEM_ID = "selected_item_id";
    private Toolbar mToolbar;
    private NavigationView mDrawer;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private int mSelectedId;

    private TabLayout mTabLayout;
    private ViewPager mPager;
    private HomePagerAdapter mAdapter;

//    private TextView mDLoginMember;
//    private TextView mDSelectedGroup;
//    private TextView mDSelectedMember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Main Activity Init
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Toolbar Init
        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);

        // Nav Drawer Init
        mDrawer = (NavigationView) findViewById(R.id.main_drawer);
        mDrawer.setNavigationItemSelectedListener(this);
//        mDrawer.setIc
        // Nav DrawerLayout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                mToolbar,
                R.string.drawer_open,
                R.string.drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        hideDrawer();

//        mSelectedId = savedInstanceState == null ? R.id.navigation_member_stock : savedInstanceState.getInt(SELECTED_ITEM_ID);
        mSelectedId = -1;
        navigate(mSelectedId);

        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mPager = (ViewPager) findViewById(R.id.pager);

        mAdapter = new HomePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mAdapter);
        mTabLayout.setTabsFromPagerAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mPager);
        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

//        mDLoginMember = (TextView) findViewById(R.id.d_login_member);
//        mDSelectedGroup = (TextView) findViewById(R.id.d_selected_group);
//        mDSelectedMember = (TextView) findViewById(R.id.d_selected_member);
    }

    @Override
    protected void onPostResume () {
        super.onPostResume();
    }

    private void showDrawer() {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }

    private void hideDrawer() {
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    private void navigate(int mSelectedId) {

        switch (mSelectedId) {
            case R.id.navigation_auth_stock: {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                if(DataManager.instance().getLoginMemberAcNo() == DataManager.instance().getSelectedMemberAcNo()) {

                    new OkDialog(this, "Can't Start Auth Account", "Auth Account is same as Selected Member Account!").show();
                } else {
                    AndroidUtil.launchActivity(this, AuthStockActivity.class);
                }
                break;
            }
            case R.id.navigation_select_group: {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                AndroidUtil.launchActivity(this, SelectGroupActivity.class);
                break;
            }
            case R.id.navigation_select_member: {
                mDrawerLayout.closeDrawer(GravityCompat.START);

                Toast.makeText(this, "Loading Select Member Page!", Toast.LENGTH_SHORT).show();
                // Launch Home Page
                AndroidUtil.launchActivity(this, SelectMemberActivity.class);
                break;
            }
            case R.id.navigation_visit: {
                mDrawerLayout.closeDrawer(GravityCompat.START);

                if (DataManager.instance().getSelectedMemberAcNo() == 0l ||
                        DataManager.instance().getLoginMemberAcNo() == DataManager.instance().getSelectedMemberAcNo()) {

                    new OkDialog(this, "Can't Start Visit", "Please Select Member to Start Visit!").show();

                } else {
                    if (DataManager.instance().getActiveVisit() == null ||
                            DataManager.instance().getActiveVisit().getVisitStatus().equals(EnumConst.VisitStatus_Ended)) {

                        new CreateVisitDialog(this).show();

                    } else if (DataManager.instance().getActiveVisit().getVisitStatus().equals(EnumConst.VisitStatus_Started)) {
                        AndroidUtil.launchActivity(this, VisitActivity.class);

                    } else {
                        new StartVisitDialog(this).show();
                    }
                }
                break;
            }
            case R.id.navigation_direct_sale: {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                AndroidUtil.launchActivity(this, DirectSaleStockActivity.class);
                break;
            }
            case R.id.navigation_owner_detail_stock: {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                AndroidUtil.launchActivity(this, OwnerDetailedStockActivity.class);
                break;
            }
            case R.id.navigation_item_info: {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                AndroidUtil.launchActivity(this, ItemInfoActivity.class);
                break;
            }
            case R.id.navigation_logout: {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                DataManager.instance().onLogout();
                AndroidUtil.launchActivity(this, LoginActivity.class);
                break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
//
//        mDLoginMember.setText("Login Member : " + DataManager.instance().getDisplayLoginMemberAcNo());
//        mDSelectedGroup.setText("Selected Group : " + DataManager.instance().getDisplaySelectedGroupAcNo());
//        mDSelectedMember.setText("Selected Member : " + DataManager.instance().getDisplaySelectedMemberAcNo());

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {

        menuItem.setChecked(true);
        mSelectedId = menuItem.getItemId();

        navigate(mSelectedId);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SELECTED_ITEM_ID, mSelectedId);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public static class CreateVisitDialog extends ActionDialog {
        public CreateVisitDialog(Activity context) {
            super(context, "Create Visit", "Member have no Active Visit!", "Cancel", "Create Visit");
        }
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ac_button_1:
                    this.dismiss();
                    break;

                case R.id.ac_button_2:
                    MrVisitS mrVisitS = MrVisit.buildCreateMrVisit();

                    if(mrVisitS == null) {
                        AndroidUtil.logout(this.context);
                        return;
                    }

                    // Load MrAccount
                    HTTPTask task = new HTTPTask(new CreateVisitCaller(context, HTTPConst.VISIT_CREATE_VISIT),
                            MrVisitS.JSONRepo.buildJSONString(mrVisitS),
                            HTTPConst.VISIT_CREATE_VISIT, null);
                    task.execute();

                    this.dismiss();
                    break;
            }
        }
    }
    public static class CreateVisitCaller extends HTTPCaller {
        CreateVisitCaller(Activity activity, String call) {
            super(activity, call);
        }
        @Override
        public void httpSuccessCallback(String jsonResponce) {
            new StartVisitDialog(activity).show();
        }
    }

    public static class StartVisitDialog extends ActionDialog {
        public StartVisitDialog(Activity context) {
            super(context, "Start Visit", "Member have Active Visit Ready to Start!", "Cancel", "Start Visit");
        }
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ac_button_1:
                    this.dismiss();
                    break;

                case R.id.ac_button_2:

                    MrVisitS mrVisitS = null;
                    if(DataManager.instance().getActiveVisit() != null) {
                        mrVisitS = DataManager.instance().getActiveVisit().buildMrVisitShort();
                    }

                    if(mrVisitS == null) {
                        AndroidUtil.logout(this.context);
                        return;
                    }
                    mrVisitS.setVisitStatus(EnumConst.VisitStatus_Started);

                    // Load MrAccount
                    HTTPTask task = new HTTPTask(new StartVisitCaller(context, HTTPConst.VISIT_START_VISIT),
                            MrVisitS.JSONRepo.buildJSONString(mrVisitS),
                            HTTPConst.VISIT_START_VISIT, null);
                    task.execute();

                    DataManager.instance().setOwnerStockDirty();

                    this.dismiss();
                    break;
            }
        }
    }
    public static class StartVisitCaller extends HTTPCaller {
        StartVisitCaller(Activity activity, String call) {
            super(activity, call);
        }
        @Override
        public void httpSuccessCallback(String jsonResponce) {
            AndroidUtil.launchActivity(activity, VisitActivity.class);
        }
    }

    protected static CFragment getPageFragment(int position) {
        switch(position) {
            case 0:
                return OwnerMrAccountShortFragment.newInstance();

            case 1:
                return OwnerStockFragment.newInstance();

            case 2:
                return OwnerMrVisitAcFragment.newInstance();
        }
        return null;
    }
}

class HomePagerAdapter extends FragmentStatePagerAdapter {

    private String[] tabName = {"Account", "Stock", "Visit Account"};

    public HomePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CFragment getItem(int position) {
        return HomeActivity.getPageFragment(position);
    }

    @Override
    public int getCount() {
        return tabName.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabName[position];
    }
}
