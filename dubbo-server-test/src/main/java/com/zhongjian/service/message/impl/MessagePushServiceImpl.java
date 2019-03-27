package com.zhongjian.service.message.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhongjian.common.config.properties.MessagePushProperties;
import com.zhongjian.common.constant.FinalDatas;
import com.zhongjian.common.constant.enums.response.CommonMessageEnum;
import com.zhongjian.common.dto.ResultDTO;
import com.zhongjian.common.util.CheckSumBuilderUtil;
import com.zhongjian.common.util.HttpClientUtil;
import com.zhongjian.common.util.MapUtil;
import com.zhongjian.dao.dto.message.MessageBodyDTO;
import com.zhongjian.dao.dto.message.MessageReqDTO;
import com.zhongjian.dao.dto.message.MessageResDTO;
import com.zhongjian.dao.dto.message.MessageResParamDTO;
import com.zhongjian.service.message.MessagePushService;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @Author: ldd
 */
@Service
public class MessagePushServiceImpl implements MessagePushService {

    @Resource
    private MessagePushProperties messagePushProperties;


    @Override
    public ResultDTO<MessageResDTO> messagePush(MessageReqDTO messageReqDTO) {
        ResultDTO<MessageResDTO> resultDTO = new ResultDTO<MessageResDTO>();
        resultDTO.setFlag(false);

        CloseableHttpClient httpClient = HttpClientUtil.getHttpClient();
        String url = messagePushProperties.getUrl();
        HttpPost httpPost = new HttpPost(url);

        String appKey = messagePushProperties.getAppKey();
        String appSecret = messagePushProperties.getAppSecret();
        String nonce = "12345";
        String curTime = String.valueOf((new Date()).getTime() / 1000L);
        String checkSum = CheckSumBuilderUtil.getCheckSum(appSecret, nonce, curTime);//参考 计算CheckSum的java代码
        // 设置请求的header
        httpPost.addHeader("AppKey", appKey);
        httpPost.addHeader("Nonce", nonce);
        httpPost.addHeader("CurTime", curTime);
        httpPost.addHeader("CheckSum", checkSum);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        try {
            // 设置请求的参数
            MessageBodyDTO messageBodyDTO = new MessageBodyDTO();
            messageBodyDTO.setMsg(messageReqDTO.getMsg());
            messageReqDTO.setBody(JSONObject.toJSONString(messageBodyDTO));
            HashMap<String, String> map = MapUtil.parseObjectToHashMap(messageReqDTO);
            List<NameValuePair> nameValuePairs = HttpClientUtil.paramsConverter(map);
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));
            // 执行请求
            HttpResponse response = httpClient.execute(httpPost);
            String message = EntityUtils.toString(response.getEntity(), "utf-8");
            MessageResParamDTO messageResParamDTO = JSONObject.parseObject(message, MessageResParamDTO.class);
            if(FinalDatas.NUMBER.equals(messageResParamDTO.getCode())){
                MessageResDTO messageResDTO = JSONObject.parseObject(messageResParamDTO.getData(),MessageResDTO.class);
                resultDTO.setStatusCode(messageResParamDTO.getCode());
                if(null!=messageResDTO){
                    resultDTO.setData(messageResDTO);
                    resultDTO.setFlag(true);
                    resultDTO.setErrorMessage(CommonMessageEnum.SUCCESS.getMsg());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultDTO;
    }
}