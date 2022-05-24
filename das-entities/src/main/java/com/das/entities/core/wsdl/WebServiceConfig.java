package com.das.entities.core.wsdl;

import com.das.entities.core.config.constant.Routes;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {
    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> soapMessageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<>(servlet, "/soap/*", "/soap_auth/*");
    }

    @Bean(name = "soap_auth")
    public DefaultWsdl11Definition soapAuthDefinition(XsdSchema soapAuthSchema) {
        DefaultWsdl11Definition serviceDef = new DefaultWsdl11Definition();
        serviceDef.setPortTypeName("SoapAuthPort");
        serviceDef.setLocationUri(Routes.SoapAuth.SOAP_AUTH);
        serviceDef.setTargetNamespace(Routes.SoapAuth.NAMESPACE_URI);
        serviceDef.setSchema(soapAuthSchema);
        return serviceDef;
    }

    @Bean(name = "soap")
    public DefaultWsdl11Definition soapDefinition(XsdSchema soapSchema) {
        DefaultWsdl11Definition serviceDef = new DefaultWsdl11Definition();
        serviceDef.setPortTypeName("AuthPort");
        serviceDef.setLocationUri(Routes.Soap.SOAP);
        serviceDef.setTargetNamespace(Routes.Soap.NAMESPACE_URI);
        serviceDef.setSchema(soapSchema);
        return serviceDef;
    }

    @Bean
    public XsdSchema soapAuthSchema() {
        return new SimpleXsdSchema(new ClassPathResource("schemas/soap_auth.xsd"));
    }

    @Bean
    public XsdSchema soapSchema() {
        return new SimpleXsdSchema(new ClassPathResource("schemas/soap.xsd"));
    }
}
