package com.deftdevs.bootstrapi.commons.model.type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "status")
public class _AllBeanStatus {

    @XmlElement
    private int status;

    @XmlElement
    private String message;

    @XmlElement
    private String details;

    public static _AllBeanStatus success() {
        return new _AllBeanStatus(Response.Status.OK.getStatusCode(), "Success", null);
    }

    public static _AllBeanStatus error(Response.Status status, String message, String details) {
        return new _AllBeanStatus(status.getStatusCode(), message, details);
    }
}
