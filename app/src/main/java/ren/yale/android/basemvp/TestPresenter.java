package ren.yale.android.basemvp;

import android.app.Dialog;

import ren.yale.android.basemvp.bean.Test;
import ren.yale.android.basemvplib.rx.subscribe.ProgressSubscriber;

/**
 * Created by yale on 2017/9/28.
 */

public class TestPresenter extends TestContract.TestPresenter {

    public void getTest(){
        mModel.getTest().bindDestoryEvent(getPublishSubject())
                .subscribe(new ProgressSubscriber<Test>(getProgressDlg()){
                    @Override
                    public void onNext(Test test) {
                        mView.getTestSuccess(test);
                    }
                });
    }

}
