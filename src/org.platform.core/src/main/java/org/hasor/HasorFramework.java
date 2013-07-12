/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hasor;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import org.more.util.ArrayUtils;
/**
 * 
 * @version : 2013-4-3
 * @author 赵永春 (zyc@byshell.org)
 */
public abstract class HasorFramework {
    public static final String MORE_WORK_HOME               = "MORE_WORK_HOME";
    public static final String MORE_DATA_HOME               = "MORE_DATA_HOME";
    public static final String MORE_TEMP_HOME               = "MORE_TEMP_HOME";
    public static final String MORE_CACHE_HOME              = "MORE_CACHE_HOME";
    /**装载的class包范畴，逗号间隔.*/
    public static final String Platform_LoadPackages        = "framework.loadPackages";
    /**框架提供的事件管理器最大可以同时执行的任务数量.*/
    public static final String Platform_EventThreadPoolSize = "framework.eventThreadPoolSize";
    //
    //
    //
    //
    //
    //
    //
    private static String callerType() {
        StackTraceElement[] stackElements = new Exception().getStackTrace();
        StackTraceElement onCode = stackElements[2];
        String callerClass = onCode.getClassName();
        return callerClass.substring(callerClass.lastIndexOf(".") + 1) + ":" + onCode.getMethodName();
    }
    //
    //
    /***/
    public static void debug(String string, Object... params) {
        Object[] paramsStr = getStringArray(params);
        System.out.println(callerType() + " ->> " + formatString(string, paramsStr));
    }
    //
    /***/
    public static void error(String string, Object... params) {
        Object[] paramsStr = getStringArray(params);
        System.err.println(callerType() + " ->> " + formatString(string, paramsStr));
    }
    //
    /***/
    public static void warning(String string, Object... params) {
        Object[] paramsStr = getStringArray(params);
        System.err.println(callerType() + " ->> " + formatString(string, paramsStr));
    }
    //
    /***/
    public static void info(String string, Object... params) {
        Object[] paramsStr = getStringArray(params);
        System.out.println(callerType() + " ->> " + formatString(string, paramsStr));
    }
    //
    /***/
    public static String formatString(String formatString, Object... args) {
        if (ArrayUtils.isBlank(args))
            return formatString;
        return String.format(formatString, args);
    }
    //
    /***/
    public static String[] getStringArray(Object... objects) {
        ArrayList<String> returnData = new ArrayList<String>();
        for (Object obj : objects) {
            if (obj == null)
                returnData.add("null");
            else
                returnData.add(logString(obj));
        }
        return returnData.toArray(new String[returnData.size()]);
    }
    //
    /***/
    public static String logString(Object object) {
        if (object == null)
            return "null";
        //
        StringBuilder logString = new StringBuilder("");
        if (object instanceof Collection) {
            //
            Collection<?> coll = (Collection<?>) object;
            for (Object obj : coll)
                logString.append(logString(obj) + " , ");
            if (logString.length() > 1)
                logString.delete(logString.length() - 3, logString.length() - 1);
            logString.insert(0, "[ ");
            logString.append("]");
        } else if (object.getClass().isEnum() == true) {
            //
            Enum<?> enumObj = (Enum<?>) object;
            logString.append(enumObj.name());
        } else if (object.getClass().isArray() == true) {
            //
            Object[] array = (Object[]) object;
            logString.append(logString(Arrays.asList(array)));
        } else {
            //
            if (object instanceof Class)
                logString.append(((Class) object).getName());
            else if (object instanceof Throwable) {
                Throwable err = (Throwable) object;
                StringWriter sw = new StringWriter();
                sw.append('\n');
                err.printStackTrace(new PrintWriter(sw));
                logString.append(sw.getBuffer());
            } else if (object instanceof URL) {
                URL url = (URL) object;
                try {
                    logString.append(URLDecoder.decode(url.toString(), "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    logString.append(url.toString());
                }
            } else if (object instanceof URI) {
                URI uri = (URI) object;
                try {
                    logString.append(URLDecoder.decode(uri.toString(), "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    logString.append(uri.toString());
                }
            } else {
                logString.append(object.toString());
            }
        }
        return logString.toString();
    }
    //
    /***/
    public static String getIndexStr(int index) {
        int allRange = 1000;
        /*-----------------------------------------*/
        int minStartIndex = Integer.MIN_VALUE;
        int minStopIndex = Integer.MIN_VALUE + allRange;
        for (int i = minStartIndex; i < minStopIndex; i++) {
            if (index == i)
                return "Min" + ((index == Integer.MIN_VALUE) ? "" : ("+" + String.valueOf(i + Math.abs(Integer.MIN_VALUE))));
        }
        int maxStartIndex = Integer.MAX_VALUE;
        int maxStopIndex = Integer.MAX_VALUE - allRange;
        for (int i = maxStartIndex; i > maxStopIndex; i--) {
            if (index == i)
                return "Max" + ((index == Integer.MAX_VALUE) ? "" : ("-" + Math.abs(Integer.MAX_VALUE - i)));
        }
        return String.valueOf(index);
    }
    /*
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     */
    /** Asserts that an argument is legal. If the given boolean is
     * not <code>true</code>, an <code>IllegalArgumentException</code>
     * is thrown.
     *
     * @param expression the outcome of the check
     * @return <code>true</code> if the check passes (does not return
     *    if the check fails)
     * @exception IllegalArgumentException if the legality test failed
     */
    public static boolean assertIsLegal(boolean expression) {
        return assertIsLegal(expression, ""); //$NON-NLS-1$
    }
    /** Asserts that an argument is legal. If the given boolean is
     * not <code>true</code>, an <code>IllegalArgumentException</code>
     * is thrown.
     * The given message is included in that exception, to aid debugging.
     *
     * @param expression the outcome of the check
     * @param message the message to include in the exception
     * @return <code>true</code> if the check passes (does not return
     *    if the check fails)
     * @exception IllegalArgumentException if the legality test failed
     */
    public static boolean assertIsLegal(boolean expression, String message) {
        if (!expression)
            throw new IllegalArgumentException(message);
        return expression;
    }
    /** Asserts that the given object is not <code>null</code>. If this
     * is not the case, some kind of unchecked exception is thrown.
     * 
     * @param object the value to test
     */
    public static void assertIsNotNull(Object object) {
        assertIsNotNull(object, ""); //$NON-NLS-1$
    }
    /** Asserts that the given object is not <code>null</code>. If this
     * is not the case, some kind of unchecked exception is thrown.
     * The given message is included in that exception, to aid debugging.
     *
     * @param object the value to test
     * @param message the message to include in the exception
     */
    public static void assertIsNotNull(Object object, String message) {
        if (object == null)
            throw new NullPointerException("null argument:" + message); //$NON-NLS-1$
    }
}