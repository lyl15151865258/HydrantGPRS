package hydrant.njmeter.cn.hydrantgprs;

/**
 * 网络配置常量值
 * Created by LiYuliang on 2017/8/16 0016.
 *
 * @author LiYuliang
 * @version 2017/10/19
 */

public class NetWork {

    /**
     * 主账号IP地址
     */
    public static final String SERVER_HOST_MAIN = "47.95.193.40";
    /**
     * 主账号端口号
     */
    public static final String SERVER_PORT_MAIN = "8072";
    /**
     * 主账号项目名
     */
    public static final String PROJECT_MAIN = "AndroidManager";
//    public static final String SERVER_HOST_MAIN = "58.240.47.50";
//    public static final String SERVER_PORT_MAIN = "5068";
//    public static final String PROJECT_MAIN = "android";

    //网络超时、socket心跳包发送间隔等
    /**
     * http请求超时时间
     */
    public static final int TIME_OUT_HTTP = 10 * 1000;
    /**
     * socket请求超时时间
     */
    public static final int TIME_OUT_SOCKET = 5 * 1000;
    /**
     * 安卓注册Socket通信
     */
    public static String ANDROID_LOGIN = "*#ANDROIDLOGIN:";
    /**
     * 安卓指令开始标记
     */
    public static String ANDROID_CMD = "*#ADNROIDCMD:";
    /**
     * 安卓数据结束
     */
    public static String ANDROID_END = "END#*";
    /**
     * 心跳包发送内容
     */
    public static final String HEART_BEAT_PACKAGE = "\r\n";
    /**
     * 心跳包发送间隔（10秒）
     */
    public static final int HEART_BEAT_RATE = 10 * 1000;

    /**
     * 消火栓默认出厂IP
     */
    public static final String DEFAULT_IP_HYDRANT = "47.95.193.40";
    /**
     * 消火栓默认出厂域名
     */
    public static final String DEFAULT_DOMAIN_NAME_HYDRANT = "www.metter.com.cn";

    /**
     * GPRS采集终端默认出厂IP
     */
    public static final String DEFAULT_IP_GPRS_COLLECTOR = "106.14.202.177";
    /**
     * GPRS采集终端默认出厂域名
     */
    public static final String DEFAULT_DOMAIN_NAME_GPRS_COLLECTOR = "www.metter.cn";
}
