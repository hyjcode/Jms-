package cn.itcast.core.listener;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * 监听器
 * 监听来自于消息服务器发送来的消息, 也就是商品id, 根据商品id删除solr索引库中对应的数据, 完成下架操作
 */
public class SolrDeleteListener implements MessageListener {

    @Autowired
    private SolrTemplate solrTemplate;

    @Override
    public void onMessage(Message message) {
        ActiveMQTextMessage atm = (ActiveMQTextMessage)message;
        try {
            //获取消息, 商品id
            String goodsId = atm.getText();

            //根据商品id删除solr索引库中对应的数据完成商品下架操作
            Query query = new SimpleQuery();
            Criteria criteria = new Criteria("item_goodsid").is(goodsId);
            query.addCriteria(criteria);
            solrTemplate.delete(query);
            solrTemplate.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
