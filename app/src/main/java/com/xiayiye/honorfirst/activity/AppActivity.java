package com.xiayiye.honorfirst.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xiayiye.honorfirst.R;
import com.xiayiye.honorfirst.ActivityManageAll;

public class AppActivity extends Activity implements AdapterView.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        ListView lvApp = findViewById(R.id.lv_app);
        lvApp.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                //跳转以前学习的页面
                goToBeforeStudy();
                break;
            case 1:
                //跳转RxJava学习页面
                toRxJavaStudy();
                break;
            case 2:
                //跳转购物车抛物线动画页面
                linAnimate();
                break;
            default:
        }
    }

    /**
     * 抛物线动画
     */
    private void linAnimate() {
        ActivityManageAll.getInstance2().goActivity(LineAnimateActivity.class);
    }

    private void toRxJavaStudy() {
        ActivityManageAll.getInstance2().goActivity(RxJavaStudyActivity.class);
    }

    private void goToBeforeStudy() {
        ActivityManageAll.getInstance1().goActivity(MainActivity.class);
    }
}
