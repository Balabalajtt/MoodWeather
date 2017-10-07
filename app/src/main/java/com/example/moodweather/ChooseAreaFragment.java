package com.example.moodweather;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moodweather.db.City;
import com.example.moodweather.db.County;
import com.example.moodweather.db.Province;
import com.example.moodweather.util.HttpUtil;
import com.example.moodweather.util.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class ChooseAreaFragment extends Fragment {

    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;
    public static final int LEVEL_SEARCH = 3;

    private ProgressDialog progressDialog;
    private TextView titleText;
    private Button backButton;
    private ListView listView;

    private ArrayAdapter<String> adapter;
    private List<String> dataList = new ArrayList<>();

    private List<Province> provinceList;
    private List<City> cityList;
    private List<County> countyList;
    private List<JSONObject> searchList = new ArrayList<>();

    private Province selectedProvince;
    private City selectedCity;

    private int currentLevel;

    private EditText editText;
    private Button searchButton;


    /**
     * 加载布局
     * 获取控件实例
     * 初始设置适配器
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.choose_area, container, false);

        titleText = (TextView) view.findViewById(R.id.title_text);
        backButton = (Button) view.findViewById(R.id.back_button);
        listView = (ListView) view.findViewById(R.id.list_view);
        editText = (EditText) view.findViewById(R.id.city_input_edit_text);
        searchButton = (Button) view.findViewById(R.id.search_city_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editText.getText().toString();
                if (!TextUtils.isEmpty(name)) {
                    String searchUrl = "https://api.heweather.com/v5/search?city="
                            + name + "&key=46c949f3635e455c890828d1ba60311c";
                    HttpUtil.sendOkHttpRequest(searchUrl, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(), "搜索城市失败",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String responseText = response.body().string();
                            Log.d("search", "onResponse: " + responseText);

                            try {
                                JSONArray he = new JSONObject(responseText).getJSONArray("HeWeather5");
                                searchList.clear();
                                for (int i = 0; i < he.length(); i++) {
                                    JSONObject basic = (JSONObject) he.getJSONObject(i);
                                    searchList.add(basic);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if (searchList.size() > 0) {
                                dataList.clear();
                                for (JSONObject basic : searchList) {
                                    try {
                                        JSONObject b = basic.getJSONObject("basic");
                                        dataList.add(b.getString("prov") + " - " + b.getString("city"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        InputMethodManager inputMethodManager =(InputMethodManager)getContext().getApplicationContext().
                                                getSystemService(MainActivity.INPUT_METHOD_SERVICE);
                                        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                                        if (!dataList.isEmpty()) {
                                            currentLevel = LEVEL_SEARCH;

                                        } else {
                                            Toast.makeText(getContext(), "未搜索到此城市",
                                                    Toast.LENGTH_SHORT).show();
                                            if (currentLevel == LEVEL_CITY) {
                                                queryCities();
                                            } else if (currentLevel == LEVEL_COUNTY) {
                                                queryCounties();
                                            } else {
                                                queryProvinces();
                                            }
                                        }
                                        adapter.notifyDataSetChanged();
                                        listView.setSelection(0);
                                    }
                                });

                            }
                        }

                    });
                }
            }
        });
//        RelativeLayout headLayout = (RelativeLayout) view.findViewById(R.id.head_relative_layout);

        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);

        if (getActivity() instanceof MainActivity) {
            titleText.setPadding(280, 10, 0, 0);
        }

        return view;
    }


    /**
     * 判断type设置listView点击事件选择省市
     * 判断type设置backButton点击事件返回上一层
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (currentLevel == LEVEL_PROVINCE) {
                    selectedProvince = provinceList.get(i);//赋值已选
                    queryCities();
                } else if (currentLevel == LEVEL_CITY) {
                    selectedCity = cityList.get(i);
                    queryCounties();
                } else if (currentLevel == LEVEL_COUNTY) {
                    String weatherId = countyList.get(i).getWeatherId();
                    if (getActivity() instanceof MainActivity) {
                        Intent intent = new Intent(getActivity(), WeatherActivity.class);
                        intent.putExtra("weather_id", weatherId);
                        startActivity(intent);
                        getActivity().finish();
                    } else if (getActivity() instanceof WeatherActivity) {
                        queryProvinces();//使侧滑重新回选择省份
                        WeatherActivity activity = (WeatherActivity) getActivity();
                        activity.drawerLayout.closeDrawers();
                        activity.swipeRefresh.setRefreshing(true);
                        activity.setWeatherId(weatherId);//使之后刷新不会回到之前选择的城市天气
                        activity.requestWeather(weatherId);
                    }
                } else if (currentLevel == LEVEL_SEARCH) {
                    String weatherId = null;
                    try {
                        weatherId = String.valueOf(searchList.get(i).getJSONObject("basic").getString("id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (getActivity() instanceof MainActivity) {
                        Intent intent = new Intent(getActivity(), WeatherActivity.class);
                        intent.putExtra("weather_id", weatherId);
                        startActivity(intent);
                        getActivity().finish();
                    } else if (getActivity() instanceof WeatherActivity) {
                        queryProvinces();//使侧滑重新回选择省份
                        WeatherActivity activity = (WeatherActivity) getActivity();
                        activity.drawerLayout.closeDrawers();
                        activity.swipeRefresh.setRefreshing(true);
                        activity.setWeatherId(weatherId);//使之后刷新不会回到之前选择的城市天气
                        activity.requestWeather(weatherId);
                    }
                    editText.setText("");
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentLevel == LEVEL_COUNTY) {//重选
                    queryCities();
                } else if (currentLevel == LEVEL_CITY) {
                    queryProvinces();
                } //else if (currentLevel == LEVEL_PROVINCE) {
//                    WeatherActivity activity = (WeatherActivity) getActivity();
//                    activity.drawerLayout.closeDrawers();
//                }
            }
        });

        queryProvinces();//开始获取省信息
    }

    /**
     * 查询全国所有的省
     * 优先从数据库查询
     * 如果没有再去服务器查询
     * 加载数据刷新界面显示选项列表
     */
    private void queryProvinces() {
        titleText.setText("中国");
        backButton.setVisibility(View.INVISIBLE);
        provinceList = DataSupport.findAll(Province.class);//从数据库获取

        if (provinceList.size() > 0) {//数据库有数据读取到list时
            dataList.clear();//清空显示列表
            for (Province province : provinceList) {
                dataList.add(province.getProvinceName());//将省份名称加入列表
            }
            adapter.notifyDataSetChanged();//刷新显示List
            listView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        } else {//没有读取到数据时去服务器查询
            String address = "http://guolin.tech/api/china";
            queryFromServer(address, "province");
        }
    }

    /**
     * 市
     */
    private void queryCities() {
        titleText.setText(selectedProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);
        cityList = DataSupport.where("provinceid = ?",
                String.valueOf(selectedProvince.getId())).find(City.class);//通过省份id寻找数据库里的城市信息
        if (cityList.size() > 0) {
            dataList.clear();
            for (City city : cityList) {
                dataList.add(selectedProvince.getProvinceName() + " - " + city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_CITY;
        } else {
            int provinceCode = selectedProvince.getProvinceCode();//构造url
            String address = "http://guolin.tech/api/china/" + provinceCode;
            queryFromServer(address, "city");
        }
    }

    /**
     * 县
     */
    private void queryCounties() {
        titleText.setText(selectedCity.getCityName());
        backButton.setVisibility(View.VISIBLE);
        countyList = DataSupport.where("cityid = ?",
                String.valueOf(selectedCity.getId())).find(County.class);
        if (countyList.size() > 0) {
            dataList.clear();
            for (County county : countyList) {
                dataList.add(selectedProvince.getProvinceName() + " - " + selectedCity.getCityName() + " - " + county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_COUNTY;
        } else {
            int provinceCode = selectedProvince.getProvinceCode();
            int cityCode = selectedCity.getCityCode();
            String address = "http://guolin.tech/api/china/" + provinceCode + "/" + cityCode;
            queryFromServer(address, "county");
        }
    }


    /**
     * 创建Callback
     * 根据传入地址和类型从服务器上查询省市县数据
     * @param address
     * @param type
     */
    private void queryFromServer(String address, final String type) {

        showProgressDialog();

        //构造callback
        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                boolean result = false;
                if ("province".equals(type)) {
                    result = Utility.handleProvinceResponse(responseText);//处理数据 存入数据库
                } else if ("city".equals(type)) {
                    result = Utility.handleCityResponse(responseText,
                            selectedProvince.getId());
                } else if ("county".equals(type)) {
                    result = Utility.handleCountyResponse(responseText,
                            selectedCity.getId());
                }
                if (result) {
                    getActivity().runOnUiThread(new Runnable() {//切换主线程进行ui操作
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type)) {//重新加载数据
                                queryCities();
                            } else if ("city".equals(type)) {
                                queryCities();
                            } else if ("county".equals(type)) {
                                queryCounties();
                            }
                        }
                    });
                }
            }
        };

        HttpUtil.sendOkHttpRequest(address, callback);

    }


    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }


    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }


}
