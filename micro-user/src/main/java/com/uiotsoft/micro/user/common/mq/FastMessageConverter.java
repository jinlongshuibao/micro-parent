package com.uiotsoft.micro.user.common.mq;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;

public class FastMessageConverter implements MessageConverter {
	
	private Serialize serialize;
	
	public FastMessageConverter(Serialize serialize) {
		this.serialize = serialize;
	}
	
	@Override
	public javax.jms.Message toMessage(Object object, Session session) throws JMSException, MessageConversionException {
		return session.createTextMessage(serialize.serializationObject(object));
	}
	
	@Override
	public Object fromMessage(javax.jms.Message message) throws JMSException, MessageConversionException {
		TextMessage textMessage = (TextMessage) message;  
		return textMessage.getText();
	}

}
