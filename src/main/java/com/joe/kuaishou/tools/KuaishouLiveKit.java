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

    public String KsCrawlerKit(String headers, String url) {

        Process process = null;
        BufferedReader inputStream = null;
        BufferedReader errorStream = null;
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer errStringBuffer = new StringBuffer();
        try {
            /*F:\Applications\workSpaces\joe-kuaishou1\KsCrawler3.0.py
             * /home/root/java-project/KsCrawler-2.7.py */
            String[] args = new String[]{"python", "F:\\Applications\\workSpaces\\crawler-kuaishou\\KsCrawler3.0.py", headers, url};
            process = Runtime.getRuntime().exec(args);

/*
            while ((inputStr = inputStream.readLine())  != null){
                logger.info("ksRespData={}",stringBuffer.append(inputStr).toString());
            }*/
            InputStream inputStream1 = process.getInputStream();
            InputStream errorStream1 = process.getErrorStream();
            readStreamInfo(errorStream1, inputStream1);
            int i = process.waitFor();
            if (i == 0) {
                logger.info("子进程正常完成");
            } else {
                logger.error("子进程异常结束");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            /*if (errorStream != null){
                try {
                    errorStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }*/
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
//        new Thread(() -> {
        int i = 0;
        while (i < 1) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStreams[1]));
            StringBuffer stringBuffer = new StringBuffer();
            String line = "";
            try {
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line);
                    logger.info("爬虫返回信息:{} ", line);
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
                            logger.error("爬虫错误信息:{} ", errorLine);
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
//        }).start();
    }


    public void writeProperties(HashMap<String, String> mapData) {
        String path = this.getClass().getClassLoader().getResource("ksLiveData.properties").getPath();
//        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("ksLiveData.properties");
//        OutputStream outputStream = new ByteArrayOutputStream();
        Properties properties = new Properties();
        FileWriter fileWriter = null;
        try {
//            byte[] buffer = new byte[1024];
//            int len;
//            if (resourceAsStream != null) {
//                while ((len = resourceAsStream.read(buffer)) != -1) { /** 将流中内容写出去 .*/
//                    outputStream.write(buffer, 0, len);
//                }
//            }
//            outputStream.flush();
            String originalUrl = mapData.get("originalUrl");
            String userAgent = mapData.get("userAgent");
            String liveCookie = mapData.get("liveCookie");

            properties.setProperty("originalUrl", originalUrl);
            properties.setProperty("userAgent", userAgent);
            properties.setProperty("liveCookie", liveCookie);
            fileWriter = new FileWriter(path);
            properties.store(fileWriter, "setData");   //保存到流
            logger.info("数据写入完成：{}", mapData.toString());
            logger.info("ksLiveData.properties的路径：{}", path);
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
//        String path = this.getClass().getClassLoader().getResource("ksLiveData.properties").getPath();
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("ksLiveData.properties");
        logger.info("ksLiveData.properties的路径：{}", resourceAsStream);
        Properties properties = new Properties();
//        FileReader fileReader = null;
        try {
//            fileReader = new FileReader(path);
            properties.load(resourceAsStream);    //从流读取properties文件内容
            String value = properties.getProperty(key);
            logger.info("数据读取：{}:{}", key, value);
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

