package com.cqvip.innocence.common.util.yml;




import com.cqvip.innocence.common.util.BaseUtil;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName YmlReadUtil
 * @Description 读取yml配置
 * @Author Innocence
 * @Date 2020/9/22 14:09
 * @Version 1.0
 */
public class YmlReadUtil extends BaseUtil{
    public static YmlReadUtil newInstance() {
        return (YmlReadUtil) BaseUtil.instance(YmlReadUtil.class.getName());
    }
    /**
     * ${} 占位符 正则表达式
     * @author Innocence
     * @date 2020/9/22
     */
    private static Pattern p1 = Pattern.compile("\\$\\{.*?\\}");

//    public YmlReadUtil(){
//        throw new AssertionError();
//    }

    /**
     * key:文件索引名
     * value：配置文件内容
     */
    private static Map<String , LinkedHashMap> ymls = new HashMap<>();
    /**
     * String:当前线程需要查询的文件名
     */
    private static ThreadLocal<String> nowFileName = new InheritableThreadLocal<>();

    /**
     * 由于springboot配置优先级是1.config>2.jar同级>3.resource/config>4.resource
     * 这里没考虑2、3的情况，当存在外部配置文件（jar同级config下），则取外部文件
     * 加载配置文件
     * @param fileName
     */
    private void loadYml(String fileName){
        nowFileName.set(fileName);
        if (!ymls.containsKey(fileName)){
            //先读取config目录的，没有再加载classpath的
            String outpath = System.getProperty("user.dir")+ File.separator+"config"+File.separator;
            File file = new File(outpath + fileName);
            if(file.exists()){
                try (InputStream in = new FileInputStream(file)){
                    LinkedHashMap linkedHashMap = new Yaml().loadAs(in, LinkedHashMap.class);
                    ymls.put(fileName ,linkedHashMap);
                }catch (FileNotFoundException e){
                    e.printStackTrace();
                }catch (IOException ex){
                    ex.printStackTrace();
                }
            }else{
                ymls.put(fileName , new Yaml().loadAs(YmlReadUtil.class.getResourceAsStream("/" + fileName),LinkedHashMap.class));
            }
        }
    }

    /**
     * 读取yml文件中的某个value。
     * 支持解析 yml文件中的 ${} 占位符
     * @param key
     * @return Object
     */
    private Object getValue(String key){
        String[] keys = key.split("[.]");
        Map ymlInfo = (Map) ymls.get(nowFileName.get()).clone();
        for (int i = 0; i < keys.length; i++) {
            Object value = ymlInfo.get(keys[i]);
            if (i < keys.length - 1){
                ymlInfo = (Map) value;
            }else if (value == null){
                throw new RuntimeException("key不存在");
            }else {
                String g;
                String keyChild;
                String v1 = String.valueOf(value);
                for(Matcher m = p1.matcher(v1); m.find(); value = v1.replace(g, (String)getValue(keyChild))) {
                    g = m.group();
                    keyChild = g.replaceAll("\\$\\{", "").replaceAll("\\}", "");
                }
                return value;
            }
        }
        return "";
    }

    /**
     * 读取yml文件中的某个value
     * @param fileName  yml名称
     * @param key
     * @return Object
     */
    public Object getValue(String fileName, String key){
        loadYml(fileName);
        return getValue(key);
    }

    /**
     * 读取yml文件中的某个value，返回String
     * @param fileName
     * @param key
     * @return String
     */
    public String getValueToString(String fileName, String key){
        return String.valueOf(getValue(fileName, key));
    }

    /**
     * 读取yml文件中的某个value，返回Integer。不能转换为integer时将抛出类型转化异常
     * @author Innocence
     * @date 2020/9/22
     * @param fileName
     * @param key
     * @return java.lang.Integer
     */
    public Integer getValueToInteger(String fileName, String key){
        return Integer.valueOf(getValueToString(fileName,key));
    }

}
