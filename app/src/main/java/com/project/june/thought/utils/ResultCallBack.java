package com.project.june.thought.utils;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by June on 2016/11/14.
 */
public class ResultCallBack<T> extends Callback<T> {
    @Override
    public T parseNetworkResponse(Response response, int id) throws Exception {
        //获取数据
        String result = response.body().string();
        Gson gson = new Gson();

        //通过反射获取传入类型
        Type genericSuperclass = getClass().getGenericSuperclass();  //获取带有泛型的父类
        Type[] actualTypeArguments = ((ParameterizedType) genericSuperclass).getActualTypeArguments();  //获取泛型参数的数组
        Class<T> clazz = (Class<T>) actualTypeArguments[0];

        //将数据转换成对象
        T t = gson.fromJson(result, clazz);

        return t;
    }

    @Override
    public void onError(Call call, Exception e, int id) {

    }

    @Override
    public void onResponse(T response, int id) {

    }
}
