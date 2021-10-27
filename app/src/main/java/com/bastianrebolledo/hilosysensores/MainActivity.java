package com.bastianrebolledo.hilosysensores;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private TextView txtProx, txtGiro, txtContador;
    private SensorManager sensorManager;
    private Sensor sensorP, sensorG;

    private boolean isStarted = false;
    private int contador = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtProx = (TextView) findViewById(R.id.txtProx);
        txtGiro = (TextView) findViewById(R.id.txtGiro);
        txtContador = (TextView) findViewById(R.id.txtContador);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        sensorP = (Sensor) sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        sensorManager.registerListener((SensorEventListener) this, sensorP, SensorManager.SENSOR_DELAY_NORMAL);

        sensorG = (Sensor) sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener((SensorEventListener) this, sensorG, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void EmpezarContador(View view) {
        if (!isStarted) {
            isStarted = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (isStarted) {
                        contador++;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                txtContador.setText("⏰ Contador: " + String.valueOf(contador));
                            }
                        });
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }else{
            Toast.makeText(this,"¡Contador ya iniciado!",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_PROXIMITY:
                txtProx.setText(String.format("\uD83D\uDC65 Sensor Proximidad: %.0f", event.values[0]));
                break;
            case Sensor.TYPE_ACCELEROMETER:
                txtGiro.setText(String.format("\uD83D\uDCF1 Sensor Giroscopio: %.0f", event.values[0]));
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}