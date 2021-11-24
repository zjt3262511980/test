package com.example.test.all;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Producer {//完整的声明创建：交换机
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
            String exchangename="direct_message_exchange";
            //7.定义路由key
            String routkey="";
            //8.指定交换机的类型,direct,topic,fanout,headers
            String tyep="direct";
            //创建交换机
            channel.exchangeDeclare(exchangename,tyep,true);//@params1:交换机名称，@params2：交换机类型，@params3：交换机是否持久化
            //声明队列
            channel.queueDeclare("queue5",true,false,false,null);
            channel.queueDeclare("queue6",true,false,false,null);
            channel.queueDeclare("queue7",true,false,false,null);
            //邦定队列和交换机的关系
            channel.queueBind("queue5",exchangename,"order");
            channel.queueBind("queue6",exchangename,"order");
            channel.queueBind("queue7",exchangename,"course");

            routkey="order";
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
