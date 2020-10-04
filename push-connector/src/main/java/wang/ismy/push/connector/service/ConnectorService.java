package wang.ismy.push.connector.service;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import wang.ismy.push.connector.ConnectorHandler;

import javax.annotation.PostConstruct;


/**
 * @author MY
 * @date 2020/6/16 20:10
 */
@Service
@Slf4j
public class ConnectorService {

    @Value("${connector.port}")
    private int port;

    @Autowired
    private ConnectorHandler connectorHandler;

    private ServerBootstrap serverBootstrap;

    @PostConstruct
    public void init(){
        new Thread(()->{
            try {
                init1();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void init1() throws InterruptedException {
        // 接收到来的连接
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // 处理已建立连接的流量
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // 复制启动服务器
            serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    // 使用 NioServerSocketChannel 将到来的连接实例化为Channel
                    .channel(NioServerSocketChannel.class)
                    // 指定处理器来处理 channel 与 channel 的事件
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(connectorHandler);
                        }
                    })
                    // 指定一些参数（针对到来的连接）
                    .option(ChannelOption.SO_BACKLOG, 128)
                    // 指定一些参数（针对channel）
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            // Bind and start to accept incoming connections.
            ChannelFuture f = serverBootstrap.bind(port).sync();
            log.info("监听{}端口",port);
            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
