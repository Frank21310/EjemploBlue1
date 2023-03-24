package com.example.ejemploblue1

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat


class MainActivity : AppCompatActivity() {
    private val REQUEST_ENABLE_BT = 0
    private val REQUEST_DISCOVER_BT = 1

    lateinit var mBlueIv: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var mStatusBlueTv: TextView = findViewById(R.id.statusBluetoothTv)
        mBlueIv   = findViewById(R.id.bluetoothIv)
        var mOnBtn: Button       = findViewById(R.id.onBtn)
        var mOffBtn:Button       = findViewById(R.id.offBtn)
        var mDiscoverBtn:Button  = findViewById(R.id.discoverableBtn)
        var mPairedBtn:Button    = findViewById(R.id.pairedBtn)
        var mPairedTv:TextView   = findViewById(R.id.pairedTv)

        //adapter
        var mBlueAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //check if bluetooth is available or not
        if (mBlueAdapter == null){
            mStatusBlueTv.text = "Bluetooth is not available"

        }
        else {
            mStatusBlueTv.text="Bluetooth is available"
        }

        //set image according to bluetooth status(on/off)
        if (mBlueAdapter.isEnabled()){
            mBlueIv.setImageResource(R.drawable.ic_action_on)
        }
        else {
            mBlueIv.setImageResource(R.drawable.ic_action_off)
        }

        //on btn click
        //on btn click
        mOnBtn.setOnClickListener {
            if (!mBlueAdapter.isEnabled) {
                //intent to on bluetooth
                val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    startActivityForResult(intent, REQUEST_ENABLE_BT)
                    showToast("Turning On Bluetooth...")
                    mBlueIv.setImageResource(R.drawable.ic_action_on)
                    return@setOnClickListener
                }

            } else {
                showToast("Bluetooth is already on")
            }
        }
        //off btn click
        mOffBtn.setOnClickListener {
            if (mBlueAdapter.isEnabled) {
                mBlueAdapter.disable()
                showToast("Turning Bluetooth Off")
                mBlueIv.setImageResource(R.drawable.ic_action_off)
            } else {
                showToast("Bluetooth is already off")
            }
        }
        //discover bluetooth btn click
        mDiscoverBtn.setOnClickListener {
            if (!mBlueAdapter.isDiscovering) {
                showToast("Making Your Device Discoverable")
                val intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
                startActivityForResult(intent, REQUEST_DISCOVER_BT)
            }
        }

        //get paired devices btn click
        mPairedBtn.setOnClickListener{
            if (mBlueAdapter.isEnabled) {
                mPairedTv.text = "Paired Devices \n"
                val devices = mBlueAdapter.bondedDevices
                for (device in devices) {
                    mPairedTv.append(
                        """                        
                        Device: ${device.name}, $device
                        """.trimIndent()+ "\n"
                    )
                }
            } else {
                //bluetooth is off so can't get paired devices
                showToast("Turn on bluetooth to get paired devices")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_ENABLE_BT -> if (resultCode == RESULT_OK) {
                //bluetooth is on
                mBlueIv.setImageResource(R.drawable.ic_action_on)
                showToast("Bluetooth is on")
            } else {
                //user denied to turn bluetooth on
                mBlueIv.setImageResource(R.drawable.ic_action_off)
                showToast("could't on bluetooth")
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}