package com.example.studentutilitysoftware.GraphSim;

import com.example.studentutilitysoftware.Models.Graphs.Graph;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Pair;

import java.util.*;

public class GraphSimulatorController {

    @FXML private RadioButton addNodeMode;
    @FXML private RadioButton addEdgeMode;
    @FXML private RadioButton renameNodeMode;
    @FXML private RadioButton reassignEdgeMode;
    @FXML private RadioButton runDijkstraMode;
    @FXML private RadioButton clearCanvasMode;

    @FXML private Canvas graphCanvas;
    @FXML private TableView<Map.Entry<String, Integer>> resultsTable;
    @FXML private TableColumn<Map.Entry<String, Integer>, String> vertexColumn;
    @FXML private TableColumn<Map.Entry<String, Integer>, Integer> distanceColumn;

    private GraphicsContext gc;
    private final Graph graph = new Graph();
    private final List<Pair<Double, Double>> nodes = new ArrayList<>();
    private final Map<Circle, String> nodeMap = new HashMap<>();
    private final Map<Pair<Circle, Circle>, Integer> edgeMap = new HashMap<>();
    private Circle sourceNode = null;

    @FXML
    public void initialize() {
        gc = graphCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, graphCanvas.getWidth(), graphCanvas.getHeight());

        ToggleGroup modeGroup = new ToggleGroup();
        addNodeMode.setToggleGroup(modeGroup);
        addEdgeMode.setToggleGroup(modeGroup);
        renameNodeMode.setToggleGroup(modeGroup);
        reassignEdgeMode.setToggleGroup(modeGroup);
        runDijkstraMode.setToggleGroup(modeGroup);
        clearCanvasMode.setToggleGroup(modeGroup);

        graphCanvas.setOnMouseClicked(this::handleCanvasClick);

        vertexColumn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getKey()));
        distanceColumn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getValue()));
    }

    private void handleCanvasClick(MouseEvent event) {
        if (addNodeMode.isSelected()) {
            addNode(event.getX(), event.getY());
        } else if (addEdgeMode.isSelected()) {
            handleEdgeCreation(event.getX(), event.getY());
        } else if (renameNodeMode.isSelected()) {
            renameNode(event.getX(), event.getY());
        } else if (reassignEdgeMode.isSelected()) {
            reassignEdge(event.getX(), event.getY());
        } else if (runDijkstraMode.isSelected()) {
            runDijkstraAlgorithm();
        } else if (clearCanvasMode.isSelected()) {
            clearCanvas();
        }
    }

    private void addNode(double x, double y) {
        for (Pair<Double, Double> node : nodes) {
            if (Math.hypot(node.getKey() - x, node.getValue() - y) < 20) {
                showAlert(Alert.AlertType.ERROR, "Error", "Node too close to existing node.");
                return;
            }
        }

        gc.setFill(Color.DEEPSKYBLUE);
        gc.fillOval(x - 10, y - 10, 20, 20);

        String nodeName = "Node" + (nodes.size() + 1);
        gc.setFill(Color.BLACK);
        gc.fillText(nodeName, x - 15, y - 15);

        nodes.add(new Pair<>(x, y));
        graph.addVertex(nodeName);
        nodeMap.put(new Circle(x, y, 10), nodeName);
    }

    private void handleEdgeCreation(double x, double y) {
        for (Map.Entry<Circle, String> entry : nodeMap.entrySet()) {
            Circle node = entry.getKey();
            if (node.contains(x, y)) {
                if (sourceNode == null) {
                    sourceNode = node;
                    highlightNode(node, Color.RED);
                } else {
                    String sourceName = nodeMap.get(sourceNode);
                    String destinationName = entry.getValue();

                    TextInputDialog dialog = new TextInputDialog();
                    dialog.setTitle("Edge Weight");
                    dialog.setHeaderText("Enter weight for edge between " + sourceName + " and " + destinationName + ":");
                    dialog.showAndWait().ifPresent(weightStr -> {
                        try {
                            int weight = Integer.parseInt(weightStr);
                            graph.addEdge(sourceName, destinationName, weight);
                            edgeMap.put(new Pair<>(sourceNode, node), weight);

                            gc.setStroke(Color.BLACK);
                            gc.strokeLine(sourceNode.getCenterX(), sourceNode.getCenterY(), node.getCenterX(), node.getCenterY());
                            gc.setFill(Color.BLACK);
                            gc.fillText(weightStr, (sourceNode.getCenterX() + node.getCenterX()) / 2,
                                    (sourceNode.getCenterY() + node.getCenterY()) / 2);
                        } catch (NumberFormatException ex) {
                            showAlert(Alert.AlertType.ERROR, "Error", "Invalid weight entered.");
                        }
                    });

                    highlightNode(sourceNode, Color.DEEPSKYBLUE);
                    sourceNode = null;
                }
                return;
            }
        }
    }

    private void renameNode(double x, double y) {
        for (Map.Entry<Circle, String> entry : nodeMap.entrySet()) {
            Circle node = entry.getKey();
            if (node.contains(x, y)) {
                TextInputDialog dialog = new TextInputDialog(entry.getValue());
                dialog.setTitle("Rename Node");
                dialog.setHeaderText("Enter new name for " + entry.getValue() + ":");
                dialog.showAndWait().ifPresent(newName -> {
                    graph.renameVertex(entry.getValue(), newName);
                    nodeMap.put(node, newName);
                    redrawGraph();
                });
                return;
            }
        }
    }

    private void reassignEdge(double x, double y) {
        // Detect if the click is near an edge
        for (Map.Entry<Pair<Circle, Circle>, Integer> entry : edgeMap.entrySet()) {
            Circle sourceNode = entry.getKey().getKey();
            Circle destinationNode = entry.getKey().getValue();
            double midX = (sourceNode.getCenterX() + destinationNode.getCenterX()) / 2;
            double midY = (sourceNode.getCenterY() + destinationNode.getCenterY()) / 2;

            // Check if the click is near the midpoint of the edge
            if (Math.hypot(midX - x, midY - y) < 10) {
                // Show a dialog for edge modification
                ChoiceDialog<String> choiceDialog = new ChoiceDialog<>("Change Weight", "Change Weight", "Reassign Edge");
                choiceDialog.setTitle("Modify Edge");
                choiceDialog.setHeaderText("What would you like to do with this edge?");
                choiceDialog.setContentText("Choose an option:");

                choiceDialog.showAndWait().ifPresent(choice -> {
                    if (choice.equals("Change Weight")) {
                        changeEdgeWeight(sourceNode, destinationNode);
                    } else if (choice.equals("Reassign Edge")) {
                        reassignEdgeNodes(sourceNode, destinationNode);
                    }
                });
                return; // Exit after handling one edge
            }
        }

        // If no edge was clicked, show a message
        showAlert(Alert.AlertType.INFORMATION, "Info", "No edge selected. Please click near an edge.");
    }

    private void changeEdgeWeight(Circle sourceNode, Circle destinationNode) {
        TextInputDialog dialog = new TextInputDialog(String.valueOf(edgeMap.get(new Pair<>(sourceNode, destinationNode))));
        dialog.setTitle("Change Edge Weight");
        dialog.setHeaderText("Enter new weight for the edge:");
        dialog.showAndWait().ifPresent(weightStr -> {
            try {
                int weight = Integer.parseInt(weightStr);
                edgeMap.put(new Pair<>(sourceNode, destinationNode), weight);
                graph.updateEdge(nodeMap.get(sourceNode), nodeMap.get(destinationNode), weight);

                // Redraw the graph to update the edge weight
                redrawGraph();
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Invalid weight entered.");
            }
        });
    }

    private void reassignEdgeNodes(Circle sourceNode, Circle destinationNode) {
        sourceNode = null; // Reset the source node for reassignment
        showAlert(Alert.AlertType.INFORMATION, "Reassign Edge", "Click on the new source node.");

        Circle finalSourceNode = sourceNode;
        graphCanvas.setOnMouseClicked(event -> {
            Circle newSource = findNodeAt(event.getX(), event.getY());
            if (newSource != null) {
                showAlert(Alert.AlertType.INFORMATION, "Reassign Edge", "Click on the new destination node.");
                graphCanvas.setOnMouseClicked(event2 -> {
                    Circle newDestination = findNodeAt(event2.getX(), event2.getY());
                    if (newDestination != null && newSource != newDestination) {
                        // Prompt for new weight
                        TextInputDialog dialog = new TextInputDialog("1");
                        dialog.setTitle("Edge Weight");
                        dialog.setHeaderText("Enter weight for the new edge:");
                        dialog.showAndWait().ifPresent(weightStr -> {
                            try {
                                int weight = Integer.parseInt(weightStr);
                                graph.removeEdge(nodeMap.get(finalSourceNode), nodeMap.get(destinationNode)); // Remove old edge
                                edgeMap.remove(new Pair<>(finalSourceNode, destinationNode)); // Remove from edge map

                                graph.addEdge(nodeMap.get(newSource), nodeMap.get(newDestination), weight);
                                edgeMap.put(new Pair<>(newSource, newDestination), weight);

                                // Redraw the graph to reflect changes
                                redrawGraph();
                            } catch (NumberFormatException ex) {
                                showAlert(Alert.AlertType.ERROR, "Error", "Invalid weight entered.");
                            }
                        });
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Error", "Invalid destination node selected.");
                    }
                });
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Invalid source node selected.");
            }
        });
    }

    private Circle findNodeAt(double x, double y) {
        for (Circle node : nodeMap.keySet()) {
            if (node.contains(x, y)) {
                return node;
            }
        }
        return null;
    }

    private void runDijkstraAlgorithm() {
        ChoiceDialog<String> sourceDialog = new ChoiceDialog<>(nodeMap.values().iterator().next(), nodeMap.values());
        sourceDialog.setTitle("Run Dijkstra");
        sourceDialog.setHeaderText("Select source node:");
        sourceDialog.showAndWait().ifPresent(source -> {
            Map<String, Integer> distances = graph.dijkstra(source);
            resultsTable.getItems().clear();
            resultsTable.getItems().addAll(distances.entrySet());
        });
    }

    private void clearCanvas() {
        gc.clearRect(0, 0, graphCanvas.getWidth(), graphCanvas.getHeight());
        nodes.clear();
        nodeMap.clear();
        edgeMap.clear();
        graph.clear();
    }

    private void highlightNode(Circle node, Color color) {
        gc.setFill(color);
        gc.fillOval(node.getCenterX() - 10, node.getCenterY() - 10, 20, 20);
    }

    private void clearCanvasForRedraw() {
        gc.clearRect(0, 0, graphCanvas.getWidth(), graphCanvas.getHeight());
    }

    private void redrawGraph() {
        // Clear the canvas
        clearCanvasForRedraw();

        // Redraw nodes
        for (Map.Entry<Circle, String> entry : nodeMap.entrySet()) {
            Circle node = entry.getKey();
            String nodeName = entry.getValue();
            gc.setFill(Color.DEEPSKYBLUE);
            gc.fillOval(node.getCenterX() - 10, node.getCenterY() - 10, 20, 20);
            gc.setFill(Color.BLACK);
            gc.fillText(nodeName, node.getCenterX() - 15, node.getCenterY() - 15);
        }

        // Redraw edges
        for (Map.Entry<Pair<Circle, Circle>, Integer> edge : edgeMap.entrySet()) {
            Circle sourceNode = edge.getKey().getKey();
            Circle destinationNode = edge.getKey().getValue();
            int weight = edge.getValue();

            // Draw the edge
            gc.setStroke(Color.BLACK);
            gc.strokeLine(sourceNode.getCenterX(), sourceNode.getCenterY(), destinationNode.getCenterX(), destinationNode.getCenterY());

            // Draw the weight on the edge
            gc.setFill(Color.BLACK);
            gc.fillText(String.valueOf(weight), (sourceNode.getCenterX() + destinationNode.getCenterX()) / 2,
                    (sourceNode.getCenterY() + destinationNode.getCenterY()) / 2);
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.showAndWait();
    }
}
