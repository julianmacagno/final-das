package com.das.entities.endpoints.soap;

import com.das.entities.core.authentication.AuthenticationService;
import com.das.entities.core.authentication.dto.LoginDto;
import com.das.entities.core.config.constant.Routes;
import com.das.soap_auth.DoLoginRequest;
import com.das.soap_auth.DoLoginResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class SoapAuthController {
    private final Logger logger = LoggerFactory.getLogger(SoapController.class);

    @Autowired
    private AuthenticationService authService;

    @PayloadRoot(namespace = Routes.SoapAuth.NAMESPACE_URI, localPart = "doLoginRequest")
    @ResponsePayload
    public DoLoginResponse doLogin(@RequestPayload DoLoginRequest request) {
        this.logger.info("Begun method doLogin - DoLoginRequest: {}", request);

        LoginDto loginDto = new LoginDto();
        loginDto.setUsername(request.getUsername());
        loginDto.setPassword(request.getPassword());

        DoLoginResponse response = new DoLoginResponse();

        response.setToken(authService.login(loginDto).getAccessToken());

        return response;
    }
}
