package com.example.test.direct;

import com.rabbitmq.client.*;

import java.io.IOException;

public class Consumer {
    private static Runnable runnable=()->{
       final String name=Thread.currentThread().getName();
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
            //接受消息,1.从那个队列接受消息
            channel.basicConsume(name, true, new DeliverCallback() {
                @Override
                public void handle(String s, Delivery delivery) throws IOException {
                    System.out.println("收到"+name+"消息：" + new String(delivery.getBody(), "UTF-8"));
                }
            }, new CancelCallback() {
                @Override
                public void handle(String s) throws IOException {
                    System.out.println("接受消息失败");
                }
            });
            System.out.println("开始接受消息");
            // System.in.read();//可以让程序不停继续接受消息
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
    };
    public static void main(String[] args) {
        // 启动三个线程去执行
        new Thread(runnable, "queue1").start();
        new Thread(runnable, "queue2").start();
        new Thread(runnable, "queue3").start();
    }
}
