package com.das.rescueapp.core.config.context;

import com.das.rescueapp.commons.enums.MessageType;
import com.das.rescueapp.core.utils.DateUtils;
import org.modelmapper.AbstractProvider;
import org.modelmapper.Conditions;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.Provider;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Date;
import java.util.function.Supplier;

@Component
public class Mapper {
    Provider<Date> localDateProvider = new AbstractProvider<Date>() {
        @Override
        public Date get() {
            return new Date();
        }
    };
    @Qualifier(value = "ModelMapperBean")
    private final ModelMapper modelMapper;

    public Mapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.addConverters();
    }

    public Object map(Object source, Class<?> destination) {
        if (source == null) return null;
        TypeMap typeMap = modelMapper.getTypeMap(source.getClass(), destination);

        if (typeMap == null) {
            modelMapper.createTypeMap(source.getClass(), destination)
                    .setPropertyCondition(Conditions.isNotNull());
        }
        return this.modelMapper.map(source, destination);

    }

    public void map(Object source, Object destination) {
        TypeMap typeMap = modelMapper.getTypeMap(source.getClass(), destination.getClass());

        if (typeMap == null) {
            modelMapper.createTypeMap(source.getClass(), destination.getClass())
                    .setPropertyCondition(Conditions.isNotNull());
        }
        this.modelMapper.map(source, destination);

    }

    public Object map(Object source, Class<?> destination, PropertyMap<?, ?> pm) {
        TypeMap typeMap = modelMapper.getTypeMap(source.getClass(), destination);
        modelMapper.addMappings(pm);
        if (typeMap == null) {
            modelMapper.createTypeMap(source.getClass(), destination)
                    .setPropertyCondition(Conditions.isNotNull());
        }
        return modelMapper.map(source, destination);

    }

    private <S, D> Converter<S, D> converterWithDestinationSupplier(Supplier<? extends D> supplier) {
        return ctx -> ctx.getMappingEngine().map(ctx.create(ctx.getSource(), supplier.get()));
    }

    public void map(Object source, Object destination, PropertyMap<?, ?> pm) {
        TypeMap typeMap = modelMapper.getTypeMap(source.getClass(), destination.getClass());
        modelMapper.addMappings(pm);
        if (typeMap == null) {
            modelMapper.createTypeMap(source.getClass(), destination.getClass())
                    .setPropertyCondition(Conditions.isNotNull());
        }
        this.modelMapper.map(source, destination);

    }

    public void addPropertyMap(PropertyMap<?, ?> pm) {
        this.modelMapper.addMappings(pm);

    }

    public void addConverter(Converter<?, ?> cv) {
        modelMapper.addConverter(cv);
    }

    public void addConverters() {
        this.modelMapper.addConverter((Converter<Date, XMLGregorianCalendar>) mappingContext -> DateUtils.dateToXmlGregorianCalendar(mappingContext.getSource()));

        this.modelMapper.addConverter((Converter<XMLGregorianCalendar, Date>) mappingContext -> DateUtils.xmlGregorianCalendarToDate(mappingContext.getSource()));

        this.modelMapper.addConverter((Converter<MessageType, String>) mappingContext -> mappingContext.getSource().getValue());

        this.modelMapper.addConverter((Converter<String, MessageType>) mappingContext -> MessageType.valueOf(mappingContext.getSource().toUpperCase()));
    }
}
