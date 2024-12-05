package com.example.studentutilitysoftware.CompressFeature;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

public class FileConverter {

    public String convertFileToString(File file) throws Exception {
        String fileName = file.getName().toLowerCase();
        Path filePath = file.toPath();

        if (fileName.endsWith(".txt")) {
            System.out.println("Converting text file...");
            return convertTextFileToString(filePath);
        } else if (fileName.endsWith(".png") || fileName.endsWith(".jpg")) {
            System.out.println("Converting image file to Base64...");
            return convertImageToBase64(filePath);
        } else if (fileName.endsWith(".pdf")) {
            System.out.println("Converting PDF file...");
            return convertPdfToString(file);
        } else {
            throw new IllegalArgumentException("Unsupported file type: " + fileName);
        }
    }

    private String convertTextFileToString(Path filePath) throws IOException {
        return Files.readString(filePath);
    }

    private String convertImageToBase64(Path filePath) throws IOException {
        byte[] fileBytes = Files.readAllBytes(filePath);
        return Base64.getEncoder().encodeToString(fileBytes);
    }

    private String convertPdfToString(File file) throws IOException {
        try (PDDocument document = PDDocument.load(file)) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            return pdfStripper.getText(document);
        }
    }

    public void writeCompressedFile(File outputFile, byte[] compressedData) throws IOException {
        try {
            Files.write(outputFile.toPath(), compressedData);
            System.out.println("Compressed file written successfully to: " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Error writing compressed file: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Writes an encoded string (e.g., Huffman encoded text) to a text file.
     *
     * @param outputFile  The file to write the encoded string to.
     * @param encodedText The encoded string to write.
     * @throws IOException If writing to the output file fails.
     */
    public void writeEncodedFile(File outputFile, String encodedText) throws IOException {
        try {
            Files.writeString(outputFile.toPath(), encodedText);
            System.out.println("Encoded text written successfully to: " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Error writing encoded text: " + e.getMessage());
            throw e;
        }
    }
}
