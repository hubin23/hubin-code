package com.just4cool.v1.util;


import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by tangang on 2016/10/31.
 */

public class OkHttpHelper {

    public static final MediaType JSONTYPE = MediaType.parse("application/json; charset=utf-8");



    private static volatile OkHttpHelper instance;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private OkHttpClient HttpClient;

    private OkHttpHelper() {
        //此处配置OkHttpClient的基本信息,okhttp3在new对象并需要配置参数一般通过build这个方法来实现,类似的还有Request：
        HttpClient=new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .build();
    }

    //单例模式
    public static OkHttpHelper getInstance() {
        if (instance == null) {
            synchronized (OkHttpHelper.class) {
                if (instance == null) {
                    instance = new OkHttpHelper();
                }
            }
        }
        return instance;
    }

    /**
     * 一般的get请求 对于一般的请求，我们希望给个url，然后取的返回的String。
     */
    public String get(String url) throws Exception {
        return get(url, null);
    }

    public String get(String url, Map<String, String> params) throws Exception {
        StringBuilder sb = new StringBuilder();
        if (params != null && params.size() > 0) {
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            sb.append("?");
            for (Map.Entry<String, String> entry : entrySet) {
                sb.append(entry.getKey());
                sb.append("=");
                try {
                    sb.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                sb.append("&");
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        Request request = new Request.Builder().url(url + sb.toString()).get().build();
        Call call = HttpClient.newCall(request);
        Response response = null;
        ResponseBody responseBody = null;
        try {
            response = call.execute();
            if (null != response) {
                responseBody = response.body();
                if (null != responseBody) {
                    return responseBody.string();
                }
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != responseBody) {
                responseBody.close();
            }
        }
        return  "";
    }

    /**
     * 一般的post请求 对于一般的请求，我们希望给个url和封装参数的Map，然后取的返回的String。
     */
    public String post(String url, JSONObject param) throws Exception {
/*        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        if (params != null && params.size() > 0) {
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                formBodyBuilder.add(entry.getKey(), entry.getValue());
            }
        }
        Request request = new Request.Builder().url(url).post(formBodyBuilder.build()).build();
        Call call = HttpClient.newCall(request);
        Response response = null;
        ResponseBody responseBody = null;*/

        //----------------test-----------------------
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(JSONTYPE, param.toJSONString());
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        ResponseBody responseBody = null;
        //-------------------------------------------

        try {
            Response response = client.newCall(request).execute();
            if (null != response) {
                responseBody = response.body();
                if (null != responseBody) {
                    return responseBody.string();
                }
            } else {
                return "";
            }
        } catch (Exception e) {
        } finally {
            if (null != responseBody) {
                responseBody.close();
            }
        }
        return  "";
    }

    /**
     * 一般的post请求 对于一般的请求，我们希望给个url和封装参数的Map，然后取的返回的String。
     */
    public String formPost(String url, Map<String, String> params) throws Exception {
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        if (params != null && params.size() > 0) {
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                formBodyBuilder.add(entry.getKey(), entry.getValue());
            }
        }
        Request request = new Request.Builder().url(url).post(formBodyBuilder.build()).build();
        Call call = HttpClient.newCall(request);
        Response response = null;
        ResponseBody responseBody = null;
        try {
            response = call.execute();
            if (null != response) {
                responseBody = response.body();
                if (null == responseBody) {
                    return responseBody.string();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != responseBody) {
                responseBody.close();
            }
        }
        return "";
    }

    public boolean sendImg(String url,File file) throws Exception{
        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), fileBody)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();
        if(response.isSuccessful()){
            return true;
        } else {
            System.out.println(response.body().string());
        }
        return false;
    }

//    /**
//     * 一般的post请求 对于一般的请求，我们希望给个url和封装参数的Map，然后取的返回的String。
//     */
//    public String formPost(String url, Map<String,String> params) throws Exception {
//        FormBody.Builder formBodyBuilder = new FormBody.Builder();
//        if (params != null && params.size() > 0) {
//            Set<Map.Entry<String, String>> entrySet = params.entrySet();
//            for (Map.Entry<String, String> entry : entrySet) {
//                formBodyBuilder.add(entry.getKey(), entry.getValue());
//            }
//        }
//        Request request = new Request.Builder().url(url).post(formBodyBuilder.build()).build();
//        Call call = HttpClient.newCall(request);
//        Response response = null;
//        ResponseBody responseBody = null;
//
//        try {
//            response = HttpClient.newCall(request).execute();
//            if (null != response) {
//                responseBody = response.body();
//                if (null != responseBody) {
//                    return responseBody.string();
//                }
//            } else {
//                return "";
//            }
//        } catch (Exception e) {
//        } finally {
//            if (null != responseBody) {
//                responseBody.close();
//            }
//        }
//        return  "";
//    }

//    public static void main(String[] args) {
//        String url = "http://192.168.3.72:5100/product/getByProductId/20170308172411";
//        JSONObject requestMap = new JSONObject();
//        String nowStr  = StringUtil.convertDateToString(new Date(),"yyyy-MM-dd")+"T"+StringUtil.convertDateToString(new Date(),"HH:mm:ss");
//        requestMap.put("additionContent","additionContent");
//        requestMap.put("additionType","additionContent");
//        requestMap.put("additionValue", "0");
//        requestMap.put("amount","0");
//        requestMap.put("annualRate","0");
//        requestMap.put("borrowerAddress","additionContent");
//        requestMap.put("borrowerBankAccount","additionContent");
//        requestMap.put("borrowerBankAccountName","additionContent");
//        requestMap.put("borrowerBankBranch","additionContent");
//        requestMap.put("borrowerBankName","additionContent");
//        requestMap.put("borrowerBankRegion","additionContent");
//        requestMap.put("borrowerIdCard","additionContent");
//        requestMap.put("borrowerName","additionContent");
//        requestMap.put("days","0");
//        requestMap.put("isPrivate","additionContent");
//        requestMap.put("productId","additionContent");
//        requestMap.put("productName","additionContent");
//        requestMap.put("productType","additionContent");
//        requestMap.put("returnRate","0");
//        requestMap.put("returnType","additionContent");
//        requestMap.put("showChannel","additionContent");
//        requestMap.put("showTime",nowStr);
//        requestMap.put("tag","additionContent");
//        requestMap.put("totalIncome","0");
//        requestMap.put("unitAmount","0");
////        requestMap.put("isTop",selfitemService.getSelfitemByid(blSelfitem.getItemid()).getTopindex());
//        requestMap.put("isTop","1");
////        requestMap.put("periods",selfitemService.getSelfitemByid(blSelfitem.getItemid()).getPeriods());
//        requestMap.put("periods","90");
//        requestMap.put("totalShare","100");
//        requestMap.put("state","additionContent");
//        requestMap.put("queueNum","201703290000000000002904");
//        requestMap.put("borrowerType","1");
//        requestMap.put("cardType","1");
//        requestMap.put("dayUnitIncome","0.05");
//        requestMap.put("expireTime",nowStr);
//        requestMap.put("maxShare","100");
//        requestMap.put("minShare","1");
////        companyRequestMap.put("accountName","accountName");
////        companyRequestMap.put("accountNo","accountName");
////        companyRequestMap.put("openBank","accountName");
////        companyRequestMap.put("borrowRate","accountName");
////        companyRequestMap.put("borrowProfit","accountName");
////        companyRequestMap.put("businessName","accountName");
////        companyRequestMap.put("businessNo","accountName");
////        companyRequestMap.put("collStartDate","accountName");
////        companyRequestMap.put("collEndDate","accountName");
////        companyRequestMap.put("startDate","accountName");
////        companyRequestMap.put("endDate","accountName");
////        companyRequestMap.put("investAmount","accountName");
////        companyRequestMap.put("projectLastDate","accountName");
//        /**
//         * "appVersion": "string",
//         "explainKey": "string",
//         "explainValue": "string",
//         "productId": "string",
//         "state": "string"
//         */
////        companyRequestMap.put("appVersion","4.0.0");
////        companyRequestMap.put("explainKey","71515156");
////        companyRequestMap.put("explainValue","0");
////        companyRequestMap.put("productId","156651-dwedwe");
////        companyRequestMap.put("state","4");
//
//        try {
//            String result = getInstance().post("http://192.168.3.72:5100/product/syncProduct",requestMap);
//            System.out.printf(result);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}