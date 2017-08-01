package com.ryo.medata.util.util;

import org.apache.logging.log4j.util.PropertiesUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * Created by houbinbin on 16/9/11.
 * - Properties 文件读写工具
 * - 避免和 spring 属性工具类混淆
 */
public class PropertiesIOUtil {

    private PropertiesIOUtil() {
    }

    private static Properties _prop = new Properties();

    /**
     * 根路径
     */
    private static final String ROOT_PATH = "/";

    /**
     * 加载配置文件属性
     *
     * @param path
     * @param fileName
     */
    public static void loadProperties(String path, String fileName) {
        try (InputStream in = PropertiesUtil.class.getResourceAsStream(path + fileName);
             BufferedReader bf = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            _prop.load(bf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载配置文件属性
     * - 默认加载根路径下文件
     *
     * @param fileName
     */
    public static void loadProperties(String fileName) {
        loadProperties(ROOT_PATH, fileName);
    }

    /**
     * 根据key读取对应的value
     *
     * @param key
     * @return
     */
    public static String getProperty(String key) {
        return _prop.getProperty(key);
    }

}
