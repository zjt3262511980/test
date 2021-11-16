package com.example.test.simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * Producer 简单队列生产者
 */
public class Producer {
    public static void main(String[] args) {
    //所以中间件技术都是基于tcp/ip协议基础上构建新型的协议规范，只不过rabbitmq遵循的是amqp
    //ip port
        // 创建连接工厂
        ConnectionFactory connectionFactory=new ConnectionFactory();
        // 设置连接属性
        connectionFactory.setHost("8.142.36.154");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        connectionFactory.setVirtualHost("/");

        Connection connection=null;
        Channel channel=null;
        try {
            //创建连接对象
             connection=connectionFactory.newConnection("生产者");
             //通过连接获取通道
            channel=connection.createChannel();
            // 申明队列queue存储消息
            /*
             *  如果队列不存在，则会创建
             *  Rabbitmq不允许创建两个相同的队列名称，否则会报错。
             *
             *  @params1： queue 队列的名称
             *  @params2： durable 队列是否持久化
             *  @params3： exclusive 是否排他，即是否私有的，如果为true,会对当前队列加锁，其他的通道不能访问，并且连接自动关闭
             *  @params4： autoDelete 是否自动删除，当最后一个消费者断开连接之后是否自动删除消息。
             *  @params5： arguments 可以设置队列附加参数，设置队列的有效期，消息的最大长度，队列的消息生命周期等等。
             * */
            String queuename="queue1";
            channel.queueDeclare(queuename,false,false,false,null);
            //准备消息内容
            String msg="hello 我是消息，123456";
            //发送消息
            channel.basicPublish("",queuename,null,msg.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //关闭通道
            if(channel!=null&&channel.isOpen()){
                try {
                    channel.close();
                }  catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //关闭连接
            if(connection!=null&&connection.isOpen()){
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
