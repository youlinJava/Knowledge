package cn.wepact.dfm.controller;


import java.io.IOException;
import java.io.InputStream;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController 
@RequestMapping("/commonUtil")
public class CommonUtilContoller {
	 
    @Autowired
    ResourceLoader resourceLoader;
	
	@RequestMapping("/getCitiesJson")
	public Object getMyJson() throws IOException {
		Object o=null;
		try {
			log.info("获取城市列表json信息");
			o=getFileJson("static/data/cities.json");
			return o;
		}catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		log.info("获取城市列表json信息结束");
		return o;
	}
	

	public String getFileJson(String fileName) throws IOException {
		Resource resource = new ClassPathResource(fileName);
		InputStream is = resource.getInputStream();
	    byte[]  bytes= FileCopyUtils.copyToByteArray(is);
	    return new String(bytes);
	}
	

}
