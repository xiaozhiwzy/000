package work.sajor.wechatpush.service;

import com.alibaba.fastjson.JSONObject;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import work.sajor.wechatpush.util.CaiHongPi;
import work.sajor.wechatpush.util.JiNianRi;

/**
 *@ClassName Pusher
 *@Description TODO
 * @Author zwy
 * @Date 2022/8/25
 */
@Service
public class Pusher {
    /**
     * 测试号的appId和secret
     */
    @Value("${wechat.appId}")
    private String appId;
    @Value("${wechat.secret}")
    private String secret;
    //模版id
    @Value("${wechat.templateId}")
    private String templateId;

    @Value("${tian.caihongpi.key}")
    private String key;

    /**
     * 恋爱
     */
    @Value("${target.lianAi}")
    private String lianAi;

    /**
     * 生日
     */
    @Value("${target.shengRi}")
    private String shengRi;


    @Autowired
    Tianqi tianqiService;

    public void push(String openId){
        //1，配置
        WxMpInMemoryConfigStorage wxStorage = new WxMpInMemoryConfigStorage();
        wxStorage.setAppId(appId);
        wxStorage.setSecret(secret);
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxStorage);
        //2,推送消息
        WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                .toUser(openId)
                .templateId(templateId)
                //.url("https://30paotui.com/")//点击模版消息要访问的网址
                .build();
        //3,如果是正式版发送模版消息，这里需要配置你的信息
        //        templateMessage.addData(new WxMpTemplateData("name", "value", "#FF00FF"));
        //                templateMessage.addData(new WxMpTemplateData(name2, value2, color2));
        //填写变量信息，比如天气之类的
        JSONObject todayWeather = tianqiService.getNanjiTianqi();
        templateMessage.addData(new WxMpTemplateData("riqi",todayWeather.getString("date") + "  "+ todayWeather.getString("week"),"#00BFFF"));
        templateMessage.addData(new WxMpTemplateData("tianqi",todayWeather.getString("text_day"),"#ffa631"));
        templateMessage.addData(new WxMpTemplateData("low",todayWeather.getString("low") + "","#173177"));
        templateMessage.addData(new WxMpTemplateData("high",todayWeather.getString("high")+ "","#FF6347" ));
        templateMessage.addData(new WxMpTemplateData("caihongpi", CaiHongPi.getCaiHongPi(key),"#FF69B4"));
        templateMessage.addData(new WxMpTemplateData("lianai", JiNianRi.getLianAi(lianAi)+"","#FF1493"));
        templateMessage.addData(new WxMpTemplateData("shengri",JiNianRi.getShengRi(shengRi)+"","#FFA500"));
        templateMessage.addData(new WxMpTemplateData("jinju",CaiHongPi.getJinJu()+"","#C71585"));

        String beizhu = "❤yj❤";

        if(JiNianRi.getLianAi(lianAi) % 365 == 0){
            beizhu = "今天是恋爱纪念日！";
        }

        templateMessage.addData(new WxMpTemplateData("beizhu",beizhu,"#44cef6"));


        try {
            System.out.println(templateMessage.toJson());
            System.out.println(wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage));
        } catch (Exception e) {
            System.out.println("推送失败：" + e.getMessage());
            e.printStackTrace();
        }
    }
}
