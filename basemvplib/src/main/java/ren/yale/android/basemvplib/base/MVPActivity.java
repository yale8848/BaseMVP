package ren.yale.android.basemvplib.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import ren.yale.android.basemvplib.rx.lifecycle.RXActivityLifeCycleEnvent;
import ren.yale.android.basemvplib.util.RefectUtil;
import rx.subjects.PublishSubject;

/**
 * Created by yale on 2017/9/28.
 */

public abstract class MVPActivity<P extends  BasePresenter,M extends BaseModel> extends AppCompatActivity
        implements BaseView{


    protected P mPresenter;
    protected M mModel;
    protected final PublishSubject<RXActivityLifeCycleEnvent> mPublishSubject = PublishSubject.create();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }
    private void init() {

        mPresenter = RefectUtil.getT(this,0);
        mModel = RefectUtil.getT(this,1);
        mPresenter.setPublishSubject(mPublishSubject);
        mPresenter.attachMV(mModel,this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPublishSubject.onNext(RXActivityLifeCycleEnvent.DESTROY);
    }

    @Override
    public void onGetDataStart() {

    }
}
