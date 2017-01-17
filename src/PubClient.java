import org.apache.commons.pool2.impl.GenericObjectPoolConfig;                                                                                                         
import redis.clients.jedis.Jedis;                                                                                                                                     
import redis.clients.jedis.JedisPoolConfig;                                                                                                                           
import redis.clients.jedis.JedisPool;

public class PubClient {

	private Jedis jedis;//
	public PubClient(String host,int port)
	{
		jedis = new Jedis(host,port);
	}
	
	public void pub(String channel,String message)
	{
		jedis.publish(channel, message);
	}
	
	public void close(String channel)
	{
		jedis.publish(channel, "quit");
		jedis.del(channel);//
	}

}
