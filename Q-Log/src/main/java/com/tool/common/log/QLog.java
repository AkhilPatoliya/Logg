package com.tool.common.log;

import android.content.Context;

import com.tool.common.log.display.DisplayManager;
import com.tool.common.log.log.PrinterManager;

/**
 * 日志管理器
 */
public class QLog {

    /**
     * 日志打印管理器
     */
    private static PrinterManager printer = new PrinterManager();

    public QLog() {
        throw new AssertionError();
    }

    /**
     * 日志输出-#BBBBBB
     *
     * @param object
     */
    public static void v(Object object) {
        printer.v(object);
    }

    /**
     * 日志输出-#0070BB
     *
     * @param object
     */
    public static void d(Object object) {
        printer.d(object);
    }

    /**
     * 日志输出-#48BB31
     *
     * @param object
     */
    public static void i(Object object) {
        printer.i(object);
    }

    /**
     * 日志输出-# BBBB23
     *
     * @param object
     */
    public static void w(Object object) {
        printer.w(object);
    }

    /**
     * 日志输出-#FF0006
     *
     * @param object
     */
    public static void e(Object object) {
        printer.e(object);
    }

    /**
     * 日志输出-#8F0005
     *
     * @param object
     */
    public static void wtf(Object object) {
        printer.wtf(object);
    }

    /**
     * Json
     *
     * @param object
     */
    public static void json(Object object) {
        printer.json(object);
    }

    /**
     * XML
     *
     * @param object
     */
    public static void xml(Object object) {
        printer.xml(object);
    }

    /**
     * 控制窗口Log的显示/关闭
     *
     * @param context
     * @param isDisplay
     */
    public static void display(Context context, boolean isDisplay) {
        if (isDisplay) {
            DisplayManager.getInstance().start(context);
        } else {
            DisplayManager.getInstance().stop(context);
        }
    }
}