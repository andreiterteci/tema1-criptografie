package ro.fmiunibuc.tema1criptografie.rest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.*;
import java.util.Base64;
import java.util.Scanner;

@Controller
public class Homework {

    @RequestMapping
    private String mainController(Model model) throws IOException {
        String textToEncrypt = "";
        String keyForOTP = "";
        Integer caesarOffset;

        textToEncrypt = getTextFromFile().getTextToEncrypt();
        keyForOTP = getTextFromFile().getKeyForOTP();
        caesarOffset = getTextFromFile().getCaesarOffset();

        if(!textToEncrypt.equals(textToEncrypt.toLowerCase())) {
            model.addAttribute("CaesarError", "CAESAR ERROR!! Textul poate contine doar litere mici si spatii!");
            return "index";
        }

        if (keyForOTP.getBytes().length < textToEncrypt.getBytes().length) {
            model.addAttribute("OPTerror", "OTP ERROR!! Lungimea cheii de criptare trebuie sa fie mai mare sau egala cu lungimea textului");
            return "index";
        }

        final String encryptWithCaesar = caesarDecipher(textToEncrypt, caesarOffset);
        final String decryptMessageWithCaesar = caesarDecipher(encryptWithCaesar, 26 - caesarOffset);

        final byte[] secretTextForOTP = encryptWithCaesar.getBytes();
        final byte[] secretKeyForOTP = keyForOTP.getBytes();

        final byte[] encryptBytesWithOTP = encryptOneTimePad(secretTextForOTP, secretKeyForOTP);
        final String encryptMessageWithOTP = Base64.getEncoder().encodeToString(encryptBytesWithOTP);
        final String decryptMessageWithOTP = decryptOneTimePad(secretTextForOTP,
                encryptBytesWithOTP, secretKeyForOTP);


        writeTextToFile("Mesaj criptat cu Caesar: " + encryptWithCaesar + "\n" +
                "Mesaj criptat cu OTP: " + encryptMessageWithOTP + "\n" +
                "Mesaj decriptat cu OTP: " + decryptMessageWithOTP + "\n" +
                "Mesaj decriptat cu Caesar: " + decryptMessageWithCaesar);

        model.addAttribute("encryptedWithOTP", encryptMessageWithOTP);
        model.addAttribute("encryptedWithCaesar", encryptWithCaesar);
        model.addAttribute("decryptedWithCaesar", decryptMessageWithCaesar);
        model.addAttribute("decryptedWithOTP", decryptMessageWithOTP);
        model.addAttribute("textFromFile", textToEncrypt);
        return "index";
    }

    private EncryptionModel getTextFromFile() throws FileNotFoundException {
        EncryptionModel encryptionModel = new EncryptionModel();
        File file = new File("/home/andrei/Facultate/criptografie/tema1criptografie/src/main/resources/files/input.txt");
        Scanner reader = new Scanner(file);
        encryptionModel.setTextToEncrypt(reader.nextLine());
        encryptionModel.setKeyForOTP(reader.nextLine());
        encryptionModel.setCaesarOffset(Integer.parseInt(reader.nextLine()));
        reader.close();
        return encryptionModel;
    }

    private void writeTextToFile(final String textToWrite) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(
                "/home/andrei/Facultate/criptografie/tema1criptografie/src/main/resources/files/output.txt"));
        writer.write(textToWrite);
        writer.close();
    }

    private byte[] encryptOneTimePad(final byte[] text, final byte[] key) {
        final byte[] encodedMessage = new byte[text.length];
        for (int i = 0; i < text.length; i++) {
            encodedMessage[i] = (byte) (text[i] ^ key[i]);
        }
        return encodedMessage;
    }

    private String decryptOneTimePad(final byte[] text, final byte[] encodedBytes, final byte[] key) throws UnsupportedEncodingException {
        final byte[] decodedBytes = new byte[text.length];

        for (int i = 0; i < encodedBytes.length; i++) {
            decodedBytes[i] = (byte) (encodedBytes[i] ^ key[i]);
        }

        String str = new String(decodedBytes, "UTF-8");
        return str;
    }

    private String ceasarCipher(String message, int offset) {
        StringBuilder result = new StringBuilder();
        for (char character : message.toCharArray()) {
            if (character != ' ') {
                int originalAlphabetPosition = character - 'a';
                int newAlphabetPosition = (originalAlphabetPosition + offset) % 26;
                char newCharacter = (char) ('a' + newAlphabetPosition);
                result.append(newCharacter);
            } else {
                result.append(character);
            }
        }
        return result.toString();
    }

    private String caesarDecipher(String message, int offset) {
        return ceasarCipher(message, 26 - (offset % 26));
    }
}
