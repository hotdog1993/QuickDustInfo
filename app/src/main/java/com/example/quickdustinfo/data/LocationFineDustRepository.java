package com.example.quickdustinfo.data;

import com.example.quickdustinfo.model.dust_material.FineDust;
import com.example.quickdustinfo.util.FineDustUtil;

import retrofit2.Callback;

public class LocationFineDustRepository implements FineDustRepository {         //미세먼지 정보를 가져오도록 FineDustRepository를 구체화
    private FineDustUtil mFindDustUtil;
    private double mLatitude;
    private double mLongitude;

    public LocationFineDustRepository() {
        mFindDustUtil = new FineDustUtil();
    }

    public LocationFineDustRepository(double lat, double lng) {
        this();
        this.mLatitude = lat;
        this.mLongitude = lng;
    }

    @Override
    public boolean isAvailable() {
        if(mLatitude != 0.0 && mLongitude != 0.0) {     //제대로 된 위도 경도만 허용
            return true;
        }
        return false;
    }

    @Override
    public void getFineDustData(Callback<FineDust> callback) {
        mFindDustUtil.getApi().getFineDust(mLatitude, mLongitude)
                .enqueue(callback);         //비동기로 콜백으로 던지면 외부에서 받음
    }
}
