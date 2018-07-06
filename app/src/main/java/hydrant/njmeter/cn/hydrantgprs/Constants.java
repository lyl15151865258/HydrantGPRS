package hydrant.njmeter.cn.hydrantgprs;

/**
 * 部分常量值，用于解决提示魔法值的提示和接口返回数据解析用
 * Created by LiYuliang on 2017/10/25.
 *
 * @author LiYuliang
 * @version 2017/11/21
 */

public class Constants {

    public static final String EMPTY = "";
    public static final String FAIL = "fail";
    public static final String NEW_LINE = "\n";
    public static final String POINT = ".";
    public static final String HYPHEN = "-";
    public static final String SUCCESS = "success";

    /**
     * EventBus标记
     */
    public static final String CONNECT_SUCCESS_SOCKET = "connectSuccess_socket";
    public static final String CONNECT_SUCCESS_WEBSOCKET = "connectSuccess_webSocket";
    public static final String CONNECT_FAIL_SOCKET = "connectFail_socket";
    public static final String CONNECT_FAIL_WEBSOCKET = "connectFail_webSocket";
    public static final String CONNECT_OPEN_SOCKET = "connectOpen_socket";
    public static final String CONNECT_OPEN_WEBSOCKET = "connectOpen_webSocket";
    public static final String CONNECT_CLOSE_SOCKET = "connectClose_socket";
    public static final String CONNECT_CLOSE_WEBSOCKET = "connectClose_webSocket";
    public static final String SHOW_TOAST_SOCKET = "showToast_socket";
    public static final String SHOW_TOAST_WEBSOCKET = "showToast_webSocket";
    public static final String SHOW_DATA_SOCKET = "showData_socket";
    public static final String SHOW_DATA_WEBSOCKET = "showData_webSocket";

    public static final int METER_ID_LENGTH = 8;
    public static final int HYDRANT_ID_LENGTH = 8;
    public static final int DTU_ID_LENGTH = 5;
    public static final int IMEI_LENGTH = 11;
    public static final String DEFAULT_METER_ID = "FFFFFFFF";

    public static final int ACTIVITY_REQUEST_CODE_100 = 100;
    public static final int ACTIVITY_REQUEST_CODE_200 = 200;
    public static final int ACTIVITY_RESULT_CODE_100 = 100;
    public static final int ACTIVITY_RESULT_CODE_200 = 200;

    /**
     * 退出程序点击两次返回键的间隔时间
     */
    public static final int EXIT_DOUBLE_CLICK_TIME = 2000;
    /**
     * 距离达到1000m进行单位转换，变为1km
     */
    public static final int KILOMETER = 1000;
    /**
     * 网页加载完成进度
     */
    public static final int PROGRESS_WEBVIEW = 100;


    /**
     * *******************************************************  数值0（int型、float型、String型） *******************************************************
     */
    public static final int ZERO_INT = 0;
    public static final float ZERO_FLOAT = 0.00f;
    public static final String ZERO_FLOAT_STRING = "0.00";
    public static final String ZERO_STRING = "0";


    /**
     * *******************************************************  百度鹰眼轨迹 *******************************************************
     */
    public static final int REQUEST_CODE = 1;

    public static final int RESULT_CODE = 1;

    public static final int DEFAULT_RADIUS_THRESHOLD = 0;

    public static final int PAGE_SIZE = 5000;

    /**
     * 轨迹分析查询间隔时间（1分钟）
     */
    public static final int ANALYSIS_QUERY_INTERVAL = 60;

    /**
     * 停留点默认停留时间（1分钟）
     */
    public static final int STAY_TIME = 60;

    /**
     * 启动停留时间
     */
    public static final int SPLASH_TIME = 3000;

    /**
     * 默认采集周期
     */
    public static final int DEFAULT_GATHER_INTERVAL = 5;

    /**
     * 默认打包周期
     */
    public static final int DEFAULT_PACK_INTERVAL = 30;

    /**
     * 实时定位间隔(单位:秒)
     */
    public static final int LOC_INTERVAL = 10;

    /**
     * 最后一次定位信息
     */
    public static final String LAST_LOCATION = "last_location";
}
