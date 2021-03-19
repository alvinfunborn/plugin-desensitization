package org.sanhenanli.desensitization.advice;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * datetime 2021/3/17 上午11:48:50
 * 脱敏切面
 *
 * @author alvinfunborn
 */ 
@Aspect
@Component
public class DesensitizeAdvice {

    @Pointcut("@annotation(Desensitize) || @within(Desensitize)")
    public void desensitize() {}

    @Around("desensitize()")
    public Object desensitizeAspect(ProceedingJoinPoint pjp) throws Throwable {
        Object obj = pjp.proceed();
        Class<?> targetClass = pjp.getTarget().getClass();
        Class<?> desensitizationTypes = null;
        Desensitize classAnnotation = targetClass.getAnnotation(Desensitize.class);
        // 类注解 
        if (classAnnotation != null) {
            desensitizationTypes = classAnnotation.types();
        }
        try {
            Method targetClassMethod = targetClass.getMethod(pjp.getSignature().getName(), (((MethodSignature) pjp.getSignature())).getParameterTypes());
            Desensitize methodAnnotation = targetClassMethod.getAnnotation(Desensitize.class);
            if (methodAnnotation != null) {
                // 方法注解 ignored 优先级高于类注解
                desensitizationTypes = methodAnnotation.types();
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        if (desensitizationTypes != null) {
            doDesensitize(obj, desensitizationTypes);
        }
        return obj;
    }

    /**
     * 执行数据脱敏
     */
    @SuppressWarnings("unchecked")
    private void doDesensitize(Object obj, Class<?> desensitizationTypes) throws IllegalAccessException {
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object fieldObj = field.get(obj);
            if (fieldObj == null) {
                continue;
            }
            Class<?> fieldClass = fieldObj.getClass();
            if (fieldClass.equals(String.class)) {
                // 是字符串类型
                DesensitizeAs desensitizeAnno = field.getAnnotation(DesensitizeAs.class);
                if (desensitizeAnno != null && desensitizationTypes.isAssignableFrom(desensitizeAnno.value().getClass())) {
                    // 字段脱敏
                    field.set(obj, desensitizeAnno.value().getDesensitizer().apply((String) fieldObj));
                }
            } else if (isCustomType(fieldClass)) {
                // 是自定义类型
                doDesensitize(fieldObj, desensitizationTypes);
            } else if (fieldClass.isArray()) {
                // 是数组类型
                if (fieldClass.getComponentType().equals(String.class)) {
                    // 是字符串数组
                    DesensitizeAs desensitizeAnno = field.getAnnotation(DesensitizeAs.class);
                    if (desensitizeAnno != null && desensitizationTypes.isAssignableFrom(desensitizeAnno.value().getClass())) {
                        // 字段脱敏
                        String[] data = (String[]) field.get(obj);
                        for (int i = 0; i < data.length; i++) {
                            data[i] = desensitizeAnno.value().getDesensitizer().apply(data[i]);
                        }
                        field.set(obj, data);
                    }
                } else if (isCustomType(fieldClass.getComponentType())) {
                    // 是自定义类型数组
                    Object[] data = (Object[]) field.get(obj);
                    for (int i = 0; i < data.length; i++) {
                        doDesensitize(data[i], desensitizationTypes);
                    }
                }
            } else if (Collection.class.isAssignableFrom(fieldClass)) {
                // 是集合类型
                Class<?> elementClass = getCollectionGenericType((Collection<?>)fieldObj);
                if (String.class.equals(elementClass)) {
                    // 是字符串集合
                    DesensitizeAs desensitizeAnno = field.getAnnotation(DesensitizeAs.class);
                    if (desensitizeAnno != null && desensitizationTypes.isAssignableFrom(desensitizeAnno.value().getClass())) {
                        // 字段脱敏
                        if (List.class.isAssignableFrom(fieldClass)) {
                            // 是List类型
                            List<String> data = (List<String>) field.get(obj);
                            for (int i = 0; i < data.size(); i++) {
                                data.set(i, desensitizeAnno.value().getDesensitizer().apply(data.get(i)));
                            }
                        } else if (Queue.class.isAssignableFrom(fieldClass)) {
                            // 是Queue类型
                            Queue<String> data = (Queue<String>) field.get(obj);
                            for (int i = 0; i < data.size(); i++) {
                                data.add(desensitizeAnno.value().getDesensitizer().apply(data.poll()));
                            }
                        }
                        // Set不做支持
                    }
                } else if (isCustomType(elementClass)) {
                    // 是自定义类型集合
                    Collection<Object> data = (Collection<Object>) field.get(obj);
                    Iterator<Object> iter = data.iterator();
                    while (iter.hasNext()) {
                        doDesensitize(iter.next(), desensitizationTypes);
                    }
                }
            } else if (Map.class.isAssignableFrom(fieldClass)) {
                // 是map类型
                Class<?> valueClass = getMapGenericType((Map<?, ?>) fieldObj);
                if (String.class.equals(valueClass)) {
                    // 值类型是字符串
                    DesensitizeAs desensitizeAnno = field.getAnnotation(DesensitizeAs.class);
                    if (desensitizeAnno != null && desensitizationTypes.isAssignableFrom(desensitizeAnno.value().getClass())) {
                        // 字段脱敏
                        Map<Object, String> data = (Map<Object, String>) field.get(obj);
                        data.forEach((k, v) -> data.put(k, desensitizeAnno.value().getDesensitizer().apply(v)));
                    }
                } else if (isCustomType(valueClass)) {
                    // 值类型是自定义类型
                    Collection<Object> data = ((Map<?, Object>) field.get(obj)).values();
                    Iterator<Object> iter = data.iterator();
                    while (iter.hasNext()) {
                        doDesensitize(iter.next(), desensitizationTypes);
                    }
                }
            }
        }
    }

    /**
     * 是否是用户自定义类型
     */
    private boolean isCustomType(Class<?> clazz) {
        return clazz != null && clazz.getClassLoader() != null;
    }

    /**
     * 获取集合类的泛型类型
     */
    private Class<?> getCollectionGenericType(Collection<?> collection) {
        return collection.isEmpty() ? null : collection.iterator().next().getClass();
    }

    /**
     * 获取Map类的值泛型类型
     */
    private Class<?> getMapGenericType(Map<?, ?> map) {
        return map.isEmpty() ? null : map.values().iterator().next().getClass();
    }
}
