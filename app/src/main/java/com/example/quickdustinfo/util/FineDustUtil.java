package com.example.quickdustinfo.util;

import com.example.quickdustinfo.model.dust_material.FineDust;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FineDustUtil {     //Retrofit을 사용할 때 이 객체 생성
    private FineDustApi mGetApi;

    public FineDustUtil() {
        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl(FineDustApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())     //GsonConverterFactory을 통해 JSON데이터 -> 자바객체 로 변환
                .build();
        mGetApi = mRetrofit.create(FineDustApi.class);
    }

    public FineDustApi getApi() {
        return mGetApi;
    }       //getApi()를 통해 해당 API 사용
}
