package ren.yale.android.basemvp;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import ren.yale.android.basemvp.bean.Test;
import ren.yale.android.basemvplib.base.MVPActivity;


public class MainActivity extends MVPActivity<TestPresenter,TestModel>
 implements TestContract.View{

    private static final String  TAG="test";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected Dialog getProgressDlg() {
        return new Dialog(this);
    }

    public void onClick(View v){
        mPresenter.getTest();
    }

    @Override
    public void getTestSuccess(Test test) {
        Log.d(TAG,test.getMessage());
    }
}
