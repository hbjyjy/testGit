import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.maven.demo.DAO.SuccessKilledDao;
import com.maven.demo.entity.SuccessKilled;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDaoTest {
	
	@Resource
	public SuccessKilledDao successKilledDao;
	
	@Test
	public void testInsertSuccessKilled() {
		successKilledDao.insertSuccessKilled(1000, 13487678988L);
	}
	
	@Test
	public void testQueryByIdWithSeckill() {
		SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(1000, 15099988899L);
		System.out.println(successKilled.getUserPhone());
		System.out.println(successKilled.getSeckill().getName());
	}
	
	
	
}