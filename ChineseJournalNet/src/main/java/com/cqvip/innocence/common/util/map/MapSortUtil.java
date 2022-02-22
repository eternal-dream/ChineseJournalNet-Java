package com.cqvip.innocence.common.util.map;

import com.google.common.collect.Maps;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;

/**
 * @author 01
 * @date 2021-09-17 16:35
 */
public class MapSortUtil {

    /**
     * 根据map的key排序
     *
     * @param map 待排序的map
     * @param isDesc 是否降序，true：降序，false：升序
     * @return 排序好的map
     */
    public static <K extends Comparable<? super K>, V> Map<K, V> sortByKey(Map<K, V> map, boolean isDesc) {
        Map<K, V> result = Maps.newLinkedHashMap();
        if (isDesc) {
            map.entrySet().stream().sorted(Map.Entry.<K, V>comparingByKey().reversed())
                    .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        } else {
            map.entrySet().stream().sorted(Map.Entry.<K, V>comparingByKey())
                    .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        }
        return result;
    }

    /**
     * 根据map的value排序
     *
     * @param map 待排序的map
     * @param isDesc 是否降序，true：降序，false：升序
     * @return 排序好的map
     */
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map, boolean isDesc) {
        Map<K, V> result = Maps.newLinkedHashMap();
        if (isDesc) {
            map.entrySet().stream().sorted(Map.Entry.<K, V>comparingByValue().reversed())
                    .forEach(e -> result.put(e.getKey(), e.getValue()));
        } else {
            map.entrySet().stream().sorted(Map.Entry.<K, V>comparingByValue())
                    .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        }
        return result;
    }

    /** map替换操作，替换键或者方法<br>
     *
     * @param map 源数据
     * @param consumer 转换。Map.Entry<K, V> 为map迭代过程中的Entry；AbstractMap.SimpleEntry 为转换后新的Entry，键值类型与返回值的键值类型
     * </>*/
    public static <K,V,T,R> Map<T,R> iteratorReplaceHandle(Map<K,V> map, Function<Map.Entry<K, V>, AbstractMap.SimpleEntry<T,R>> consumer) {
        if ( map!=null ) {
            Map<T,R> newMap = new HashMap<>(map.size());
            Iterator<Map.Entry<K, V>> iter = map.entrySet().iterator();
            while (iter.hasNext()) {
                AbstractMap.SimpleEntry<T,R> newEntry = consumer.apply(iter.next());
                newMap.put(newEntry.getKey(), newEntry.getValue());
            }
            return newMap;
        }
        return null;
    }

}
