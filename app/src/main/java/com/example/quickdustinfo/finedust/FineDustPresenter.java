package com.example.quickdustinfo.finedust;

import com.example.quickdustinfo.data.FineDustRepository;
import com.example.quickdustinfo.model.dust_material.FineDust;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FineDustPresenter implements FineDustContract.UserActionsListener{        //실제 사용자의 동작에 대한 처리는 여기서 다함, fragment는 뷰의 역할만 함
    private final FineDustRepository mRepository;       //미세먼지 데이터를 제공받는 저장소
    private final FineDustContract.View mView;          //UI 변경을 정의하는 View 인터페이스

    public FineDustPresenter(FineDustRepository repository, FineDustContract.View view) {
        this.mRepository = repository;
        this.mView = view;
    }

    @Override
    public void loadFineDustData() {        //데이터 가져오는 흐름 알 수 있음
        if(mRepository.isAvailable()) {     //데이터 제공이 가능하면
            mView.loadingStart();           //로딩 시작
            mRepository.getFineDustData(new Callback<FineDust>() {      //데이터 가져오기
                @Override
                public void onResponse(Call<FineDust> call, Response<FineDust> response) {
                    mView.showFineDustResult(response.body());          //데이터 표시하기
                    mView.loadingEnd();                                 //로딩 끝
                }

                @Override
                public void onFailure(Call<FineDust> call, Throwable t) {
                    mView.showLoadError(t.getLocalizedMessage());       //에러 표시하기
                    mView.loadingEnd();                                 //로딩 끝
                }
            });
        }
    }
}
