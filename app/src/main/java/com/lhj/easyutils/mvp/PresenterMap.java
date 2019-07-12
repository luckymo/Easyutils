package com.lhj.easyutils.mvp;

import java.util.HashMap;

public class PresenterMap<P extends BasePresenter> {
    private static final String DEFAULT_KEY="DefaultKey";
    private HashMap<String , P> map=new HashMap<>();

    public final void put(String key ,P presenter){
        map.put(DEFAULT_KEY+":"+key,presenter);
    }

    public final P get(String key){
        return  map.get(DEFAULT_KEY+":"+key);
    }

    public final void clear(){
        for (P presenter:map.values()) {
            presenter.clear();
        }
        map.clear();
    }

    public int getSize(){
        return map.size();
    }

    public HashMap<String ,P> getMap(){
        return map;
    }
}
