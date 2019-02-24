package com.uiotsoft.micro.user.config;


import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.jms.Topic;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;

import com.uiotsoft.micro.user.common.mq.FastMessageConverter;
import com.uiotsoft.micro.user.common.mq.Serialize;


/**
 * Jms 配置类 
 * 监听方法参数 final Message<TestBean> message   (详情参照JMSTest)
 */
//@Configuration
public class JMSConfig  {
	

	///////////////////////////////////////////////////////连接工厂//////////////////////////////////////////////////////////////
		
/*	*//**
	* 
	* @param url
	* @return
	*//*
	@Bean
	public ConnectionFactory connectionFactory() {
		ActiveMQConnectionFactory amqFactory = new ActiveMQConnectionFactory();
		CachingConnectionFactory factory = new CachingConnectionFactory(amqFactory);
		factory.setSessionCacheSize(10);
		return factory;
	}
*/
	
	////////////////////////////////////////////////////////////////未来需要统一的推荐组合//////////////////////////////////////////////
	
	@Bean
	public MessageConverter fastMessageConverter(Serialize serialize){
		return new FastMessageConverter(serialize);
	}
	
	@Bean
    public DefaultJmsListenerContainerFactory fastJmsListenerContainerFactory(ConnectionFactory connectionFactory, MessageConverter fastMessageConverter) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setPubSubDomain(true);
        factory.setMessageConverter(fastMessageConverter);
        return factory;
    }
	
	@Bean
	public JmsTemplate fastJmsTemplate(ConnectionFactory connectionFactory , MessageConverter fastMessageConverter){
		JmsTemplate jmsTemplate=new JmsTemplate(connectionFactory);
		jmsTemplate.setMessageConverter(fastMessageConverter);
		return jmsTemplate;
	}
	
	//////////////////////////////////////////Example queue/////////////////////////////////////////////////

	@Bean
	public Queue queue() {
		return new ActiveMQQueue("sample.queue");
	}
	
	@Bean
	public Topic topic() {
		return new ActiveMQTopic("sample.topic");
	}

	

}

