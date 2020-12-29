package com.example.quickdustinfo.finedust;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.quickdustinfo.R;
import com.example.quickdustinfo.data.FineDustRepository;
import com.example.quickdustinfo.data.LocationFineDustRepository;
import com.example.quickdustinfo.model.dust_material.FineDust;

public class FineDustFragment extends Fragment implements FineDustContract.View{        //fragment가 해야할 일을 FineDustContract에 있으므로 여기서 프레그먼트가 할 일을 재정의 하면 됨

    private TextView mLocationTextView;
    private TextView mTimeTextView;
    private TextView mDustTextView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FineDustRepository mRepository;
    private FineDustPresenter mPresenter;

    public static FineDustFragment newInstance(double lat, double lng) {
        Bundle args = new Bundle();
        args.putDouble("lat", lat);
        args.putDouble("lng", lng);
        FineDustFragment fragment = new FineDustFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getArguments() != null) {
            double lat = getArguments().getDouble("lat");
            double lng = getArguments().getDouble("lng");
            mRepository = new LocationFineDustRepository(lat, lng);
        }else {
            mRepository = new LocationFineDustRepository();
        }
        mPresenter = new FineDustPresenter(mRepository, this);
        mPresenter.loadFineDustData();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fine_dust, container, false);
        mLocationTextView = view.findViewById(R.id.result_location_text);
        mTimeTextView = view.findViewById(R.id.result_time_text);
        mDustTextView = view.findViewById(R.id.result_dust_text);

        //뷰페이저가 날라갔다가 다시 불러올 때마다 다시 불러오면 너무 많이 api 받아야 하므로 savedInstace에 저장하려고 함
        if(savedInstanceState != null) {
            //복원하는 코드
            mLocationTextView.setText(savedInstanceState.getString("location"));
            mTimeTextView.setText(savedInstanceState.getString("time"));
            mDustTextView.setText(savedInstanceState.getString("dust"));
        }
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.YELLOW, Color.BLUE);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {   //재로딩 하도록
            @Override
            public void onRefresh() {
                mPresenter.loadFineDustData();
            }
        });


        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {     //끝나기 전에 값들 저장
        super.onSaveInstanceState(outState);
        outState.putString("location", mLocationTextView.getText().toString());
        outState.putString("time", mTimeTextView.getText().toString());
        outState.putString("dust", mDustTextView.getText().toString());
    }

    @Override
    public void showFineDustResult(FineDust fineDust) {     //결과를 보여줌(3개의 텍스트(관측장소 및, 관측시간 및, 미세먼지 및)에 뿌려줌)
        mLocationTextView.setText(fineDust.getWeather().getDust().get(0).getStation().getName());   //지역명
        mTimeTextView.setText(fineDust.getWeather().getDust().get(0).getTimeObservation());     //시간
        mDustTextView.setText(fineDust.getWeather().getDust().get(0)
                .getPm10().getValue() + " ㎍/m³, " + fineDust.getWeather().getDust().get(0).getPm10().getGrade());

    }

    @Override
    public void showLoadError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadingStart() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void loadingEnd() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void reload(double lat, double lng) {
        mRepository = new LocationFineDustRepository(lat, lng);
        mPresenter = new FineDustPresenter(mRepository, this);
        mPresenter.loadFineDustData();
    }
}
