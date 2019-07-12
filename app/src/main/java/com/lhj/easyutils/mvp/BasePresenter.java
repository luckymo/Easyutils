package com.lhj.easyutils.mvp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

public class BasePresenter<T> {
    protected Context mContext;
    protected T mView;

    protected void clear(){

    }

    public void attachView(Context context,T view){
        this.mContext=context;
        this.mView=view;
    }

    public void detachView(){
        this.mView=null;
    }

    public boolean isAttachView(){
        return this.mView!=null;
    }

    public void onMakePersenter(@NonNull Bundle state){

    }

    public void onDestroyPresenter(){
        this.mContext=null;
        detachView();
    }

    public void onSaveInstanceState(Bundle state){

    }
}
