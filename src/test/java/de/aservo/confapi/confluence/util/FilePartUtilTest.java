package de.aservo.confapi.confluence.util;

import com.atlassian.plugins.rest.common.multipart.FilePart;
import com.opensymphony.webwork.config.Configuration;
import de.aservo.confapi.commons.exception.InternalServerErrorException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static com.atlassian.confluence.setup.ConfluenceBootstrapConstants.TEMP_DIR_PROP;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FilePartUtilTest {

    public static final String EXPORT_FILE_NAME = "confluence-export.zip";
    public static final String DOWNLOAD_PATH = "/path/to/download";

    @Test
    public void testCreateFile() throws IOException, NoSuchMethodException {
        final FilePart filePart = mock(FilePart.class);
        doReturn(EXPORT_FILE_NAME).when(filePart).getName();
        doReturn(mock(InputStream.class)).when(filePart).getInputStream();

        final File uploadDirectory = spy(new File(DOWNLOAD_PATH));
        doReturn(true).when(uploadDirectory).exists();
        doReturn(true).when(uploadDirectory).mkdirs();

        final File writtenFile = new File(uploadDirectory, EXPORT_FILE_NAME);

        try (MockedStatic<FilePartUtil> filePartUtilMockedStatic = mockStatic(FilePartUtil.class)) {
            filePartUtilMockedStatic.when(FilePartUtil::getUploadDirectory).thenReturn(uploadDirectory);
            filePartUtilMockedStatic.when(() -> FilePartUtil.writeToFile(any(), any(), anyString())).thenReturn(writtenFile);
            assertNotNull(FilePartUtil.createFile(filePart));
        }
    }

    @Test(expected = InternalServerErrorException.class)
    public void testCreateFileCannotCreateUploadDirectory() throws IOException, NoSuchMethodException {
        final FilePart filePart = mock(FilePart.class);
        doReturn(EXPORT_FILE_NAME).when(filePart).getName();
        doReturn(mock(InputStream.class)).when(filePart).getInputStream();

        final File uploadDirectory = spy(new File(DOWNLOAD_PATH));
        doReturn(false).when(uploadDirectory).exists();
        doReturn(false).when(uploadDirectory).mkdirs();

        try (MockedStatic<FilePartUtil> filePartUtilMockedStatic = mockStatic(FilePartUtil.class)) {
            filePartUtilMockedStatic.when(FilePartUtil::getUploadDirectory).thenReturn(uploadDirectory);
            FilePartUtil.createFile(filePart);
        }
    }

    @Test(expected = InternalServerErrorException.class)
    public void testCreateFileUploadPathIsFile() throws IOException, NoSuchMethodException {
        final FilePart filePart = mock(FilePart.class);
        doReturn(EXPORT_FILE_NAME).when(filePart).getName();
        doReturn(mock(InputStream.class)).when(filePart).getInputStream();

        final File uploadDirectory = spy(new File(DOWNLOAD_PATH));
        doReturn(true).when(uploadDirectory).exists();
        doReturn(true).when(uploadDirectory).isFile();

        try (MockedStatic<FilePartUtil> filePartUtilMockedStatic = mockStatic(FilePartUtil.class)) {
            filePartUtilMockedStatic.when(FilePartUtil::getUploadDirectory).thenReturn(uploadDirectory);
            FilePartUtil.createFile(filePart);
        }
    }

    @Test
    public void testGetUploadDirectory() {
        try (MockedStatic<Configuration> configurationMockedStatic = mockStatic(Configuration.class)) {
            configurationMockedStatic.when(() -> Configuration.getString(TEMP_DIR_PROP)).thenReturn(DOWNLOAD_PATH);
            assertNotNull(FilePartUtil.getUploadDirectory());
        }
    }

}
