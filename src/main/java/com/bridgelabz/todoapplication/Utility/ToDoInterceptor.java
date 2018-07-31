package com.bridgelabz.todoapplication.Utility;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;

import com.bridgelabz.todoapplication.Utility.utilservice.RedisRepositoryImplementation;
import com.bridgelabz.todoapplication.tokenutility.TokenUtility;

/**
 * Purpose: 
 * @author: Sameer Saurabh
   @since:13-07-2018
   @version:1.0.0
 *
 */
@Component
public class ToDoInterceptor implements HandlerInterceptor {
	
	@Autowired
	TokenUtility tokenUtility;
	
	@Autowired
	private RedisRepositoryImplementation redis;

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.HandlerInterceptor#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) {
		System.out.println("In Interceptor");
		String tokenFromHeader= request.getHeader("JWTtoken");
		System.out.println("Token Found "+tokenFromHeader);
		
		String userId = tokenUtility.parseJWT(tokenFromHeader).getId();
		String tokenFromRedis = redis.getToken(userId);
		if (tokenFromRedis == null) {
			return false;
		}
		request.setAttribute("userId", userId);
		System.out.println();
		return true;

	}

}
