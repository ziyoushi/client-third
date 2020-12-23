package com.cc.develop.third.client.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * com.cc.develop.third.client.util
 *
 * @author changchen
 * @email java@mail.com
 * @date 2020-12-22 13:08:11
 */
@Slf4j
public class HttpClientUtil {

    public static String httpPostXML(String url, String xmlString) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build(); //创建CloseableHttpClient
        HttpPost httpPost = new HttpPost(url); //实现HttpPost
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(2000).build();
        httpPost.setConfig(requestConfig); //设置httpPost的状态
        httpPost.addHeader("Content-Type", "text/xml"); //设置httpPost的请求头中的MIME类型为xml
        try {
            if (null != xmlString) {
                StringEntity requestEntity = new StringEntity(xmlString, "utf-8");
                httpPost.setEntity(requestEntity); //设置请求体
            }
            return resposeResultXML(httpClient, httpPost, url);
        } catch (IOException e) {
            log.error("post请求提交失败:" + url, e);
        } finally {
            httpPost.releaseConnection();
        }
        return null;
    }

    /**
     * 返回提取
     *
     * @param httpClient
     * @param httpPost
     * @param url
     * @return
     * @throws IOException
     */
    private static String resposeResultXML(CloseableHttpClient httpClient, HttpPost httpPost, String url) throws IOException {
        CloseableHttpResponse result = httpClient.execute(httpPost);
        //请求发送成功，并得到响应
        if (result.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String str = "";
            try {
                HttpEntity entity = result.getEntity();

                if (entity != null) {
                    str = EntityUtils.toString(entity, "utf-8");
                    return str;
                } else {
                    return null;
                }
            } catch (Exception e) {
                log.error("post请求提交失败:" + url, e);
            }
        }
        return null;
    }

    /**
     * post 请求
     *
     * @param url
     * @param jsonParam
     * @return
     */
    public static String httpPost(String url, JSONObject jsonParam) {
        // post请求返回结果
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String jsonResult = null;
        HttpPost httpPost = new HttpPost(url);
        // 设置请求和传输超时时间
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(2000).setConnectTimeout(2000).build();
        httpPost.setConfig(requestConfig);
        try {
            if (null != jsonParam) {
                // 解决中文乱码问题
                StringEntity entity = new StringEntity(jsonParam.toString(),
                        "utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");
                httpPost.setEntity(entity);

            }
            jsonResult = responseResult(httpClient, httpPost, url);
        } catch (IOException e) {
            log.error("post请求提交失败:" + url, e);
        } finally {
            httpPost.releaseConnection();
        }
        return jsonResult;
    }

    public static String httpPost(String url, String param,String signature,String timestamp) {
        // post请求返回结果
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String jsonResult = null;
        HttpPost httpPost = new HttpPost(url);
        // 设置请求和传输超时时间
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(2000).setConnectTimeout(2000).build();
        httpPost.setConfig(requestConfig);
        try {
            if (null != param) {
                // 解决中文乱码问题
                StringEntity entity = new StringEntity(param, "utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");

                httpPost.setEntity(entity);
                //设置时间
                httpPost.setHeader("TimeStamp",timestamp);
                //设置sha1验证值
                httpPost.setHeader("Signature",signature);
            }
            jsonResult = responseResult(httpClient, httpPost, url);
        } catch (IOException e) {
            log.error("post请求提交失败:" + url, e);
        } finally {
            httpPost.releaseConnection();
        }
        return jsonResult;
    }

    /**
     * post 请求 参数是string
     *
     * @param url
     * @param strParam
     * @return
     */
    public static String httpPost(String url, String strParam) {
        // post请求返回结果
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String jsonResult = null;
        HttpPost httpPost = new HttpPost(url);
        // 设置请求和传输超时时间
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(2000).setConnectTimeout(2000).build();
        httpPost.setConfig(requestConfig);
        try {
            if (null != strParam) {
                // 解决中文乱码问题
                StringEntity entity = new StringEntity(strParam, "utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");

                httpPost.setEntity(entity);
            }
            jsonResult = responseResult(httpClient, httpPost, url);
        } catch (IOException e) {
            log.error("post请求提交失败:" + url, e);
        } finally {
            httpPost.releaseConnection();
        }
        return jsonResult;
    }


    /**
     * 返回提取
     *
     * @param httpClient
     * @param httpPost
     * @param url
     * @return
     * @throws IOException
     */
    private static String responseResult(CloseableHttpClient httpClient, HttpPost httpPost, String url) throws IOException {
        CloseableHttpResponse result = httpClient.execute(httpPost);
        //请求发送成功，并得到响应
        if (result.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String str = "";
            try {
                //读取服务器返回过来的json字符串数据
                str = EntityUtils.toString(result.getEntity(), "utf-8");
                //把json字符串转换成json对象
                if (str.startsWith("[")) {
                    //如果返回的是数组
                    JSONArray jsonArray = JSONArray.parseArray(str);
                    String string = jsonArray.toJSONString();
                    return string;
                }
                return JSONObject.toJSONString(str);
            } catch (Exception e) {
                log.error("post请求提交失败:" + url, e);
            }
        }
        return null;
    }

    private static JSONObject responseResultByResponseCode(CloseableHttpClient httpClient, HttpPost httpPost, String url) throws IOException {
        CloseableHttpResponse result = httpClient.execute(httpPost);
        JSONObject responseInfo = new JSONObject();
        responseInfo.put("statusCode", result.getStatusLine().getStatusCode());
        //请求发送成功，并得到响应
//        if (result.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
        if (true) {
            try {
                //读取服务器返回过来的json字符串数据
                String str = EntityUtils.toString(result.getEntity(), "utf-8");
                //把json字符串转换成json对象
                if (StringUtils.isNotBlank(str)) {
                    responseInfo.put("data", JSONObject.parseObject(str));
                }
            } catch (Exception e) {
                log.error("post请求提交失败:" + url, e);
            }
        }
        log.info("数据共享-返回：{}, {}", url, responseInfo.toJSONString());
        return responseInfo;
    }

    public static JSONObject httpPostReturnCode(String url, String param) {
        // post请求返回结果
//        log.debug("请求参数：{},{}:", url, param);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        // 设置请求和传输超时时间
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(2000).setConnectTimeout(2000).build();
        httpPost.setConfig(requestConfig);
        try {
            if (null != param) {
                // 解决中文乱码问题
                StringEntity entity = new StringEntity(param, "utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");
                httpPost.setEntity(entity);
            }
            JSONObject result = responseResultByResponseCode(httpClient, httpPost, url);
//            JSONObject tmp = responseResult(httpClient, httpPost, url);
//            log.info("请求提交提取结果：{}", tmp.toJSONString());
            //请求发送成功，并得到响应
            return result;
        } catch (IOException e) {
            log.error("post请求提交失败:{}, {}", url, e.getMessage());
        } finally {
            httpPost.releaseConnection();
        }
        return null;
    }

    /**
     * 使用该方法 调试
     * @param url
     * @param param
     * @param signature
     * @param timestamp
     * @return
     */
    public static JSONObject httpPostReturnCodeWithParam(String url, String param, String signature, String timestamp) {
        //封装post请求
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        // 设置请求和传输超时时间
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(2000).setConnectTimeout(2000).build();
        httpPost.setConfig(requestConfig);
        try {
            if (null != param) {
                // 解决中文乱码问题
                StringEntity entity = new StringEntity(param, "utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");
                httpPost.setEntity(entity);
                httpPost.setHeader("Signature", signature);
                httpPost.setHeader("TimeStamp", timestamp);
                httpPost.setHeader("code","WJBDB001");

            }
            JSONObject result = responseResultByResponseCode(httpClient, httpPost, url);
            return result;
        } catch (IOException e) {
            log.error("post请求提交失败:{}, {}", url, e.getMessage());
        } finally {
            httpPost.releaseConnection();
        }
        return null;
    }


}
