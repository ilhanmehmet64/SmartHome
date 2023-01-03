package com.example.smarthome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private Button btnLed_on_off, btnUpdateInfo;
    private TextView tv_temperatureValue,tv_moistureValue,tv_distanceValue, tv_LedStatus, tv_garageStatus;

    private Boolean ledValue, ledStatus;

    private DatabaseReference mReference, mDatabase;
    private Retrofit retrofit;
    private String baseUrl = "https://smarthome-8739c-default-rtdb.firebaseio.com/";
    private DataInfoApi DataInfoApi;
    private Call<DataInfo> DataInfoCall;
    private DataInfo dataInfo;
    private String hum, temp, distance;

    public void init(){
        btnLed_on_off = findViewById(R.id.btnLed_on_off);
        tv_temperatureValue = findViewById(R.id.tv_temperatureValue);
        tv_moistureValue = findViewById(R.id.tv_moistureValue);
        tv_distanceValue = findViewById(R.id.tv_distanceValue);
        tv_LedStatus = findViewById(R.id.tv_LedStatus);
        tv_garageStatus = findViewById(R.id.tv_garageStatus);
        btnUpdateInfo = findViewById(R.id.btnUpdateInfo);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        setRetrofitSettings();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        btnUpdateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRetrofitSettings();
            }
        });

        btnLed_on_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mReference = FirebaseDatabase.getInstance().getReference("led");
                mReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        System.out.println(snapshot.getValue());
                        ledValue = (Boolean) snapshot.getValue();
                        System.out.println(ledValue);

                        if (ledValue) {
                            mDatabase.child("led").setValue(false);
                            setRetrofitSettings();
                            Toast.makeText(getApplicationContext(),"Led Turned Off",Toast.LENGTH_LONG).show();

                        } else {
                            mDatabase.child("led").setValue(true);
                            setRetrofitSettings();
                            Toast.makeText(getApplicationContext(),"Led Turned On",Toast.LENGTH_LONG).show();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    private void setRetrofitSettings(){
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        DataInfoApi = retrofit.create(DataInfoApi.class);
        DataInfoCall = DataInfoApi.getDataInfo();

        DataInfoCall.enqueue(new Callback<DataInfo>() {
            @Override
            public void onResponse(Call<DataInfo> call, Response<DataInfo> response) {
                dataInfo = response.body();

                hum = String.valueOf(dataInfo.getHumidity());
                temp = String.valueOf(dataInfo.getTemperature());
                distance = String.valueOf(dataInfo.getDistance());
                ledStatus = dataInfo.getLed();

                tv_moistureValue.setText(hum);
                tv_distanceValue.setText(distance);
                tv_temperatureValue.setText(temp + " C°");
                if(ledStatus == true){
                    tv_LedStatus.setText("Led On");
                }
                else {
                    tv_LedStatus.setText("Led Off");
                }

                if(dataInfo.getDistance() <= 60){
                    tv_garageStatus.setText("Garage Full");
                }
                else{
                    tv_garageStatus.setText("Garage Empty");
                }
            }

            @Override
            public void onFailure(Call<DataInfo> call, Throwable t) {
                System.out.println(t.toString());
            }
        });
    }
}