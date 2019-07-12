package com.lhj.easyutils.base;

import android.app.Activity;
import android.os.Bundle;

import com.lhj.easyutils.mvp.BaseMvpView;
import com.lhj.easyutils.mvp.BasePresenter;
import com.lhj.easyutils.mvp.PresenterDispatch;
import com.lhj.easyutils.mvp.PresenterProviders;


public abstract class BaseMvpActivity<P extends BasePresenter> extends Activity implements BaseMvpView {

    private PresenterProviders mPresenterProviders;
    private PresenterDispatch mPresenterDispatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        mPresenterProviders = PresenterProviders.inject(this);
        mPresenterDispatch = new PresenterDispatch(mPresenterProviders);

        mPresenterDispatch.attachView(this, this);
        mPresenterDispatch.onCreatePresenter(savedInstanceState);
        init();
        initView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mPresenterDispatch.onSaveInstanceState(outState);
    }

    protected abstract int getContentView();

    public abstract void init();

    public abstract void initView();

    protected P getPresenter() {
        return mPresenterProviders.getPresenter(0);
    }

    public PresenterProviders getPresenterProviders() {
        return mPresenterProviders;
    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void complete() {

    }

    @Override
    public void showLoading(boolean isShow) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenterDispatch.detachView();
    }
}
