package com.bayron.jogodavelha.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bayron.jogodavelha.interfaces.OnGameAction;
import com.bayron.jogodavelha.widgets.BlackboardView;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements OnGameAction {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());

        setContentView(new BlackboardView(this, this));
    }

    @Override
    public void onNewGame() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    @Override
    public void onExitGame() {
        finish();
    }
}
