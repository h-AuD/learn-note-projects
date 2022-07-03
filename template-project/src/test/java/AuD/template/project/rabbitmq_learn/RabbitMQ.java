package AuD.template.project.rabbitmq_learn;

import com.rabbitmq.client.ConnectionFactory;

/**
 * Description: TODO
 *
 * @author AuD/胡钊
 * @ClassName RabbitMQ
 * @date 2022/1/4 9:41
 * @Version 1.0
 */
public abstract class RabbitMQ {

    /** rabbitMQ连接信息 */
    private static final String host="127.0.0.1";
    private static final int port = 5672;
    private static final String virtualHost = "/";
    private static final String username="AuD";
    private static final String password="123456";

    protected ConnectionFactory connectionFactory = build(host, port, virtualHost, username, password);

    /**
     *
     * @param host rabbitmq安装的主机名(eg.IP 或 域名)
     * @param port 端口
     *             note -- rabbitmq安装有2个端口: server_port(5672)/rabbitmq服务端口 web_manage_plug_port(15672)/界面插件管理端口
     *             rabbitmq服务端口:
     * @param virtualHost   虚拟主机
     * @param username  连接用户名
     * @param password  连接密码
     * @return
     */
    private ConnectionFactory build(String host,int port,String virtualHost,String username,String password) {
        // new一个连接工厂 对象
        ConnectionFactory connectionFactory = new ConnectionFactory();
        // 设置IP,port,用户名,密码,虚拟主机
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setVirtualHost(virtualHost);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        // 创建rabbitmq连接
        // Connection connection = connectionFactory.newConnection();
        return connectionFactory;
    }

}
