package com.example.studentutilitysoftware.Models;

import java.util.Stack;

public class newExpenseStack {

    private Stack<Expense> expenseStack;

    public newExpenseStack() {
        expenseStack = new Stack<>();
    }

    // Push a new expense onto the stack
    public void pushExpense(Expense expense) {
        if (expense == null) {
            throw new IllegalArgumentException("Expense cannot be null");
        }
        expenseStack.push(expense);
        System.out.println("Expense added: " + expense);
    }

    // Pop the most recent expense off the stack
    public Expense popExpense() {
        if (expenseStack.isEmpty()) {
            System.out.println("No expenses to remove.");
            return null;
        }
        Expense removedExpense = expenseStack.pop();
        System.out.println("Expense removed: " + removedExpense);
        return removedExpense;
    }

    // Peek at the most recent expense without removing it
    public Expense peekExpense() {
        if (expenseStack.isEmpty()) {
            System.out.println("No expenses to display.");
            return null;
        }
        Expense topExpense = expenseStack.peek();
        System.out.println("Top expense: " + topExpense);
        return topExpense;
    }

    // Check if the stack is empty
    public boolean isEmpty() {
        return expenseStack.isEmpty();
    }

    // Get the total number of expenses in the stack
    public int getSize() {
        return expenseStack.size();
    }

    // Return the stack directly
    public Stack<Expense> getExpenses() {
        return expenseStack;
    }
}
