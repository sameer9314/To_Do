package com.bridgelabz.todoapplication.configuration;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Purpose : Class to provide the configuration for RabbitMQConfig.
 * 
 * @author Sameer Saurabh
 * @version 1.0
 * @Since 20/07/2018
 */
@Configuration
public class RabbitMQConfig {

	@Value("")
	String queueName;

	@Value("")
	String exchange;

	@Value("")
	private String routingkey;

	/**
	 * Simple container collecting information to describe a queue. 
	 * Used in conjunction with AmqpAdmin.
	 * 
	 * @return Queue
	 */
	@Bean
	Queue queue() {
		return new Queue(queueName, false);
	}

	/**
	 * Simple container collecting information to describe a direct exchange.
	 * SUsed in conjunction with administrative operations.
	 * 
	 * @return DirectExchange
	 */
	@Bean
	DirectExchange exchange() {
		return new DirectExchange(exchange);
	}

	/**
	 * Simple container collecting information to describe a binding. 
	 * STakes String destination and exchange names as arguments to facilitate wiring using code based configuration. 
	 * Can be used in conjunction with AmqpAdmin, or created via a BindingBuilder.
	 * 
	 * @param queue
	 * @param exchange
	 * @return Binding
	 */
	@Bean
	Binding binding(Queue queue, DirectExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(routingkey);
	}

	/**
	 * To convert the message.
	 * 
	 * @return Jackson2JsonMessageConverter
	 */
	@Bean
	public MessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	/**
	 * Specifies a basic set of AMQP operations. 
	 * Provides synchronous send and receive methods. 
	 * The convertAndSend(Object) and receiveAndConvert() methods allow let you send and receive POJO objects. 
	 * Implementations are expected to delegate to an instance of MessageConverter 
	 * to perform conversion to and from AMQP byte[] payload type.
	 * 
	 * @param connectionFactory
	 * @return RabbitTemplate
	 */
	@Bean
	public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(jsonMessageConverter());
		return rabbitTemplate;
	}

	/**
	 * A RabbitListenerContainerFactory implementation to build a regular SimpleMessageListenerContainer.
	 * 
	 * @param connectionFactory
	 * @param configurer
	 * @return SimpleRabbitListenerContainerFactory
	 */
	@Bean
	public SimpleRabbitListenerContainerFactory jsaFactory(ConnectionFactory connectionFactory,
			SimpleRabbitListenerContainerFactoryConfigurer configurer) {
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		configurer.configure(factory, connectionFactory);
		factory.setMessageConverter(jsonMessageConverter());
		return factory;
	}
}