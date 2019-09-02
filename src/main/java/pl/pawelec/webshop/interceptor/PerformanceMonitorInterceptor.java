/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class PerformanceMonitorInterceptor implements HandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(PerformanceMonitorInterceptor.class.getName());
    ThreadLocal<StopWatch> localStopWatch = new ThreadLocal<StopWatch>();

    public boolean preHandle(HttpServletRequest request, HttpServletResponse hsr1, Object handler) throws Exception {
        StopWatch stopWatch = new StopWatch(handler.toString());
        stopWatch.start(handler.toString());
        localStopWatch.set(stopWatch);
        LOGGER.info(" ==================================================== START ===================== ");
        LOGGER.info("the current request path: " + getUrlPath(request));
        LOGGER.info("start time: " + getCurrentTime());
        return true;
    }


    public void postHandle(HttpServletRequest hsr, HttpServletResponse hsr1, Object o, ModelAndView mav) throws Exception {
        LOGGER.info("end time: " + getCurrentTime());
    }


    public void afterCompletion(HttpServletRequest hsr, HttpServletResponse hsr1, Object o, Exception excptn) throws Exception {
        StopWatch stopWatch = localStopWatch.get();
        stopWatch.stop();
        LOGGER.info("the total time duration of process: " + stopWatch.getTotalTimeMillis() + " ms");
        localStopWatch.set(null);
        LOGGER.info(" ==================================================== END ======================= ");

    }

    private String getUrlPath(HttpServletRequest request) {
        String currentPath = request.getRequestURI();
        String requestParameters = request.getQueryString();
        requestParameters = requestParameters == null ? "" : "?" + requestParameters;
        return currentPath + requestParameters;
    }

    private String getCurrentTime() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime curDateTime = LocalDateTime.now();
        return dateTimeFormatter.format(curDateTime);
    }

}
