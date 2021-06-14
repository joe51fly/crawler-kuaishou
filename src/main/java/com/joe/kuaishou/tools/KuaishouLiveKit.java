package com.joe.kuaishou.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Properties;

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

    private static final String winPath =  "F:\\Applications\\workSpaces\\crawler-kuaishou\\KsCrawler3.0.py";
    private static final String linuxPath =  "/home/root/java-project/KsCrawler-2.7.py";

   public String KsCrawlerKit(String headers, String url) {
        Process process = null;
        BufferedReader inputStream = null;
        BufferedReader errorStream = null;
        try {
            String[] args = new String[]{"python",winPath, headers, url};
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


    public void writeProperties(HashMap<String, String> mapData) {
        String path = this.getClass().getClassLoader().getResource("ksLiveData.properties").getPath();
        Properties properties = new Properties();
        FileWriter fileWriter = null;
        try {
            String originalUrl = mapData.get("originalUrl");
            String userAgent = mapData.get("userAgent");
            String liveCookie = mapData.get("liveCookie");

            properties.setProperty("originalUrl", originalUrl);
            properties.setProperty("userAgent", userAgent);
            properties.setProperty("liveCookie", liveCookie);
            fileWriter = new FileWriter(path);
            properties.store(fileWriter, "setData");   //保存到流
            logger.info("数据写入完成：{}", mapData.toString());
        } catch (FileNotFoundException e) {
            logger.error("数据读取失败：{}", e);
            e.printStackTrace();
        } catch (IOException e) {
            logger.error("数据读取失败：{}", e);
            e.printStackTrace();
        } finally {
           if (fileWriter != null){
               try {
                   fileWriter.close();
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
        }
    }

    public String readProperties(String key) {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("ksLiveData.properties");
        Properties properties = new Properties();
        try {
            properties.load(resourceAsStream);    //从流读取properties文件内容
            String value = properties.getProperty(key);
            return value;
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
        return "";
    }
}

