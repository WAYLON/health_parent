package com.waylon.qiniu;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.junit.Test;

public class QiNiuTest {

    @Test
    public void test1() {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region2());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        String accessKey = "C6WjFfvJ44iSIn5JvEUf8MZvXE7UsDYAvUAqhXlQ";
        String secretKey = "Nqiq65P0gni1RktgvE9XY73LJL4lM38ftvit6AnM";
        String bucket = "waylon-health";
        //如果是Windows情况下，格式是 D:\\qiniu\\test.png
        String localFilePath = "C:\\Users\\80481\\Desktop\\1.jpg";
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = null;
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            /*System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }*/
        }
    }

    @Test
    public void test2() {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region2());
        //...其他参数参考类注释
        String accessKey = "C6WjFfvJ44iSIn5JvEUf8MZvXE7UsDYAvUAqhXlQ";
        String secretKey = "Nqiq65P0gni1RktgvE9XY73LJL4lM38ftvit6AnM";
        String bucket = "waylon-health";
        String key = "FuXqdK4zpLts2Hw_iXyqaHhxfYMC";
        //过期天数，该文件10天后删除
        int days = 10;
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            //bucketManager.deleteAfterDays(bucket, key, days);
            bucketManager.delete(bucket, key);
        } catch (QiniuException ex) {
            //System.err.println(ex.response.toString());
        }
    }
}
