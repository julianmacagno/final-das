package com.das.entities.core.config.context;

import org.modelmapper.AbstractConverter;
import org.modelmapper.AbstractProvider;
import org.modelmapper.Conditions;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.Provider;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
@EnableScheduling
@Scope("singleton")
public class BeanConfiguration {
    @Bean(name = "ModelMapperBean")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public ModelMapper create() {
        ModelMapper modelMapper = new ModelMapper();
        Provider<Date> localDateProvider = new AbstractProvider<Date>() {
            @Override
            public Date get() {
                return new Date();
            }
        };

        Converter<String, Date> toStringDate = new AbstractConverter<String, Date>() {
            @Override
            protected Date convert(String source) {
                try {
                    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(source);
                } catch (ParseException e) {
                    return null;
                }
            }
        };

        Converter<Date, String> toDateString = new AbstractConverter<Date, String>() {
            @Override
            protected String convert(Date source) {
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(source);
            }
        };

        modelMapper.createTypeMap(String.class, Date.class);
        modelMapper.createTypeMap(Date.class, String.class);
        modelMapper.addConverter(toStringDate);
        modelMapper.addConverter(toDateString);
        modelMapper.getTypeMap(String.class, Date.class).setProvider(localDateProvider);
        modelMapper.getConfiguration().setFieldMatchingEnabled(true);
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }
}
