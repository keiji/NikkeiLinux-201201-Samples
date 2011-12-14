
package jp.co.c_lis.sample.nikkei_linux.nl201201.sample02.ui;

import jp.co.c_lis.sample.nikkei_linux.nl201201.sample02.R;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ChannelListener;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends Activity implements DeviceListFragment.Listener {
    private static final String LOG_TAG = "MainActivity";

    private DeviceListFragment mListFragment = null;
    private DeviceDetailFragment mDetailFragment = null;

    /*
     * (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        FragmentManager fm = (FragmentManager) getFragmentManager();
        mDetailFragment = (DeviceDetailFragment) fm.findFragmentById(R.id.detail);
        mListFragment = (DeviceListFragment) fm.findFragmentById(R.id.list);

        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(),
                new ChannelListener() {
                    @Override
                    public void onChannelDisconnected() {
                        Log.d(LOG_TAG, "onChannelDisconnected");
                    }
                });

        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        registerReceiver(new WifiP2pBroadcastReceiver(), filter);

    }

    private WifiP2pManager mManager = null;
    private Channel mChannel = null;

    /*
     * (non-Javadoc)
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume() {
        super.onResume();

        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(), "Discovery Initiated",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int reasonCode) {
                Toast.makeText(getApplicationContext(),
                        "Discovery Failed : " + getDiscoveryFailureReasonCode(reasonCode),
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    private static String getDiscoveryFailureReasonCode(int reasonCode) {
        switch (reasonCode) {
            case WifiP2pManager.ERROR:
                return "ERROR";
            case WifiP2pManager.P2P_UNSUPPORTED:
                return "P2P_UNSUPPORTED";
            case WifiP2pManager.BUSY:
                return "BUSY";
        }
        return "UNKNOWN";
    }

    private class WifiP2pBroadcastReceiver extends BroadcastReceiver {

        /*
         * (non-Javadoc)
         * @see
         * android.content.BroadcastReceiver#onReceive(android.content.Context,
         * android.content.Intent)
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION
                    .equals(action)) {

                if (mManager != null) {
                    mManager.requestPeers(mChannel, mListFragment);
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * @see
     * jp.co.c_lis.sample.nikkei_linux.nl201201.sample02.ui.DeviceListFragment
     * .Listener#onDeviceClick(android.net.wifi.p2p.WifiP2pDevice)
     */
    @Override
    public void onDeviceClick(WifiP2pDevice device) {
        mDetailFragment.showDetail(device);
    }

}
