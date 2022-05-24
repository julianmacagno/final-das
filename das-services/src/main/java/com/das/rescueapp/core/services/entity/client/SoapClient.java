package com.das.rescueapp.core.services.entity.client;

import com.das.rescueapp.core.config.security.JwtUtil;
import com.das.rescueapp.endpoints.entity.model.Entity;
import com.das.soapconsumer.wsdl.DoLoginRequest;
import com.das.soapconsumer.wsdl.DoLoginResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.WebServiceException;
import org.springframework.ws.client.WebServiceIOException;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.client.core.SoapActionCallback;
import org.springframework.ws.transport.context.TransportContext;
import org.springframework.ws.transport.context.TransportContextHolder;
import org.springframework.ws.transport.http.HttpUrlConnection;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SoapClient extends WebServiceGatewaySupport {
    private final Logger logger = LoggerFactory.getLogger(SoapClient.class);
    Map<String, String> headerMap = new HashMap<>();
    Map<String, String> tokenMap = new HashMap<>();

    @Autowired
    private JwtUtil jwtUtil;

    public Object soapRequest(String uri, Object request, String actionCallback, Entity entity) {
        if (tokenMap.get(entity.getName()) == null || this.jwtUtil.getExpirationDateFromToken(tokenMap.get(entity.getName())) == null || this.jwtUtil.isTokenExpired(tokenMap.get(entity.getName()))) {
            this.doLogin(entity);
        }

        this.headerMap.put("Authorization", "Bearer " + tokenMap.get(entity.getName()));

        try {
            return getWebServiceTemplate().marshalSendAndReceive(uri, request, getRequestCallback(headerMap, actionCallback));
        } catch (WebServiceException e) {
            if (e.getMessage() == null || e.getMessage().contains("403") || e.getMessage().contains("401")) {
                this.doLogin(entity);
                this.logger.warn("Was unauthorized, negotiating new token {}", tokenMap.get(entity.getName()));
                return this.soapRequest(uri, request, actionCallback, entity);
            } else {
                this.logger.error("Error sending SOAP request: {}", e.getMessage());
                throw e;
            }
        }
    }

    private void doLogin(Entity entity) {
        this.logger.info("Begun method doLogin - Entity: {}", entity);

        DoLoginRequest request = new DoLoginRequest();
        request.setUsername(entity.getUsername());
        request.setPassword(entity.getPassword());

        try {
            DoLoginResponse response = (DoLoginResponse) getWebServiceTemplate().marshalSendAndReceive(
                    entity.getUrl() + "/soap_auth",
                    request,
                    new SoapActionCallback("http://das.com/soapconsumer/wsdl/DoLoginRequest")
            );
            this.tokenMap.put(entity.getName(), response.getToken());
        } catch (WebServiceIOException e) {
            this.logger.error(e.getMessage());
            throw e;
        }

    }

    private WebServiceMessageCallback getRequestCallback(Map<String, String> headers, String actionCallback) {
        return message -> {
            ((SoapMessage) message).setSoapAction(actionCallback);
            TransportContext context = TransportContextHolder.getTransportContext();
            HttpUrlConnection connection = (HttpUrlConnection) context.getConnection();
            setConnHeaders(connection, headers);
        };
    }

    private void setConnHeaders(HttpUrlConnection connection, Map<String, String> headers) {
        headers.forEach((name, value) -> {
            try {
                connection.addRequestHeader(name, value);
            } catch (IOException e) {
                this.logger.error("Error adding headers to connection: {}", e.getMessage());
            }
        });
    }
}
