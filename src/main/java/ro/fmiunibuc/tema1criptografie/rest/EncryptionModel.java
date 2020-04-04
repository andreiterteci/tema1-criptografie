package ro.fmiunibuc.tema1criptografie.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EncryptionModel {
    private String textToEncrypt;
    private String keyForOTP;
    private Integer caesarOffset;
}
