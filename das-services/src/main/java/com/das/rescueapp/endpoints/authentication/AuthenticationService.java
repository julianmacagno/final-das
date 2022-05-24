package com.das.rescueapp.endpoints.authentication;

import com.das.rescueapp.commons.enums.IssueType;
import com.das.rescueapp.commons.exceptions.PortalError;
import com.das.rescueapp.commons.exceptions.RescueAppException;
import com.das.rescueapp.core.config.context.Mapper;
import com.das.rescueapp.core.config.security.JwtUser;
import com.das.rescueapp.core.config.security.JwtUtil;
import com.das.rescueapp.core.dao.DaoFactory;
import com.das.rescueapp.endpoints.authentication.dto.AuthenticationDto;
import com.das.rescueapp.endpoints.authentication.dto.LoginDto;
import com.das.rescueapp.endpoints.authentication.dto.RegisterDto;
import com.das.rescueapp.endpoints.authentication.model.User;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AuthenticationService {
    private final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
    private final JwtUtil jwtUtil;
    private final JavaMailSenderImpl mailSender;
    private AuthenticationDao authenticationDao = null;
    @Autowired
    private Environment environment;
    @Autowired
    private Mapper mapper;

    public AuthenticationService(JwtUtil jwtUtil, JavaMailSenderImpl mailSender) {
        this.jwtUtil = jwtUtil;
        this.mailSender = mailSender;
        try {
            this.authenticationDao = (AuthenticationDao) DaoFactory.getDao(AuthenticationDao.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public User register(RegisterDto registerDto) {
        User user = (User) this.mapper.map(registerDto, User.class);
        user.setValidated(false);
        try {
            user = this.authenticationDao.insert(user);
        } catch (SQLException e) {
            this.logger.error(e.getMessage());
            throw new RescueAppException(IssueType.INTERNAL_SERVER, PortalError.ERROR_INSERTING_USER);
        }
        this.sendAuthenticationEmail(user.getEmail(), user.getCuil(), user.getLanguage());
        return user;
    }

    private void sendAuthenticationEmail(String to, String cuil, String language) {
        this.logger.info("Begun method sendAuthenticationEmail - To: {}, Cuil: {}", to, cuil);

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "utf-8");
            if (language != null && language.equals("es-ES")) {
                message.setSubject("Validacion de email");
                message.setText("<h1>Por favor cliquee el siguiente link para validar su cuenta: <a href=\"http://localhost:4200/validate-user/" + DigestUtils.sha256Hex(cuil) + "\">Validar</a></h1>", true);
            } else {
                message.setSubject("Email validation");
                message.setText("<h1>Please click the next link to validate your account: <a href=\"http://localhost:4200/validate-user/" + DigestUtils.sha256Hex(cuil) + "\">Validate</a></h1>", true);
            }
            message.setTo(to);
            String from = environment.getProperty("spring.mail.username");
            message.setFrom(from != null ? from : "");
            this.mailSender.send(mimeMessage);

            this.logger.info("Ended method sendAuthenticationEmail");
        } catch (Exception ex) {
            this.logger.error("Error sending email: {}", ex.getMessage());
            throw new RescueAppException(IssueType.INTERNAL_SERVER, PortalError.ERROR_SENDING_REGISTRATION_EMAIL);
        }
    }

    public User validateUser(String cuil) {
        this.logger.info("Begun method validateUser - Cuil: {}", cuil);
        User user = new User();
        user.setCuil(cuil);
        try {
            user = this.authenticationDao.validateUser(user);
        } catch (SQLException e) {
            throw new RescueAppException(IssueType.INTERNAL_SERVER, PortalError.INTERNAL_SERVER_ERROR);
        }
        if (user != null) {
            return user;
        } else {
            throw new RescueAppException(IssueType.NOT_FOUND, PortalError.USER_NOT_FOUND);
        }
    }

    public AuthenticationDto login(LoginDto loginDTO) throws RescueAppException {
        this.logger.info("Begun method validateUserAndPassword - LoginDto: {}", loginDTO);

        try {
            List<User> userList = authenticationDao.login(new User(loginDTO));
            if (userList != null && userList.size() > 0) {
                User user = userList.get(0);
                if (user.getValidated()) {
                    if (user.getCanceled()) {
                        throw new RescueAppException(IssueType.INTERNAL_SERVER, PortalError.USER_LOCKED);
                    }
                    String jwtToken = this.jwtUtil.generateToken(getJwtUser(user));
                    return new AuthenticationDto(jwtToken);
                } else {
                    throw new RescueAppException(IssueType.UNAUTHORIZED, PortalError.USER_NOT_VALIDATED);
                }
            } else {
                throw new RescueAppException(IssueType.UNAUTHORIZED, PortalError.USER_NOT_FOUND);
            }
        } catch (RescueAppException e) {
            this.logger.error("User not authorized: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            this.logger.error("Error fetching data: {}", e.getMessage());
            throw new RescueAppException(IssueType.INTERNAL_SERVER, PortalError.INTERNAL_SERVER_ERROR);
        }
    }

    private JwtUser getJwtUser(User user) {
        if (user == null) {
            return null;
        }

        try {
            Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
            return new JwtUser(user, grantedAuthorities);
        } catch (Exception e) {
            this.logger.error("Login failed with username {}. Details => {}", user.getCuil(), e.getMessage());
            return null;
        }
    }

    public AuthenticationDto refreshToken(String token) {
        if (this.jwtUtil.canTokenBeRefreshed(token)) {
            return new AuthenticationDto(this.jwtUtil.refreshToken(token));
        }
        throw new RescueAppException(IssueType.UNAUTHORIZED, PortalError.USER_NOT_AUTHORIZED);
    }
}
