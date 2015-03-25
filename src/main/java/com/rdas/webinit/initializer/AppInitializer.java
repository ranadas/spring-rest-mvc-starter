package com.rdas.webinit.initializer;

import java.util.List;
import java.util.Set;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.rdas.config.AppConfig;

/**
 * Notes on Java con
 *  This is the most important config class here.
 *
 *  Servlet 3.0 introduces a ServletContainerInitializer interface whose implementing classes are notified by the
 *  container about webapp startup events. Spring 3.1 uses this in its WebApplicationInitializer interface,
 *  which is the hook through which you can set up ApplicationContext without using the web.xml
 *
 */
@EnableWebMvc
@Configuration
// @PropertySource({"classpath:config.properties"})
// @Import({ AppConfig.class})
// @ComponentScan(basePackages = "com.rdas.webappinit")
public class AppInitializer extends WebMvcConfigurerAdapter implements WebApplicationInitializer {

    private static final transient Logger logger = LoggerFactory.getLogger(AppInitializer.class);

    private static final String CONFIG_LOCATION = "com.rdas";
    private static final String MAPPING_URL = "/";

    @Autowired
    private MessageSource messageSource;

    // onStartup is overriding from WebApplicationInitializer. This is basically the DispatcherServlet part of web xml.

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        // Create the 'root' Spring application context
        WebApplicationContext context = getApplicationContext();
        // context.register(ApplicationConfig.class, WebMvcConfig.class);

        // Manage the lifecycle of the root application context
        servletContext.addListener(new ContextLoaderListener(context));

        // Register Encoding Filter
        addEncodingFilter(servletContext);

        // Register Logging Filter
        addLoggingFilter(servletContext);

        // Register and map the dispatcher servlet
        addServiceDispatcherServlet(servletContext, context);
    }

    @Bean
    public ViewResolver pageViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/jsp/");
        resolver.setSuffix(".jsp");
        return resolver;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**").addResourceLocations("/css/");
        registry.addResourceHandler("/js/**").addResourceLocations("/js/");
        registry.addResourceHandler("/image/**").addResourceLocations("/image/");
    }

    // following overriding from WebMvcConfigurerAdapter

    /**
     * to enable forwarding to the “default” Servlet. The “default” Serlvet is used to handle static
     * content such as CSS, HTML and images.
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.getObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
        converters.add(converter);
    }

    @Bean
    public LocalValidatorFactoryBean validator() {
        LocalValidatorFactoryBean validatorFactoryBean = new LocalValidatorFactoryBean();
        validatorFactoryBean.setValidationMessageSource(messageSource);
        return validatorFactoryBean;
    }

    @Override
    public Validator getValidator() {
        return validator();
    }

    // all private methods used

    /**
     * The trick to get rid of applicationContext.xml files is achieved by using AnnotationConfigWebApplicationContext
     * instead of XmlWebApplicationContext.
     */
    private AnnotationConfigWebApplicationContext getApplicationContext() {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.setConfigLocation(CONFIG_LOCATION);
        context.register(AppConfig.class);
        return context;
    }

    private void addServiceDispatcherServlet(ServletContext servletContext, WebApplicationContext rootContext) {

        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("servicesDispatcher", new DispatcherServlet(rootContext));
        dispatcher.setLoadOnStartup(1);
        Set<String> mappingConflicts = dispatcher.addMapping(MAPPING_URL);

        if (!mappingConflicts.isEmpty()) {
            for (String s : mappingConflicts) {
                logger.error("Mapping conflict: " + s);
            }
            throw new IllegalStateException("'ServicesDispatcher' could not be mapped to '" + MAPPING_URL + "'");
        }
    }

    private void addEncodingFilter(ServletContext container) {
        FilterRegistration.Dynamic filterRegistrationDynamic = container.addFilter("encodingFilter", new CharacterEncodingFilter());
        filterRegistrationDynamic.setInitParameter("encoding", "UTF-8");
        filterRegistrationDynamic.setInitParameter("forceEncoding", "true");
        filterRegistrationDynamic.addMappingForUrlPatterns(null, true, "/*");
    }

    private void addLoggingFilter(ServletContext container) {
        FilterRegistration.Dynamic filterRegistrationDynamic = container.addFilter("loggingFilter", new CommonsRequestLoggingFilter());
        filterRegistrationDynamic.addMappingForUrlPatterns(null, true, "/*");
    }
}
