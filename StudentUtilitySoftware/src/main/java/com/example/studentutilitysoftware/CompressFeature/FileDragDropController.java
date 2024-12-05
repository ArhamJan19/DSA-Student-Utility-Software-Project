package com.example.studentutilitysoftware.CompressFeature;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.FileChooser;

import java.io.File;

public class FileDragDropController {

    @FXML
    private Label dropZone;

    @FXML
    private Label statusLabel;

    @FXML
    private ProgressBar progressBar;

    private File selectedFile;

    @FXML
    public void initialize() {
        dropZone.setOnDragOver(event -> {
            if (event.getGestureSource() != dropZone && event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });

        dropZone.setOnDragDropped(this::handleFileDrop);
    }

    private void handleFileDrop(DragEvent event) {
        Dragboard dragboard = event.getDragboard();
        if (dragboard.hasFiles()) {
            selectedFile = dragboard.getFiles().get(0); // Get the first file
            if (validateFile(selectedFile)) {
                dropZone.setText("File Selected: " + selectedFile.getName());
                statusLabel.setText("Ready for Compression");
            } else {
                dropZone.setText("Invalid File");
                statusLabel.setText("Supported formats: .txt, .png, .jpg, .pdf");
            }
        }
        event.setDropCompleted(true);
        event.consume();
    }

    private boolean validateFile(File file) {
        String fileName = file.getName().toLowerCase();
        boolean isValid = fileName.endsWith(".txt") || fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".pdf");
        System.out.println("File validation result: " + isValid);
        return isValid;
    }

    @FXML
    private void compressFile() {
        if (selectedFile == null) {
            statusLabel.setText("No file selected!");
            return;
        }

        // Ask user to save the encoded text file
        FileChooser textFileChooser = new FileChooser();
        textFileChooser.setTitle("Save Encoded Text File");
        textFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text File (*.txt)", "*.txt"));
        File encodedFile = textFileChooser.showSaveDialog(null);

        if (encodedFile == null) {
            statusLabel.setText("Encoded file save operation cancelled!");
            return;
        }

        // Ask user to save the compressed file
        FileChooser compressedFileChooser = new FileChooser();
        compressedFileChooser.setTitle("Save Compressed File");
        compressedFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Compressed File (*.compressed)", "*.compressed"));
        File compressedFile = compressedFileChooser.showSaveDialog(null);

        if (compressedFile == null) {
            statusLabel.setText("Compressed file save operation cancelled!");
            return;
        }

        // Run the compression task
        Task<Void> compressionTask = createCompressionTask(selectedFile, encodedFile, compressedFile);
        progressBar.progressProperty().bind(compressionTask.progressProperty());

        compressionTask.setOnSucceeded(event -> {
            statusLabel.setText("Files Saved Successfully!");
            progressBar.progressProperty().unbind();
            progressBar.setProgress(1.0);
        });

        compressionTask.setOnFailed(event -> {
            statusLabel.setText("Compression Failed!");
            progressBar.progressProperty().unbind();
            progressBar.setProgress(0);
        });

        new Thread(compressionTask).start();
    }

    private Task<Void> createCompressionTask(File inputFile, File encodedFile, File compressedFile) {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                updateProgress(0, 100);

                // Step 1: Convert file to string
                FileConverter fileConverter = new FileConverter();
                String fileContent = fileConverter.convertFileToString(inputFile);
                if (fileContent == null || fileContent.isBlank()) {
                    throw new IllegalArgumentException("File content is empty or invalid!");
                }
                System.out.println("File content successfully converted to string.");
                updateProgress(20, 100);

                // Step 2: Compress using Huffman coding
                HuffmanCoding huffman = new HuffmanCoding();
                System.out.println("Starting compression...");
                huffman.compress(fileContent, encodedFile.getAbsolutePath(), compressedFile.getAbsolutePath());
                System.out.println("Compression completed successfully.");
                updateProgress(100, 100);

                return null;
            }
        };
    }
}
