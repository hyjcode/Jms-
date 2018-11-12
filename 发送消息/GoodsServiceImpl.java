package cn.itcast.core.service;

@Service
@Transactional
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private JmsTemplate jmsTemplate;

    //定义发送到的目标对象
    @Autowired
    private ActiveMQTopic topicPageAndSolrDestination;

    @Autowired
    private ActiveMQQueue queueSolrDeleteDestination;

   
//点对点
    @Override
    public void delete(Long[] ids) {
        if (ids != null) {
            for(final Long id : ids){
                //将商品id作为消息发送给消息服务器
                jmsTemplate.send(queueSolrDeleteDestination, new MessageCreator() {
                    @Override
                    public Message createMessage(Session session) throws JMSException {
                        TextMessage textMessage = session.createTextMessage(String.valueOf(id));
                        return textMessage;
                    }
                });

            }
        }

    }
//订阅模式
    @Override
    public void updateStatus(Long[] ids, String status) {
        if (ids != null) {
            for (final Long id : ids) {
               
                // 将商品id作为消息发送给消息服务器
                if ("1".equals(status)) {
                    //发送, 第一个参数是发送到的目标队列, 第二个参数是发送的消息对象
                    jmsTemplate.send(topicPageAndSolrDestination, new MessageCreator() {
                        @Override
                        public Message createMessage(Session session) throws JMSException {
                            //创建文本的消息对象
                            TextMessage textMessage = session.createTextMessage(String.valueOf(id));
                            return textMessage;
                        }
                    });
                }
            }
        }
    }

}
