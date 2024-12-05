package com.example.studentutilitysoftware.DecompresFeature;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.HashMap;

public class FileDecompressionUI {

    @FXML
    private Label compressedFileDropZone;

    @FXML
    private Label encodedStringDropZone;

    @FXML
    private Label huffmanCodesDropZone;

    @FXML
    private Label statusLabel;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Button DecompressButton;

    private File compressedFile;
    private File encodedStringFile;
    private File huffmanCodesFile;

    @FXML
    public void initialize() {
        setupDropZone(compressedFileDropZone, file -> compressedFile = file);
        setupDropZone(encodedStringDropZone, file -> encodedStringFile = file);
        setupDropZone(huffmanCodesDropZone, file -> huffmanCodesFile = file);

        DecompressButton.setOnAction(event -> {
            if (compressedFile == null || encodedStringFile == null || huffmanCodesFile == null) {
                statusLabel.setText("Please provide all required files.");
            } else {
                decompressFile();
            }
        });
    }

    private void setupDropZone(Label dropZone, java.util.function.Consumer<File> fileConsumer) {
        dropZone.setOnDragOver(event -> {
            if (event.getDragboard().hasFiles()) {
                event.acceptTransferModes(javafx.scene.input.TransferMode.COPY);
            }
            event.consume();
        });

        dropZone.setOnDragDropped(event -> {
            if (event.getDragboard().hasFiles()) {
                File file = event.getDragboard().getFiles().get(0);
                fileConsumer.accept(file);
                dropZone.setText("File Selected: " + file.getName());
                statusLabel.setText("File selected successfully.");
            }
            event.setDropCompleted(true);
            event.consume();
        });
    }

    private void decompressFile() {
        Task<Void> decompressionTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                try {
                    // Step 1: Read Huffman codes
                    updateMessage("Reading Huffman codes...");
                    HashMap<String, Character> huffmanCodes = readHuffmanCodes(huffmanCodesFile);
                    updateMessage("Huffman codes read successfully.");

                    // Step 2: Read encoded string
                    updateMessage("Reading encoded string...");
                    String encodedString = readEncodedString(encodedStringFile);
                    updateMessage("Encoded string read successfully.");

                    // Step 3: Decode the string
                    updateMessage("Decoding the string...");
                    String decodedString = decodeHuffmanString(encodedString, huffmanCodes);
                    updateMessage("String decoded successfully.");

                    // Step 4: Save decoded string (Run FileChooser on the JavaFX Application Thread)
                    final File[] outputFile = new File[1]; // Array to hold the file reference across threads
                    Platform.runLater(() -> {
                        try {
                            updateMessage("Choosing save location...");
                            outputFile[0] = chooseSaveLocation(); // Let user choose save location
                            if (outputFile[0] != null) {
                                saveDecodedString(outputFile[0], decodedString);
                                updateMessage("Decoded string saved successfully.");
                            } else {
                                updateMessage("Save operation canceled by user.");
                            }
                        } catch (Exception e) {
                            updateMessage("Error during file saving: " + e.getMessage());
                            System.err.println("Error during file saving: " + e.getMessage());
                        }
                    });

                    // Ensure the UI thread completes the file-saving operation
                    while (outputFile[0] == null) {
                        Thread.sleep(100); // Pause the background thread until file operation completes
                    }
                } catch (Exception e) {
                    updateMessage("Error occurred: " + e.getMessage());
                    System.err.println("Decompression error: " + e.getMessage());
                    throw e; // Rethrow to trigger failure handler
                }
                return null;
            }
        };

        decompressionTask.setOnSucceeded(event -> statusLabel.setText("Decompression completed successfully."));
        decompressionTask.setOnFailed(event -> statusLabel.setText("Decompression failed. Check logs for details."));
        decompressionTask.messageProperty().addListener((obs, oldMessage, newMessage) -> statusLabel.setText(newMessage));
        progressBar.progressProperty().bind(decompressionTask.progressProperty());

        new Thread(decompressionTask).start();
    }




    private HashMap<String, Character> readHuffmanCodes(File huffmanCodesFile) throws IOException {
        HashMap<String, Character> huffmanCodes = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(huffmanCodesFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(": ");
                if (parts.length == 2) {
                    String key = parts[1];
                    char value = parts[0].equals(" ") ? ' ' : parts[0].charAt(0);
                    huffmanCodes.put(key, value);
                    System.out.println("Loaded code: " + key + " -> " + value);
                } else {
                    System.err.println("Invalid Huffman code format: " + line);
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading Huffman codes: " + e.getMessage());
            throw e;
        }
        return huffmanCodes;
    }


    private String readEncodedString(File encodedStringFile) throws IOException {
        StringBuilder encodedString = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(encodedStringFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                encodedString.append(line);
                System.out.println("Read encoded line: " + line);
            }
        } catch (Exception e) {
            System.err.println("Error reading encoded string: " + e.getMessage());
            throw e;
        }
        return encodedString.toString();
    }


    private String decodeHuffmanString(String encodedString, HashMap<String, Character> huffmanCodes) {
        StringBuilder decodedString = new StringBuilder();
        StringBuilder currentCode = new StringBuilder();

        try {
            for (char bit : encodedString.toCharArray()) {
                currentCode.append(bit);
                if (huffmanCodes.containsKey(currentCode.toString())) {
                    char decodedChar = huffmanCodes.get(currentCode.toString());
                    decodedString.append(decodedChar);
                    System.out.println("Decoded: " + currentCode + " -> " + decodedChar);
                    currentCode.setLength(0); // Reset the current code
                }
            }

            if (currentCode.length() > 0) {
                System.err.println("Remaining unmatched code: " + currentCode);
            }
        } catch (Exception e) {
            System.err.println("Error during decoding: " + e.getMessage());
            throw e;
        }

        return decodedString.toString();
    }

    private void saveDecodedString(File outputFile, String decodedString) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            writer.write(decodedString);
            System.out.println("Decoded string saved to: " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error saving decoded string: " + e.getMessage());
            throw new RuntimeException("Failed to save decoded string.", e);
        }
    }


    private File chooseSaveLocation() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Decoded File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text File (*.txt)", "*.txt"));
        return fileChooser.showSaveDialog(null);
    }
}
