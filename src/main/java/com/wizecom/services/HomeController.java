package com.wizecom.services;

import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.*;
import java.net.*;
import com.mongodb.Mongo;
import com.wizecom.model.User;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired(required = false)
	MongoTemplate mongoTemplate;
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		
		logger.info("Welcome home! the client locale is "+ locale.toString());
				
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "home";
	}
	
	@RequestMapping(value = "/getAd", method=RequestMethod.GET)
	public HttpEntity<String> getAd(HttpEntity<byte[]> requestEntity) throws URISyntaxException {
		
		String cookie = requestEntity.getHeaders().getFirst("Cookie");
		logger.info(cookie);
		HttpHeaders responseHeaders = new HttpHeaders();
		if(cookie != null && !cookie.trim().isEmpty()) {//TODO: choose a random partner and send a redirect with userId info.
			responseHeaders.set("AdNwkId", "1234545454545");
			responseHeaders.setLocation(new URI("http://partner.cloudfoundry.com/getAd"));
			HttpEntity<String> response = new ResponseEntity<String>("Welcome Again!", responseHeaders, HttpStatus.MOVED_TEMPORARILY);
			try {
	            User user = mongoTemplate.findOne(new Query(Criteria.where("id").is(cookie.split("=")[1])), User.class);
	            user.setCounter(user.getCounter()+1);
	            mongoTemplate.save(user);
	            logger.info(mongoTemplate.findOne(new Query(Criteria.where("id").is(cookie.split("=")[1])), User.class).toString());
	        }
	        catch(Exception ex) {
	            logger.error(ex.getMessage());
	        }
			return response;
		} else {
			responseHeaders.set("Set-Cookie", "userId=1234545454545"); //TODO: use a cookie generator algorithm.
			responseHeaders.set("AdNwkId", "");
			HttpEntity<String> response = new ResponseEntity<String>("Welcome!", responseHeaders, HttpStatus.OK);
			try {
	            User user = new User();
	            user.setId("1234545454545");
	            user.setHeaders(requestEntity.getHeaders().toString());
	            user.setCounter(1);
	            mongoTemplate.insert(user);
	            logger.info(mongoTemplate.findOne(new Query(Criteria.where("id").is("1234545454545")), User.class).toString());
	        }
	        catch(Exception ex) {
	            logger.error(ex.getMessage());
	        }
			return response;
		}
	}
	
}
