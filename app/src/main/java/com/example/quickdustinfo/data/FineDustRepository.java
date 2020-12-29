package com.example.quickdustinfo.data;

import com.example.quickdustinfo.model.dust_material.FineDust;

import retrofit2.Callback;

public interface FineDustRepository {                       //데이터 결과 받는 것을 추상화
    boolean isAvailable();
    void getFindDustData(Callback<FineDust> callback);      //결과를 FineDust에 담아서 콜백으로 받겠다.
}
