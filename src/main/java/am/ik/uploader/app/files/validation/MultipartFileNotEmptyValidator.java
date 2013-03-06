package am.ik.uploader.app.files.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.web.multipart.MultipartFile;

public class MultipartFileNotEmptyValidator
                                           implements
                                           ConstraintValidator<MultipartFileNotEmpty, MultipartFile> {

    public void initialize(MultipartFileNotEmpty constraintAnnotation) {
    }

    public boolean isValid(MultipartFile multipartFile,
            ConstraintValidatorContext constraintContext) {
        if (multipartFile == null) {
            return true;
        }

        if ("".equals(multipartFile.getOriginalFilename())
                || multipartFile.isEmpty()) {
            return false;
        }
        return true;
    }

}
