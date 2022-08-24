package cn.terly.web;

import cn.terly.service.ConverterService;
import cn.terly.service.api.UnknownSourceFormat;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FilenameUtils;
import org.jodconverter.core.document.DefaultDocumentFormatRegistry;
import org.jodconverter.core.document.DocumentFormat;
import org.jodconverter.core.office.OfficeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@SuppressWarnings({"SpringJavaAutowiredFieldsWarningInspection", "unused"})
@Log4j2
@RestController
@RequestMapping("/conversion")
public class ConversionController
{
  @Autowired
  private ConverterService converterService;

  @RequestMapping(path = "", method = RequestMethod.POST)
  public ResponseEntity<?> convert( @RequestParam(name = "format", defaultValue = "pdf") final String targetFormatExt,
                                    @RequestParam("file") final MultipartFile inputMultipartFile) throws IOException, OfficeException{
    final DocumentFormat conversionTargetFormat = DefaultDocumentFormatRegistry.getFormatByExtension(targetFormatExt);

    if (conversionTargetFormat == null) {
      log.error(String.format("Unknown conversion target %s", targetFormatExt));
      return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
    }

    ByteArrayOutputStream convertedFile;
    try {
      convertedFile = converterService.doConvert(
        conversionTargetFormat,
        inputMultipartFile.getInputStream(),
        inputMultipartFile.getOriginalFilename()
      );
    } catch (UnknownSourceFormat unknownSourceFormat) {
      log.error(unknownSourceFormat.getMessage());
      return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(unknownSourceFormat.getMessage());
    } catch (OfficeException officeException) {
      log.error(officeException.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    final HttpHeaders headers = new HttpHeaders();
    String targetFilename = String.format(
      "%s.%s",
      FilenameUtils.getBaseName(inputMultipartFile.getOriginalFilename()),
      conversionTargetFormat.getExtension()
    );
    headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + targetFilename);
    headers.setContentType(MediaType.parseMediaType(conversionTargetFormat.getMediaType()));
    return ResponseEntity.ok().headers(headers).body(convertedFile.toByteArray());
  }
}

