package com.nightfarmer.h5plugin;

import android.app.Activity;
import android.util.Log;
import android.webkit.JavascriptInterface;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangfan on 2016/5/10 0010.
 */
public class H5PluginObject {
    Map<String, Object> objectMap = new HashMap<>();

    private final Activity activity;
    private final String activityMD5;

    public H5PluginObject(Activity activity) {
        this.activity = activity;
        activityMD5 = MD5Util.encode32(activity.toString());
        objectMap.put(activityMD5, activity);
    }

    @JavascriptInterface
    public String getRuntime() {
        return activityMD5;
    }

    @JavascriptInterface
    public String getValue(String objStr) {
        Object obj = objectMap.get(objStr);
        if (obj == null) return "undefine";
        return obj.toString();
    }

    @JavascriptInterface
    public String invoke(String objStr, String method) {
        return invokePr(objStr, method);
    }

    @JavascriptInterface
    public String invoke(String objStr, String method, String p1) {
        return invokePr(objStr, method, p1);
    }

    @JavascriptInterface
    public String invoke(String objStr, String method, String p1, String p2) {
        return invokePr(objStr, method, p1, p2);
    }

    private String invokePr(String objStr, String methodP, Object... args) {
        args2value(args);
        Object obj = objectMap.get(objStr);
        if (obj == null) return "";
        try {
            Method[] methods = obj.getClass().getMethods();
            ArrayList<Method> methods1 = new ArrayList<>();
            ArrayList<Class> classes = new ArrayList<>();
            for (Object arg : args) {
                classes.add(String.class);
            }
            try {
                Method method1 = obj.getClass().getMethod(methodP, classes.toArray(new Class[classes.size()]));
                methods1.add(method1);
            } catch (NoSuchMethodException ignored) {
            }
            methods1.addAll(Arrays.asList(methods));
            for (Method method :
                    methods1) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length == args.length && method.getName().equals(methodP)) {
                    boolean matches = args2Params(parameterTypes, args);
                    if (!matches) continue;
                    method.setAccessible(true);
                    Object invokeResult = method.invoke(obj, args);
                    if (invokeResult == null) invokeResult = "undefine";
                    String resultMD5 = MD5Util.encode32(invokeResult.toString());
                    objectMap.put(resultMD5, invokeResult);
                    return resultMD5;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @JavascriptInterface
    public String newObject(String clsStr) {
        return newObjectPr(clsStr);
    }

    @JavascriptInterface
    public String newObject(String clsStr, String p1) {
        return newObjectPr(clsStr, p1);
    }

    @JavascriptInterface
    public String newObject(String clsStr, String p1, String p2) {
        return newObjectPr(clsStr, p1, p2);
    }

    private String newObjectPr(String clsStr, Object... args) {
        args2value(args);

        String objMd5 = "undefine";
        Log.i("xx", "xxxxx");
        try {
            Class<?> aClass = Class.forName(clsStr);
            Constructor<?>[] constructors = aClass.getConstructors();
            for (Constructor constructor :
                    constructors) {
                boolean varArgs = constructor.isVarArgs();
                Class[] parameterTypes = constructor.getParameterTypes();
                if (parameterTypes.length == args.length) {
                    boolean matches = args2Params(parameterTypes, args);
                    if (!matches) continue;
                    Object newInstance = constructor.newInstance(args);
                    objMd5 = MD5Util.encode32(newInstance.toString());
                    objectMap.put(objMd5, newInstance);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objMd5;
    }

    private void args2value(Object[] args) {
        for (int i = 0; i < args.length; i++) {
            Object o = objectMap.get(args[i].toString());
            if (o == null && args[i].toString().contains(".class")) {
                try {
                    o = Class.forName(args[i].toString());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            args[i] = o == null ? args[i] : o;
        }
    }

    private boolean args2Params(Class[] parameterTypes, Object[] args) {
        for (int i = 0; i < parameterTypes.length; i++) {
            if (parameterTypes[i].getName().startsWith("[")) {
                return false;
            }

            if (parameterTypes[i].isInstance(args[i])) {
                continue;
            }

//            if (String.class == parameterTypes[i]) continue;

            if (Integer.class == parameterTypes[i] || "int".equals(parameterTypes[i].getName())) {
                try {
                    args[i] = Integer.valueOf(args[i].toString());
                    continue;
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            if (Long.class == parameterTypes[i] || "long".equals(parameterTypes[i].getName())) {
                try {
                    args[i] = Long.valueOf(args[i].toString());
                    continue;
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            if (Double.class == parameterTypes[i] || "double".equals(parameterTypes[i].getName())) {
                try {
                    args[i] = Double.valueOf(args[i].toString());
                    continue;
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            if (Float.class == parameterTypes[i] || "float".equals(parameterTypes[i].getName())) {
                try {
                    args[i] = Float.valueOf(args[i].toString());
                    continue;
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            if (Boolean.class == parameterTypes[i] || "boolean".equals(parameterTypes[i].getName())) {
                try {
                    args[i] = Boolean.valueOf(args[i].toString());
                    continue;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


//            if (String.class == parameterTypes[i]) continue;


            return false;
        }
        return true;
    }
}