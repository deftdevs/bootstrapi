package com.deftdevs.bootstrapi.crowd.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "configurationStatus")
public class _AllBeanConfigStatus {

    @XmlElement
    private int status;

    @XmlElement
    private String message;

    @XmlElement
    private String details;

    public static _AllBeanConfigStatus success() {
        return new _AllBeanConfigStatus(Response.Status.OK.getStatusCode(), "Success", null);
    }

    public static _AllBeanConfigStatus error(Response.Status status, String message, String details) {
        return new _AllBeanConfigStatus(status.getStatusCode(), message, details);
    }
}
