//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package tcy.common.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

public class HttpClientUtils {
    private static Logger LOGGER = LoggerFactory.getLogger(HttpClientUtils.class);
    private static String DEFAULT_CHARSET = "UTF-8";

    public HttpClientUtils() {
    }

    public static Object post(String url, Map<String, String> params) throws Exception {
        return post(url, params, DEFAULT_CHARSET);
    }

    public static String post(String url, Map<String, String> params, String charSet) throws Exception {
        return post(url, params, charSet, (Map)null);
    }

    public static String postFile(String url, Map<String, HttpEntity> params) throws Exception {
        HashMap headers = new HashMap();
        headers.put("enctype", "multipart/form-data");
        return postFile(url, params, headers);
    }

    public static String postFile(String url, Map<String, HttpEntity> params, Map<String, String> headers) throws Exception {
        return postFile(url, params, headers, DEFAULT_CHARSET);
    }

    public static String postFile(String url, Map<String, HttpEntity> params, Map<String, String> headers, String charSet) throws Exception {
        CloseableHttpClient client = getHttpClient();
        HttpPost httpPost = new HttpPost(url);
        setFileEntity(httpPost, params);
        setHeaders(httpPost, headers);
        CloseableHttpResponse response = null;

        String var8;
        try {
            response = client.execute(httpPost);
            HttpEntity e = response.getEntity();
            var8 = getResponseText(e, charSet);
        } catch (Exception var20) {
            LOGGER.error("Exception: {}", var20);
            throw new Exception(var20);
        } finally {
            try {
                client.close();
            } catch (IOException var19) {
                LOGGER.error("Exception: {}", var19);
                throw new Exception(var19);
            }

            try {
                response.close();
            } catch (IOException var18) {
                LOGGER.error("Exception: {}", var18);
                throw new Exception(var18);
            }
        }

        return var8;
    }

    public static void setFileEntity(HttpEntityEnclosingRequest httpEntityEnclosingRequest, Map<String, HttpEntity> params) {
        if(params != null && params.size() > 0) {
            Iterator iterator = params.entrySet().iterator();

            while(iterator.hasNext()) {
                Entry entry = (Entry)iterator.next();
                httpEntityEnclosingRequest.setEntity((HttpEntity)entry.getValue());
            }
        }

    }

    public static String post(String url, Map<String, String> params, String charSet, Map<String, String> headers) throws Exception {
        CloseableHttpClient client = getHttpClient();
        ArrayList nameValuePairList = new ArrayList();
        if(params != null && params.size() > 0) {
            Set httpPost = params.entrySet();
            Iterator urlEncodedFormEntity = httpPost.iterator();

            while(urlEncodedFormEntity.hasNext()) {
                Entry response = (Entry)urlEncodedFormEntity.next();
                nameValuePairList.add(new BasicNameValuePair(response.getKey()==null?"":response.getKey().toString(),
                        response.getValue()==null?"":response.getValue().toString()));
            }
        }

        HttpPost httpPost1 = new HttpPost(url);
        CloseableHttpResponse response1 = null;
        setHeaders(httpPost1, headers);

        String var10;
        try {
            UrlEncodedFormEntity urlEncodedFormEntity1 = new UrlEncodedFormEntity(nameValuePairList, charSet);
            httpPost1.setEntity(urlEncodedFormEntity1);
            response1 = client.execute(httpPost1);
            HttpEntity e = response1.getEntity();
            var10 = getResponseText(e, charSet);
        } catch (Exception var22) {
            LOGGER.error("Exception: {}", var22);
            throw new Exception(var22);
        } finally {
            try {
                client.close();
            } catch (IOException var21) {
                LOGGER.error("Exception: {}", var21);
                throw new Exception(var21);
            }

            try {
                response1.close();
            } catch (IOException var20) {
                LOGGER.error("Exception: {}", var20);
                throw new Exception(var20);
            }
        }

        return var10;
    }

    public static void setHeaders(HttpRequestBase base, Map<String, String> headerMap) {
        if(headerMap != null && headerMap.size() > 0) {
            Set headerEntry = headerMap.entrySet();
            Iterator it = headerEntry.iterator();

            while(it.hasNext()) {
                Entry entry = (Entry)it.next();
                base.setHeader(entry.getKey().toString(), entry.getValue().toString());
            }
        }

    }

    public static String postXml(String url, String xmlContent) throws Exception {
        return postXml(url, xmlContent, DEFAULT_CHARSET);
    }

    public static String postXml(String url, String xmlContent, String charSet) throws Exception {
        CloseableHttpClient httpClient = getHttpClient();
        HttpPost httpPost = new HttpPost(url);
        StringEntity reqEntity = null;
        CloseableHttpResponse response = null;

        String var8;
        try {
            reqEntity = new StringEntity(xmlContent, charSet);
            httpPost.setHeader("Content-Type", "application/xml");
            httpPost.setEntity(reqEntity);
            response = httpClient.execute(httpPost);
            HttpEntity e = response.getEntity();
            var8 = getResponseText(e, charSet);
        } catch (Exception var20) {
            LOGGER.error("Exception: {}", var20);
            throw new Exception(var20);
        } finally {
            try {
                httpClient.close();
            } catch (IOException var19) {
                LOGGER.error("Exception: {}", var19);
                throw new Exception(var19);
            }

            try {
                response.close();
            } catch (IOException var18) {
                LOGGER.error("Exception: {}", var18);
                throw new Exception(var18);
            }
        }

        return var8;
    }

    public static String get(String url) throws Exception {
        return get(url, DEFAULT_CHARSET);
    }

    private static CloseableHttpClient getHttpClient() {
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(1000).setConnectTimeout(30000).setSocketTimeout(8000).build();
        CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
        return httpClient;
    }

    public static String get(String url, String charSet) throws Exception {
        CloseableHttpClient httpClient = getHttpClient();
        CloseableHttpResponse response = null;

        String var6;
        try {
            HttpGet e = new HttpGet(url);
            response = httpClient.execute(e);
            HttpEntity entity = response.getEntity();
            var6 = getResponseText(entity, charSet);
        } catch (Exception var18) {
            LOGGER.error("Exception: {}", var18);
            throw new Exception(var18);
        } finally {
            try {
                httpClient.close();
            } catch (IOException var17) {
                LOGGER.error("Exception: {}", var17);
                throw new Exception(var17);
            }

            try {
                response.close();
            } catch (IOException var16) {
                LOGGER.error("Exception: {}", var16);
                throw new Exception(var16);
            }
        }

        return var6;
    }

    private static String getResponseText(HttpEntity entity, String charSet) throws IOException {
        return entity != null? EntityUtils.toString(entity, charSet):null;
    }
}
