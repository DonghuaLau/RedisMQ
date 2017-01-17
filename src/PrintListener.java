import org.apache.commons.pool2.impl.GenericObjectPoolConfig;                                                                                                         
import redis.clients.jedis.JedisPubSub;                                                                                                                                     
import java.util.Date;
import org.apache.commons.lang3.time.DateFormatUtils;

public class PrintListener extends JedisPubSub{

	@Override
	public void onMessage(String channel, String message) {
		String time = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
		System.out.println("message receive:" + message + ",channel:" + channel + "..." + time);
		//此处我们可以取消订阅
		if(message.equalsIgnoreCase("quit")){
			this.unsubscribe(channel);
		}
	}
}

