package com.deftdevs.bootstrapi.jira.service;

import atlassian.mail.server.DefaultTestPopMailServerImpl;
import atlassian.mail.server.DefaultTestSmtpMailServerImpl;
import atlassian.mail.server.OtherTestPopMailServerImpl;
import atlassian.mail.server.OtherTestSmtpMailServerImpl;
import com.atlassian.mail.MailException;
import com.atlassian.mail.server.MailServerManager;
import com.atlassian.mail.server.PopMailServer;
import com.atlassian.mail.server.SMTPMailServer;
import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.model.MailServerPopModel;
import com.deftdevs.bootstrapi.commons.model.MailServerSmtpModel;
import com.deftdevs.bootstrapi.jira.model.util.MailServerPopModelUtil;
import com.deftdevs.bootstrapi.jira.model.util.MailServerSmtpModelUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MailServerServiceTest {

    @Mock
    private MailServerManager mailServerManager;

    private MailServerServiceImpl mailServerService;

    @BeforeEach
    public void setup() {
        mailServerService = new MailServerServiceImpl(mailServerManager);
    }

    @Test
    void testGetSmtpMailServer() {
        final SMTPMailServer smtpMailServer = new DefaultTestSmtpMailServerImpl();
        doReturn(smtpMailServer).when(mailServerManager).getDefaultSMTPMailServer();

        final MailServerSmtpModel bean = mailServerService.getMailServerSmtp();

        assertEquals(smtpMailServer.getName(), bean.getName());
        assertEquals(smtpMailServer.getDescription(), bean.getDescription());
        assertEquals(smtpMailServer.getHostname(), bean.getHost());
        assertEquals(smtpMailServer.getTimeout(), (long) bean.getTimeout());
        assertEquals(smtpMailServer.getUsername(), bean.getUsername());
        assertNull(bean.getPassword());
        assertEquals(smtpMailServer.getDefaultFrom(), bean.getFrom());
        assertEquals(smtpMailServer.getPrefix(), bean.getPrefix());
        assertEquals(smtpMailServer.isTlsRequired(), bean.getUseTls());
        assertEquals(smtpMailServer.getMailProtocol().getProtocol(), bean.getProtocol());
        assertEquals(smtpMailServer.getPort(), String.valueOf(bean.getPort()));
    }

    @Test
    void testGetSmtpMailServerIsNull() {
        final MailServerSmtpModel response = mailServerService.getMailServerSmtp();
        assertNull(response);
    }

    @Test
    void testPutSmtpMaiLServerUpdate() throws Exception {
        final SMTPMailServer defaultSmtpMailServer = new DefaultTestSmtpMailServerImpl();
        doReturn(true).when(mailServerManager).isDefaultSMTPMailServerDefined();
        doReturn(defaultSmtpMailServer).when(mailServerManager).getDefaultSMTPMailServer();

        final SMTPMailServer updateSmtpMailServer = new OtherTestSmtpMailServerImpl();
        final MailServerSmtpModel requestMailServerSmtpModel = MailServerSmtpModelUtil.toMailServerSmtpModel(updateSmtpMailServer);
        assertNotNull(requestMailServerSmtpModel);
        final MailServerSmtpModel responseMailServerSmtpModel = mailServerService.setMailServerSmtp(requestMailServerSmtpModel);

        final ArgumentCaptor<SMTPMailServer> smtpMailServerCaptor = ArgumentCaptor.forClass(SMTPMailServer.class);
        verify(mailServerManager).update(smtpMailServerCaptor.capture());
        final SMTPMailServer smtpMailServer = smtpMailServerCaptor.getValue();

        assertEquals(MailServerSmtpModelUtil.toMailServerSmtpModel(updateSmtpMailServer), MailServerSmtpModelUtil.toMailServerSmtpModel(smtpMailServer));
        assertEquals(requestMailServerSmtpModel, responseMailServerSmtpModel);
    }

    @Test
    void testPutSmtpMaiLServerCreate() throws Exception {
        final SMTPMailServer createSmtpMailServer = new DefaultTestSmtpMailServerImpl();
        final MailServerSmtpModel requestMailServerSmtpModel = MailServerSmtpModelUtil.toMailServerSmtpModel(createSmtpMailServer);
        assertNotNull(requestMailServerSmtpModel);
        final MailServerSmtpModel responseMailServerSmtpModel = mailServerService.setMailServerSmtp(requestMailServerSmtpModel);

        final ArgumentCaptor<SMTPMailServer> smtpMailServerCaptor = ArgumentCaptor.forClass(SMTPMailServer.class);
        verify(mailServerManager).create(smtpMailServerCaptor.capture());
        final SMTPMailServer smtpMailServer = smtpMailServerCaptor.getValue();

        assertEquals(MailServerSmtpModelUtil.toMailServerSmtpModel(createSmtpMailServer), MailServerSmtpModelUtil.toMailServerSmtpModel(smtpMailServer));
        assertEquals(requestMailServerSmtpModel, responseMailServerSmtpModel);
    }

    @Test
    void testPutMailServerSmtpDefaultConfig() throws MailException {
        final MailServerSmtpModel mailServerSmtpModel = new MailServerSmtpModel();

        mailServerService.setMailServerSmtp(mailServerSmtpModel);

        final ArgumentCaptor<SMTPMailServer> smtpMailServerCaptor = ArgumentCaptor.forClass(SMTPMailServer.class);
        verify(mailServerManager).create(smtpMailServerCaptor.capture());
        final SMTPMailServer smtpMailServer = smtpMailServerCaptor.getValue();

        assertEquals(mailServerSmtpModel.getName(), smtpMailServer.getName());
        assertEquals(mailServerSmtpModel.getDescription(), smtpMailServer.getDescription());
        assertEquals(mailServerSmtpModel.getFrom(), smtpMailServer.getDefaultFrom());
        assertEquals(mailServerSmtpModel.getPrefix(), smtpMailServer.getPrefix());
        assertEquals(mailServerSmtpModel.getHost(), smtpMailServer.getHostname());
        assertEquals(smtpMailServer.getMailProtocol().getDefaultPort(), smtpMailServer.getPort());
        assertEquals(mailServerSmtpModel.getUsername(), smtpMailServer.getUsername());
    }

    @Test
    void testPutSmtpMaiLServerWithoutPort() throws Exception {
        final SMTPMailServer createSmtpMailServer = new DefaultTestSmtpMailServerImpl();
        createSmtpMailServer.setPort(null);

        final MailServerSmtpModel requestMailServerSmtpModel = MailServerSmtpModelUtil.toMailServerSmtpModel(createSmtpMailServer);
        assertNotNull(requestMailServerSmtpModel);
        final MailServerSmtpModel responseMailServerSmtpModel = mailServerService.setMailServerSmtp(requestMailServerSmtpModel);

        final ArgumentCaptor<SMTPMailServer> smtpMailServerCaptor = ArgumentCaptor.forClass(SMTPMailServer.class);
        verify(mailServerManager).create(smtpMailServerCaptor.capture());
        final SMTPMailServer smtpMailServer = smtpMailServerCaptor.getValue();

        assertEquals(createSmtpMailServer.getMailProtocol().getDefaultPort(), smtpMailServer.getPort());
        assertEquals(requestMailServerSmtpModel, responseMailServerSmtpModel);
    }

    @Test
    void testPutSmtpMaiLServerException() throws MailException {
        doThrow(new MailException("SMTP test exception")).when(mailServerManager).create(any());

        final SMTPMailServer createSmtpMailServer = new DefaultTestSmtpMailServerImpl();
        final MailServerSmtpModel requestMailServerSmtpModel = MailServerSmtpModelUtil.toMailServerSmtpModel(createSmtpMailServer);
        assertNotNull(requestMailServerSmtpModel);

        assertThrows(BadRequestException.class, () -> {
            mailServerService.setMailServerSmtp(requestMailServerSmtpModel);
        });
    }

    @Test
    void testGetPopMailServer() {
        final PopMailServer popMailServer = new DefaultTestPopMailServerImpl();
        doReturn(popMailServer).when(mailServerManager).getDefaultPopMailServer();

        final MailServerPopModel bean = mailServerService.getMailServerPop();

        assertEquals(popMailServer.getName(), bean.getName());
        assertEquals(popMailServer.getDescription(), bean.getDescription());
        assertEquals(popMailServer.getHostname(), bean.getHost());
        assertEquals(popMailServer.getTimeout(), (long) bean.getTimeout());
        assertEquals(popMailServer.getUsername(), bean.getUsername());
        assertNull(bean.getPassword());
        assertEquals(popMailServer.getMailProtocol().getProtocol(), bean.getProtocol());
        assertEquals(popMailServer.getPort(), String.valueOf(bean.getPort()));
    }

    @Test
    void testGetPopMailServerIsNull() {
        final MailServerPopModel bean = mailServerService.getMailServerPop();
        assertNull(bean);
    }

    @Test
    void testPutPopMaiLServerUpdate() throws Exception {
        final PopMailServer defaultPopMailServer = new DefaultTestPopMailServerImpl();
        doReturn(defaultPopMailServer).when(mailServerManager).getDefaultPopMailServer();

        final PopMailServer updatePopMailServer = new OtherTestPopMailServerImpl();
        final MailServerPopModel requestMailServerPopModel = MailServerPopModelUtil.toMailServerPopModel(updatePopMailServer);
        assertNotNull(requestMailServerPopModel);
        final MailServerPopModel responseMailServerPopModel = mailServerService.setMailServerPop(requestMailServerPopModel);

        final ArgumentCaptor<PopMailServer> popMailServerCaptor = ArgumentCaptor.forClass(PopMailServer.class);
        verify(mailServerManager).update(popMailServerCaptor.capture());
        final PopMailServer popMailServer = popMailServerCaptor.getValue();

        assertEquals(MailServerPopModelUtil.toMailServerPopModel(updatePopMailServer), MailServerPopModelUtil.toMailServerPopModel(popMailServer));
        assertEquals(requestMailServerPopModel, responseMailServerPopModel);
    }

    @Test
    void testPutPopMaiLServerCreate() throws Exception {
        final PopMailServer createPopMailServer = new DefaultTestPopMailServerImpl();
        final MailServerPopModel requestMailServerPopModel = MailServerPopModelUtil.toMailServerPopModel(createPopMailServer);
        assertNotNull(requestMailServerPopModel);
        final MailServerPopModel responseMailServerPopModel = mailServerService.setMailServerPop(requestMailServerPopModel);

        final ArgumentCaptor<PopMailServer> popMailServerCaptor = ArgumentCaptor.forClass(PopMailServer.class);
        verify(mailServerManager).create(popMailServerCaptor.capture());
        final PopMailServer popMailServer = popMailServerCaptor.getValue();

        MailServerPopModel from1 = MailServerPopModelUtil.toMailServerPopModel(createPopMailServer);
        MailServerPopModel from2 = MailServerPopModelUtil.toMailServerPopModel(popMailServer);

        assertEquals(from1, from2);
        assertEquals(requestMailServerPopModel, responseMailServerPopModel);
    }

    @Test
    void testPutPopMaiLServerWithoutPort() throws Exception {
        final PopMailServer createPopMailServer = new DefaultTestPopMailServerImpl();
        createPopMailServer.setPort(null);

        final MailServerPopModel requestMailServerPopModel = MailServerPopModelUtil.toMailServerPopModel(createPopMailServer);
        assertNotNull(requestMailServerPopModel);
        final MailServerPopModel responseMailServerPopModel = mailServerService.setMailServerPop(requestMailServerPopModel);

        final ArgumentCaptor<PopMailServer> popMailServerCaptor = ArgumentCaptor.forClass(PopMailServer.class);
        verify(mailServerManager).create(popMailServerCaptor.capture());
        final PopMailServer popMailServer = popMailServerCaptor.getValue();

        assertEquals(createPopMailServer.getMailProtocol().getDefaultPort(), popMailServer.getPort());
        assertEquals(requestMailServerPopModel, responseMailServerPopModel);
    }

    @Test
    void testPutPopMaiLServerException() throws Exception {
        doThrow(new MailException("POP test exception")).when(mailServerManager).create(any());

        final PopMailServer createPopMailServer = new DefaultTestPopMailServerImpl();
        final MailServerPopModel requestMailServerPopModel = MailServerPopModelUtil.toMailServerPopModel(createPopMailServer);
        assertNotNull(requestMailServerPopModel);

        assertThrows(BadRequestException.class, () -> {
            mailServerService.setMailServerPop(requestMailServerPopModel);
        });
    }

}
