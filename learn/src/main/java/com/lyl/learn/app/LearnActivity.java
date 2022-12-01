package com.lyl.learn.app;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lyl.arouter.annotations.IsActivity;
import com.lyl.base_app.BaseListActivity;
import com.lyl.baseutil.adapter.viewholder.SmartViewHolder;
import com.lyl.learn.R;
import com.lyl.learn.model.ItemModel;
import com.lyl.learn.model.LearnItemModel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@IsActivity(value = "com.lyl.learn/LearnActivity")
public class LearnActivity extends BaseListActivity<ItemModel> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);

        setContentView(R.layout.activity_learn);
//        ButterKnife.bind(this);
        initView();
    }

    //    @OnClick(value = R.id.btn_loginSuccess)
    public void onLoginSuccessCallBack(View v) {

    }

    //    @OnClick(value = R.id.btn_loginFail)
    public void onLoginFailCallBack(View v) {

    }

    @Override
    protected void initView() {
        super.initView();
        mSmartRefresh.autoRefresh();
    }

    @Override
    protected void onListBindDataViewHolder(SmartViewHolder holder, ItemModel model, int position) {
        holder.text(R.id.tv_title, model.getCn() + "\n" + model.getEn());
    }

    @Override
    protected int getListDataLayoutId(int viewType) {
        return R.layout.item_learn_model;
    }

    @Override
    protected int getListEmptyLayoutId() {
        return R.layout.list_empty;
    }

    @Override
    protected int getSRViewId() {
        return R.id.smart_refresh;
    }

    @Override
    protected int getRVListId() {
        return R.id.rv_list;
    }

    List<LearnItemModel> mTabData = new ArrayList<>();

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mTabData = getTabData();
        mBaseEmptyRecyclerAdapter.refresh(getListData(mTabData));
        refreshLayout.finishRefresh();
    }

    @Override
    protected AdapterView.OnItemClickListener getOnItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        };
    }

    private List<LearnItemModel> getTabData() {
        String json = readRawFile(R.raw.data);
        Type type = new TypeToken<List<LearnItemModel>>() {
        }.getType();
        List<LearnItemModel> data = new Gson().fromJson(json, type);
        return data;
    }

    private List<ItemModel> getListData(List<LearnItemModel> data) {
        if (data == null) {
            return new ArrayList<>();
        }
        List<ItemModel> result = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            LearnItemModel lItem = data.get(i);
            for (int j = 0; j < lItem.getEnData().size(); j++) {
                ItemModel item = new ItemModel();
                item.setEn(lItem.getEnData().get(j));
                item.setCn(lItem.getCnData().get(j));
                item.setPosition(i);
                result.add(item);
            }
        }
        return result;
    }

    public String readRawFile(int resId) {
        Resources res = getResources();
        InputStream in = null;
        BufferedReader br = null;
        StringBuffer sb = new StringBuffer();
        sb.append("");
        try {
            in = res.openRawResource(resId);
            String str;
            br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            while ((str = br.readLine()) != null) {
                sb.append(str);
                sb.append("\n");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("Error", "FILE NOT EXSIT...");
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            Log.e("Error", "FILE CODE ERRER...");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("Error", "FILE READ ERROR...");
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
