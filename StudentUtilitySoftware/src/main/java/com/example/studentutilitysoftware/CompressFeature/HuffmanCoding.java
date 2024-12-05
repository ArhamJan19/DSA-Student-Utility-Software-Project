//package com.example.studentutilitysoftware;
//
//import java.io.*;
//import java.util.HashMap;
//import java.util.PriorityQueue;
//import java.util.zip.DeflaterOutputStream;
//
//public class HuffmanCoding {
//
//    private HashMap<Character, String> huffmanCode;
//
//    public void compress(String text, String encodedOutputPath, String compressedOutputPath) throws Exception {
//        // Step 1: Calculate frequency and build Huffman Tree
//        HashMap<Character, Integer> frequency = calculateFrequency(text);
//        HuffmanNode root = buildHuffmanTree(frequency);
//
//        // Step 2: Generate Huffman codes
//        huffmanCode = new HashMap<>();
//        generateHuffmanCodes(root, "");
//
//        // Debug: Log Huffman Codes
//        System.out.println("Huffman Codes: " + huffmanCode);
//
//        // Step 3: Encode the text
//        StringBuilder encodedString = new StringBuilder();
//        for (char c : text.toCharArray()) {
//            if (!huffmanCode.containsKey(c)) {
//                throw new IllegalArgumentException("Character not found in Huffman Code Map: " + c);
//            }
//            encodedString.append(huffmanCode.get(c));
//        }
//        System.out.println("Encoded String Length: " + encodedString.length());
//
//        // Step 4: Save the encoded string to a text file
//        saveEncodedString(encodedString.toString(), encodedOutputPath);
//
//        // Step 5: Compress the encoded string using Deflater
//        compressEncodedString(encodedString.toString(), compressedOutputPath);
//
//        System.out.println("Compression completed successfully.");
//    }
//
//    private HashMap<Character, Integer> calculateFrequency(String text) {
//        HashMap<Character, Integer> frequency = new HashMap<>();
//        for (char c : text.toCharArray()) {
//            frequency.put(c, frequency.getOrDefault(c, 0) + 1);
//        }
//        System.out.println("Frequency Table: " + frequency);
//        return frequency;
//    }
//
//    private HuffmanNode buildHuffmanTree(HashMap<Character, Integer> frequency) {
//        PriorityQueue<HuffmanNode> pq = new PriorityQueue<>();
//        frequency.forEach((character, freq) -> pq.add(new HuffmanNode(character, freq)));
//
//        while (pq.size() > 1) {
//            HuffmanNode left = pq.poll();
//            HuffmanNode right = pq.poll();
//            HuffmanNode parent = new HuffmanNode('\0', left.freq + right.freq, left, right);
//            pq.add(parent);
//        }
//        System.out.println("Huffman Tree Built Successfully.");
//        return pq.poll();
//    }
//
//    private void generateHuffmanCodes(HuffmanNode node, String code) {
//        if (node == null) return;
//
//        if (node.left == null && node.right == null) {
//            huffmanCode.put(node.character, code);
//            System.out.println("Character: " + node.character + " -> Code: " + code); // Debug log
//        }
//
//        generateHuffmanCodes(node.left, code + "0");
//        generateHuffmanCodes(node.right, code + "1");
//    }
//
//    private void saveEncodedString(String encodedString, String filePath) throws IOException {
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
//            writer.write(encodedString);
//        }
//        System.out.println("Encoded string saved to: " + filePath);
//    }
//
//    private void compressEncodedString(String encodedString, String compressedOutputPath) throws IOException {
//        try (FileOutputStream fos = new FileOutputStream(compressedOutputPath);
//             DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(fos)) {
//            deflaterOutputStream.write(encodedString.getBytes());
//        }
//        System.out.println("Compressed file saved to: " + compressedOutputPath);
//    }
//
//    private static class HuffmanNode implements Comparable<HuffmanNode> {
//        char character;
//        int freq;
//        HuffmanNode left, right;
//
//        HuffmanNode(char character, int freq) {
//            this.character = character;
//            this.freq = freq;
//        }
//
//        HuffmanNode(char character, int freq, HuffmanNode left, HuffmanNode right) {
//            this.character = character;
//            this.freq = freq;
//            this.left = left;
//            this.right = right;
//        }
//
//        @Override
//        public int compareTo(HuffmanNode o) {
//            return this.freq - o.freq;
//        }
//    }
//}


package com.example.studentutilitysoftware.CompressFeature;

import com.example.studentutilitysoftware.Models.HuffmanTree;
import javafx.application.Platform;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.HashMap;
import java.util.zip.DeflaterOutputStream;

public class HuffmanCoding {

    private HashMap<Character, String> huffmanCode;

    public void compress(String text, String encodedOutputPath, String compressedOutputPath) throws Exception {
        // Step 1: Calculate frequency
        HashMap<Character, Integer> frequency = calculateFrequency(text);

        // Step 2: Build Huffman Tree and generate codes
        HuffmanTree huffmanTree = new HuffmanTree(frequency);
        huffmanCode = huffmanTree.getHuffmanCode();

        // Debug: Log Huffman Codes
        System.out.println("Huffman Codes: " + huffmanCode);

        // Step 3: Encode the text
        StringBuilder encodedString = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (!huffmanCode.containsKey(c)) {
                throw new IllegalArgumentException("Character not found in Huffman Code Map: " + c);
            }
            encodedString.append(huffmanCode.get(c));
        }
        System.out.println("Encoded String Length: " + encodedString.length());

        // Step 4: Save the encoded string to a text file
        saveEncodedString(encodedString.toString(), encodedOutputPath);

        // Step 5: Compress the encoded string using Deflater
        compressEncodedString(encodedString.toString(), compressedOutputPath);

        letUserChooseAndSaveHuffmanCodes(huffmanCode);

        System.out.println("Compression completed successfully.");
    }

    private HashMap<Character, Integer> calculateFrequency(String text) {
        HashMap<Character, Integer> frequency = new HashMap<>();
        for (char c : text.toCharArray()) {
            frequency.put(c, frequency.getOrDefault(c, 0) + 1);
        }
        System.out.println("Frequency Table: " + frequency);
        return frequency;
    }

    private void saveEncodedString(String encodedString, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(encodedString);
        }
        System.out.println("Encoded string saved to: " + filePath);
    }

    private void compressEncodedString(String encodedString, String compressedOutputPath) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(compressedOutputPath);
             DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(fos)) {
            deflaterOutputStream.write(encodedString.getBytes());
        }
        System.out.println("Compressed file saved to: " + compressedOutputPath);
    }

    public void letUserChooseAndSaveHuffmanCodes(HashMap<Character, String> huffmanCode) {
        Platform.runLater(() -> {
            try {
                // Create a FileChooser instance
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save Huffman Codes");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Huffman Codes File (*.txt)", "*.txt"));

                // Show the Save dialog
                File chosenFile = fileChooser.showSaveDialog(null);

                // Check if the user selected a file
                if (chosenFile != null) {
                    saveHuffmanCodes(huffmanCode, chosenFile.getAbsolutePath());
                    System.out.println("Huffman codes saved to: " + chosenFile.getAbsolutePath());
                } else {
                    System.out.println("Save operation cancelled.");
                }
            } catch (Exception e) {
                System.err.println("An error occurred while saving Huffman codes: " + e.getMessage());
            }
        });
    }

    private void saveHuffmanCodes(HashMap<Character, String> huffmanCode, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (HashMap.Entry<Character, String> entry : huffmanCode.entrySet()) {
                char character = entry.getKey();
                String code = entry.getValue();

                // Write each character and its code to the file
                if (character == '\n') {
                    writer.write("\\n: " + code); // Handle newline character
                } else if (character == '\r') {
                    writer.write("\\r: " + code); // Handle carriage return
                } else if (character == '\t') {
                    writer.write("\\t: " + code); // Handle tab character
                } else {
                    writer.write(character + ": " + code);
                }
                writer.newLine();
            }
        }
        System.out.println("Huffman codes saved to: " + filePath);
    }

}
