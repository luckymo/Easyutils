package com.lhj.easyutils.mvp;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class PresenterProviders {
    private static final String TAG=PresenterProviders.class.getName();
    private PresenterMap mPresenterMap=new PresenterMap();
    private Activity mActivity;
    private Fragment mFragment;
    private Class<?> mClass;

    public static PresenterProviders inject(Activity activity){
    return new PresenterProviders(activity,null);
    }

    public static PresenterProviders inject(Fragment fragment){
        return new PresenterProviders(null,fragment);
    }

    private PresenterProviders(Activity activity,Fragment fragment){
    if(activity!=null){
        this.mActivity=activity;
        mClass=this.mActivity.getClass();
    }
    if(fragment!=null){
        this.mFragment=fragment;
        mClass=this.mFragment.getClass();
    }
        resolverMakePresenter();
        resolvePresenterVariable();
    }


    private static Application checkApplication(Activity activity) {
        Application application = activity.getApplication();
        if (application == null) {
            throw new IllegalStateException("Your activity/fragment is not yet attached to Application. You can't request PresenterProviders before onCreate call.");
        }
        return application;
    }

    private static Activity checkActivity(Fragment fragment) {
        Activity activity = fragment.getActivity();
        if (activity == null) {
            throw new IllegalStateException("Can't create PresenterProviders for detached fragment");
        }
        return activity;
    }

    private static Context checkContext(Context context) {
        Context resultContent = null;
        if (context instanceof Activity) {
            resultContent = context;
        }
        if (resultContent == null) {
            throw new IllegalStateException("Context must Activity Context");
        }
        return resultContent;
    }

    private <P extends BasePresenter> PresenterProviders resolverMakePresenter(){
        //	该元素如果存在指定类型的注解，则返回这些注解，否则返回 null。
        MakePresenter makePresenter=mClass.getAnnotation(MakePresenter.class);
        if(makePresenter!=null){
            Class<P> [] classes= (Class<P>[]) makePresenter.presenter();
            for (Class<P> clazz : classes) {
                String canonicalName = clazz.getCanonicalName();
                Log.v(TAG,"PresenterProviders canonicalName = "+canonicalName);
                try {
                    mPresenterMap.put(canonicalName, clazz.newInstance());
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return this;
    }


    private <P extends BasePresenter> PresenterProviders resolvePresenterVariable() {
        for (Field field : mClass.getDeclaredFields()) {
            //获取字段上的注解
            Annotation[] anns = field.getDeclaredAnnotations();
            if (anns.length < 1) {
                continue;
            }
            if (anns[0] instanceof PresenterResolver) {
                String canonicalName = field.getType().getName();
                Log.v(TAG,"PresenterProviders canonicalName = "+canonicalName);
                P presenterInstance = (P) mPresenterMap.get(canonicalName);
                if (presenterInstance != null) {
                    try {
                        field.setAccessible(true);
                        field.set(mFragment != null ? mFragment : mActivity, presenterInstance);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return this;
    }


    public <P extends BasePresenter> P getPresenter(int index) {
        MakePresenter createPresenter = mClass.getAnnotation(MakePresenter.class);
        if (createPresenter == null) {
            return null;
        }
        if (createPresenter.presenter().length == 0) {
            return null;
        }
        if (index >= 0 && index < createPresenter.presenter().length) {
            String key = createPresenter.presenter()[index].getCanonicalName();
            BasePresenter presenter = mPresenterMap.get(key);
            if (presenter != null) {
                return (P) presenter;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public PresenterMap getPresenterStore() {
        return mPresenterMap;
    }

    public void release() {
      mPresenterMap=new PresenterMap();
        mClass=null;
       mActivity=null;
         mFragment=null;
    }
}
