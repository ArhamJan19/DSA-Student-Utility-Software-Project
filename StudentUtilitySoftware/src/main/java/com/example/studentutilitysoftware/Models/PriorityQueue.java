package com.example.studentutilitysoftware.Models;

import java.util.ArrayList;
import java.util.List;

public class PriorityQueue {

    // Inner class for heap nodes
    private static class Node {
        Expense data;

        Node(Expense data) {
            this.data = data;
        }
    }

    private List<Node> heap;

    public PriorityQueue() {
        heap = new ArrayList<>();
    }

    // Add an expense to the priority queue
    public void addExpense(Expense expense) {
        heap.add(new Node(expense));
        heapifyUp(heap.size() - 1);
    }

    // Retrieve and remove the highest-priority expense
    public Expense pollExpense() {
        if (heap.isEmpty()) {
            return null;
        }
        Expense topExpense = heap.get(0).data;
        Node lastNode = heap.remove(heap.size() - 1);
        if (!heap.isEmpty()) {
            heap.set(0, lastNode);
            heapifyDown(0);
        }
        return topExpense;
    }

    // Retrieve all expenses in sorted order without removing them
    public List<Expense> getAllExpenses() {
        List<Expense> sortedExpenses = new ArrayList<>();
        List<Node> tempHeap = new ArrayList<>(heap);

        while (!heap.isEmpty()) {
            sortedExpenses.add(pollExpense());
        }

        // Restore original heap
        heap = tempHeap;

        return sortedExpenses;
    }

    // Heapify up for maintaining heap property
    private void heapifyUp(int index) {
        int parentIndex = (index - 1) / 2;

        while (index > 0 && heap.get(index).data.getTitle().compareTo(heap.get(parentIndex).data.getTitle()) < 0) {
            // Swap nodes
            Node temp = heap.get(index);
            heap.set(index, heap.get(parentIndex));
            heap.set(parentIndex, temp);

            index = parentIndex;
            parentIndex = (index - 1) / 2;
        }
    }

    // Heapify down for maintaining heap property
    private void heapifyDown(int index) {
        int leftChildIndex = 2 * index + 1;
        int rightChildIndex = 2 * index + 2;
        int smallest = index;

        if (leftChildIndex < heap.size() &&
                heap.get(leftChildIndex).data.getTitle().compareTo(heap.get(smallest).data.getTitle()) < 0) {
            smallest = leftChildIndex;
        }

        if (rightChildIndex < heap.size() &&
                heap.get(rightChildIndex).data.getTitle().compareTo(heap.get(smallest).data.getTitle()) < 0) {
            smallest = rightChildIndex;
        }

        if (smallest != index) {
            // Swap nodes
            Node temp = heap.get(index);
            heap.set(index, heap.get(smallest));
            heap.set(smallest, temp);

            heapifyDown(smallest);
        }
    }
}
