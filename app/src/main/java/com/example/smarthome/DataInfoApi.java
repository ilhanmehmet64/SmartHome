package com.example.smarthome;

import retrofit2.Call;
import retrofit2.http.GET;

public interface DataInfoApi {
    @GET(".json")
    Call<DataInfo> getDataInfo();
}
