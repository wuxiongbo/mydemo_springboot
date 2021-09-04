package com.example.util;

import com.aliyun.oss.common.utils.BinaryUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

/**
 * <p>描述类的信息</p>
 *
 * <pre>
 * @author wuxiongbo
 * @date 2021/9/4
 * </pre>
 */
public class MyUtil {
    /**
     * 验证上传回调的Request
     *
     * @param request
     * @param ossCallbackBody    oss 回调请求 应用服务器时，带的 消息体  以及请求头。
     * @return
     * @throws NumberFormatException
     * @throws IOException
     */
    public static boolean verifyOSSCallbackRequest(HttpServletRequest request, String ossCallbackBody)
            throws NumberFormatException, IOException {
        boolean ret = false;

        // 获取 签名
        String autorizationInput = request.getHeader("Authorization");
        byte[] authorization = BinaryUtil.fromBase64String(autorizationInput); //base64解码后的签名


        // 获取 公钥
        //   1. 获取公钥地址
        String pubKeyInput = request.getHeader("x-oss-pub-key-url");
        byte[] pubKey = BinaryUtil.fromBase64String(pubKeyInput); // 公钥URL ，base64解码
        String pubKeyAddr = new String(pubKey); // 公钥URL
        // 为了确认该public_key是由OSS颁发的，用户需要校验 x-oss-pub-key-url头的值必须以 http://gosspublic.alicdn.com/ 或者 https://gosspublic.alicdn.com/ 开头。
        if (!pubKeyAddr.startsWith("http://gosspublic.alicdn.com/")
                && !pubKeyAddr.startsWith("https://gosspublic.alicdn.com/")) {
            System.out.println("pub key addr must be oss addrss");
            return false;
        }
        //   2. 获取公钥
        String publicKey  = executeGet(pubKeyAddr);
        publicKey  = publicKey.replace("-----BEGIN PUBLIC KEY-----", "");
        publicKey  = publicKey.replace("-----END PUBLIC KEY-----", "");


        //  拼接待签名字符串。  签名字符串 = uri + 请求参数 + 回调消息体
        String authStr = "";  // 签名字符串

        // 1)uri
        String uri = request.getRequestURI();
        String decodeUri = java.net.URLDecoder.decode(uri, "UTF-8");
        authStr += decodeUri;
        // 2)请求参数
        String queryString = request.getQueryString();
        if (queryString != null && !queryString.equals("")) {
            authStr += "?" + queryString;
        }
        // 3)回调消息体
        authStr += "\n" + ossCallbackBody;


        // 校验， 确保回调请求是由OSS发起
        ret = doCheck(authStr, authorization, publicKey );

        return ret;
    }
    /**
     * 验证RSA
     *
     * @param content     内容
     * @param sign        签名
     * @param publicKey   公钥
     * @return
     */
    private static boolean doCheck(String content, byte[] sign, String publicKey) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = BinaryUtil.fromBase64String(publicKey);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
            java.security.Signature signature = java.security.Signature.getInstance("MD5withRSA");
            signature.initVerify(pubKey);
            signature.update(content.getBytes());
            boolean bverify = signature.verify(sign);

            return bverify;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    private static String executeGet(String url) {
        BufferedReader in = null;

        String content = null;
        try {
            // 定义HttpClient
            @SuppressWarnings("resource")
            DefaultHttpClient client = new DefaultHttpClient();
            // 实例化HTTP方法
            HttpGet request = new HttpGet();
            request.setURI(new URI(url));
            HttpResponse response = client.execute(request);

            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            // 拼接 公钥字符串
            StringBuffer sb = new StringBuffer("");
            String line = "";
            // 获取系统分隔符
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line + NL);
            }
            in.close();
            content = sb.toString();

        } catch (Exception e) {
        } finally {
            if (in != null) {
                try {
                    in.close();// 最后要关闭BufferedReader
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return content;
        }
    }
}
