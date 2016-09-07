package fr.alick.counter;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.garmin.android.connectiq.ConnectIQ;
import com.garmin.android.connectiq.IQApp;
import com.garmin.android.connectiq.IQDevice;
import com.garmin.android.connectiq.exception.InvalidStateException;
import com.garmin.android.connectiq.exception.ServiceUnavailableException;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ConnectIQ mConnectIQ;
    private IQDevice mDevice;
    private IQApp mApp;
    private boolean mSdkReady = false;

    private TextView counterTextView;
    private TextView connectStatusTextView;

    private int counter = 0;

    private static final String LOGGING = MainActivity.class.getSimpleName();

    private ConnectIQ.IQDeviceEventListener mDeviceEventListener = new ConnectIQ.IQDeviceEventListener() {
        @Override
        public void onDeviceStatusChanged(IQDevice device, IQDevice.IQDeviceStatus status) {
            updateStatus(status);
        }
    };

    ConnectIQ.IQApplicationEventListener mAppEventListener = new ConnectIQ.IQApplicationEventListener() {
        @Override
        public void onMessageReceived(IQDevice iqDevice, IQApp iqApp, List<Object> message, ConnectIQ.IQMessageStatus iqMessageStatus) {

            if (message.size() > 0) {
                for (Object o : message) {
                    counter = (Integer) o;
                    counterTextView.setText(Integer.toString(counter));

                }
            } else {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle(R.string.received_message);
                dialog.setMessage("Received an empty message from the watch");
                dialog.setPositiveButton(android.R.string.ok, null);
                dialog.create().show();
            }
        }
    };

    private ConnectIQ.ConnectIQListener mListener = new ConnectIQ.ConnectIQListener() {
        @Override
        public void onInitializeError(ConnectIQ.IQSdkErrorStatus errStatus) {
            if( null != connectStatusTextView)
                connectStatusTextView.setText(R.string.initialization_error + errStatus.name());
            mSdkReady = false;
        }

        @Override
        public void onSdkReady() {
            loadDevices();
            mSdkReady = true;
        }

        @Override
        public void onSdkShutDown() {
            mSdkReady = false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        connectStatusTextView = (TextView)findViewById(R.id.connect_status);

        // TETHERED connection type for sim test.
        // Use WIRELESS for real device bluetooth connection.
        mConnectIQ = ConnectIQ.getInstance(this, ConnectIQ.IQConnectType.TETHERED);

        // Device Connect IQ Application ID (manifest file).
        mApp = new IQApp("b36cddb3d31040b6ba72ca00d7cb2d85");

        // Initialize the SDK
        mConnectIQ.initialize(this, true, mListener);

        counterTextView = (TextView) findViewById(R.id.counter);
        counterTextView.setTextSize(200);
        counterTextView.setText(String.valueOf(counter));
    }

    private void sendCounterUpdateToGarmin(final int counter) {
        try {
            mConnectIQ.sendMessage(mDevice, mApp, counter, new ConnectIQ.IQSendMessageListener() {
                @Override
                public void onMessageStatus(IQDevice device, IQApp app, ConnectIQ.IQMessageStatus status) {
                    Toast.makeText(MainActivity.this, status.name(), Toast.LENGTH_LONG).show();
                }
            });
        } catch (InvalidStateException e) {
            Toast.makeText(this, "ConnectIQ is not in a valid state", Toast.LENGTH_LONG).show();
        } catch (ServiceUnavailableException e) {
            Toast.makeText(this, "ConnectIQ service is unavailable.", Toast.LENGTH_LONG).show();
        }
    }

    public void onClickUp(View view) {
        counter--;
        counterTextView.setText(Integer.toString(counter));
        sendCounterUpdateToGarmin(counter);
    }

    public void onClickDown(View view) {
        counter++;
        counterTextView.setText(Integer.toString(counter));
        sendCounterUpdateToGarmin(counter);
    }

    public void onClickReset(View view) {
        counter = 0;
        counterTextView.setText(Integer.toString(counter));
        sendCounterUpdateToGarmin(0);
    }

    private void updateStatus(IQDevice.IQDeviceStatus status) {
        Log.d(LOGGING, "Updating Connect Status");
        switch(status) {
            case CONNECTED:
                connectStatusTextView.setText(R.string.status_connected);
                connectStatusTextView.setTextColor(Color.GREEN);
                break;
            case NOT_CONNECTED:
                connectStatusTextView.setText(R.string.status_not_connected);
                connectStatusTextView.setTextColor(Color.RED);
                break;
            case NOT_PAIRED:
                connectStatusTextView.setText(R.string.status_not_paired);
                connectStatusTextView.setTextColor(Color.RED);
                break;
            case UNKNOWN:
                connectStatusTextView.setText(R.string.status_unknown);
                connectStatusTextView.setTextColor(Color.RED);
                break;
        }
    }

    // List of known devices
    public void loadDevices() {
        try {
            List<IQDevice> devices = mConnectIQ.getKnownDevices();

            if (devices != null) {

                for (IQDevice device : devices) {
                    mConnectIQ.registerForEvents(device, mDeviceEventListener, mApp, mAppEventListener);
                    mDevice = device;

                    IQDevice.IQDeviceStatus status = mConnectIQ.getDeviceStatus(device);
                    updateStatus(status);
                }
            }
        } catch (InvalidStateException e) {
        } catch (ServiceUnavailableException e) {
            // Garmin Connect Mobile is not installed or needs to be upgraded.
            if (null != connectStatusTextView) {
                connectStatusTextView.setText(R.string.service_unavailable);
            }
        }
    }
}
