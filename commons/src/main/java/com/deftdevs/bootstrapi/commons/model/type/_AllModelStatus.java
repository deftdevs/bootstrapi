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
public class _AllModelStatus {

    @XmlElement
    private int status;

    @XmlElement
    private String message;

    @XmlElement
    private String details;

    public static _AllModelStatus success() {
        return new _AllModelStatus(Response.Status.OK.getStatusCode(), "Success", null);
    }

    public static _AllModelStatus error(Response.Status status, String message, String details) {
        final int code = status != null
                ? status.getStatusCode()
                : Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
        return new _AllModelStatus(code, message, details);
    }

    public static _AllModelStatus error(int statusCode, String message, String details) {
        return new _AllModelStatus(statusCode, message, details);
    }
}
