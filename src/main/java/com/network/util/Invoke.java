package com.network.util;

import java.lang.reflect.Method;

public class Invoke {

    public Invoke() {
    }

    public static Object invoke(Object object, String methodName, Class[] paramsType, Object[] params) throws Exception {
        Method method = object.getClass().getMethod(methodName, paramsType);
        return method.invoke(object, params);
    }

}
