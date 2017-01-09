package com.example.Tools;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by MBENBEN on 2017/1/8.
 */

public class IdArray {
    private static SharedPreferences spf;
    private static HashMap<String, String> Ids;

    public static void init(Context context){
        spf = context.getSharedPreferences("users",
                context.MODE_PRIVATE);
        if(spf != null) {
            Ids = (HashMap<String, String>) spf.getAll();
        }else{
            Ids = new HashMap<>();
        }
    }
    public static int getSize(){
        return Ids.size();
    }
    public static String[] getItem(int type, int cur){
        String[] item = new String[2];
        switch (type){
            case 0:
                //账号
                item[0] = cur+"";
                item[1] = Ids.get(cur+"");
                break;
            case 1:
                //位置
                Iterator<Map.Entry<String, String>> iterator = Ids.entrySet().iterator();
                int k = 0;
                while(iterator.hasNext()){
                    Map.Entry<String, String> entry = iterator.next();
                    if(k == cur){
                        item[0] = entry.getKey();
                        item[1] = entry.getValue();
                        break;
                    }else if(!iterator.hasNext()){
                        item[0] = "null";
                        item[1] = "null";
                    }
                    k ++;
                }
                break;
        }
        return item;
    }
    public static void add(String id, String psw, boolean rember){
        if(rember){
            spf.edit().putString(id, psw).commit();
        }
        Ids.put(id, psw);
    }
    public static void remove(String id){
        spf.edit().remove(id).commit();
        Ids.remove(id);
    }
    public static void flush(){
        Ids.clear();
        Ids.putAll((Map<String, String>) spf.getAll());
    }
}
