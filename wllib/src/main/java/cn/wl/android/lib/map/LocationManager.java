package cn.wl.android.lib.map;

import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

import java.util.concurrent.TimeUnit;

import cn.wl.android.lib.config.WLConfig;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Cancellable;

/**
 * Created by JustBlue on 2019-11-12.
 *
 * @email: bo.li@cdxzhi.com
 * @desc: 定位数据
 */
public final class LocationManager {

    private static final class LocHolder {

        private static final LocationClient mClient =
                new LocationClient(WLConfig.getContext());

        private static LocationClientOption getOption() {
            LocationClientOption option = new LocationClientOption();

            option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
            //可选，设置定位模式，默认高精度
            //LocationMode.Hight_Accuracy：高精度；
            //LocationMode. Battery_Saving：低功耗；
            //LocationMode. Device_Sensors：仅使用设备；

            option.setCoorType("bd09ll");
            //可选，设置返回经纬度坐标类型，默认GCJ02
            //GCJ02：国测局坐标；
            //BD09ll：百度经纬度坐标；
            //BD09：百度墨卡托坐标；
            //海外地区定位，无需设置坐标类型，统一返回WGS84类型坐标

            option.setScanSpan(5000);
            //可选，设置发起定位请求的间隔，int类型，单位ms
            //如果设置为0，则代表单次定位，即仅定位一次，默认为0
            //如果设置非0，需设置1000ms以上才有效

            option.setOpenGps(true);
            //可选，设置是否使用gps，默认false
            //使用高精度和仅用设备两种定位模式的，参数必须设置为true

            option.setLocationNotify(true);
            //可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false

            option.setIgnoreKillProcess(false);
            //可选，定位SDK内部是一个service，并放到了独立进程。
            //设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)

            option.SetIgnoreCacheException(false);
            //可选，设置是否收集Crash信息，默认收集，即参数为false

            option.setWifiCacheTimeOut(5 * 60 * 1000);
            //可选，V7.2版本新增能力
            //如果设置了该接口，首次启动定位时，会先判断当前Wi-Fi是否超出有效期，若超出有效期，会先重新扫描Wi-Fi，然后定位

            option.setEnableSimulateGps(false);
            //可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false

            return option;
        }

        private static final Observable<BDLocation> mIns = Observable
                .<BDLocation>create(emitter -> {
                    mClient.setLocOption(getOption());

                    BDAbstractLocationListener listener = new BDAbstractLocationListener() {
                        @Override
                        public void onReceiveLocation(BDLocation bdLocation) {
                            if (bdLocation == null) {
                                return;
                            }
                            int locType = bdLocation.getLocType();

                            if (BDLocation.TypeGpsLocation == locType ||
                                    BDLocation.TypeNetWorkLocation == locType) {
                                LatLng latLng = new LatLng(bdLocation.getLatitude(),
                                        bdLocation.getLongitude());

                                if (WLConfig.isDebug()) {
                                    Log.e("LocationManager",
                                            "定位信息:" + bdLocation.getAddress() +
                                                    ", lat=" + bdLocation.getLatitude() +
                                                    ", lng = " + bdLocation.getLongitude());
                                }

                                LocationManager.mLast = latLng;

                                emitter.onNext(bdLocation);
                            }
                        }
                    };

                    mClient.registerLocationListener(listener);

                    emitter.setCancellable(() -> {
                        // 当下流没有观察者是自动暂停定位
                        mClient.stop();
                        mClient.unRegisterLocationListener(listener);
                    });

                    mClient.start();
                })
                .replay(1)
                .refCount(60, TimeUnit.SECONDS);
    }

    public static Observable<BDLocation> globalLocEvent() {
        return LocHolder.mIns;
    }

    private static LatLng mLast;

    public static LatLng getLast() {
        return mLast;
    }
}
