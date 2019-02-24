package com.uiotsoft.micro.user.common.jms;

import java.io.Serializable;

import javax.jms.Queue;
import javax.jms.Topic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.uiotsoft.micro.user.UserServer;
import com.uiotsoft.micro.user.common.mq.Serialize;

import lombok.Data;

@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
@SpringBootTest (classes = {UserServer.class})
public class JMSTest {
	
    @Autowired
    private JmsTemplate fastJmsTemplate;

	@Autowired
	private Queue queue;
	

	@Autowired
	private Topic topic;
	

	@Test
	public void testQueue() {
		TestBean bean = new TestBean();
		TestBean bean1 = new TestBean();
		bean.setName("xufan");
		bean.setValue("aaaa");
		bean1.setName("xufan1");
		bean1.setValue("aaaa1");
		fastJmsTemplate.convertAndSend(this.queue, bean);
		fastJmsTemplate.convertAndSend(this.topic, bean1);

		
		
	}
	
	@Autowired
	private Serialize serialize;

	@JmsListener(destination = "sample.queue")
	public void receiveQueue(final Message<String> message) throws InterruptedException {
		Thread.sleep(1000);
		System.out.println("receiveQueue=" + serialize.deserializationObject(message.getPayload(), TestBean.class));
	}
	
	
	@JmsListener(destination = "sample.topic",containerFactory="fastJmsListenerContainerFactory")
    public void receiveTopic(final Message<String> message) throws InterruptedException {
		System.out.println("receiveTopic=" + serialize.deserializationObject(message.getPayload(), TestBean.class));
    }
	
	@JmsListener(destination = "sample.topic",containerFactory="fastJmsListenerContainerFactory")
    public void receiveTopic1(final Message<String> message) {
		System.out.println("receiveTopic=" + serialize.deserializationObject(message.getPayload(), TestBean.class));
    }
	
	
}



@Data
class TestBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3317925372592724744L;

	private String name;
	
	private String value;

	@Override
	public String toString() {
		return "TestBean [name=" + name + ", value=" + value + "]";
	}
	
	
	
	
}
