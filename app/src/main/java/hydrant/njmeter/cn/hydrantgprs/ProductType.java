package hydrant.njmeter.cn.hydrantgprs;

/**
 * 产品类型以及蓝牙工具操作类型
 * Created by LiYuliang on 2017/8/22 0022.
 *
 * @author LiYuliang
 * @version 2017/11/01
 */

public class ProductType {

    //**********************************************************************************************************************************************
    //*****************************************************************  工厂代码  *****************************************************************
    //**********************************************************************************************************************************************
    /**
     * 工厂代码
     */
    public static final String FACTORY_CODE_STRING = "001111";

    //**********************************************************************************************************************************************
    //***************************************************************  产品类型代码  ***************************************************************
    //**********************************************************************************************************************************************
    /**
     * 智能水表
     */
    public static final String WATER_METER_STRING = "10";
    public static final int WATER_METER_INT = 10;
    /**
     * 智能热量表
     */
    public static final String HEAT_METER = "20";
    /**
     * M_Bus主机、GPRS采集终端
     */
    public static final String COLLECTOR = "48";
    /**
     * 通断控制器、无线暖气控制阀
     */
    public static final String ON_OFF_CONTROLLER = "49";
    /**
     * 智能控制阀
     */
    public static final String CONTROL_VALVE = "50";
    /**
     * LoRa无线模块
     */
    public static final String LORA = "54";
    /**
     * 智能衡流阀
     */
    public static final String CONSTANT_FLOW_VALVE = "49";
    public static final int CONSTANT_FLOW_VALVE_INT = 56;
    /**
     * 智能消火栓
     */
    public static final String HYDRANT_STRING = "59";
    public static final int HYDRANT_INT = 59;

    public static final int HYDRANT_FLOWMETER = 5900;
    public static final int HYDRANT_I = 5901;
    public static final int HYDRANT_II = 5902;
    public static final int HYDRANT_III = 5903;

    /**
     * 智能消火栓用途——消防
     */
    public static final int HYDRANT_USETYPE_XIAOFANG = 1;
    /**
     * 智能消火栓用途——绿化
     */
    public static final int HYDRANT_USETYPE_LVHUA = 2;
    /**
     * 智能消火栓用途——洒水
     */
    public static final int HYDRANT_USETYPE_SASHUI = 3;

    //******************************************************************************************************************************************
    //**********************************************  蓝牙工具操作类型代码（用于指令解析时的switch-case） **************************************
    //******************************************************************************************************************************************

    //**********************************************  蓝牙工具操作水表代码（用于指令解析时的switch-case） **************************************
    /**
     * 读取水表数据
     */
    public static final int WATER_METER_READ_METER_DATA = 1001;
    /**
     * 读取水表内部参数（流量系数等）
     */
    public static final int WATER_METER_READ_METER_INTER_PARAMETER = 1002;
    /**
     * 水表普通开关阀
     */
    public static final int WATER_METER_OPEN_CLOSE_VALVE_NORMAL = 1003;
    /**
     * 水表强制开关阀
     */
    public static final int WATER_METER_OPEN_CLOSE_VALVE_MANDATORY = 1004;
    /**
     * 水表校正时钟
     */
    public static final int WATER_METER_CORRECTION_TIME = 1005;
    /**
     * 水表读取压力标定
     */
    public static final int WATER_METER_READ_PRESSURE = 1006;
    /**
     * 水表压力标定成功
     */
    public static final int WATER_METER_SET_PRESSURE_SUCCESS = 1007;
    /**
     * 水表压力标定失败
     */
    public static final int WATER_METER_SET_PRESSURE_FAIL = 1008;
    /**
     * 水表读取分段参数
     */
    public static final int WATER_METER_READ_SEGMENTED_PARAMETER = 1009;
    /**
     * 水表读取测量参数
     */
    public static final int WATER_METER_READ_MEASUREMENT_PARAMETER = 1010;
    /**
     * 水表读取流量误差
     */
    public static final int WATER_METER_READ_FLOW_DEVIATION = 1011;
    /**
     * 水表读取修改记录
     */
    public static final int WATER_METER_READ_MODIFY_RECORD = 1012;
    /**
     * 水表读取日报月报（不带单位）
     */
    public static final int WATER_METER_READ_DAILY_REPORT_WITHOUT_UNIT = 1013;
    /**
     * 水表读取日报月报（带单位）
     */
    public static final int WATER_METER_READ_DAILY_REPORT_WITH_UNIT = 1014;
    /**
     * 水表充流量
     */
    public static final int WATER_METER_RECHARGE_FLOW = 1015;
    /**
     * 水表设置初始水量
     */
    public static final int WATER_METER_INITIALIZE_FLOW = 1016;
    /**
     * 水表读取剩余水量
     */
    public static final int WATER_METER_READ_REMAIN_FLOW = 1017;


    //**********************************************  蓝牙工具操作热表代码（用于指令解析时的switch-case） **************************************
    /**
     * 读取热表数据
     */
    public static final int HEAT_METER_READ_METER_DATA = 2001;
    /**
     * 读取热表内部参数（流量系数等）
     */
    public static final int HEAT_METER_READ_METER_INTER_PARAMETER = 2002;
    /**
     * 热表开关阀
     */
    public static final int HEAT_METER_OPEN_CLOSE_VALVE = 2003;
    /**
     * 热表校正时钟
     */
    public static final int HEAT_METER_CORRECTION_TIME = 2004;

    //**********************************************  蓝牙工具操作消火栓代码（用于指令解析时的switch-case） **************************************
    /**
     * 消火栓读取报警参数
     */
    public static final int HYDRANT_READ_WARNING_PARAMETER = 4901;
    /**
     * 消火栓读取报警状态
     */
    public static final int HYDRANT_READ_WARNING_STATE = 4902;
    /**
     * 消火栓读取表数据
     */
    public static final int HYDRANT_READ_METER_DATA = 4903;
    /**
     * 消火栓读取GPS信息
     */
    public static final int HYDRANT_READ_GPS = 4904;
    /**
     * 消火栓读取当前使用数据
     */
    public static final int HYDRANT_READ_CURRENT_USE_DATA = 4905;
    /**
     * 消火栓读取历史使用数据
     */
    public static final int HYDRANT_READ_HISTORY_USE_DATA = 4906;
    /**
     * 消火栓读取报警使能
     */
    public static final int HYDRANT_READ_WARNING_ENABLE = 4907;
    /**
     * 消火栓读取内部参数（流量系数等）
     */
    public static final int HYDRANT_READ_METER_INTER_PARAMETER = 4908;
    /**
     * 消火栓开关阀门锁
     */
    public static final int HYDRANT_OPEN_CLOSE_VALVE = 4909;
    /**
     * 消火栓校正时钟
     */
    public static final int HYDRANT_CORRECTION_TIME = 4910;
    /**
     * 消火栓修改类型
     */
    public static final int HYDRANT_CHANGE_TYPE = 4911;

    //**********************************************  蓝牙工具操作恒流阀代码（用于指令解析时的switch-case） **************************************
    /**
     * 恒流阀读取数据
     */
    public static final int CONSTANT_FLOW_VALVE_READ_PARAMETER = 5601;
    /**
     * 指定圈数
     */
    public static final int CONSTANT_FLOW_VALVE_TURNS = 5611;
    /**
     * 减小圈数
     */
    public static final int CONSTANT_FLOW_VALVE_REDUCE_TURNS = 5612;
    /**
     * 增大圈数
     */
    public static final int CONSTANT_FLOW_VALVE_ADD_TURNS = 5613;
    /**
     * 指定流速
     */
    public static final int CONSTANT_FLOW_VALVE_FLOW = 5621;
    /**
     * 增大流速
     */
    public static final int CONSTANT_FLOW_VALVE_ADD_FLOW = 5622;
    /**
     * 减小流速
     */
    public static final int CONSTANT_FLOW_VALVE_REDUCE_FLOW = 5623;
    /**
     * 流速、圈数回零
     */
    public static final int CONSTANT_FLOW_VALVE_ZEROING = 5630;
    /**
     * 流量点标定1
     */
    public static final int CONSTANT_FLOW_VALVE_DEMARCATE1 = 5641;
    /**
     * 流量点标定2
     */
    public static final int CONSTANT_FLOW_VALVE_DEMARCATE2 = 5642;
    /**
     * 流量点标定3
     */
    public static final int CONSTANT_FLOW_VALVE_DEMARCATE3 = 5643;
    /**
     * 流量点标定4
     */
    public static final int CONSTANT_FLOW_VALVE_DEMARCATE4 = 5644;
    /**
     * 流量点标定5
     */
    public static final int CONSTANT_FLOW_VALVE_DEMARCATE5 = 5645;
    /**
     * 设置睡眠月份
     */
    public static final int CONSTANT_FLOW_VALVE_SLEEP_MONTHS = 5650;
    /**
     * 设置上传间隔
     */
    public static final int CONSTANT_FLOW_VALVE_UPLOAD_INTERVAL = 5651;

}
