package com.lhj.easyutils.mvp;

public interface BaseMvpView {

    void showError(String message);

    void showLoading(boolean isShow);

    void complete();
}
