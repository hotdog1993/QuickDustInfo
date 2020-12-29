package com.example.quickdustinfo.data;

import com.example.quickdustinfo.model.dust_material.FineDust;

import retrofit2.Callback;

public interface FineDustRepository {
    boolean isAvailable();
    void getFindDustData(Callback<FineDust> callback);      //결과를 FineDust에 담아서 콜백으로 받겠다.
}
