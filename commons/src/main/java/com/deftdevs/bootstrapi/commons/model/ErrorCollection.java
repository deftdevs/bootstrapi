package com.deftdevs.bootstrapi.commons.model;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@XmlRootElement(name = BootstrAPI.ERROR)
public class ErrorCollection {

    @XmlElement
    private final List<String> errorMessages = new ArrayList<>();

    public ErrorCollection addErrorMessage(String errorMessage) {
        if (errorMessage != null) {
            errorMessages.add(errorMessage);
        }
        return this;
    }

    public ErrorCollection addErrorMessages(List<String> messages) {
        if (messages != null) {
            messages.forEach(this::addErrorMessage);
        }
        return this;
    }

    public boolean hasAnyErrors() {
        return !errorMessages.isEmpty();
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public static ErrorCollection of(String... messages) {
        return of(Arrays.asList(messages));
    }

    public static ErrorCollection of(List<String> messages) {
        return new ErrorCollection().addErrorMessages(messages);
    }

}
