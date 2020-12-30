package com.example.quickdustinfo.finedust;

import com.example.quickdustinfo.model.dust_material.FineDust;

public class FineDustContract {
    public interface View {        //UI를 변경하는 메서드들을 미리 정의
                                                        //UI가 변경될 경우
        void showFineDustResult(FineDust fineDust);     //미세먼지 정보를 받아 화면에 표시
        void showLoadError(String message);             //정보를 가져오지 못할 때 에러를 표시
        void loadingStart();                            //로딩 시 로딩 중을 표시
        void loadingEnd();                              //로딩을 끝내기 위함
        void reload(double lat, double lng);            //새로고침
    }

    interface UserActionsListener {     //사용자 액션을 미리 메서드로 정의
        void loadFineDustData();                        //새로고침 할 때 무엇을 할 지 & 데이터 가져오는 흐름을 알 수 있음
    }
}
