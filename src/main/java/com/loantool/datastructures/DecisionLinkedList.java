package com.loantool.datastructures;

import com.loantool.models.LoanDecision;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//Singly Linked List for storing loan decisions in chronological order
public class DecisionLinkedList implements Iterable<LoanDecision> {
    private DecisionNode head;
    private DecisionNode tail;
    private int size;

    private static class DecisionNode {
        LoanDecision decision;
        DecisionNode next;

        DecisionNode(LoanDecision decision) {
            this.decision = decision;
        }
    }

    public void add(LoanDecision decision) {
        DecisionNode newNode = new DecisionNode(decision);
        if (head == null) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    public void addAll(List<LoanDecision> decisions) {
        for (LoanDecision d : decisions) {
            add(d);
        }
    }

    public LoanDecision get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        DecisionNode current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.decision;
    }

    public List<LoanDecision> toList() {
        List<LoanDecision> list = new ArrayList<>();
        DecisionNode current = head;
        while (current != null) {
            list.add(current.decision);
            current = current.next;
        }
        return list;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return head == null;
    }

    public void clear() {
        head = tail = null;
        size = 0;
    }

}
