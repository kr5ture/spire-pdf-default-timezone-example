package com.example;

import com.spire.pdf.PdfDocument;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExampleTest {
    @Test
    void when_file_created_at_17_07_UTC_then_should_return_same_when_default_timezone_offset_has_plus_8() throws IOException {
        // given
        TimeZone.setDefault(TimeZone.getTimeZone("Australia/Perth")); // UTC+8
        // and - file created at 2022-12-10 18:07:43 UTC+1 [Europe/Warsaw] which is 2022-12-10 17:07:43 UTC
        Path filePath = Path.of(getClass().getResource("/example.pdf").getPath());
        // and
        PdfDocument pdfDocument = new PdfDocument(Files.newInputStream(filePath, StandardOpenOption.READ));

        // when
        Instant result = pdfDocument.getDocumentInformation()
            .getCreationDate()
            .toInstant(); // <-- should be 2022-12-10 17:07:43 UTC+0

        // then - fails:
        // Expected :2022-12-10T17:07:43Z
        // Actual   :2022-12-10T10:07:43Z
        assertEquals("2022-12-10T17:07:43Z", result.toString());
    }

    @Test
    void when_file_created_at_17_07_UTC_then_should_return_same_when_default_timezone_is_UTC() throws IOException {
        // given
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        // and - file created at 2022-12-10 18:07:43 UTC+1 [Europe/Warsaw] which is 2022-12-10 17:07:43 UTC
        Path filePath = Path.of(getClass().getResource("/example.pdf").getPath());
        // and
        PdfDocument pdfDocument = new PdfDocument(Files.newInputStream(filePath, StandardOpenOption.READ));

        // when
        Instant result = pdfDocument.getDocumentInformation()
            .getCreationDate()
            .toInstant(); // <-- should be 2022-12-10 17:07:43 UTC+0

        // then - fails:
        // Expected :2022-12-10T17:07:43Z
        // Actual   :2022-12-10T18:07:43Z
        assertEquals("2022-12-10T17:07:43Z", result.toString());
    }

    @Test
    void when_file_created_at_17_07_UTC_then_should_return_same_when_default_timezone_is_same_as_one_from_file_creation() throws IOException {
        // given
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Warsaw")); // UTC+1 at 2022-12-10 (winter time)
        // and - file created at 2022-12-10 18:07:43 UTC+1 [Europe/Warsaw] which is 2022-12-10 17:07:43 UTC
        Path filePath = Path.of(getClass().getResource("/example.pdf").getPath());
        // and
        PdfDocument pdfDocument = new PdfDocument(Files.newInputStream(filePath, StandardOpenOption.READ));

        // when
        Instant result = pdfDocument.getDocumentInformation()
            .getCreationDate()
            .toInstant(); // <-- should be 2022-12-10 17:07:43 UTC+0

        // then - passed!
        assertEquals("2022-12-10T17:07:43Z", result.toString());
    }
}
