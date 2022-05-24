package com.das.rescueapp.endpoints.entity;

import com.das.rescueapp.core.config.constant.Routes;
import com.das.rescueapp.endpoints.entity.dto.EntityDto;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("")
@Api(tags = "Entity")
@Tag(name = "Entity")
public class EntityController {
    private final Logger logger = LoggerFactory.getLogger(EntityController.class);

    @Autowired
    private EntityService entityService;

    @GetMapping(value = Routes.Api.Entity.ENTITY)
    public List<EntityDto> getEntityAvailability() {
//        this.logger.info("Begun method getEntityAvailability");
        return entityService.getEntityAvailability();
    }
}
