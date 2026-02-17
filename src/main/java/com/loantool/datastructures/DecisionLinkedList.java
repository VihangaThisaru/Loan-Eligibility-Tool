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

    @Override
    public Iterator<LoanDecision> iterator() {
        return new Iterator<LoanDecision>() {
            private DecisionNode current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public LoanDecision next() {
                LoanDecision decision = current.decision;
                current = current.next;
                return decision;
            }
        };
    }

    // Get a string values for auit display
    public String getAuditTrail() {
        StringBuilder sb = new StringBuilder();
        sb.append("Decision Audit Trail (Linked List)\n");
        sb.append("================================\n");
        DecisionNode current = head;
        int index = 1;
        while (current != null) {
            sb.append(index++).append(". ").append(current.decision.getApplicant().getId())
                    .append(" -> ").append(current.decision.getRiskTier().getDisplayName())
                    .append(" | ");
            if (current.next != null) {
                sb.append("next: ").append(current.next.decision.getApplicant().getId());
            } else {
                sb.append("(tail)");
            }
            sb.append("\n");
            current = current.next;
        }
        return sb.toString();
    }
}
