import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.maven.demo.DAO.RedisDao;
import com.maven.demo.DAO.SeckillDao;
import com.maven.demo.entity.Seckill;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class RedisDaoTest {
	@Autowired
	private RedisDao redisDao;
	
	@Autowired
	private SeckillDao seckillDao;
	
	private long seckillId = 1000;
	
	@Test
	public void testGetSeckill() throws Exception {
		Seckill seckill = this.seckillDao.queryById(seckillId);
		String result = this.redisDao.putSeckill(seckill);
		System.out.println(result);
		Seckill sk = this.redisDao.getSeckill(seckillId);
		System.out.println(sk);
	}

}
