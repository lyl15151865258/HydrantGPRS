package hydrant.njmeter.cn.hydrantgprs;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import utils.AnalysisUtils;
import utils.LogUtils;
import utils.MathUtils;
import utils.RegexUtils;
import utils.ScreenTools;

public class MainActivity extends BaseActivity {

    private ImageView iv_search_bluetooth_device;
    private TextView tvImei, tvGprsParam, tvDate, tvTime;
    private RadioButton radioTcpServer;
    private EditText editTextIMEI, etIp, editTextPort;
    private CheckBox checkboxSyncTime;
    private Button btn_read_imei, btnReadComm;

    boolean isSetParamLegal;

    private static Context context;
    private final static String SSP_UUID = "00001101-0000-1000-8000-00805F9B34FB";      //SPP服务UUID号
    private static InputStream inputStream;                                             //输入流，用来接收蓝牙数据
    public static String data = "";                                                     //显示用数据缓存
    private static BluetoothDevice bluetoothDevice = null;                              //蓝牙设备
    public static BluetoothSocket bluetoothSocket = null;                               //蓝牙通信socket
    private static boolean exit = false;                                                //切换不同蓝牙工具时避免创建多个接收数据线程
    private BluetoothAdapter bluetoothAdapter;                                          //本地蓝牙适配器
    private boolean bluetoothIsOpened = false;                                          //获取进入页面时蓝牙打开状态，退出页面时根据这个判断是否需要关闭蓝牙
    private static String tx;                                                           //发送的指令
    //蓝牙搜索相关
    private static ArrayAdapter<String> adapter1, adapter2;                             //已配对和未配对list的适配器
    private static ArrayList<String> deviceList_bonded = new ArrayList<>();             //已配对列表
    private static ArrayList<String> deviceList_found = new ArrayList<>();              //未配对列表
    private DeviceReceiver deviceReceiver = new DeviceReceiver();                       //蓝牙搜索广播
    private boolean isConnecting = false;                                               //标记是否正在连接过程中
    private boolean isCancelDiscovery = false;                                          //标记是否是取消搜索（不显示“没有搜索到*****”）

    private MyHandler myHandler = new MyHandler(this);
    private ConnectBluetoothDialog connectBluetoothDialog;

    private int mYear, mMonth, mDay, mHour, mMinute, mSecond;

    private String productType = ProductType.HYDRANT_STRING;
    private SyncTimeTask syncTimeTask;
    private static boolean shouldWait = false;

    private static final String[] authBaseArr = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    private static final int authBaseRequestCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        LinearLayout ll_root = (LinearLayout) findViewById(R.id.ll_root);
        ll_root.setPadding(0, ScreenTools.getStatusBarHeight(this), 0, 0);
        iv_search_bluetooth_device = (ImageView) findViewById(R.id.iv_search_bluetooth_device);
        iv_search_bluetooth_device.setOnClickListener(onClickListener);
        isSetParamLegal = true;

        editTextPort = findViewById(R.id.editTextPort);
        radioTcpServer = findViewById(R.id.radioTcpServer);

        tvImei = findViewById(R.id.tv_imei);
        tvGprsParam = findViewById(R.id.tvGprsParam);
        tvDate = findViewById(R.id.tv_date);
        tvTime = findViewById(R.id.tv_time);
        editTextIMEI = findViewById(R.id.et_imei);
        etIp = findViewById(R.id.et_ip);
        editTextPort = findViewById(R.id.editTextPort);
        radioTcpServer = findViewById(R.id.radioTcpServer);
        checkboxSyncTime = findViewById(R.id.CheckBoxsyn);

        btn_read_imei = findViewById(R.id.btn_read_imei);
        btn_read_imei.setOnClickListener(onClickListener);
        findViewById(R.id.btn_set_imei).setOnClickListener(onClickListener);

        (findViewById(R.id.btnGprsDefault)).setOnClickListener(onClickListener);
        (findViewById(R.id.btnDomainNameDefault)).setOnClickListener(onClickListener);
        (findViewById(R.id.btnSetComm)).setOnClickListener(onClickListener);

        btnReadComm = findViewById(R.id.btnReadComm);
        btnReadComm.setOnClickListener(onClickListener);
        (findViewById(R.id.btn_change_time)).setOnClickListener(onClickListener);

        initBluetooth();
        initBroadcastReceiver();

        // 申请权限
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            if (!hasBasePhoneAuth()) {
                this.requestPermissions(authBaseArr, authBaseRequestCode);
                return;
            }
        }
        syncTimeTask = new SyncTimeTask(this);
        syncTimeTask.execute();
    }

    private boolean hasBasePhoneAuth() {
        PackageManager pm = this.getPackageManager();
        for (String auth : authBaseArr) {
            if (pm.checkPermission(auth, this.getPackageName()) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    //初始化手机蓝牙
    private void initBluetooth() {
        try {
            //获取本地蓝牙适配器
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            //如果打开本地蓝牙设备不成功，提示信息，结束程序
            if (bluetoothAdapter == null) {
                showToast("无法打开手机蓝牙");
                finish();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        bluetoothIsOpened = bluetoothAdapter.isEnabled();
        // 设置设备可以被搜索
        new Thread() {
            public void run() {
                if (!bluetoothIsOpened) {
                    if (!bluetoothAdapter.enable()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showToast("您禁止了打开蓝牙");
                            }
                        });
                    }
                }
            }
        }.start();
    }

    private void initBroadcastReceiver() {
        //注册蓝牙广播接收者
        IntentFilter filterStart = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        IntentFilter filterEnd = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        context.registerReceiver(deviceReceiver, filterStart);
        context.registerReceiver(deviceReceiver, filterEnd);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String tx;
            switch (view.getId()) {
                case R.id.iv_search_bluetooth_device:
                    if (hasBasePhoneAuth()) {
                        try {
                            if (isConnecting) {
                                showToast("正在连接蓝牙设备，请稍后");
                            } else {
                                ConnectBluetoothDevice();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        showToast("需授予定位权限并打开GPS才能连接蓝牙");
                    }
                    break;
                case R.id.btn_set_imei:
                    String imei = editTextIMEI.getText().toString().replace(" ", "");
                    if (imei.length() == Constants.IMEI_LENGTH) {
                        int checksum = 557;
                        StringBuilder sb = new StringBuilder();
                        sb.append("7B89002B3030303030303030303030684811111111001111070EC14100");
                        for (int i = 0; i < imei.length(); i++) {
                            sb.append('3');
                            sb.append(imei.charAt(i));
                            checksum += 3 * 16 + Integer.parseInt("" + (imei.charAt(i)));
                        }
                        String cs = Integer.toHexString(checksum);
                        sb.append(cs.substring(cs.length() - 2));
                        sb.append("167B");
                        System.out.println(sb.toString());
                        tx = sb.toString();
                        writeData(tx);
                    } else {
                        showToast("请输入11位IMEI号");
                    }
                    break;
                case R.id.btn_read_imei:
                    tvImei.setText("");
                    tx = "7B89002030303030303030303030306848111111110011110703C1420023167B";
                    writeData(tx);
                    break;
                case R.id.btnGprsDefault:
                    etIp.setText(NetWork.DEFAULT_IP_HYDRANT);
                    break;
                case R.id.btnDomainNameDefault:
                    etIp.setText(NetWork.DEFAULT_DOMAIN_NAME_HYDRANT);
                    break;
                case R.id.btnSetComm:
                    //7B89003D30303030303030303030306848111111110011110703C11E0122544350222C2235382E3234302E34372E3530222C2235303034220D2361167B
                    StringBuilder sb = new StringBuilder();
                    sb.append("7B89003D30303030303030303030306848111111110011110703C11E01");
                    StringBuilder param = new StringBuilder();
                    if (radioTcpServer.isChecked()) {
                        param.append("\"TCP\"");
                    } else {
                        param.append("\"UDP\"");
                    }
                    param.append(",");
                    param.append("\"");
                    String ip = etIp.getText().toString();
                    param.append(ip);
                    param.append("\",\"");
                    String port = editTextPort.getText().toString();
                    param.append(port);
                    param.append("\"");
                    if (TextUtils.isEmpty(ip)) {
                        showToast("请输入IP地址或域名");
                        return;
                    }
                    if (TextUtils.isEmpty(port)) {
                        showToast("请输入端口号");
                        return;
                    }
                    if (!RegexUtils.checkPort(port)) {
                        showToast("端口号输入错误");
                        return;
                    }
                    if (RegexUtils.checkIpAddress(ip) || RegexUtils.checkDomainName(ip)) {
                        sb.append(MathUtils.getHexStr(param.toString()));
                        sb.append("0D23");
                        String checkStr = sb.substring(sb.indexOf("68"));
                        sb.append(AnalysisUtils.getCSSum(checkStr, 0));
                        sb.append("167B");
                        sb.replace(6, 8, sb.length() / 2 < 17 ? ("0" + Integer.toHexString(sb.length() / 2)) : (Integer.toHexString(sb.length() / 2)));
                        tx = sb.toString();
                        writeData(tx);
                    } else {
                        showToast("IP地址或域名输入错误");
                    }
                    break;
                case R.id.btnReadComm:
                    tvGprsParam.setText("");
                    tx = "7B89002130303030303030303030306848111111110011110703C12F000111167B";
                    writeData(tx);
                    break;
                case R.id.btn_change_time:
                    String x = "";
                    String rx = "";
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    SimpleDateFormat sdf1 = new SimpleDateFormat("ssmmHHddMMyyyy");
                    x = tvDate.getText().toString() + " " + tvTime.getText().toString();
                    try {
                        Date xd = sdf.parse(x);
                        rx = sdf1.format(xd);
                    } catch (Exception e) {
                        showToast("请正确输入当前时间");
                        return;
                    }

                    tx = adjusttime("FFFFFFFF", productType, "001111", rx);
                    writeData(tx);
                    break;
                default:
                    break;
            }
        }
    };

    public String adjusttime(String meterid, String producttypetx, String factorycode, String x) {
        String r, rx;
        try {
            rx = x.substring(0, 10) + x.substring(12, 14) + x.substring(10, 12);
            String cs = AnalysisUtils.getCSSum("68" + producttypetx + meterid + factorycode + "040AA01500" + rx, 0);
            r = "68" + producttypetx + meterid + factorycode + "040AA01500" + rx + cs + "16";
        } catch (Exception e) {
            e.printStackTrace();
            r = "";
        }
        return r;
    }

    public void writeData(final String tx) {
        if (!(bluetoothSocket != null && bluetoothSocket.isConnected())) {
            showToast("请先返回连接蓝牙工具");
            return;
        }
        LogUtils.d("bluetooth", "发送的指令为：" + tx);
        data = "";
        try {
            OutputStream os = bluetoothSocket.getOutputStream();    //蓝牙连接输出流
            byte[] bos = new byte[tx.length() / 2];
            for (int i = 0; i < (tx.length() / 2); i++) {           //手机中换行为0a,将其改为0d 0a后再发送
                bos[i] = (byte) HexS2ToInt(tx.substring(2 * i, 2 * i + 2));
            }
            os.write(bos);
            //发送显示消息，进行显示刷新
            //myHandler.sendMessage(myHandler.obtainMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //发送数据功能函数
    static int HexS1ToInt(char ch) {
        if ('a' <= ch && ch <= 'f') {
            return ch - 'a' + 10;
        }
        if ('A' <= ch && ch <= 'F') {
            return ch - 'A' + 10;
        }
        if ('0' <= ch && ch <= '9') {
            return ch - '0';
        }
        throw new IllegalArgumentException(String.valueOf(ch));
    }

    static int HexS2ToInt(String S) {
        int r;
        char a[] = S.toCharArray();
        r = HexS1ToInt(a[0]) * 16 + HexS1ToInt(a[1]);
        return r;
    }

    public String getHexStr(TcpUdpParam param) {
        String strtcp = param.toString();
        byte[] tcpbytes = strtcp.getBytes();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tcpbytes.length; i++) {
            sb.append(Integer.toHexString(tcpbytes[i]));
        }
        System.out.println(sb.toString().toUpperCase());
        return sb.toString().toUpperCase();
    }

    public int getCheckSum(String param) {
        StringBuilder sb = new StringBuilder();
        int res = 0;
        for (int i = 0; i < param.length() / 2; i++) {
            res += AnalysisUtils.HexS2ToInt(param.substring(i * 2, i * 2 + 2));
        }
        return res;
    }

    //连接蓝牙设备
    private void ConnectBluetoothDevice() {
        if (!bluetoothAdapter.isEnabled()) {
            new Thread() {
                public void run() {
                    if (!bluetoothAdapter.enable()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showToast("您禁止了打开蓝牙");
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showToast("手机蓝牙已打开，请重新连接");
                            }
                        });
                    }
                }
            }.start();
        } else {
            if (bluetoothSocket == null || !bluetoothSocket.isConnected()) {
                //如未连接设备则进行设备搜索
                showBluetoothList();
            } else {
                //提示是否断开蓝牙
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(R.string.app_notice).setIcon(android.R.drawable.ic_dialog_info).setNegativeButton(R.string.amenderr_setCancle, null);
                builder.setPositiveButton(R.string.amenderr_setOk, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        disConnect();
                        iv_search_bluetooth_device.setImageResource(R.drawable.bluetooth_disconnected);
                    }
                });
                builder.show();
            }
        }
    }

    //显示蓝牙设备列表
    private void showBluetoothList() {
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
            isCancelDiscovery = true;
        } else {
            isCancelDiscovery = false;
        }
        bluetoothAdapter.startDiscovery();
        if (connectBluetoothDialog == null) {
            connectBluetoothDialog = new ConnectBluetoothDialog(context);
        }
        adapter1 = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, deviceList_bonded);
        ListView lv_bonded = connectBluetoothDialog.findViewById(R.id.lv_bonded);
        lv_bonded.setOnItemClickListener(onItemClickListener);
        ListView lv_found = connectBluetoothDialog.findViewById(R.id.lv_found);
        lv_found.setOnItemClickListener(onItemClickListener);
        adapter2 = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, deviceList_found);
        lv_bonded.setAdapter(adapter1);
        lv_found.setAdapter(adapter2);
        deviceList_bonded.clear();
        deviceList_found.clear();
        adapter1.notifyDataSetChanged();
        adapter2.notifyDataSetChanged();
        connectBluetoothDialog.setCancelable(true);
        connectBluetoothDialog.show();
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ArrayList<String> list;
            switch (parent.getId()) {
                case R.id.lv_bonded:
                    list = deviceList_bonded;
                    try {
                        if (bluetoothAdapter != null && bluetoothAdapter.isDiscovering()) {
                            bluetoothAdapter.cancelDiscovery();
                        }
                        String msg = list.get(position);
                        String mac = msg.substring(msg.length() - 17);
                        connectBluetoothDialog.cancel();
                        connectDevice(mac);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.lv_found:
                    list = deviceList_found;
                    try {
                        if (bluetoothAdapter != null && bluetoothAdapter.isDiscovering()) {
                            bluetoothAdapter.cancelDiscovery();
                        }
                        String msg = list.get(position);
                        String mac = msg.substring(msg.length() - 17);
                        connectBluetoothDialog.cancel();
                        connectDevice(mac);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    //点击连接蓝牙设备
    private void connectDevice(final String mac) {
        //蓝牙连接会阻塞线程，开启子线程连接
        new Thread(new Runnable() {
            @Override
            public void run() {
                isConnecting = true;
                // 得到蓝牙设备句柄
                bluetoothDevice = bluetoothAdapter.getRemoteDevice(mac);
                // 用服务号得到socket
                try {
                    bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(UUID.fromString(SSP_UUID));
                } catch (IOException e) {
                    myHandler.sendMessage(myHandler.obtainMessage(-1));
                    isConnecting = false;
                }
                try {
                    bluetoothSocket.connect();
                    myHandler.sendMessage(myHandler.obtainMessage(0));
                } catch (IOException e) {
                    try {
                        myHandler.sendMessage(myHandler.obtainMessage(-1));
                        bluetoothSocket.close();
                        bluetoothSocket = null;
                    } catch (IOException ee) {
                        myHandler.sendMessage(myHandler.obtainMessage(-1));
                        isConnecting = false;
                    }
                    isConnecting = false;
                    return;
                }
                //打开接收线程
                try {
                    inputStream = bluetoothSocket.getInputStream();   //得到蓝牙数据输入流
                } catch (IOException e) {
                    myHandler.sendMessage(myHandler.obtainMessage(-2));
                    isConnecting = false;
                    return;
                }
                exit = false;
                isConnecting = false;
                createReadThread();
            }
        }).start();
    }

    //蓝牙搜索广播
    private class DeviceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            LogUtils.d("bluetooth", "当前蓝牙状态：" + action);
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                LogUtils.d("bluetooth", "正在搜索蓝牙设备");
                //搜索到新设备
                BluetoothDevice btd = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //搜索没有配过对的蓝牙设备
                if (btd.getBondState() != BluetoothDevice.BOND_BONDED) {
                    LogUtils.d("bluetooth", "搜索到已配对的蓝牙设备");
                    if (!deviceList_found.contains(btd.getName() + '\n' + btd.getAddress())) {
                        deviceList_found.add(btd.getName() + '\n' + btd.getAddress());
                        try {
                            adapter2.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else if (btd.getBondState() != BluetoothDevice.BOND_NONE) {
                    LogUtils.d("bluetooth", "搜索到未配对的蓝牙设备");
                    if (!deviceList_bonded.contains(btd.getName() + '\n' + btd.getAddress())) {
                        deviceList_bonded.add(btd.getName() + '\n' + btd.getAddress());
                        try {
                            adapter1.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                LogUtils.d("bluetooth", "结束搜索蓝牙设备");
                //搜索结束
                if (deviceList_bonded.size() == 0 && !isCancelDiscovery) {
                    deviceList_bonded.add("没有搜索到已配对的蓝牙设备");
                    try {
                        adapter1.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (deviceList_found.size() == 0 && !isCancelDiscovery) {
                    deviceList_found.add("没有搜索到未配对的蓝牙设备");
                    try {
                        adapter2.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    //断开蓝牙连接
    public static void disConnect() {
        //关闭连接socket
        if (bluetoothSocket != null) {
            try {
                bluetoothSocket.close();
                bluetoothSocket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //关闭输入流
        if (inputStream != null) {
            try {
                inputStream.close();
                inputStream = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (bluetoothDevice != null) {
            bluetoothDevice = null;
        }
        //关闭接收数据的子线程
        exit = true;
    }

    //创建接收数据线程
    private void createReadThread() {
        new Thread() {
            public void run() {
                int num;
                byte[] buffer = new byte[1024];
                //接收线程
                while (!exit) {
                    try {
                        while (!exit) {
                            num = inputStream.read(buffer);         //读入数据
                            String s0 = "";
                            for (int i = 0; i < num; i++) {
                                int b = (int) buffer[i];
                                if (b < 0) b = 256 + b;
                                s0 = s0 + Integer.toHexString(b / 16) + Integer.toHexString(b % 16);
                            }
                            data += s0;   //写入接收缓存
                            if (inputStream.available() == 0)
                                break;  //短时间没有数据才跳出进行显示
                        }
                    } catch (IOException e) {
                        myHandler.sendMessage(myHandler.obtainMessage(1));
                        break;
                    }
                    //发送显示消息，进行显示刷新
                    myHandler.sendMessage(myHandler.obtainMessage(2));
                }
            }
        }.start();
    }

    //自定义的Handler，Handler类应该定义成静态类，否则可能导致内存泄露
    private static class MyHandler extends Handler {
        WeakReference<MainActivity> mActivity;

        MyHandler(MainActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MainActivity theActivity = mActivity.get();
            switch (msg.what) {
                case -2:
                    theActivity.showToast("接收数据失败");
                    break;
                case -1:
                    theActivity.showToast("连接失败");
                    break;
                case 0:
                    theActivity.iv_search_bluetooth_device.setImageResource(R.drawable.bluetooth_connected);
                    theActivity.showToast("连接" + bluetoothDevice.getName() + "成功");
                    break;
                case 1:
                    disConnect();
                    theActivity.iv_search_bluetooth_device.setImageResource(R.drawable.bluetooth_disconnected);
                    theActivity.showToast("蓝牙连接断开");
                    break;
                case 2:
                    data = data.toUpperCase().replace(" ", "");
                    if (data == null || "".equals(data)) {
                        return;
                    }
                    if (data.contains("4D4F4449465920495031202020204F4B")) {
                        theActivity.showToast("修改网络参数参数成功");
                        theActivity.btnReadComm.performClick();
                        data = "";
                    } else if (data.contains("494D454920534554204F4B2020202020")) {
                        theActivity.showToast("修改IMEI号成功");
                        theActivity.btn_read_imei.performClick();
                        data = "";
                    } else {
                        System.out.println("data:" + data);
                        int start7B = data.indexOf("7B");
                        if (start7B != -1) {
                            String newdata = data.substring(start7B + 2);
                            System.out.println("newdata:" + newdata);
                            int end7B = newdata.lastIndexOf("7B");
                            if (end7B != -1) {
                                int length = AnalysisUtils.HexS2ToInt(newdata.substring(4, 6));
                                String aframe = newdata.substring(0, end7B);
                                if (aframe.length() == 2 * (length - 2)) {
                                    System.out.println("aframe:" + aframe);
                                    StringBuilder builder = new StringBuilder();
                                    builder.append(aframe);
                                    if (builder.charAt(46) == '8'
                                            && builder.charAt(47) == '7'
                                            && builder.charAt(50) == 'C'
                                            && builder.charAt(51) == '1'
                                            && builder.charAt(52) == '4'
                                            && builder.charAt(53) == '2') {
                                        String imei = aframe.substring(4 + 2, 4 + 2 + 22);
                                        System.out.println(imei);
                                        StringBuilder sb = new StringBuilder();
                                        for (int i = 0; i < imei.length() / 2; i++) {
                                            sb.append((Integer.parseInt(imei.substring(i * 2, (i + 1) * 2))) - 30);
                                        }
                                        System.out.println(sb.toString());
                                        theActivity.tvImei.setText(sb.toString());
                                        theActivity.tvImei.setTextColor(ContextCompat.getColor(theActivity, R.color.darkgreen));
                                        theActivity.showToast("读取IMEI号成功");
                                        data = "";
                                    } else if (builder.charAt(46) == '8'
                                            && builder.charAt(47) == '7'
                                            && builder.charAt(50) == 'C'
                                            && builder.charAt(51) == '1'
                                            && builder.charAt(52) == '1'
                                            && builder.charAt(53) == 'F') {
                                        StringBuilder param = new StringBuilder();
                                        switch (aframe.substring(58, 64)) {
                                            case "544350":
                                                //TCP连接
                                                theActivity.radioTcpServer.setChecked(true);
                                                param.append("\"TCP\"");
                                                break;
                                            case "554450":
                                                //UDP连接
                                                theActivity.radioTcpServer.setChecked(false);
                                                param.append("\"UDP\"");
                                                break;
                                            default:
                                                break;
                                        }
                                        param.append(",");
                                        param.append("\"");
                                        String strtcpip = aframe.substring(56);
                                        String[] arrtcpip = strtcpip.split("222C22");
                                        String ip = arrtcpip[1];
                                        param.append(MathUtils.getStringHex(ip));
                                        param.append("\",\"");
                                        String strport = arrtcpip[2];
                                        int index = strport.indexOf("22");
                                        if (index % 2 == 1) {
                                            index++;
                                        }
                                        strport = strport.substring(0, index);
                                        param.append(MathUtils.getStringHex(strport));
                                        param.append("\"");
                                        theActivity.tvGprsParam.setText(param.toString());
                                        theActivity.tvGprsParam.setTextColor(ContextCompat.getColor(theActivity, R.color.darkgreen));
                                        theActivity.showToast("读取网络配置成功");
                                        data = "";
                                    }
                                }
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private static class SyncTimeTask extends AsyncTask<Void, Void, Void> {

        private WeakReference<MainActivity> mainActivityWeakReference;

        private SyncTimeTask(MainActivity fragment) {
            mainActivityWeakReference = new WeakReference<>(fragment);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            while (true) {
                if (isCancelled()) {
                    break;
                }
                if (shouldWait) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                publishProgress();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate();
            if (isCancelled()) {
                return;
            }
            MainActivity mainActivity = mainActivityWeakReference.get();
            if (mainActivity.checkboxSyncTime.isChecked()) {
                final Calendar c = Calendar.getInstance();
                mainActivity.mYear = c.get(Calendar.YEAR);
                mainActivity.mMonth = c.get(Calendar.MONTH);
                mainActivity.mDay = c.get(Calendar.DAY_OF_MONTH);
                mainActivity.mHour = c.get(Calendar.HOUR_OF_DAY);
                mainActivity.mMinute = c.get(Calendar.MINUTE);
                mainActivity.mSecond = c.get(Calendar.SECOND);
                mainActivity.tvDate.setText(new StringBuilder().append(mainActivity.mYear).append("-")
                        .append((mainActivity.mMonth + 1) < 10 ? "0" + (mainActivity.mMonth + 1) : (mainActivity.mMonth + 1))
                        .append("-")
                        .append((mainActivity.mDay < 10) ? "0" + mainActivity.mDay : mainActivity.mDay));
                mainActivity.tvTime.setText(new StringBuilder().append((mainActivity.mHour < 10) ? "0" + mainActivity.mHour : mainActivity.mHour)
                        .append(":")
                        .append((mainActivity.mMinute < 10) ? "0" + mainActivity.mMinute : mainActivity.mMinute)
                        .append(":")
                        .append((mainActivity.mSecond < 10) ? "0" + mainActivity.mSecond : mainActivity.mSecond));
            }
            shouldWait = true;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == authBaseRequestCode) {
            for (int ret : grantResults) {
                if (ret == 0) {
                    continue;
                } else {
                    showToast("必须授予定位权限才能连接蓝牙");
                    return;
                }
            }
            try {
                if (isConnecting) {
                    showToast("正在连接蓝牙设备，请稍后");
                } else {
                    ConnectBluetoothDevice();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (deviceReceiver != null) {
            try {
                context.unregisterReceiver(deviceReceiver);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        if (bluetoothSocket != null) {
            //关闭连接socket
            try {
                bluetoothSocket.close();
                bluetoothSocket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (bluetoothDevice != null) {
            bluetoothDevice = null;
        }
        myHandler.removeCallbacksAndMessages(null);
        //如果异步任务不为空并且状态是运行时，就取消这个异步任务
        if (syncTimeTask != null && syncTimeTask.getStatus() == AsyncTask.Status.RUNNING) {
            syncTimeTask.cancel(true);
        }
    }
}