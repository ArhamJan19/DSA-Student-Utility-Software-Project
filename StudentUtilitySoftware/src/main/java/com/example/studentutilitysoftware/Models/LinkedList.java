package com.example.studentutilitysoftware.Models;

import java.util.ArrayList;
import java.util.List;

public class LinkedList {

    // Node class for the linked list
    private static class Node {
        Expense data;
        Node next;

        Node(Expense data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node head; // Head of the linked list

    public LinkedList() {
        this.head = null;
    }

    // Add a new expense to the linked list
    public void addExpense(Expense expense) {
        Node newNode = new Node(expense);
        if (head == null) {
            head = newNode;
        } else {
            Node current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
    }

    // Retrieve all expenses as a list
    public List<Expense> getExpenses() {
        List<Expense> expenses = new ArrayList<>();
        Node current = head;
        while (current != null) {
            expenses.add(current.data);
            current = current.next;
        }
        return expenses;
    }

    // Clear the linked list
    public void clear() {
        head = null;
    }

    // Check if the linked list is empty
    public boolean isEmpty() {
        return head == null;
    }
}
