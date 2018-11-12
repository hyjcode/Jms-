package cn.itcast.core.listener;

import cn.itcast.core.service.SolrManagerService;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * 监听器
 * 监听来自于消息服务器发送过来的消息, 也就是商品id
 * 根据商品id, 获取库存数据, 将数据放入solr索引库供前台系统搜索使用
 */
public class SolrManagerListener implements MessageListener{

    @Autowired
    private SolrManagerService solrManagerService;

    @Override
    public void onMessage(Message message) {
        //Message对象是JDK自带的, 从里面提取文本消息不容易, 所以强转成activeMq的文本消息对象
        ActiveMQTextMessage atm = (ActiveMQTextMessage)message;
        try {
            //获取文本消息, 也就是商品id
            String goodsId = atm.getText();
            //调用service方法, 根据商品ID获取商品的库存数据并放入solr索引库供前台系统搜索使用
            solrManagerService.addItemToSolr(Long.parseLong(goodsId));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
