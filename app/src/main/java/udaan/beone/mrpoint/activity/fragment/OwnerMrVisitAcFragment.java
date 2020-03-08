package udaan.beone.mrpoint.activity.fragment;

import java.util.ArrayList;
import java.util.List;

import udaan.beone.mrpoint.data.DataManager;
import udaan.beone.mrpoint.http.model.MemberVisitAc;
import udaan.beone.mrpoint.http.model.MrAccount;
import udaan.beone.mrpoint.http.util.HTTPConst;
import udaan.beone.mrpoint.http.util.HTTPTask;
import udaan.beone.mrpoint.util.DateUtil;
import udaan.beone.mrpoint.util.EnumConst;

public class OwnerMrVisitAcFragment extends MrVisitAcFragment {

    public OwnerMrVisitAcFragment() {
    }

    public static OwnerMrVisitAcFragment newInstance() {
        return new OwnerMrVisitAcFragment();
    }
    public boolean isMemberVisitAcDirty() {
        return DataManager.instance().isOwnerVisitAcDirty();
    }
    public MemberVisitAc getMemberVisitAc() {
        return DataManager.instance().getOwnerVisitAc();
    }
    public MrAccount getMemberAccount() {
        return DataManager.instance().getOwnerAccount();
    }
    public long getMemberAcNo() {
        return DataManager.instance().getSelectedMemberAcNo();
    }
    public void setMemberVisitAc(MemberVisitAc memberVisitAc) {
        DataManager.instance().setOwnerVisitAc(memberVisitAc);
    }
}
