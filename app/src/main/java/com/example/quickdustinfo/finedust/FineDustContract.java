package com.example.quickdustinfo.finedust;

import com.example.quickdustinfo.model.dust_material.FineDust;

public class FineDustContract {
    interface View {
        void showFineDustResult(FineDust fineDust);
        void showLoadError(String message);
        void loadingStart();
        void loadingEnd();
        void reload(double lat, double lng);
    }

    interface UserActionsListener {
        void loadFineDustData();
    }
}
