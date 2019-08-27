package pl.pawelec.webshop.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.format.FormatterRegistry;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.util.UrlPathHelper;
import pl.pawelec.webshop.converter.CustomerIdToCustomerConverter;
import pl.pawelec.webshop.converter.DeliveryIdToDeliveryConverter;
import pl.pawelec.webshop.converter.ProductIdToProductConverter;
import pl.pawelec.webshop.converter.RepositoryConverter;
import pl.pawelec.webshop.interceptor.PerformanceMonitorInterceptor;

import java.util.Locale;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        UrlPathHelper urlPathHelper = new UrlPathHelper();
        urlPathHelper.setRemoveSemicolonContent(false);
        configurer.setUrlPathHelper(urlPathHelper);
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public LocalValidatorFactoryBean validator(MessageSource messageSource) {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource);
        return bean;
    }

    @Bean
    public LocaleResolver localeResolver(){
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(Locale.getDefault());
        return  localeResolver;
    }

    @Bean
    public PerformanceMonitorInterceptor performanceMonitorInterceptor() {
        return new PerformanceMonitorInterceptor();
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("language");
        return localeChangeInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(performanceMonitorInterceptor());
        registry.addInterceptor(localeChangeInterceptor());
    }

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipart = new CommonsMultipartResolver();
        multipart.setMaxUploadSize(20 * 1024 * 1024);
        return multipart;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new RepositoryConverter());
        registry.addConverter(new ProductIdToProductConverter());
        registry.addConverter(new DeliveryIdToDeliveryConverter());
        registry.addConverter(new CustomerIdToCustomerConverter());
    }
}
