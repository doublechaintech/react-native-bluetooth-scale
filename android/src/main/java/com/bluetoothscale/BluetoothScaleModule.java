package com.bluetoothscale;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.util.Map;
import java.util.HashMap;
import android.content.IntentFilter;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.Arguments;




@ReactModule(name = BluetoothScaleModule.NAME)
public class BluetoothScaleModule extends ReactContextBaseJavaModule {
  public static final String NAME = "BluetoothScale";

  private final ReactApplicationContext reactContext;
  public static final String ACTION_DATA_RECEIVED = "com.doublechaintech.bluetooth.scale.DATA_RECEIVED";



  public BluetoothScaleModule(ReactApplicationContext reactContext) {
    super(reactContext);

    this.reactContext = reactContext;

    IntentFilter bluetoothScaleIntentFilter = new IntentFilter();
    bluetoothScaleIntentFilter.addAction(BARCODE_DATA_ACTION);
    bluetoothScaleIntentFilter.setPriority(Integer.MAX_VALUE);
    getReactApplicationContext().registerReceiver(bluetoothScaleReceiver, bluetoothScaleIntentFilter);
  }

  private final BroadcastReceiver bluetoothScaleReceiver = new BroadcastReceiver() {




      @Override
      public void onReceive(Context context, Intent intent) {
          try {
            WritableMap params = Arguments.createMap();
            String actionName = intent.getAction();
            if (ACTION_DATA_RECEIVED.equals(actionName)) {
              params.putString("weight", intent.getStringExtra("weight"));
            }
            

            
            sendEvent(getReactApplicationContext(), "onEvent", params);
          } catch (Exception e) {
            WritableMap errorParams = Arguments.createMap();
            errorParams.putString("message", e.getMessage());
            sendEvent(getReactApplicationContext(), "onError", errorParams);
          }
      }
    };
  
    private void sendEvent(ReactContext reactContext, String eventName, WritableMap params) {
      getReactApplicationContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName,
          params);
    }
   

    







  @Override
  @NonNull
  public String getName() {
    return NAME;
  }


  // Example method
  // See https://reactnative.dev/docs/native-modules-android
  @ReactMethod
  public void multiply(double a, double b, Promise promise) {
    promise.resolve(a * b);
  }
}
