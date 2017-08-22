package com.example.qthjen.gestureshake;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class ShakeDectector implements SensorEventListener {

    float SHAKE_THRESHOLD_GRAVITY = 2.0F;
    int SHAKE_SLOP_TIME_MS = 500;
    int SHAKE_COUNT_RESET_TIME_MS = 3000;

    public OnShakeListener listener;
    public long mShakeTimestamp;
    public int mShakeCount;
    public void setOnShakeListener(OnShakeListener onShakeListener) {
        this.listener = onShakeListener;
    }

    public interface OnShakeListener {
        public void onShake(int count);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if ( listener != null) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            float gX = x/ SensorManager.GRAVITY_EARTH;
            float gY = y/ SensorManager.GRAVITY_EARTH;
            float gZ = z/ SensorManager.GRAVITY_EARTH;

            float gForce = (float) Math.sqrt(gX*gX + gY*gY + gZ*gZ);
            if ( gForce > SHAKE_THRESHOLD_GRAVITY ) {
                long now = System.currentTimeMillis();
                if ( mShakeTimestamp + SHAKE_SLOP_TIME_MS > now) {
                    return;
                }
                if ( mShakeTimestamp + SHAKE_COUNT_RESET_TIME_MS < now) {
                    mShakeCount = 0;
                }
                mShakeTimestamp = now;
                mShakeCount++;
                listener.onShake(mShakeCount);

            }

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
