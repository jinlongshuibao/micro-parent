package com.uiotsoft.micro.gateway.config;

import java.util.List;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RocketmqConfiguration {
	
	@Autowired
	private RocketmqProperties properties;
	
	@Bean
	public DefaultMQProducer accessTokenMQProducer() throws MQClientException {
		DefaultMQProducer producer = new DefaultMQProducer("P_ACCESS_TOKEN");
	    producer.setNamesrvAddr(properties.getNamesrvAddr());
	    producer.start();
	    return producer;
	}
	
	@Bean
	public DefaultMQPushConsumer accessTokenMQConsumer() throws MQClientException {
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("C_ACCESS_TOKEN_GATEWAY");
		consumer.setNamesrvAddr(properties.getNamesrvAddr());
		consumer.setConsumeMessageBatchMaxSize(10);
		consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
		consumer.subscribe("AccessTokenTopic", "*");
		consumer.registerMessageListener(new MessageListenerConcurrently() {
			@Override
			public ConsumeConcurrentlyStatus consumeMessage(
					List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
				 try {
	                    for (MessageExt messageExt : msgs) {


	                        String messageBody = new String(messageExt.getBody(), "utf-8");

	                        System.out.println("gateway 消费消息：Msg: " + messageExt.getMsgId() + ",msgBody: " + messageBody);//输出消息内容

	                    }
	                } catch (Exception e) {
	                    e.printStackTrace();
	                    return ConsumeConcurrentlyStatus.RECONSUME_LATER; //稍后再试
	                }
	                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS; //消费成功
			}


        });
        consumer.start();
	    return consumer;
	}

	@Bean
	public DefaultMQPushConsumer accessTokenMQConsumer1() throws MQClientException {
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("C_ACCESS_TOKEN_GATEWAY1");
		consumer.setNamesrvAddr(properties.getNamesrvAddr());
		consumer.setConsumeMessageBatchMaxSize(10);
		consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
		consumer.subscribe("AccessTokenTopic", "*");
		consumer.registerMessageListener(new MessageListenerConcurrently() {
			@Override
			public ConsumeConcurrentlyStatus consumeMessage(
					List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
				 try {
	                    for (MessageExt messageExt : msgs) {


	                        String messageBody = new String(messageExt.getBody(), "utf-8");

	                        System.out.println("gateway1 消费消息：Msg: " + messageExt.getMsgId() + ",msgBody: " + messageBody);//输出消息内容

	                    }
	                } catch (Exception e) {
	                    e.printStackTrace();
	                    return ConsumeConcurrentlyStatus.RECONSUME_LATER; //稍后再试
	                }
	                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS; //消费成功
			}


        });
        consumer.start();
	    return consumer;
	}
}
