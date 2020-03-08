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

public class AuthMrVisitAcFragment extends MrVisitAcFragment {

    public AuthMrVisitAcFragment() {
    }

    public static AuthMrVisitAcFragment newInstance() {
        return new AuthMrVisitAcFragment();
    }
    public boolean isMemberVisitAcDirty() {
        return DataManager.instance().isAuthVisitAcDirty();
    }
    public MemberVisitAc getMemberVisitAc() {
        return DataManager.instance().getAuthVisitAc();
    }
    public MrAccount getMemberAccount() {
        return DataManager.instance().getAuthAccount();
    }
    public long getMemberAcNo() {
        return DataManager.instance().getLoginMemberAcNo();
    }
    public void setMemberVisitAc(MemberVisitAc memberVisitAc) {
        DataManager.instance().setAuthVisitAc(memberVisitAc);
    }
}
