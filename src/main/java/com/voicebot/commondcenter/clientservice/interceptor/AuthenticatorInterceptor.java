package com.voicebot.commondcenter.clientservice.interceptor;

import com.voicebot.commondcenter.clientservice.entity.Application;
import com.voicebot.commondcenter.clientservice.entity.Authentication;
import com.voicebot.commondcenter.clientservice.entity.Client;
import com.voicebot.commondcenter.clientservice.service.AuthenticationService;
import com.voicebot.commondcenter.clientservice.service.ClientService;
import com.voicebot.commondcenter.clientservice.service.impl.LoggingServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.util.Date;
import java.util.Optional;

@Component
public class AuthenticatorInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticatorInterceptor.class);


    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    ClientService clientService;

    @Autowired
    private LoggingServiceImpl loggingService;
    /**
     * This is not a good practice to use sysout. Always integrate any logger with
     * your application. We will discuss about integrating logger with spring boot
     * application in some later article
     */
    @SuppressWarnings("unused")
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
        if (logger.isDebugEnabled())
            logger.debug("In preHandle we are Intercepting the Request");
        if (logger.isDebugEnabled())
            logger.debug("---------------------------------------------");
        String requestURI = request.getRequestURI();

        /*****************************
         * URL NOT CHECK AUTHENTICATION
         *************************************/

        System.out.println(request.getRequestURI());

        if (request.getRequestURI().contains("swagger")) {
            return true;
        }
        if (request.getRequestURI().contains("/api-docs")) {
            return true;
        }

        if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
            return true;
        }
        if (request.getMethod().equalsIgnoreCase("POST") && request.getRequestURI().contains("/register")) {
            return true;
        }
        if (request.getMethod().equalsIgnoreCase("POST") && request.getRequestURI().contains("/login")) {
            return true;
        }

        if (request.getRequestURI().contains("/error")) {
            return true;
        }

        /*************************************
         * CHECK HEADER
         *************************************/

        String accessToken = request.getHeader("X-AUTH-LOG-HEADER");
        if (accessToken == null || accessToken.isEmpty()) {
            response.getWriter().write("Authentication failed.");
            response.setStatus(401);
            return false;
        }

        /**************************************
         * CHECK AUTHENTICTION
         *************************************/
        HttpSession session = request.getSession();
        Boolean isValid = false;// service.isValidUser(accessToken);

        Optional<Authentication> authentication
                = authenticationService.authenticate(accessToken);

        /*************************************
         * CHECK SESSION TIME OUT *
         *************************************/
        if (authentication.isPresent()) {
            Date lastLogin = authentication.get().getLastLogin();
            long lastLoginInMills = lastLogin.getTime();
            long milliseconds = 24 * 60 * 60 * 1000;
            if (System.currentTimeMillis() > (lastLoginInMills + milliseconds)) {
                response.getWriter().write("Session expire. Please login again");
                response.setStatus(401);
                return false;
            } else {
                isValid = true;
                Optional<Client> client = clientService.findOne(authentication.get().getEntityId());
                request.setAttribute("userId", authentication.get().getUserName());
                request.setAttribute("name", authentication.get().getName());
                request.setAttribute("id",authentication.get().getEntityId());
                request.setAttribute("type",authentication.get().getUserType());
            }
        }

        if (logger.isDebugEnabled())
            logger.debug("isValid :" + (isValid == null));
        if (!isValid) {
            response.getWriter().write("Authentication failed.");
            response.setStatus(401);
            return false;
        }
        // if(logger.isDebugEnabled()) logger.debug(accessToken);
        if (logger.isDebugEnabled())
            logger.debug("RequestURI::" + requestURI + " || Search for Person with personId ::" + authentication.get().getUserName());
        if (logger.isDebugEnabled())
            logger.debug("---------------------------------------------");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object object, ModelAndView model)
            throws Exception {

        ContentCachingResponseWrapper responseCacheWrapperObject = new ContentCachingResponseWrapper(response);
        byte[] responseArray = responseCacheWrapperObject.getContentAsByteArray();
        String responseStr = new String(responseArray, responseCacheWrapperObject.getCharacterEncoding());


        loggingService.displayResp(request, response, responseStr);
        logger.debug("---------------------------------------------");
        logger.debug("In postHandle request processing " + "completed by @RestController");
        logger.debug("---------------------------------------------");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception arg3)
            throws Exception {
        if (logger.isDebugEnabled())
            logger.debug("---------------------------------------------");
        if (logger.isDebugEnabled())
            logger.debug("In afterCompletion Request Completed");
        if (logger.isDebugEnabled())
            logger.debug("---------------------------------------------");
    }
}
