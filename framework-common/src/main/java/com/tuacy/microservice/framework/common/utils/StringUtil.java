package com.tuacy.microservice.framework.common.utils;

import java.math.BigInteger;
import java.util.List;
import java.util.logging.Logger;

/**
 * @name: StringUtil
 * @author: tuacy.
 * @date: 2018/9/17.
 * @version: 1.0
 * @Description:
 */
public class StringUtil {

    private static Logger logger = Logger.getLogger(StringUtil.class.getName());

    /**
     * 是否为空，如果为空或者为空字符串则返回不通过 false
     *
     * @param string
     * @return
     */
    public static boolean validEmpty(String string) {
        return !(string == null || "".equals(string));
    }

    /**
     * 字符串是否为空
     *
     * @param string
     * @return
     */
    public static boolean isEmplty(String string) {
        return string == null || "".equals(string);
    }

    /**
     * 判断是有值，并肯不是全选
     *
     * @param string
     * @return
     */
    public static boolean validEmptyOrAll(String string) {
        return !(string == null || "".equals(string) || "-1".equals(string));
    }

    /**
     * 返回空字符串代替null
     *
     * @param string
     * @return
     */
    public static String nullToEmpty(String string) {
        return string == null ? "" : string;
    }

    /**
     * 比较两个版本号，返回较高版本号
     *
     * @param version1
     * @param version2
     * @return
     */
    public static String VersionCompare(String version1, String version2) {
        String highVersion = "";
        if (validEmpty(version1) && validEmpty(version2)) {
            String[] verArr1 = version1.split("\\.");
            String[] verArr2 = version2.split("\\.");

            int length = verArr1.length > verArr2.length ? verArr1.length : verArr2.length;    //取层级最高的版本号
            for (int i = 0; i < length; i++) {
                int val1 = verArr1.length > i ? Integer.parseInt(verArr1[i]) : 0;
                int val2 = verArr2.length > i ? Integer.parseInt(verArr2[i]) : 0;

                if (val1 != val2) {
                    highVersion = val1 > val2 ? version1 : version2;
                    break;
                }
            }

            // 完全相同，取第一个版本号
            if (highVersion == "") {
                highVersion = version1;
            }
        }
        return highVersion;
    }

    /**
     * @param list
     * @return
     */
    public static String listToString(List<Integer> list, char separator) {
        StringBuilder ret = new StringBuilder();
        if (list != null && list.size() > 0) {
            for (Integer item : list) {
                ret.append(item.toString()).append(separator);
            }
            if (ret.length() > 0) {
                ret.deleteCharAt(ret.length() - 1);
            }
        }
        return ret.toString();
    }


    /**
     * list<Integer>转换为string
     *
     * @param list
     * @return
     */
    public static String listToString(List<Integer> list) {
        return listToString(list, ',');
    }

    /**
     * list<Integer>转换为string
     *
     * @param list
     * @return
     */
    public static String strListToString(List<String> list) {
        StringBuilder ret = new StringBuilder();
        if (list != null && list.size() > 0) {
            for (String item : list) {
                ret.append("'").append(item).append("',");
            }
            if (ret.length() > 0) {
                ret.deleteCharAt(ret.length() - 1);
            }
        }
        return ret.toString();
    }

    /**
     * 把字符串按分隔符转换为数组
     *
     * @param str  字符串
     * @param expr 分隔符
     * @return
     */
    public static String[] stringSplitToArray(String str, String expr) {
        return str.split(expr);
    }

    /**
     * 字符数组转换为string
     *
     * @param arr
     * @return
     */
    public static String strArrayToString(String[] arr, String separator) {
        StringBuilder ret = new StringBuilder();
        if (arr != null && arr.length > 0) {
            for (String s : arr) {
                ret.append(s).append(separator);
            }
            if (ret.length() > 0) {
                ret.deleteCharAt(ret.length() - 1);
            }
        }
        return ret.toString();
    }

    /**
     * Integer数组转换为string
     *
     * @param arr
     * @return
     */
    public static String arrayToString(Integer[] arr) {
        return arrayToString(arr, ",");
    }

    /**
     * Integer数组转换为string
     *
     * @param arr
     * @return
     */
    public static String arrayToString(Integer[] arr, String separator) {
        StringBuilder ret = new StringBuilder();
        if (arr != null && arr.length > 0) {
            for (Integer i : arr) {
                ret.append(i).append(separator);
            }
            if (ret.length() > 0) {
                ret.deleteCharAt(ret.length() - 1);
            }
        }
        return ret.toString();
    }

    /**
     * 计算百分比
     *
     * @param d1
     * @param b2
     * @return
     */
    public static Double doubleDivision(Double d1, BigInteger b2) {
        double ret = 0d;
        if (!b2.equals(new BigInteger("0"))) {
            ret = d1 * 100 / b2.intValue();
        }
        return Math.round(ret * 10) / 10.0;
    }

    /**
     * String 转 Int
     *
     * @param str
     * @return
     */
    public static int StringParseInt(String str) {
        int result = 0;
        if (validEmpty(str)) {
            try {
                Double d = Double.parseDouble(str.trim());
                result = d.intValue();
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    /**
     * String 转 Int
     *
     * @param str
     * @return
     */
    public static long StringParseLong(String str) {
        long result = 0;
        if (validEmpty(str)) {
            try {
                Double d = Double.parseDouble(str.trim());
                result = d.longValue();
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 判断ID是否在listStr之中
     *
     * @param listStr eg。4,3,2,1
     * @param id
     * @return
     */
    public static boolean checkIdInList(String listStr, long id) {
        if (StringUtil.validEmpty(listStr) && id > 0) {
            String[] idArr = listStr.split(",");
            for (String s : idArr) {
                if (id == StringUtil.StringParseInt(s)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * id 列表转换为  逗号分隔的字符串
     *
     * @param ids
     * @return
     */
    public static String arrayToString(List<Long> ids) {
        return arrayToString(ids, ",");
    }

    /**
     * id 列表转换为  逗号分隔的字符串
     *
     * @param ids
     * @param separator
     * @return
     */
    public static String arrayToString(List<Long> ids, String separator) {
        StringBuilder ret = new StringBuilder();
        if (ids != null && ids.size() > 0) {
            for (Long i : ids) {
                ret.append(i).append(separator);
            }
            if (ret.length() > 0) {
                ret.deleteCharAt(ret.length() - 1);
            }
        }
        return ret.toString();
    }

    /**
     * 检测最后一个元素是否与传入ID相同
     *
     * @param id
     * @param targetVal
     * @return
     */
    public static boolean checkIsParentOnlyLastLevel(long id, String targetVal) {
        if (validEmpty(targetVal)) {
            String[] idArr = targetVal.split(":");
            if (idArr.length > 2 && idArr[idArr.length - 2].equals(id)) {   // 监测最后一个节点是否相等
                return true;
            }
        }
        return false;
    }


    /**
     * 两个Integer比较
     *
     * @param first
     * @param second
     * @return
     */
    public static boolean equalWithInteger(Integer first, Integer second) {
        if (first != null && second != null) {
            return first.intValue() == second.intValue();
        }
        return false;
    }

    /**
     * 两个Long比较
     *
     * @param first
     * @param second
     * @return
     */
    public static boolean equalWithLong(Long first, Long second) {
        if (first != null && second != null) {
            return first.intValue() == second.intValue();
        }
        return false;
    }

}
