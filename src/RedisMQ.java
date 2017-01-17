
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;                                                                                                         
import redis.clients.jedis.Jedis;                                                                                                                                     
import redis.clients.jedis.JedisPubSub;                                                                                                                                     
import redis.clients.jedis.JedisPoolConfig;                                                                                                                           
import redis.clients.jedis.JedisPool;
import org.apache.commons.lang3.RandomStringUtils;
import java.lang.InterruptedException;


public class RedisMQ
{
	public static void main(String args[]) throws Exception 
	{
		//redis_test();
		//try{
			redisMQTest();
		//}catch(InterruptedException e){
		//	e.printStackTrace();
		//}
	}

    public static void redis_test() 
    {
        JedisPool pool = new JedisPool(new JedisPoolConfig(), "127.0.0.1");
        Jedis jedis = null;      
        try {
          jedis = pool.getResource();

          jedis.set("foo0", "Hello World!");
          String foobar = jedis.get("foo0");
		  System.out.println(foobar);

          //jedis.zadd("sose", 0, "car"); jedis.zadd("sose", 0, "bike");
          //Set<String> sose = jedis.zrange("sose", 0, -1);
        } finally {
          if (jedis != null) {
            jedis.close();
          }
        }
        /// ... when closing your application:
        pool.destroy();
    }


	/**
	 * @param args
	 * @source from: http://shift-alt-ctrl.iteye.com/blog/1867454
	 */
	public static void redisMQTest() throws Exception {
		PubClient pubClient = new PubClient(Constants.host, Constants.port);
		final String channel = "pubsub-channel";
		pubClient.pub(channel, "before1");
		pubClient.pub(channel, "before2");
		Thread.sleep(2000);
		//消息订阅着非常特殊，需要独占链接，因此我们需要为它创建新的链接；
		//此外，jedis客户端的实现也保证了“链接独占”的特性，sub方法将一直阻塞，
		//直到调用listener.unsubscribe方法
		Thread subThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try{
					SubClient subClient = new SubClient(Constants.host, Constants.port);
					System.out.println("----------subscribe operation begin-------");
					JedisPubSub listener = new PrintListener();
					//在API级别，此处为轮询操作，直到unsubscribe调用，才会返回
					subClient.sub(listener, channel);
					System.out.println("----------subscribe operation end-------");
				}catch(Exception e){
					e.printStackTrace();
				}
				
			}
		});
		subThread.start();
		int i=0;
		while(i < 10){
			String message = RandomStringUtils.random(6, true, true);//apache-commons
			pubClient.pub(channel, message);
			i++;
			Thread.sleep(1000);
		}
		//被动关闭指示，如果通道中，消息发布者确定通道需要关闭，那么就发送一个“quit”
		//那么在listener.onMessage()中接收到“quit”时，其他订阅client将执行“unsubscribe”操作。
		pubClient.close(channel);
		//此外，你还可以这样取消订阅
		//listener.unsubscribe(channel);

	}

}
