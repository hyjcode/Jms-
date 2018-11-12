package cn.itcast.core.listener;

import cn.itcast.core.service.CmsService;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.Map;

/**
 * 自定义监听器
 * 监听来自于消息服务器发送来的消息也就是商品id
 * 根据商品id获取商品, 商品详情, 库存, 分类数据, 根据模板和数据生成静态化页面
 */
public class PageListener implements MessageListener {

    @Autowired
    private CmsService cmsService;

    @Override
    public void onMessage(Message message) {
        ActiveMQTextMessage atm = (ActiveMQTextMessage)message;

        try {
            //获取消息, 也就是商品id
            String goodsId = atm.getText();
            //根据商品id获取生成静态页面所需要的所有数据
            Map<String, Object> rootMap = cmsService.findGoodsData(Long.parseLong(goodsId));
            //生成静态页面
            cmsService.createPage(rootMap, Long.parseLong(goodsId));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
