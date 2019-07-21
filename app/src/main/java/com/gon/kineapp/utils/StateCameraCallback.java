package com.gon.kineapp.utils;

import android.hardware.camera2.CameraDevice;

public abstract class StateCameraCallback extends CameraDevice.StateCallback {

    @Override
    public void onDisconnected(CameraDevice camera) {}

    @Override
    public void onError(CameraDevice camera, int error) {}

}
