package com.keeko.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import com.keeko.config.StaticConstant;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

@Slf4j
public class AliyunOSSUtil {

    public static String uploadImage(InputStream is, String id, String extension) {
        String endpoint = StaticConstant.OSS_END_POINT;
        String accessKeyId = StaticConstant.OSS_ACCESS_KEY_ID;
        String accessKeySecret = StaticConstant.OSS_ACCESS_KEY_SECRET;
        OSSClient client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        String baseUrl = "http://zt-pic.oss-cn-beijing.aliyuncs.com/";
        String bucketName = "zt-pic";
        String fileName = "cr/tmp/" + id + "." + extension;
        try {
            client.putObject(bucketName, fileName, is);
            return baseUrl + fileName;
        } catch (OSSException oe) {
            System.out.println(oe.getMessage());
        } catch (ClientException ce) {
            System.out.println(ce.getMessage());
        } finally {
            client.shutdown();
        }
        return null;
    }

    public static String uploadMedia(InputStream is, String id, String extension) {
        String endpoint = StaticConstant.OSS_END_POINT;
        String accessKeyId = StaticConstant.OSS_ACCESS_KEY_ID;
        String accessKeySecret = StaticConstant.OSS_ACCESS_KEY_SECRET;
        OSSClient client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        String baseUrl = "http://zt-media.oss-cn-beijing.aliyuncs.com/";
        String bucketName = "zt-media";
        String fileName = "cr/tmp/" + id + "." + extension;

        try {
            client.putObject(bucketName, fileName, is);
            return baseUrl + fileName;
        } catch (OSSException oe) {
            log.error(oe.getMessage());
        } catch (ClientException ce) {
            log.error(ce.getMessage());
        } finally {
            client.shutdown();
        }
        return null;
    }

    /**
     * ?????????????????????
     *
     * @param phone ?????????
     */
    public static SmsSingleSenderResult sendSmsCode(final String phone) throws HTTPException, IOException {
        //????????????SDK AppID 1400??????
        int appid = 1400214620;
        //????????????SDK AppKey
        String appkey = "c5427155380e1abeb5c5a98d76adcfbd";
        // NOTE: ?????????????????????ID?????????????????????????????????
        int templateId = 343085;
        //????????????????????????????????????
        String smsSign = "??????";
        //????????????
        String activetime = "5";
        //???????????????
        SmsSingleSender singleSender = new SmsSingleSender(appid, appkey);
        //????????????6???????????????
        String smsCode = "";
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            smsCode += random.nextInt(10);
        }
        String[] params = {"", smsCode, activetime};
        //smsCode ?????????????????????
        EhcacheUtil.getInstance().put("CONSTANT", phone, smsCode);
        // ????????????????????????????????????????????????????????????????????????????????????13800138000?????????????????????????????????
        return singleSender.sendWithParam("86", phone, templateId, params, smsSign, "", "");
    }

}
