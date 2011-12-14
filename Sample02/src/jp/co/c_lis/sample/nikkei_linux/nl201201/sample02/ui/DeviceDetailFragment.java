
package jp.co.c_lis.sample.nikkei_linux.nl201201.sample02.ui;

import jp.co.c_lis.sample.nikkei_linux.nl201201.sample02.R;
import android.app.Fragment;
import android.net.wifi.p2p.WifiP2pDevice;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DeviceDetailFragment extends Fragment {
    private static final String LOG_TAG = "DetailFragment";
    private static final boolean DEBUG_FLG = true;
    
    /*
     * (non-Javadoc)
     * @see android.app.Fragment#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    /*
     * (non-Javadoc)
     * @see android.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.detail, null);
        mDeviceAddress = (TextView)v.findViewById(R.id.address);
        mDeviceName = (TextView)v.findViewById(R.id.name);
        mPrimaryDeviceType = (TextView)v.findViewById(R.id.pri_type);
        mSecondaryDeviceType = (TextView)v.findViewById(R.id.sec_type);
        mStatus = (TextView)v.findViewById(R.id.status);
        return v;
    }
    
    private TextView mDeviceAddress = null;
    private TextView mDeviceName = null;
    private TextView mPrimaryDeviceType = null;
    private TextView mSecondaryDeviceType = null;
    private TextView mStatus = null;
    
    public void showDetail(WifiP2pDevice device) {
        mDeviceAddress.setText(device.deviceAddress);
        mDeviceName.setText(device.deviceName);
        mPrimaryDeviceType.setText(device.primaryDeviceType);
        mSecondaryDeviceType.setText(device.secondaryDeviceType);
        mStatus.setText(getStatus(device.status));
    }
    
    private static String getStatus(int status) {
        switch(status) {
            case WifiP2pDevice.CONNECTED:
                return "CONNECTED";
            case WifiP2pDevice.INVITED:
                return "INVITED";
            case WifiP2pDevice.FAILED:
                return "FAILED";
            case WifiP2pDevice.AVAILABLE:
                return "AVAILABLE";
            case WifiP2pDevice.UNAVAILABLE:
                return "UNAVAILABLE";
        }
        return "UNKNOWN";
    }
}
