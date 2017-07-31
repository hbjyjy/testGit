import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.maven.demo.DAO.SeckillDao;
import com.maven.demo.entity.Seckill;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {
	@Resource
	private SeckillDao seckillDao;
	@Test
	public void testReduceNumber() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse("2016-02-02");
		int ct = seckillDao.reduceNumber(1000, date);
		System.out.println(ct);
	}
	@Test
	public void testQueryById() throws Exception {
		long id = 1000;
		Seckill seckill = seckillDao.queryById(id);
		System.out.println(seckill.getName());
	}
	@Test
	public void testQueryAll() throws Exception {
		List<Seckill> list = seckillDao.queryAll(0, 3);
		for(Seckill seckill : list) {
			System.out.println(seckill.getName());
		}
	}

}
