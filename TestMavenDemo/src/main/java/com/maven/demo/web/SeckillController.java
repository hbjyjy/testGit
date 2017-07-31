package com.maven.demo.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.maven.demo.dto.Exposer;
import com.maven.demo.dto.SeckillExecution;
import com.maven.demo.dto.SeckillResult;
import com.maven.demo.entity.Seckill;
import com.maven.demo.enums.SeckillStatEnum;
import com.maven.demo.exception.RepeatKillException;
import com.maven.demo.exception.SeckillCloseException;
import com.maven.demo.service.SeckillService;
import com.maven.demo.util.ExcelUtil;

@Controller
@RequestMapping("/seckill")
public class SeckillController {
	private Logger logger = LoggerFactory.getLogger(SeckillController.class);
	
	@Autowired
	private SeckillService seckillService;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Model model) {
		List<Seckill> list = this.seckillService.getSeckillList();
		model.addAttribute("list", list);
		logger.info("进入到list页面！");
		return "list";
	}
	
	@RequestMapping(value = "/{seckillId}/detail", method = RequestMethod.GET)
	public String detail(@PathVariable("seckillId") Long seckillId, Model model) {
		if(seckillId == null) {
			return "redirect:/seckill/list";
		}
		Seckill seckill = this.seckillService.getById(seckillId);
		if(seckill == null) {
			return "redirect:/seckill/list";
		}
		model.addAttribute("seckill", seckill);
		return "detail";
	}
	
	//ajax json
	@RequestMapping(value = "/{seckillId}/exposer", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public SeckillResult<Exposer> exposer(@PathVariable("seckillId") Long seckillId) {
		SeckillResult<Exposer> result;
		try{
			Exposer exposer = this.seckillService.exportSeckillUrl(seckillId);
			result = new SeckillResult<Exposer>(true, exposer);
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			result = new SeckillResult<Exposer>(false, e.getMessage());
		}
		return result;
	}
	
	//ajax json
		@RequestMapping(value = "/{seckillId}/{md5}/execution", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
		@ResponseBody
		public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId, @PathVariable("md5") String md5, @CookieValue(value = "killPhone", required = false) Long userPhone) {
			if(userPhone == null) {
				return new SeckillResult<SeckillExecution>(false, "未注册");
			}
			SeckillResult<SeckillExecution> result;
			try{
				SeckillExecution seckillExecution = this.seckillService.executeSeckillProcedure(seckillId, userPhone, md5);
				result = new SeckillResult<SeckillExecution>(true, seckillExecution);
				return result;
			} catch(SeckillCloseException e1){
				SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStatEnum.END);
				return new SeckillResult<SeckillExecution>(true, seckillExecution);
			} catch(RepeatKillException e2){
				SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStatEnum.REPEAT_KILL);
				return new SeckillResult<SeckillExecution>(true, seckillExecution);
			} catch(Exception e) {
				logger.error(e.getMessage(), e);
				SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
				return new SeckillResult<SeckillExecution>(true, seckillExecution);
			}
		}
		
		@RequestMapping(value = "/time/now", method = RequestMethod.GET)
		@ResponseBody
		public SeckillResult<Long> time() {
			return new SeckillResult<Long>(true, new Date().getTime());
		}
		
		@RequestMapping(value = "/QueryData", method = RequestMethod.GET)
		@ResponseBody
		public Map<String, Object> QueryData(@RequestParam("sEcho") String sEcho, @RequestParam("iDisplayStart") int iDisplayStart,
				@RequestParam("iDisplayLength") int iDisplayLength, @RequestParam("killName") String killName) {
			List<Seckill> list = this.seckillService.getPageList(iDisplayStart, iDisplayLength, killName);
			long totalCount = this.seckillService.getTotalCount(killName);
			Map<String, Object> map = new HashMap<String, Object>();
	        map.put("aaData",list);
	        map.put("iTotalRecords", totalCount);
	        map.put("iTotalDisplayRecords", totalCount);
	        map.put("sEcho",sEcho);
			return map;
		}
		
		
		@RequestMapping(value = "/downloadExcel", method = RequestMethod.GET)
		public String download(HttpServletRequest request,HttpServletResponse response) throws IOException {
			String fileName="excel文件";
	        //填充projects数据
	        List<Seckill> projects=createData();
	        List<Map<String,Object>> list=createExcelRecord(projects);
	        String columnNames[]={"ID","名称","开始时间","结束时间","创建时间"};//列名
	        String keys[]    =     {"seckillId","name","startTime","endTime","createTime"};//map中的key
	        ByteArrayOutputStream os = new ByteArrayOutputStream();
	        try {
	            ExcelUtil.createWorkBook(list,keys,columnNames).write(os);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        byte[] content = os.toByteArray();
	        InputStream is = new ByteArrayInputStream(content);
	        // 设置response参数，可以打开下载页面
	        response.reset();
	        response.setContentType("application/vnd.ms-excel;charset=utf-8");
	        response.setHeader("Content-Disposition", "attachment;filename="+ new String((fileName + ".xls").getBytes(), "iso-8859-1"));
	        ServletOutputStream out = response.getOutputStream();
	        BufferedInputStream bis = null;
	        BufferedOutputStream bos = null;
	        try {
	            bis = new BufferedInputStream(is);
	            bos = new BufferedOutputStream(out);
	            byte[] buff = new byte[2048];
	            int bytesRead;
	            // Simple read/write loop.
	            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
	                bos.write(buff, 0, bytesRead);
	            }
	        } catch (final IOException e) {
	            throw e;
	        } finally {
	            if (bis != null)
	                bis.close();
	            if (bos != null)
	                bos.close();
	        }
	        return null;
		}
		
		private List<Seckill> createData() {
	        return this.seckillService.getSeckillList();
	    }
		
		private List<Map<String, Object>> createExcelRecord(List<Seckill> projects) {
	        List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();
	        Map<String, Object> map = new HashMap<String, Object>();
	        map.put("sheetName", "seckill");
	        listmap.add(map);
	        Seckill project=null;
	        for (int j = 0; j < projects.size(); j++) {
	            project=projects.get(j);
	            Map<String, Object> mapValue = new HashMap<String, Object>();
	            mapValue.put("seckillId", project.getSeckillId());
	            mapValue.put("name", project.getName());
	            mapValue.put("startTime", project.getStartTime());
	            mapValue.put("endTime", project.getEndTime());
	            mapValue.put("createTime", project.getCreateTime());
	            listmap.add(mapValue);
	        }
	        return listmap;
	    }
		
		@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
		@ResponseBody
		public String uploadFile(@RequestParam("uploadify") CommonsMultipartFile[] files,HttpServletRequest request) {
			String returnUrl = "";
			for(int i=0; i<files.length; i++) {
				if(!files[i].isEmpty()) {
					try {
						String filePath = request.getSession().getServletContext().getRealPath("/") + "resource" + File.separator + "uploadFile" + File.separator + files[i].getOriginalFilename();
						System.out.println(filePath);
						File file = new File(filePath);
						//File file = new File("C:\\Users\\IBM_ADMIN\\Desktop\\praise\\uploadFile\\" + new Date().getTime() + "-" + files[i].getOriginalFilename());
						FileOutputStream os = new FileOutputStream(file);
						FileInputStream fis = (FileInputStream) files[i].getInputStream();
						byte[] bytes = new byte[1024];
						int b;
						while((b = fis.read(bytes)) != -1) {
							os.write(bytes, 0, b);
						}
						os.flush();
						os.close();
						fis.close();
						returnUrl = "resource/uploadFile/" + files[i].getOriginalFilename();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
			}
			return returnUrl;
		}
	
}
