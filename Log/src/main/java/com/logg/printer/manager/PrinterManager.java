package com.logg.printer.manager;

import android.text.TextUtils;

import com.logg.Logg;
import com.logg.config.LoggConfig;
import com.logg.config.LoggConstant;
import com.logg.printer.DefaultPrinter;
import com.logg.printer.JsonPrinter;
import com.logg.printer.Type;
import com.logg.printer.XmlPrinter;
import com.logg.util.ObjectUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * 日志管理器
 */
public class PrinterManager {

    // Default Printer
    private DefaultPrinter defaultPrinter = null;
    // Json Printer
    private JsonPrinter jsonPrinter = null;
    // XML Printer
    private XmlPrinter xmlPrinter = null;

    /**
     * Parameter configuration
     */
    private LoggConfig setting = null;

    public PrinterManager() {
        defaultPrinter = new DefaultPrinter();
        jsonPrinter = new JsonPrinter();
        xmlPrinter = new XmlPrinter();

        setting = LoggConfig.getConfig();
    }

    /**
     * 日志输出-#BBBBBB
     *
     * @param object
     */
    public void v(Object object) {
        this.printer(Type.V, object);
    }

    /**
     * 日志输出-#0070BB
     *
     * @param object
     */
    public void d(Object object) {
        this.printer(Type.D, object);
    }

    /**
     * 日志输出-#48BB31
     *
     * @param object
     */
    public void i(Object object) {
        this.printer(Type.I, object);
    }

    /**
     * 日志输出-# BBBB23
     *
     * @param object
     */
    public void w(Object object) {
        this.printer(Type.W, object);
    }

    /**
     * 日志输出-#FF0006
     *
     * @param object
     */
    public void e(Object object) {
        this.printer(Type.E, object);
    }

    /**
     * 日志输出-#8F0005
     *
     * @param object
     */
    public void wtf(Object object) {
        this.printer(Type.WTF, object);
    }

    /**
     * Json
     *
     * @param object
     */
    public void json(Object object) {
        this.printer(Type.J, object);
    }

    /**
     * XML
     *
     * @param object
     */
    public void xml(Object object) {
        this.printer(Type.X, object);
    }

    /**
     * 打印字符串
     *
     * @param type
     * @param object
     */
    private synchronized void printer(Type type, Object object) {
        // 是否允许日志输出
        if (setting != null && !setting.isDebug()) {
            return;
        }

        switch (type) {
            case V:
            case D:
            case I:
            case W:
            case E:
            case WTF:
                String o = ObjectUtil.objectToString(object);
                if (o.length() > LoggConstant.LINE_MAX) {
                    for (String subMsg : bigStringToList(o)) {
                        defaultPrinter.printer(type, getTag(), subMsg);
                    }
                    return;
                } else {
                    defaultPrinter.printer(type, getTag(), o);
                }
                break;
            case J:
                jsonPrinter.printer(type, getTag(), ObjectUtil.objectToString(object));
                break;
            case X:
                xmlPrinter.printer(type, getTag(), ObjectUtil.objectToString(object));
                break;
            default:
                break;
        }
    }

    /**
     * 拼接Tag前缀信息
     *
     * @return
     */
    private String getTag() {
        if (setting != null) {
            if (TextUtils.isEmpty(setting.getTag())) {
                return spliceTag();
            } else {
                return setting.getTag();
            }
        } else {
            return spliceTag();
        }
    }

    /**
     * 拼接Tag前缀信息
     *
     * @return
     */
    private String spliceTag() {
        StackTraceElement caller = getCurrentStackTrace();
        if (caller == null) {
            return "";
        }
        String stackTrace = caller.toString();
        stackTrace = stackTrace.substring(stackTrace.lastIndexOf('('), stackTrace.length());
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        return String.format("%s.%s%s", callerClazzName, caller.getMethodName(), stackTrace);
    }

    /**
     * 获取当前堆栈信息
     *
     * @return
     */
    private StackTraceElement getCurrentStackTrace() {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();

        int stackOffset = -1;

        for (int i = 5; i < trace.length; i++) {
            StackTraceElement e = trace[i];

            if (Logg.class.equals(Logger.class) && i < trace.length - 1 && trace[i + 1].getClassName()
                    .equals(Logger.class.getName())) {
                continue;
            }
            if (e.getClassName().equals(Logg.class.getName())) {
                stackOffset = ++i;
            }
        }

        return stackOffset != -1 ? trace[stackOffset] : null;
    }

    /**
     * 超大文本字符串转化为List
     *
     * @param message
     * @return
     */
    private List<String> bigStringToList(String message) {
        List<String> stringList = new ArrayList<>();
        int index = 0;
        int maxLength = LoggConstant.LINE_MAX;
        int countOfSub = message.length() / maxLength;
        if (countOfSub > 0) {
            for (int i = 0; i < countOfSub; i++) {
                String sub = message.substring(index, index + maxLength);
                stringList.add(sub);
                index += maxLength;
            }
            stringList.add(message.substring(index, message.length()));
        } else {
            stringList.add(message);
        }
        return stringList;
    }
}