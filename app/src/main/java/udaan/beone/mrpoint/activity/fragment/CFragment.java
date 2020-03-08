package udaan.beone.mrpoint.activity.fragment;

import android.support.v4.app.Fragment;

/**
 * Created by Vaibhav on 30-05-2016.
 */
public abstract class CFragment extends Fragment {

    public abstract void initRefreshData();
    protected abstract void reloadData();
}
