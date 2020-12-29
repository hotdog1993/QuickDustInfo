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

public class FineDustFragment extends Fragment implements FineDustContract.View{        //fragment가 해야할 일을 FineDustContract에 있으므로 여기서 프레그먼트가 할 일을 재정의

    private TextView mLocationTextView;
    private TextView mTimeTextView;
    private TextView mDustTextView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FineDustRepository mRepository;
    private FineDustPresenter mPresenter;

    public static FineDustFragment newInstance(double lat, double lng) {                //fragment 생성 시! 좌푯값을 받기 위해 팩토리 메서드 작성
        Bundle args = new Bundle();
        args.putDouble("lat", lat);
        args.putDouble("lng", lng);
        FineDustFragment fragment = new FineDustFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {        //fragment가 생성되면 좌푯값이 있을 때와 없을 때(현재 위치 모를 때)를 고려하여 메서드 재정의
        super.onActivityCreated(savedInstanceState);
        if(getArguments() != null) {                                            //좌푯값이 있을 때
            double lat = getArguments().getDouble("lat");
            double lng = getArguments().getDouble("lng");
            mRepository = new LocationFineDustRepository(lat, lng);
        }else {                                                                 //좌푯값이 없을 때
            mRepository = new LocationFineDustRepository();
        }
        mPresenter = new FineDustPresenter(mRepository, this);
        mPresenter.loadFineDustData();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fine_dust, container, false);    //LayoutInflater를 통해 fragment에 표시할 레이아웃 파일을 지정
        mLocationTextView = view.findViewById(R.id.result_location_text);                           //각 뷰들을 findViewById()로 가져옴
        mTimeTextView = view.findViewById(R.id.result_time_text);                                   //각 뷰들을 findViewById()로 가져옴
        mDustTextView = view.findViewById(R.id.result_dust_text);                                   //각 뷰들을 findViewById()로 가져옴

        //뷰페이저가 날라갔다가 다시 불러올 때마다 다시 불러오면 너무 많이 api 받아야 하므로 savedInstace에 저장하려고 함
        if(savedInstanceState != null) {
            //프래그먼트가 날아가더라도 복원하는 코드
            mLocationTextView.setText(savedInstanceState.getString("location"));
            mTimeTextView.setText(savedInstanceState.getString("time"));
            mDustTextView.setText(savedInstanceState.getString("dust"));
        }
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);                         //로딩 표시를 위한 SwipeRefreshLayout의 초기화 코드
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

    //↓↓↓↓↓↓↓↓↓이 밑으로는 UI 갱신을 처리, 이렇게 재정의 된 메서드들은 FineDustPresenter가 제어한다.

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
