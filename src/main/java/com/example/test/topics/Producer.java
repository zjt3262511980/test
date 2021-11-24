package com.example.test.topics;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * Producer 简单队列生产者
 */
public class Producer {
    public static void main(String[] args) {

        // 1.创建连接工厂
        ConnectionFactory connectionFactory=new ConnectionFactory();
        // 2.设置连接属性
        connectionFactory.setHost("8.142.36.154");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        connectionFactory.setVirtualHost("/");

        Connection connection=null;
        Channel channel=null;
        try {
            // 3: 从连接工厂中获取连接
             connection=connectionFactory.newConnection("生产者");
            // 4: 从连接中获取通道channel
            channel=connection.createChannel();
            //5.准备消息内容
            String msg="hello 我是消息，123456";
            //6.准备交换机
            String exchangename="topic_exchange";
            //7.定义路由key
            String routkey="com.course.user";
            //8.指定交换机的类型
            String tyep="topics";

            //发送消息
            channel.basicPublish(exchangename,routkey,null,msg.getBytes());
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
