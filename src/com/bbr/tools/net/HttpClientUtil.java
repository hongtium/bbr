package cn.atsport.tools.net;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;


/**
 * HttpClientUtil
 * User: chenjx
 * Date: 2010-9-8
 */
public class HttpClientUtil {
    HttpClient httpclient;
    HttpResponse response;

    public HttpClientUtil() {
        httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
    }

    /**
     * 带参数的构造
     * @param connect_timeout   连接建立的超时（毫秒）
     * @param so_timeout    SOCKET维持的超时（毫秒）
     */
    public HttpClientUtil(int connect_timeout, int so_timeout) {
        httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
        httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, new Integer(connect_timeout));
        httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, new Integer(so_timeout));
    }


    public List<Cookie> getCookies() {
        return ((AbstractHttpClient) httpclient).getCookieStore().getCookies();
    }

    public Map<String,String> getMapCookies() {
        Map<String,String> map = new HashMap<String,String>();
        List<Cookie> cookies = ((AbstractHttpClient)httpclient).getCookieStore().getCookies();
        for (Cookie c : cookies) {
            map.put(c.getName(), c.getValue());
        }
        return map;
    }

    public String getCookie(String key) {
        for (Cookie c : ((AbstractHttpClient) httpclient).getCookieStore()
                .getCookies()) {
            if (c.getName().equals("key"))
                return c.getValue();
        }
        return null;
    }



    public String get(String url) throws Exception {
        HttpGet httpget = new HttpGet(url);
        response = httpclient.execute(httpget);
        HttpEntity httpEntity = response.getEntity();
        String html = null;
        if (httpEntity != null) {
            html = EntityUtils.toString(httpEntity);
            httpEntity.consumeContent();
        }
        return html;
    }

    public String get(String url, List<NameValuePair> nvps) throws Exception {
        HttpGet httpget = new HttpGet(url);

        response = httpclient.execute(httpget);
        HttpEntity httpEntity = response.getEntity();
        String html = null;
        if (httpEntity != null) {
            html = EntityUtils.toString(httpEntity);
            httpEntity.consumeContent();
        }
        return html;
    }

    public String post(String url, List<NameValuePair> nvps) throws Exception {
        HttpPost httppost = new HttpPost(url);
        httppost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
        response = httpclient.execute(httppost);
        HttpEntity httpEntity = response.getEntity();
        String html = null;
        if (httpEntity != null) {
            html = EntityUtils.toString(httpEntity);
            httpEntity.consumeContent();
        }
        return html;
    }

    @SuppressWarnings("unchecked")
    public String post(String url, Map map) throws IOException {
        List<NameValuePair> nvps = new ArrayList();
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            NameValuePair nvp = new BasicNameValuePair(key, value);
            nvps.add(nvp);
        }
        HttpPost httppost = new HttpPost(url);
        httppost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
        response = httpclient.execute(httppost);
        HttpEntity httpEntity = response.getEntity();
        String html = null;
        if (httpEntity != null) {
            html = EntityUtils.toString(httpEntity);
            httpEntity.consumeContent();
        }
        return html;
    }

    public String post(String url, String str) throws Exception {
        httpclient.getConnectionManager().closeIdleConnections(30,
                TimeUnit.SECONDS);
        HttpPost httppost = new HttpPost(url);// "http://web-proxy.qq.com/conn_s"
        StringEntity reqEntity = new StringEntity(str);
        httppost.setEntity(reqEntity);
        response = httpclient.execute(httppost);
        HttpEntity httpEntity = response.getEntity();
        String html = null;
        if (httpEntity != null) {
            html = EntityUtils.toString(httpEntity);
            httpEntity.consumeContent();
        }
        return html;
    }

    public void getImg(String url, String path) throws IOException {
        HttpGet httpget = new HttpGet(url);
        response = httpclient.execute(httpget);
        HttpEntity httpEntity = response.getEntity();
        byte[] b = EntityUtils.toByteArray(httpEntity);
        File storeFile = new File(path);
        FileOutputStream output = new FileOutputStream(storeFile);
        output.write(b);
        output.close();
        if (httpEntity != null) {
            httpEntity.consumeContent();
        }
    }

    public void close() throws IOException {
        httpclient.getConnectionManager().shutdown();
    }
}
