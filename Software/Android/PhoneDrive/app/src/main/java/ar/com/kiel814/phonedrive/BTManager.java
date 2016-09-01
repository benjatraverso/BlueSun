package ar.com.kiel814.phonedrive;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.widget.Toast;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class BTManager
{
	private BluetoothAdapter adapter;
	private BluetoothSocket socket;
	private BluetoothDevice device;
	Activity activity;

	//String address = null;
	//private ProgressDialog progress;
	//BluetoothAdapter myBluetooth = null;
	//private boolean isBtConnected = false;
	private static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

	private boolean connected;

	public BTManager()
	{
		adapter = null;
		socket = null;
		device = null;
		activity = null;
		connected = false;
	}

	public void initialize(Activity _activity)
	{
		activity = _activity;
		adapter = BluetoothAdapter.getDefaultAdapter();
		if(adapter == null)
		{
			Toast.makeText(activity.getApplicationContext(), "Bluetooth device not available", Toast.LENGTH_LONG).show();
		}
		else
		{
			if (!adapter.isEnabled())
			{
				Intent enable = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				activity.startActivityForResult(enable, 1);
			}
		}
	}

	public boolean connect(String address)
	{
		try
		{
			device = adapter.getRemoteDevice(address);
			socket = device.createInsecureRfcommSocketToServiceRecord(uuid);
			adapter.cancelDiscovery();
			socket.connect();
			connected = true;
		}
		catch (IOException e)
		{
			Toast.makeText(activity.getApplicationContext(), "Bluetooth error on connection.", Toast.LENGTH_LONG).show();
			connected = false;
		}
		return connected;
	}

	public void disconnect()
	{
		if (socket != null)
		{
			try
			{
				socket.close();
			}
			catch (IOException e)
			{
				Toast.makeText(activity.getApplicationContext(), "Bluetooth error on disconnection.", Toast.LENGTH_LONG).show();
			}
		}
	}

	public Set<BluetoothDevice> getPairedDevices()
	{
		return adapter.getBondedDevices();
	}

	public boolean isConnected()
	{
		return connected;
	}

	public void send(String msg)
	{
		try
		{
			socket.getOutputStream().write(msg.getBytes());
		}
		catch(IOException e)
		{
			Toast.makeText(activity.getApplicationContext(), "Bluetooth error sending data.", Toast.LENGTH_LONG).show();
		}
	}
}