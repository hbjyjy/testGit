import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.maven.demo.dto.Exposer;
import com.maven.demo.dto.SeckillExecution;
import com.maven.demo.entity.Seckill;
import com.maven.demo.service.SeckillService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml", "classpath:spring/spring-service.xml"})
public class SeckillServiceTest {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SeckillService seckillService;
	
	@Test
	public void testGetSeckillList() throws Exception {
		List<Seckill> list = this.seckillService.getSeckillList();
		logger.info("list={}" + list);
	}
	
	@Test
	public void testGetById() throws Exception {
		Seckill seckill = this.seckillService.getById(1000);
		logger.info("seckill={}" + seckill);
	}
	
	@Test
	public void testExportSeckillUrl() throws Exception {
		Exposer exposer = this.seckillService.exportSeckillUrl(1000);
		logger.info("exposer={}" + exposer.getMd5());
	}
	
	@Test
	public void testExecuteSeckill() throws Exception {
		SeckillExecution seckillExecution = this.seckillService.executeSeckill(1000, 13456787877L, "509fecdf3cf3a157375e7065f42ea7bb");
		logger.info("seckillExecution={}" + seckillExecution.getSuccessKilled().getUserPhone());
	}

}
