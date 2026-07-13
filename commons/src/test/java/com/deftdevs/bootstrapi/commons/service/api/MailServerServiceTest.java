package com.deftdevs.bootstrapi.commons.service.api;

import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.model.MailServerModel;
import com.deftdevs.bootstrapi.commons.model.MailServerPopModel;
import com.deftdevs.bootstrapi.commons.model.MailServerSmtpModel;
import com.deftdevs.bootstrapi.commons.model.type.ServiceResult;
import com.deftdevs.bootstrapi.commons.util.FieldNames;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class MailServerServiceTest {

    private MailServerService mailServerService;

    @BeforeEach
    void setup() {
        mailServerService = mock(MailServerService.class, CALLS_REAL_METHODS);
    }

    @Test
    void testGetMailServer() {
        doReturn(MailServerSmtpModel.EXAMPLE_1).when(mailServerService).getMailServerSmtp();
        doReturn(MailServerPopModel.EXAMPLE_1).when(mailServerService).getMailServerPop();

        final MailServerModel mailServerModel = mailServerService.getMailServer();

        assertEquals(MailServerSmtpModel.EXAMPLE_1, mailServerModel.getSmtp());
        assertEquals(MailServerPopModel.EXAMPLE_1, mailServerModel.getPop());
    }

    @Test
    void testSetMailServerAppliesSmtpAndPop() {
        doReturn(MailServerSmtpModel.EXAMPLE_1).when(mailServerService).setMailServerSmtp(MailServerSmtpModel.EXAMPLE_1);
        doReturn(MailServerPopModel.EXAMPLE_1).when(mailServerService).setMailServerPop(MailServerPopModel.EXAMPLE_1);

        final ServiceResult<MailServerModel> result = mailServerService.setMailServer(
                new MailServerModel(MailServerSmtpModel.EXAMPLE_1, MailServerPopModel.EXAMPLE_1));

        assertEquals(MailServerSmtpModel.EXAMPLE_1, result.getModel().getSmtp());
        assertEquals(MailServerPopModel.EXAMPLE_1, result.getModel().getPop());
        assertEquals(200, result.getStatus().get(FieldNames.of(MailServerModel.class, MailServerSmtpModel.class)).getStatus());
        assertEquals(200, result.getStatus().get(FieldNames.of(MailServerModel.class, MailServerPopModel.class)).getStatus());
    }

    @Test
    void testSetMailServerSkipsNullSubFields() {
        final ServiceResult<MailServerModel> result = mailServerService.setMailServer(new MailServerModel());

        assertTrue(result.getStatus().isEmpty());
        verify(mailServerService, never()).setMailServerSmtp(MailServerSmtpModel.EXAMPLE_1);
        verify(mailServerService, never()).setMailServerPop(MailServerPopModel.EXAMPLE_1);
    }

    @Test
    void testSetMailServerRecordsPerSubFieldFailure() {
        doThrow(new BadRequestException("invalid smtp config"))
                .when(mailServerService).setMailServerSmtp(MailServerSmtpModel.EXAMPLE_1);
        doReturn(MailServerPopModel.EXAMPLE_1).when(mailServerService).setMailServerPop(MailServerPopModel.EXAMPLE_1);

        final ServiceResult<MailServerModel> result = mailServerService.setMailServer(
                new MailServerModel(MailServerSmtpModel.EXAMPLE_1, MailServerPopModel.EXAMPLE_1));

        assertNull(result.getModel().getSmtp());
        assertEquals(MailServerPopModel.EXAMPLE_1, result.getModel().getPop());
        assertEquals(400, result.getStatus().get(FieldNames.of(MailServerModel.class, MailServerSmtpModel.class)).getStatus());
        assertEquals(200, result.getStatus().get(FieldNames.of(MailServerModel.class, MailServerPopModel.class)).getStatus());
    }
}
