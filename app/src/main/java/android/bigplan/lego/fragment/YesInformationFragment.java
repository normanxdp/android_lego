package android.bigplan.lego.fragment;

import android.bigplan.lego.R;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.List;




/**
 * 我关注的专家列表
 * Created by Brian on 2016/7/6.
 */
public class YesInformationFragment extends Fragment {

    ListView listview;
    TextView nodataTv;

    RelativeLayout nodataLayout;
String actid="";

    public String getActid() {
        return actid;
    }

    public void setActid(String actid) {
        this.actid = actid;
    }
    String allyId="";

    public String getAllyId() {
        return allyId;
    }

    public void setAllyId(String allyId) {
        this.allyId = allyId;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_jihuashezhi, null);
        return view;
    }



    private void noData(boolean b) {

    }
}
