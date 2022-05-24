package com.das.rescueapp.endpoints.announcement;

import com.das.rescueapp.core.config.constant.Routes;
import com.das.rescueapp.endpoints.announcement.dto.AnnouncementDto;
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
@Api(tags = "Announcement")
@Tag(name = "Announcement")
public class AnnouncementController {
    private final Logger logger = LoggerFactory.getLogger(AnnouncementController.class);
    @Autowired
    private AnnouncementService announcementService;

    @GetMapping(Routes.Api.Announcement.ANNOUNCEMENT)
    public List<AnnouncementDto> getAnnouncement() {
//        this.logger.info("Begun method getAnnouncementById");
        return this.announcementService.getAnnouncement();
    }
}
