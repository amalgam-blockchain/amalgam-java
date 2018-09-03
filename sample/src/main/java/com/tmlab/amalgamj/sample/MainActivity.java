package com.tmlab.amalgamj.sample;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.tmlab.amalgamj.Amalgam;
import com.tmlab.amalgamj.Connection;
import com.tmlab.amalgamj.DynamicGlobalProperties;
import com.tmlab.amalgamj.Response;

public class MainActivity extends AppCompatActivity {

    private TextView mBlockNumber;
    private TextView mCurrentWitness;
    private TextView mCurrentSupply;

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBlockNumber = findViewById(R.id.block_number);
        mCurrentWitness = findViewById(R.id.current_witness);
        mCurrentSupply = findViewById(R.id.current_supply);

        Connection.setNodeUrl("https://api.amalgam.money");
        mHandler = new Handler();
    }

    @Override
    public void onResume() {
        super.onResume();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Amalgam.getDynamicGlobalProperties(new Amalgam.OnGetDynamicGlobalPropertiesListener() {
                    @Override
                    public void onFinish(Response response, DynamicGlobalProperties properties) {
                        if (response.isSuccess()) {
                            mBlockNumber.setText(properties.head_block_number.toString());
                            mCurrentWitness.setText(properties.current_witness);
                            mCurrentSupply.setText(properties.current_supply.toString());
                        }
                    }
                });
                mHandler.postDelayed(this, 3000);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacksAndMessages(null);
    }
}
