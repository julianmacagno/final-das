//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2022.05.23 at 09:11:25 PM ART 
//


package com.das.soap;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.das.soap package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.das.soap
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetAvailabilityRequest }
     * 
     */
    public GetAvailabilityRequest createGetAvailabilityRequest() {
        return new GetAvailabilityRequest();
    }

    /**
     * Create an instance of {@link GetAvailabilityResponse }
     * 
     */
    public GetAvailabilityResponse createGetAvailabilityResponse() {
        return new GetAvailabilityResponse();
    }

    /**
     * Create an instance of {@link ServiceAvailability }
     * 
     */
    public ServiceAvailability createServiceAvailability() {
        return new ServiceAvailability();
    }

    /**
     * Create an instance of {@link MessageRequest }
     * 
     */
    public MessageRequest createMessageRequest() {
        return new MessageRequest();
    }

    /**
     * Create an instance of {@link SoapMessageDto }
     * 
     */
    public SoapMessageDto createSoapMessageDto() {
        return new SoapMessageDto();
    }

    /**
     * Create an instance of {@link MessageResponse }
     * 
     */
    public MessageResponse createMessageResponse() {
        return new MessageResponse();
    }

    /**
     * Create an instance of {@link ReceiveStatsRequest }
     * 
     */
    public ReceiveStatsRequest createReceiveStatsRequest() {
        return new ReceiveStatsRequest();
    }

    /**
     * Create an instance of {@link StatDto }
     * 
     */
    public StatDto createStatDto() {
        return new StatDto();
    }

    /**
     * Create an instance of {@link ReceiveStatsResponse }
     * 
     */
    public ReceiveStatsResponse createReceiveStatsResponse() {
        return new ReceiveStatsResponse();
    }

    /**
     * Create an instance of {@link ReasonRequest }
     * 
     */
    public ReasonRequest createReasonRequest() {
        return new ReasonRequest();
    }

    /**
     * Create an instance of {@link ReasonResponse }
     * 
     */
    public ReasonResponse createReasonResponse() {
        return new ReasonResponse();
    }

    /**
     * Create an instance of {@link SoapReasonDto }
     * 
     */
    public SoapReasonDto createSoapReasonDto() {
        return new SoapReasonDto();
    }

}