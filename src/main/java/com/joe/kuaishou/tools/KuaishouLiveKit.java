package com.joe.kuaishou.tools;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

/**
 * 获取快手我的关注正在直播的列表信息
 */
public class KuaishouLiveKit {
    private static final Logger logger = LoggerFactory.getLogger(KuaishouLiveKit.class);


    private String threadData;

    public String getThreadData() {
        return threadData;
    }

    public void setThreadData(String threadData) {
        this.threadData = threadData;
    }


    public String KsCrawlerKit(String headers, String payload, String url, String pythonPath) {
        Process process = null;
        BufferedReader inputStream = null;
        BufferedReader errorStream = null;
        try {
            String[] args = null;
            if (StringUtils.isBlank(payload)) {
                args = new String[]{"python", pythonPath, headers, url};
            } else {
                args = new String[]{"python", pythonPath, headers, payload, url};
            }
            process = Runtime.getRuntime().exec(args);
            InputStream inputStream1 = process.getInputStream();
            InputStream errorStream1 = process.getErrorStream();
            readStreamInfo(errorStream1, inputStream1);
            int i = process.waitFor();
            if (i == 0) {
                logger.info("python爬虫正常完成");
            } else {
                logger.error("python爬虫异常结束");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
        /*return stringBuffer.toString();*/
        return threadData;
    }


    /**
     * 读取Runtime.exec运行子进程的输入流
     *
     * @param inputStreams 输入流
     */
    private void readStreamInfo(InputStream... inputStreams) {
        int i = 0;
        while (i < 1) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStreams[1]));
            StringBuffer stringBuffer = new StringBuffer();
            String line = "";
            try {
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line);
                    logger.info("python爬虫返回信息:{} ", line);
                    threadData = stringBuffer.toString();
                    i = 1;
                }
                i = 1;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            BufferedReader errorBufferedReader = null;
            try {
                if (inputStreams[0].available() != 0) {
                    errorBufferedReader = new BufferedReader(new InputStreamReader(inputStreams[0]));
                    StringBuffer errorStringBuffer = new StringBuffer();
                    String errorLine = "";
                    try {
                        while ((errorLine = errorBufferedReader.readLine()) != null) {
                            errorStringBuffer.append(errorLine);
                            logger.error("python爬虫返回错误信息:{} ", errorLine);
                            threadData = errorStringBuffer.toString();
                            i = 1;
                        }
                        i = 1;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (errorBufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    public void writeProperties(HashMap<String, String> mapData, String ksProfilePath,String comment) {
        String path = this.getClass().getClassLoader().getResource(ksProfilePath).getPath();
        Properties properties = new Properties();
        FileWriter fileWriter = null;
        try {
            Iterator<Map.Entry<String, String>> entryIterator = mapData.entrySet().iterator();
            while (entryIterator.hasNext()){
                Map.Entry entry = (Map.Entry)entryIterator.next();
                Object key = entry.getKey();
                Object value = entry.getValue();
                properties.setProperty((String) key, (String) value);
//                System.out.println(key+":"+value);
            }
            /*//直播数据写入
            String originalUrl = mapData.get("originalUrl");
            String userAgent = mapData.get("userAgent");
            String liveCookie = mapData.get("liveCookie");
            //短视频数据的写入
            String originalPostUrl = mapData.get("originalPostUrl");
            String shortVideoCookie = mapData.get("shortVideoCookie");
            String shortVideoHost = mapData.get("shortVideoHost");
            String contentType = mapData.get("contentType");

            if (originalPostUrl != null) {
                properties.setProperty("userAgent", userAgent);
                properties.setProperty("originalPostUrl", originalPostUrl);
                properties.setProperty("shortVideoCookie", shortVideoCookie);
                properties.setProperty("shortVideoHost", shortVideoHost);
                properties.setProperty("contentType", contentType);
            } else {
                properties.setProperty("originalUrl", originalUrl);
                properties.setProperty("userAgent", userAgent);
                properties.setProperty("liveCookie", liveCookie);
            }*/
            fileWriter = new FileWriter(path,true);
            properties.store(fileWriter, comment);   //保存到流
            logger.info("数据写入完成：{}", mapData.toString());
        } catch (FileNotFoundException e) {
            logger.error("数据读取失败：{}", e);
            e.printStackTrace();
        } catch (IOException e) {
            logger.error("数据读取失败：{}", e);
            e.printStackTrace();
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public HashMap<String, String> readProperties(String ksProfilePath) {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(ksProfilePath);
        Properties properties = new Properties();
        HashMap<String, String> ksProfileMap = new HashMap<String, String>();
        try {
            properties.load(resourceAsStream);    //从流读取properties文件内容
            Set<String> ksProfileKey = properties.stringPropertyNames();
            Iterator<String> ksyIterator = ksProfileKey.iterator();
            while (ksyIterator.hasNext()) {
                String key = ksyIterator.next();
                String value = properties.getProperty(key);
                ksProfileMap.put(key, value);
            }
            return ksProfileMap;
        } catch (IOException e) {
            logger.error("数据读取失败：{}", e);
            e.printStackTrace();
        } finally {
            if (resourceAsStream != null) {
                try {
                    resourceAsStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return ksProfileMap;
    }
}

