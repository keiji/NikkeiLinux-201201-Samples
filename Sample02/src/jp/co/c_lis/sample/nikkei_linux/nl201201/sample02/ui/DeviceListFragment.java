
package jp.co.c_lis.sample.nikkei_linux.nl201201.sample02.ui;

import java.util.ArrayList;

import jp.co.c_lis.sample.nikkei_linux.nl201201.sample02.R;
import android.app.Activity;
import android.app.ListFragment;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class DeviceListFragment extends ListFragment implements PeerListListener {
    private static final String LOG_TAG = "DeviceListFragment";
    private static final boolean DEBUG_FLG = true;

    private final ArrayList<WifiP2pDevice> mList = new ArrayList<WifiP2pDevice>();
    private Adapter mAdapter = null;

    /*
     * (non-Javadoc)
     * @see android.app.ListFragment#onCreateView(android.view.LayoutInflater,
     * android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.list, null);
        return v;
    }

    public interface Listener {
        public void onDeviceClick(WifiP2pDevice device);
    }

    private Listener mListener = new Listener() {
        @Override
        public void onDeviceClick(WifiP2pDevice device) {
        }
    };

    /*
     * (non-Javadoc)
     * @see android.app.Fragment#onAttach(android.app.Activity)
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof Listener) {
            mListener = (Listener) activity;
        }
    }

    /*
     * (non-Javadoc)
     * @see android.app.ListFragment#onListItemClick(android.widget.ListView,
     * android.view.View, int, long)
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        WifiP2pDevice device = (WifiP2pDevice) l.getAdapter().getItem(position);
        mListener.onDeviceClick(device);
    }

    /*
     * (non-Javadoc)
     * @see
     * android.net.wifi.p2p.WifiP2pManager.PeerListListener#onPeersAvailable
     * (android.net.wifi.p2p.WifiP2pDeviceList)
     */
    @Override
    public void onPeersAvailable(WifiP2pDeviceList peers) {
        mList.clear();
        mList.addAll(peers.getDeviceList());

        if (mAdapter == null) {
            mAdapter = new Adapter();
            setListAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    private class Adapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            if (convertView == null) {
                convertView = View.inflate(getActivity(), R.layout.list_row, null);
                holder = new ViewHolder();
                holder.address = (TextView) convertView.findViewById(R.id.address);
                holder.name = (TextView) convertView.findViewById(R.id.name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            WifiP2pDevice device = (WifiP2pDevice) getItem(position);
            holder.address.setText(device.deviceAddress);
            holder.name.setText(device.deviceName);
            return convertView;
        }

        private class ViewHolder {
            TextView address = null;
            TextView name = null;
        }
    }

}
