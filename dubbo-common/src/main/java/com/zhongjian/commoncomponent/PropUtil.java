package com.zhongjian.commoncomponent;

import javax.annotation.PostConstruct;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


@Component
@Data
public class PropUtil {

	@Autowired
	private PropComponent propComponent;
	
	@PostConstruct
	private void setProp() {
		setYxUrl((String)propComponent.getMap().get("user.yx.msgurl"));
		setYxAccid((String)propComponent.getMap().get("user.yx.accid"));
		setYxAppKey((String)propComponent.getMap().get("user.yx.appkey"));
		setYxAppSecret((String)propComponent.getMap().get("user.yx.appsecret"));
		
		setAliAppId((String)propComponent.getMap().get("pay.ali.appid"));
		setAliBusinessId((String)propComponent.getMap().get("pay.ali.businessid"));
		setAliRSAPrivateKey((String)propComponent.getMap().get("pay.ali.rsaprivatekey"));
		setAliPayPublicKey((String)propComponent.getMap().get("pay.ali.paypublickey"));
		setAliNotifyUrl((String)propComponent.getMap().get("pay.ali.notifyurl"));
		setAliUrl((String)propComponent.getMap().get("pay.ali.url"));
		setAliCharset((String)propComponent.getMap().get("pay.ali.charset"));
		setAliFormat((String)propComponent.getMap().get("pay.ali.format"));
		setAliSignType((String)propComponent.getMap().get("pay.ali.signtype"));

		setWxAppUrl((String)propComponent.getMap().get("pay.wxApp.url"));
		setWxAppAppId((String)propComponent.getMap().get("pay.wxApp.appId"));
		setWxAppMchId((String)propComponent.getMap().get("pay.wxApp.mchId"));
		setWxAppNotifyUrl((String)propComponent.getMap().get("pay.wxApp.notifyUrl"));
		setWxAppKey((String)propComponent.getMap().get("pay.wxApp.key"));

		setWxAppLetsId((String)propComponent.getMap().get("pay.wxApp.appletsId"));
		setWxAppLetsKey((String)propComponent.getMap().get("pay.wxApp.appletsKey"));
		setWxAppletsNotifyUrl((String)propComponent.getMap().get("pay.wxApp.appletsNotifyUrl"));
	}

    private String yxUrl;
    private String yxAppKey;
    private String yxAppSecret;
    private String yxAccid;
    
    private String aliAppId;
    private String aliBusinessId;
    private String aliRSAPrivateKey;
    private String aliPayPublicKey;
    private String aliNotifyUrl;
    private String aliUrl;
    private String aliCharset;
    private String aliFormat;
    private String aliSignType;

	private String wxAppUrl;
	private String wxAppNotifyUrl;
	private String wxAppAppId;
	private String wxAppMchId;
	private String wxAppKey;

	private String wxAppLetsId;
	private String wxAppLetsKey;
	private String WxAppletsNotifyUrl;

}
