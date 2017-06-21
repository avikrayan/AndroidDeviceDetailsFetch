package rayan.avik.androiddeviceversionfetch;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class MainActivity extends AppCompatActivity {
    Button check,model,manufacturer,ipAddress,deviceID,systemInfo,cpuinfo,memoInfo;
    TextView show,showinfo;
    int CVersion;
    String DeviceModel, DeviceName , IPaddress, androidDeviceId;
    Boolean IPValue;

    ProcessBuilder processBuilder;
    String[] DATA = {"/system/bin/cat", "/proc/cpuinfo"};
    InputStream inputStream;
    Process process ;
    byte[] byteArry ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        show = (TextView) findViewById(R.id.text);
        showinfo= (TextView) findViewById(R.id.tv_systeminfo);
        check = (Button) findViewById(R.id.btn_check);
        model = (Button) findViewById(R.id.btn_check_model);
        manufacturer = (Button) findViewById(R.id.btn_check_manufacturer);
        ipAddress = (Button) findViewById(R.id.btn_check_ip_address);
        deviceID = (Button) findViewById(R.id.btn_check_device_id);
        systemInfo = (Button) findViewById(R.id.btn_check_system_info);
        cpuinfo = (Button) findViewById(R.id.btn_check_cpu_info);
        memoInfo = (Button) findViewById(R.id.btn_check_memo_info);

        ///////////Check Memory Info /////////////
        memoInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showinfo.setText(getMemoInfo());
            }
        });

        /////////Check CPU info ////////////
        cpuinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCPUinfo();
            }
        });


        /////////////Check System Information////////////
        systemInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showinfo.setText(
                        "SERIAL: " + Build.SERIAL + "\n" +
                                "MODEL: " + Build.MODEL + "\n" +
                                "ID: " + Build.ID + "\n" +
                                "Manufacture: " + Build.MANUFACTURER + "\n" +
                                "Brand: " + Build.BRAND + "\n" +
                                "Type: " + Build.TYPE + "\n" +
                                "User: " + Build.USER + "\n" +
                                "BASE: " + Build.VERSION_CODES.BASE + "\n" +
                                "INCREMENTAL: " + Build.VERSION.INCREMENTAL + "\n" +
                                "SDK:  " + Build.VERSION.SDK + "\n" +
                                "BOARD: " + Build.BOARD + "\n" +
                                "BRAND: " + Build.BRAND + "\n" +
                                "HOST: " + Build.HOST + "\n" +
                                "FINGERPRINT: "+Build.FINGERPRINT + "\n" +
                                "Version Code: " + Build.VERSION.RELEASE
                );
            }
        });


        //////////////Check Device ID ////////////
        deviceID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                androidDeviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                show.setText(androidDeviceId);
            }
        });

        ////////// Check IP Address //////////////////
        ipAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetwordDetect();
            }
        });

        /////////// Check Device Name //////////////
        model.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceModel= android.os.Build.MODEL;
                show.setText(DeviceModel);
            }
        });

        ////////// Check Manufacturer ////////////
        manufacturer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceName= android.os.Build.MANUFACTURER;
                show.setText(DeviceName);
            }
        });

       //////////// Check Version ///////////////
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CVersion = android.os.Build.VERSION.SDK_INT;

                switch (CVersion){

                    case 11 :
                        show.setText("Honeycomb");
                        break;

                    case 12 :
                        show.setText("Honeycomb");
                        break;

                    case 13 :
                        show.setText("Honeycomb");
                        break;

                    case 14 :
                        show.setText("Ice Cream Sandwich");
                        break;

                    case 15 :
                        show.setText("Ice Cream Sandwich");
                        break;

                    case 16 :
                        show.setText("Jelly Bean");
                        break;

                    case 17 :
                        show.setText("Jelly Bean");
                        break;

                    case 18 :
                        show.setText("Jelly Bean");
                        break;

                    case 19 :
                        show.setText("KitKat");
                        break;

                    case 21 :
                        show.setText("Lollipop");
                        break;

                    case 22 :
                        show.setText("Lollipop");
                        break;

                    case 23 :
                        show.setText("Marshmallow");
                        break;

                    case 24 :
                        show.setText("Nougat");
                        break;

                    case 25 :
                        show.setText("Nougat");
                        break;

                    default:
                        Toast.makeText(MainActivity.this,"Not Found", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });
    }

    private String getMemoInfo() {
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        ActivityManager activityManager =
                (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(memoryInfo);

        Runtime runtime = Runtime.getRuntime();

        String strMemInfo =
                "MemoryInfo.availMem = " + memoryInfo.availMem + "\n"
                        + "MemoryInfo.totalMem = " + memoryInfo.totalMem + "\n" // API 16
                        + "\n"
                        + "Runtime.maxMemory() = " + runtime.maxMemory() + "\n"
                        + "Runtime.totalMemory() = " + runtime.totalMemory() + "\n"
                        + "Runtime.freeMemory() = " + runtime.freeMemory() + "\n";

        return strMemInfo;
    }


    private void getCPUinfo() {
        String Holder = "";
        byteArry = new byte[1024];
        try{
            processBuilder = new ProcessBuilder(DATA);

            process = processBuilder.start();

            inputStream = process.getInputStream();

            while(inputStream.read(byteArry) != -1){

                Holder = Holder + new String(byteArry);
            }
            inputStream.close();

        } catch(IOException ex){

            ex.printStackTrace();
        }
        showinfo.setText(Holder);
    }


    private void NetwordDetect() {

        boolean WIFI = false;

        boolean MOBILE = false;

        ConnectivityManager CM = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo[] networkInfo = CM.getAllNetworkInfo();

        for (NetworkInfo netInfo : networkInfo) {

            if (netInfo.getTypeName().equalsIgnoreCase("WIFI"))

                if (netInfo.isConnected())

                    WIFI = true;

            if (netInfo.getTypeName().equalsIgnoreCase("MOBILE"))

                if (netInfo.isConnected())

                    MOBILE = true;
        }

        if(WIFI == true)

        {
            IPaddress = GetDeviceipWiFiData();
            show.setText(IPaddress);
        }

        if(MOBILE == true)
        {
            IPaddress = GetDeviceipMobileData();
            show.setText(IPaddress);
        }
    }


    private String GetDeviceipMobileData() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
                 en.hasMoreElements();) {
                NetworkInterface networkinterface = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = networkinterface.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception ex) {
            Log.e("Current IP", ex.toString());
        }
        return null;
    }

    private String GetDeviceipWiFiData() {
        @SuppressLint("WifiManagerLeak") WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);

        @SuppressWarnings("deprecation")

        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

        return ip;
    }

}
