package net.sxkeji.blacksearch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.squareup.okhttp.Request;

import net.sxkeji.blacksearch.adapters.CityRecyclerAdapter;
import net.sxkeji.blacksearch.adapters.ProvinceRecyclerAdapter;
import net.sxkeji.blacksearch.beans.BlackHospitalBean;
import net.sxkeji.blacksearch.beans.BlackPutianBean;
import net.sxkeji.blacksearch.beans.BlackhospitalsEntity;
import net.sxkeji.blacksearch.beans.ProvincesBean;
import net.sxkeji.blacksearch.beans.ProvincesListBean;
import net.sxkeji.blacksearch.http.HttpClient;
import net.sxkeji.blacksearch.http.HttpResponseHandler;
import net.sxkeji.blacksearch.utils.FileUtil;
import net.sxkeji.blacksearch.utils.GsonUtil;
import net.sxkeji.blacksearch.utils.ViewUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 城市选择器
 * Created by zhangshixin on 4/11/2016.
 */
public class CitySelectTabActivity extends AppCompatActivity {
    private final String TAG = "CitySelectTabActivity";

    @Bind(R.id.recycler_province)
    RecyclerView recyclerProvince;
    @Bind(R.id.recycler_city)
    RecyclerView recyclerCity;
    private Toolbar toolbar;
    private LinearLayoutManager provinceLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<ProvincesBean.CitysEntity> cityList;
    private ArrayList<ProvincesBean> allProvinceList;
    private BlackPutianBean.BlackhospitalsEntity localHospitalList;   //本地保存的医院数据
    private ArrayList<TextView> selectProvinceTextViewList; //保存添加到"当前选择"的省份，为了准备以后的点击事件
    private ProvinceRecyclerAdapter provinceAdapter;
    private CityRecyclerAdapter cityAdapter;
    private List<BlackhospitalsEntity> blackHospitals;    //从github上获取的医院名单列表
    private List<BlackhospitalsEntity.HospitalEntity> selectHospitals;
    private String provinceStr; //选择的省份名称
    private String cityStr; //选择的城市名称
    private BlackPutianBean blackPutianBean;        //另一种格式，本地的莆田医院数据
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical_select);
        ButterKnife.bind(this);
        getData();
        initViews();
        setListeners();
    }

    private void getData() {
        getBlackHospitalUrl();
    }

    private void getBlackHospitalUrl() {
        AVQuery<AVObject> query = new AVQuery<>("_File");
        query.whereEqualTo("name", "blackhospital.json");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    if (list.size()>0) {
                        AVObject avObject = list.get(0);
                        if (avObject != null)
                            url = avObject.getString("url");
                        getBlackHospitalData(url);
                    }
                    Log.e(TAG, "find success " + url);
                } else {
                    Toast.makeText(CitySelectTabActivity.this, "查询失败" + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getBlackHospitalData(String url) {
        Map<String, String> map = new HashMap<>();
        map.put("name", "zsx");
        HttpClient.builder(this).get(url, map, new HttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                if (!TextUtils.isEmpty(content)) {
                    BlackHospitalBean blackHospitalBean = (BlackHospitalBean) GsonUtil.jsonToBean(content, BlackHospitalBean.class);
                    blackHospitals = blackHospitalBean.getBlackhospitals();

                    Log.e(TAG, "onSuccess blackhospitals's size " + blackHospitals.size());
                } else {

                    Log.e(TAG, "onSuccess , but content is empty");
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
                super.onFailure(request, e);
                Log.e(TAG, "onFailure " + e.toString());
            }
        });
    }

    private void setListeners() {
    }

    private void initViews() {
        initProvinceData();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        selectProvinceTextViewList = new ArrayList<>();
        cityAdapter = new CityRecyclerAdapter(this);
        if (allProvinceList != null && allProvinceList.size() > 0) {
            cityAdapter.setData((ArrayList<ProvincesBean.CitysEntity>) allProvinceList.get(0).getCitys());
        }
        linearLayoutManager = new LinearLayoutManager(this);
        cityAdapter.setOnItemClickListener(new CityRecyclerAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(ProvincesBean.CitysEntity cityBean, int position) {
                cityStr = cityBean.getName();

                cityAdapter.setSelectPos(position);
                Log.e(TAG, "select city " + cityBean.getName());
                searchHospital(cityBean);
                showHospital(cityBean);
            }
        });
        recyclerCity.setLayoutManager(linearLayoutManager);
        recyclerCity.setAdapter(cityAdapter);

        provinceLayoutManager = new LinearLayoutManager(this);
        provinceAdapter = new ProvinceRecyclerAdapter(this, 0);
        provinceAdapter.setData(allProvinceList);
        provinceAdapter.setOnItemClickListener(new ProvinceRecyclerAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(ProvincesBean provinceBean, int position) {
                provinceStr = provinceBean.getName();

                ArrayList<ProvincesBean.CitysEntity> cityList = (ArrayList<ProvincesBean.CitysEntity>) provinceBean.getCitys();
                if (cityList != null) {
                    cityAdapter.setData(cityList);
                }
                provinceAdapter.changeSelecPos(position);       //改变选择背景状态

            }
        });
        recyclerProvince.setLayoutManager(provinceLayoutManager);
        recyclerProvince.setAdapter(provinceAdapter);
    }

    /**
     * 搜索该城市的医院列表
     *
     * @param cityBean
     */
    private void searchHospital(ProvincesBean.CitysEntity cityBean) {
        selectHospitals = new ArrayList<>();
        localHospitalList = new BlackPutianBean.BlackhospitalsEntity();
        String cityName = cityBean.getName();
        if (blackHospitals != null) {           //采用服务端数据
            for (BlackhospitalsEntity hospitalEntity : blackHospitals) {
                List<BlackhospitalsEntity.HospitalEntity> hospitalList = hospitalEntity.getHospital();
                for (BlackhospitalsEntity.HospitalEntity hospital : hospitalList) {
                    if (cityName.contains(hospital.getCity())) {
                        selectHospitals.add(hospital);
                        Log.e(TAG, "hospital " + hospital.getName());
                    }
                }
            }
        } else {         //采用本地数据
            String allprovinces = FileUtil.readAssets(this, "putian.json");
            if (!TextUtils.isEmpty(allprovinces)) {
                Log.e(TAG, "read assets putian.json" + allprovinces.toString());
                BlackPutianBean blackPutianBean = (BlackPutianBean) GsonUtil.jsonToBean(allprovinces, BlackPutianBean.class);
                if (blackPutianBean != null) {
                    List<BlackPutianBean.BlackhospitalsEntity> localBlackPutianList = blackPutianBean.getBlackhospitals();
                    if (localBlackPutianList != null && localBlackPutianList.size() > 0) {
                        for (BlackPutianBean.BlackhospitalsEntity localBlack : localBlackPutianList) {
                            BlackPutianBean.BlackhospitalsEntity.HospitalEntity hospital = localBlack.getHospital();
                            if (cityName.contains(hospital.getCity())) {
                                localHospitalList = localBlack;
                                return;
                            }
                        }

                    } else {
                        Log.e(TAG, "provincesList is null");
                    }
                }
            }
        }
    }

    /**
     * 显示医院列表
     *
     * @param cityBean
     */
    private void showHospital(ProvincesBean.CitysEntity cityBean) {
        List<String> localHospital = null;
        if (localHospitalList != null) {
            if (localHospitalList.getHospital() != null) {
                localHospital = localHospitalList.getHospital().getHospital();
            }
        }
        View view = ViewUtils.showPopupWindow(this, R.layout.pop_search_result, toolbar, 1);
        TextView tvProvince = (TextView) view.findViewById(R.id.tv_province);
        TextView tvCity = (TextView) view.findViewById(R.id.tv_city);
        TextView tvCount = (TextView) view.findViewById(R.id.tv_count);
        LinearLayout llHosputals = (LinearLayout) view.findViewById(R.id.ll_result);
        tvProvince.setText(provinceStr);
        tvCity.setText(cityStr);
        if (selectHospitals != null && selectHospitals.size() > 0) {
            tvCount.setText("共有 " + selectHospitals.size() + " 家");
            setupPopViewWithNet(llHosputals, view);
        } else if (localHospital != null && localHospital.size() > 0) {
            tvCount.setText("共有 " + localHospital.size() + " 家");
            setupPopViewWithLocal(llHosputals, view);
        } else {
            Toast.makeText(this, "恭喜你所在的城市没有莆田医院！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 使用本地的JSON格式数据填充
     *
     * @param llHosputals
     * @param view
     */

    private void setupPopViewWithLocal(LinearLayout llHosputals, View view) {
        List<String> hospitalList = localHospitalList.getHospital().getHospital();

        for (String hospitalName : hospitalList) {
            View hospitalDetail = View.inflate(this, R.layout.item_hospital_detail, null);
            TextView tvName = (TextView) hospitalDetail.findViewById(R.id.tv_name);
            TextView tvWebSite = (TextView) hospitalDetail.findViewById(R.id.tv_website);
            TextView tvtel = (TextView) hospitalDetail.findViewById(R.id.tv_tel);

            String name = hospitalName;

            if (!TextUtils.isEmpty(name)) {
                tvName.setText(name);
            }

            llHosputals.addView(hospitalDetail);
        }
    }

    /**
     * 使用服务器端的JSON格式数据填充数据
     *
     * @param llHosputals
     * @param view
     */
    private void setupPopViewWithNet(LinearLayout llHosputals, View view) {
        for (BlackhospitalsEntity.HospitalEntity hospital : selectHospitals) {
            View hospitalDetail = View.inflate(this, R.layout.item_hospital_detail, null);
            TextView tvName = (TextView) hospitalDetail.findViewById(R.id.tv_name);
            TextView tvWebSite = (TextView) hospitalDetail.findViewById(R.id.tv_website);
            TextView tvtel = (TextView) hospitalDetail.findViewById(R.id.tv_tel);

            String name = hospital.getName();
            String tel = hospital.getTel();
            String homepage = hospital.getHomepage();

            if (!TextUtils.isEmpty(name)) {
                tvName.setText(name);
            }
            if (!TextUtils.isEmpty(tel)) {
                tvtel.setText(tel);
                tvtel.setAutoLinkMask(Linkify.PHONE_NUMBERS);
                tvtel.setMovementMethod(LinkMovementMethod.getInstance());
            }
            if (!TextUtils.isEmpty(homepage)) {
                tvWebSite.setText(homepage);
                tvWebSite.setAutoLinkMask(Linkify.ALL);
                tvWebSite.setMovementMethod(LinkMovementMethod.getInstance());
            }

            llHosputals.addView(hospitalDetail);
        }
    }


    private void initProvinceData() {
        String allprovinces = FileUtil.readAssets(this, "allprovinces.json");
        if (!TextUtils.isEmpty(allprovinces)) {
            Log.e(TAG, "read assets " + allprovinces.toString());
            ProvincesListBean provincesListBean = (ProvincesListBean) GsonUtil.jsonToBean(allprovinces, ProvincesListBean.class);
            if (provincesListBean != null) {
                ArrayList<ProvincesBean> provincesList = provincesListBean.getProvincesList();
                if (provincesList != null && provincesList.size() > 0) {
                    allProvinceList = provincesList;
                } else {
                    Log.e(TAG, "provincesList is null");
                }
            }
        }
    }


}

