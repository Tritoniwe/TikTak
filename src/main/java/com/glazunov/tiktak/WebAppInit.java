package com.glazunov.tiktak;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate4.support.OpenSessionInViewFilter;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.http.HttpSessionListener;

public class WebAppInit implements WebApplicationInitializer {
    private static final Logger logger = LoggerFactory.getLogger(WebAppInit.class);

    public void onStartup(ServletContext servletContext) throws ServletException {

        AnnotationConfigWebApplicationContext mvcContext = new AnnotationConfigWebApplicationContext();
        mvcContext.register(MvcConfiguration.class);

        FilterRegistration.Dynamic hibernateFilter = servletContext.addFilter("hibernateFilter", new OpenSessionInViewFilter());
        hibernateFilter.setInitParameter("sessionFactoryBeanName", "sessionFactory");
        hibernateFilter.addMappingForUrlPatterns(null, true, "/*");

        // Manage the lifecycle of the root application context
        servletContext.addListener(new ContextLoaderListener(mvcContext));

        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("DispatcherServlet", new DispatcherServlet(mvcContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

    }
}
