package jp.co.c_lis.sample.nikkei_linux.nl201201.sample01.ui;

import java.nio.charset.Charset;

import jp.co.c_lis.sample.nikkei_linux.nl201201.sample01.R;
import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcAdapter.OnNdefPushCompleteCallback;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements CreateNdefMessageCallback, OnNdefPushCompleteCallback {

	private NfcAdapter mNfcAdapter = null;
	private EditText mMessage = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mMessage = (EditText) findViewById(R.id.message);

		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		if (mNfcAdapter != null) {
	        mNfcAdapter.setNdefPushMessageCallback(this, this);
			mNfcAdapter.setOnNdefPushCompleteCallback(this, this);
		} else {
			Toast.makeText(this, "NFCが無効か、NFCを搭載していない端末の可能性があります。",
					Toast.LENGTH_LONG).show();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
			processIntent(getIntent());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onNewIntent(android.content.Intent)
	 */
	@Override
	public void onNewIntent(Intent intent) {
		setIntent(intent);
	}

	private void processIntent(Intent intent) {
		Parcelable[] rawMsgs = intent
				.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
		NdefMessage msg = (NdefMessage) rawMsgs[0];

		String text = new String(msg.getRecords()[0].getPayload());
		mMessage.setText(text);
	}

	private NdefRecord createMimeRecord(String mimeType, byte[] payload) {
		byte[] mimeBytes = mimeType.getBytes(Charset.forName("US-ASCII"));
		NdefRecord mimeRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA,
				mimeBytes, new byte[0], payload);

		return mimeRecord;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.nfc.NfcAdapter.OnNdefPushCompleteCallback#onNdefPushComplete(
	 * android.nfc.NfcEvent)
	 */
	public void onNdefPushComplete(NfcEvent arg0) {
		mHandler.obtainMessage(HANDLE_MESSAGE_SENT).sendToTarget();
	}

	private static final int HANDLE_MESSAGE_SENT = 0x1;

	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HANDLE_MESSAGE_SENT:
				Toast.makeText(getApplicationContext(), "メッセージを送信しました",
						Toast.LENGTH_LONG).show();
				break;
			}
		}
	};

	private NdefMessage getNdefMessage() {
		String msg = mMessage.getText().toString();
		String mimeType = "application/jp.co.c_lis.sample.nikkei_linux.nl201201.sample01";
		NdefMessage ndefMessage = new NdefMessage(
				new NdefRecord[] { createMimeRecord(mimeType, msg.getBytes()) });

		return ndefMessage;
	}

	/*
	 * (non-Javadoc)
	 * @see android.nfc.NfcAdapter.CreateNdefMessageCallback#createNdefMessage(android.nfc.NfcEvent)
	 */
    public NdefMessage createNdefMessage(NfcEvent event) {
        return getNdefMessage();
    }
}