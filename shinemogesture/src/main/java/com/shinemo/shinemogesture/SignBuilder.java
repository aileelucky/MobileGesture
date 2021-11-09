package com.shinemo.shinemogesture;

import android.content.Context;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okio.Buffer;

/**
 * Created by Jin on 2017/6/9.
 * Description 排序加密类
 */
public class SignBuilder {

    /**
     * 重新构造Request，加入加密的sign
     *
     * @param time header中的参数time
     */
    public static Request newBuilder(Context context, Request.Builder builder, String time) throws IOException {
        Request request = builder.build();

        Map<String, String> rootMap = getBaseMap(context, time);

        // Head
        HttpUrl mHttpUrl = request.url();
        Set<String> paramNames = mHttpUrl.queryParameterNames();
        for (String key : paramNames) {
            if (!rootMap.containsKey(key)) {
                rootMap.put(key, mHttpUrl.queryParameter(key));
            }
        }

        // Body
        // 普通提交情况
        if (request.body() instanceof FormBody) {
            FormBody requestBody = (FormBody) request.body();

            for (int i = 0; i < requestBody.size(); i++) {
                if (!rootMap.containsKey(requestBody.name(i))) {
                    rootMap.put(requestBody.name(i), requestBody.value(i));
                }
            }
            // 处理参数和图片一起按MultiPart方式提交上来的情况
        } else if (request.body() instanceof MultipartBody) {
            try {
                MultipartBody requestBody = (MultipartBody) request.body();
                List<MultipartBody.Part> part = requestBody.parts();
                for (MultipartBody.Part item : part) {
                    /**
                     * 普通multipart 参数的MediaType ：multipart/form-data; charset=utf-8
                     * 剔除掉图片，把其他类型的参数筛选出来
                     * MultipartBody.Part中的一些方法，如item.body().contentType()，需要高版本的okHttp支持，建议引入3.8.1+
                     */
                    if (!isImgMediaType(item.body().contentType())) {
                        // 获取value
                        Buffer itemBuffer = new Buffer();
                        item.body().writeTo(itemBuffer);
                        Charset charset = Charset.forName("UTF-8");
                        String value = itemBuffer.readString(charset);

                        /**
                         * 从header中截取key
                         * See {@link MultipartBody.Part) } createFormData方法中通过（form-data; name=）拼接header
                         */
                        String key_[] = item.headers().toString().split("form-data; name=\"");
                        String key = "";
                        if (key_.length >= 2) {
                            String key__[] = key_[1].split("\"");
                            if (key__.length >= 2) {
                                key = key__[0];
                            }
                        }

                        /**
                         * 重复的不加入(主要是多张图片上传重复的参数，和后台约定只取第一张)
                         */
                        if (!rootMap.containsKey(key))
                            rootMap.put(key, value);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String encryptedSign = encryptToSHA(getSign(rootMap));
        builder.addHeader("sign", encryptedSign);
        request = builder.build();
        return request;
    }

    /**
     * 项目中上传图片的可能格式是这三个 image/jpg", "image/jpeg", "image/png
     */
    private static boolean isImgMediaType(MediaType type) {
        return MediaType.parse("image/jpg").equals(type)
                || MediaType.parse("image/jpeg").equals(type)
                || MediaType.parse("image/png").equals(type);
    }

    /**
     * 加密
     */
    public static String encryptToSHA(String info) {
        byte[] digest = null;
        try {
            MessageDigest alga = MessageDigest.getInstance("SHA-1");
            alga.update(info.getBytes());
            digest = alga.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return byte2hex(digest);
    }

    /**
     * Convert byte[] to hex string.这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
     *
     * @return hex string
     */
    public static String byte2hex(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return "";
        }
        for (int i = 0; i < src.length; i++) {
            String hv = Integer.toHexString(src[i] & 0xFF);
            if (hv.length() < 2) {
                stringBuilder.append("0");
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 对Map元素进行排序
     */
    private static String sort(Map<String, String> params) {
        List<String> list = new ArrayList<>();
        for (String s : params.keySet()) {
            list.add(s);
        }
        Collections.sort(list);
        StringBuilder sortAsc = new StringBuilder("");
        for (String s : list) {
            sortAsc.append(s + "=" + params.get(s) + "&");
        }
        return sortAsc.toString();
    }

    /**
     * 末尾加上一个参数
     */
    private static String getSign(Map<String, String> params) {
        return sort(params) + "key=uama1209";
    }

    /**
     * Head里面默认传输的基础参数（和header保持一致即可）
     */
    public static Map<String, String> getBaseMap(Context context, String time) {
        Map<String, String> params = new HashMap<>();

        params.put("Content-Type", "application/json;charset=UTF-8");
        params.put("auth-os","2");
        params.put("sysId","12");
        return params;
    }
}
