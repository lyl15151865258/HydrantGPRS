package utils;

/**
 * 解析工具类
 * 1、字符串与16进制的相互转换
 * 2、字符串与2进制的相互转换
 * 3、指令计算CS校验和
 * 4、验证CS校验和的正确性
 * 5、消用水量的标准单位转换
 * 6、消火栓开阀门锁方式
 */

public class AnalysisUtils {

    private static int HexS1ToInt(char ch) {
        int r = 0;
        if ('a' <= ch && ch <= 'f') {
            r = ch - 'a' + 10;
        }
        if ('A' <= ch && ch <= 'F') {
            r = ch - 'A' + 10;
        }
        if ('0' <= ch && ch <= '9') {
            r = ch - '0';
        }
        return r;
    }

    public static int HexS2ToInt(String S) {
        char a[] = S.toCharArray();
        return HexS1ToInt(a[0]) * 16 + HexS1ToInt(a[1]);
    }

    public static int HexS4ToInt(String S) {
        char a[] = S.toCharArray();
        return HexS1ToInt(a[0]) * 16 * 16 * 16 + HexS1ToInt(a[1]) * 16 * 16 + HexS1ToInt(a[2]) * 16 + HexS1ToInt(a[3]);
    }

    public static int HexS8ToInt(String S) {
        return HexS4ToInt(S.substring(0, 4)) * 16 * 16 * 16 * 16 + HexS4ToInt(S.substring(4, 8));
    }

    public static String HexS2ToBinS(String S) {
        String r = "";
        S = S.toUpperCase();
        char a[] = S.toCharArray();
        if (a[0] == '0') {
            r = r + "0000";
        } else if (a[0] == '1') {
            r = r + "0001";
        } else if (a[0] == '2') {
            r = r + "0010";
        } else if (a[0] == '3') {
            r = r + "0011";
        } else if (a[0] == '4') {
            r = r + "0100";
        } else if (a[0] == '5') {
            r = r + "0101";
        } else if (a[0] == '6') {
            r = r + "0110";
        } else if (a[0] == '7') {
            r = r + "0111";
        } else if (a[0] == '8') {
            r = r + "1000";
        } else if (a[0] == '9') {
            r = r + "1001";
        } else if (a[0] == 'A') {
            r = r + "1010";
        } else if (a[0] == 'B') {
            r = r + "1011";
        } else if (a[0] == 'C') {
            r = r + "1100";
        } else if (a[0] == 'D') {
            r = r + "1101";
        } else if (a[0] == 'E') {
            r = r + "1110";
        } else if (a[0] == 'F') {
            r = r + "1111";
        }

        if (a[1] == '0') {
            r = r + "0000";
        } else if (a[1] == '1') {
            r = r + "0001";
        } else if (a[1] == '2') {
            r = r + "0010";
        } else if (a[1] == '3') {
            r = r + "0011";
        } else if (a[1] == '4') {
            r = r + "0100";
        } else if (a[1] == '5') {
            r = r + "0101";
        } else if (a[1] == '6') {
            r = r + "0110";
        } else if (a[1] == '7') {
            r = r + "0111";
        } else if (a[1] == '8') {
            r = r + "1000";
        } else if (a[1] == '9') {
            r = r + "1001";
        } else if (a[1] == 'A') {
            r = r + "1010";
        } else if (a[1] == 'B') {
            r = r + "1011";
        } else if (a[1] == 'C') {
            r = r + "1100";
        } else if (a[1] == 'D') {
            r = r + "1101";
        } else if (a[1] == 'E') {
            r = r + "1110";
        } else if (a[1] == 'F') {
            r = r + "1111";
        }
        return r;
    }

    public static String BinS4ToHexS2(String S) {
        String r = "";
        switch (S) {
            case "0000":
                r = "0";
                break;
            case "0001":
                r = "1";
                break;
            case "0010":
                r = "2";
                break;
            case "0011":
                r = "3";
                break;
            case "0100":
                r = "4";
                break;
            case "0101":
                r = "5";
                break;
            case "0110":
                r = "6";
                break;
            case "0111":
                r = "7";
                break;
            case "1000":
                r = "8";
                break;
            case "1001":
                r = "9";
                break;
            case "1010":
                r = "A";
                break;
            case "1011":
                r = "B";
                break;
            case "1100":
                r = "C";
                break;
            case "1101":
                r = "D";
                break;
            case "1110":
                r = "E";
                break;
            case "1111":
                r = "F";
                break;
        }
        return r;
    }

    public static String BinS8ToHexS2(String S) {
        String r = "";
        switch (S.substring(0, 4)) {
            case "0000":
                r = "0";
                break;
            case "0001":
                r = "1";
                break;
            case "0010":
                r = "2";
                break;
            case "0011":
                r = "3";
                break;
            case "0100":
                r = "4";
                break;
            case "0101":
                r = "5";
                break;
            case "0110":
                r = "6";
                break;
            case "0111":
                r = "7";
                break;
            case "1000":
                r = "8";
                break;
            case "1001":
                r = "9";
                break;
            case "1010":
                r = "A";
                break;
            case "1011":
                r = "B";
                break;
            case "1100":
                r = "C";
                break;
            case "1101":
                r = "D";
                break;
            case "1110":
                r = "E";
                break;
            case "1111":
                r = "F";
                break;
        }
        switch (S.substring(4, 8)) {
            case "0000":
                r += "0";
                break;
            case "0001":
                r += "1";
                break;
            case "0010":
                r += "2";
                break;
            case "0011":
                r += "3";
                break;
            case "0100":
                r += "4";
                break;
            case "0101":
                r += "5";
                break;
            case "0110":
                r += "6";
                break;
            case "0111":
                r += "7";
                break;
            case "1000":
                r += "8";
                break;
            case "1001":
                r += "9";
                break;
            case "1010":
                r += "A";
                break;
            case "1011":
                r += "B";
                break;
            case "1100":
                r += "C";
                break;
            case "1101":
                r += "D";
                break;
            case "1110":
                r += "E";
                break;
            case "1111":
                r += "F";
                break;
        }
        return r;
    }

    //获取CS校验和
    public static String getCSSum(String S, int Start) {
        String r;
        int sumL = 0;
        for (int i = (Start * 2); i < S.length(); i = i + 2) {
            sumL = (sumL + AnalysisUtils.HexS2ToInt(S.substring(i, i + 2))) % 256;
        }
        r = Integer.toHexString(sumL / 16) + Integer.toHexString(sumL % 16);
        r = r.toUpperCase();
        return r;
    }

    public static int checksum(String S, int Start, String checksumS) {
        int r = 0;
        int sumL = 0;
        for (int i = (Start * 2); i < S.length(); i = i + 2) {
            sumL = (sumL + AnalysisUtils.HexS2ToInt(S.substring(i, i + 2))) % 256;
        }
        if ((Integer.toHexString(sumL / 16) + Integer.toHexString(sumL % 16)).toUpperCase().equals(checksumS))
            r = 1;
        return r;
    }

    public static byte[] getdatacodeforgprs(String control, String gprs_ASCII, String dataarea) {
        int lens = ("7B" + control + "00" + gprs_ASCII + dataarea + "7B").length() / 2 + 1;
        String Lens = Integer.toHexString(lens / 16) + Integer.toHexString(lens % 16);
        String rx = "7B" + control + "00" + Lens + gprs_ASCII + dataarea + "7B";
        byte[] arri = new byte[rx.length() / 2];
        for (int i = 0; i < (rx.length() / 2); i++) {
            arri[i] = (byte) AnalysisUtils.HexS2ToInt(rx.substring(2 * i, 2 * i + 2));
        }
        return arri;
    }

    //获取消火栓开阀门锁方式
    public static String getOpenValveMethod(String device) {
        String open_device;
        switch (device) {
            case "01":
                open_device = "IC卡";
                break;
            case "02":
                open_device = "蓝牙工具";
                break;
            case "03":
                open_device = "手机App";
                break;
            default:
                open_device = "未知设备";
                break;
        }
        return open_device;
    }

    /**
     * 获取水量转换为标准单位（m³）的倍数关系
     *
     * @param value 设备传回的单位标记（可以是指令中截取的，也可以是解析后的单位）
     * @return 倍数关系
     */
    public static double getFlowMultiple(String value) {
        double unit;
        switch (value) {
            case "26":
                unit = 0.000001;
                break;
            case "27":
                unit = 0.00001;
                break;
            case "28":
                unit = 0.0001;
                break;
            case "29":
                unit = 0.001;
                break;
            case "2A":
                unit = 0.01;
                break;
            case "2B":
                unit = 0.1;
                break;
            case "2C":
                unit = 1;
                break;
            case "mL":
                unit = 0.000001;
                break;
            case "10mL":
                unit = 0.00001;
                break;
            case "100mL":
                unit = 0.0001;
                break;
            case "L":
                unit = 0.001;
                break;
            case "10L":
                unit = 0.01;
                break;
            case "100L":
                unit = 0.1;
                break;
            case "m³":
                unit = 1;
                break;
            default:
                unit = 1;
                break;
        }
        return unit;
    }

    /**
     * 获取流速转换为标准单位（m³/h）的倍数关系
     *
     * @param value 设备传回的单位标记（可以是指令中截取的，也可以是解析后的单位）
     * @return 倍数关系
     */
    public static double getFlowRateMultiple(String value) {
        double unit;
        switch (value) {
            case "32":
                unit = 0.001;
                break;
            case "33":
                unit = 0.01;
                break;
            case "34":
                unit = 0.1;
                break;
            case "35":
                unit = 1;
                break;
            case "L/h":
                unit = 0.001;
                break;
            case "10L/h":
                unit = 0.01;
                break;
            case "100L/h":
                unit = 0.1;
                break;
            case "m³/h":
                unit = 1;
                break;
            default:
                unit = 1;
                break;
        }
        return unit;
    }

    /**
     * 获取功率转换为标准单位（kW）的倍数关系
     *
     * @param value 设备传回的单位标记（可以是指令中截取的，也可以是解析后的单位）
     * @return 倍数关系
     */
    public static double getPowerMultiple(String value) {
        double unit;
        switch (value) {
            case "14":
                unit = 0.001;
                break;
            case "15":
                unit = 0.01;
                break;
            case "16":
                unit = 0.1;
                break;
            case "17":
                unit = 1;
                break;
            case "W":
                unit = 0.001;
                break;
            case "10W":
                unit = 0.01;
                break;
            case "100W":
                unit = 0.1;
                break;
            case "kW":
                unit = 1;
                break;
            default:
                unit = 1;
                break;
        }
        return unit;
    }

    /**
     * 获取冷热量转换为标准单位（kW*h）的倍数关系
     *
     * @param value 设备传回的单位标记（可以是指令中截取的，也可以是解析后的单位）
     * @return 倍数关系
     */
    public static double getHeatMultiple(String value) {
        double unit;
        switch (value) {
            case "02":
                unit = 0.001;
                break;
            case "03":
                unit = 0.01;
                break;
            case "04":
                unit = 0.1;
                break;
            case "05":
                unit = 1;
                break;
            case "0B":
                unit = 0.0002778;
                break;
            case "0C":
                unit = 0.002778;
                break;
            case "0D":
                unit = 0.02778;
                break;
            case "0E":
                unit = 0.2778;
                break;
            case "0F":
                unit = 2.778;
                break;
            case "10":
                unit = 27.78;
                break;
            case "11":
                unit = 277.8;
                break;
            case "12":
                unit = 2778;
                break;
            case "13":
                unit = 27778;
                break;
            case "W*h":
                unit = 0.001;
                break;
            case "10W*h":
                unit = 0.01;
                break;
            case "100W*h":
                unit = 0.1;
                break;
            case "kW*h":
                unit = 1;
                break;
            case "kJ":
                unit = 0.0002778;
                break;
            case "10kJ":
                unit = 0.002778;
                break;
            case "100kJ":
                unit = 0.02778;
                break;
            case "MJ":
                unit = 0.2778;
                break;
            case "10MJ":
                unit = 2.778;
                break;
            case "100MJ":
                unit = 27.78;
                break;
            case "GJ":
                unit = 277.8;
                break;
            case "10GJ":
                unit = 2778;
                break;
            case "100GJ":
                unit = 27778;
                break;
            default:
                unit = 1;
                break;
        }
        return unit;
    }
}
