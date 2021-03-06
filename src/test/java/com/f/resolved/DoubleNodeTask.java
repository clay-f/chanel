package com.f.resolved;

import java.util.Objects;

public class DoubleNodeTask<T> {
    private DoubleNode first;
    private DoubleNode last;

    private class DoubleNode {
        private DoubleNode prev;
        private T item;
        private DoubleNode next;

        public DoubleNode(T item) {
            prev = null;
            this.item = item;
            next = null;
        }
    }

    public void addFirst(T t) {
        DoubleNode oldfirst = first;
        first = new DoubleNode(t);
        if (oldfirst == null) {
            last = first;
        } else {
            first.next = oldfirst;
            oldfirst.prev = first;
        }
    }

    public void addLast(T t) {
        Objects.requireNonNull(t);
        DoubleNode oldLast = last;
        last = new DoubleNode(t);
        if (oldLast == null) {
            first = last;
        } else {
            oldLast.next = last;
            last.prev = oldLast;
        }
    }

    public T removeFirst() {
        if (first.prev == null && first.next == null) {
            DoubleNode item = first;
            clean();
            return item.item;
        }
        DoubleNode oldfirst = first;
        first = oldfirst.next;
        oldfirst.next = null;
        first.prev = null;
        return oldfirst.item;
    }

    private void clean() {
        first = null;
        last = null;
    }

    public T removeLast() {
        Objects.requireNonNull(last);
        if (last.prev == null && last.next == null) {
            DoubleNode item = last;
            clean();
            return item.item;
        }
        DoubleNode item = last;
        last = last.prev;
        last.next = null;
        item.prev = null;
        return item.item;
    }

    public void addBefore(T target, T newVal) {
        addBefore(first, target, newVal);
    }

    private void addBefore(DoubleNode node, T target, T newVal) {
        if (node == null) return;
        if (node.item.equals(target)) {
            DoubleNode newNode = new DoubleNode(newVal);
            if (node.prev == null) {
                node.prev = newNode;
                newNode.next = node;
            } else {
                DoubleNode oldnode = node.prev;
                oldnode.next = newNode;
                newNode.prev = oldnode;
                newNode.next = node;
                node.prev = newNode;
            }
        } else {
            addBefore(node.next, target, newVal);
        }
    }

    public void addAfter(T target, T newVal) {
        Objects.requireNonNull(target);
        Objects.requireNonNull(newVal);
        addAfter(first, target, newVal);
    }

    private void addAfter(DoubleNode node, T target, T newVal) {
        if (node == null) return;
        if (node.item.equals(target)) {
            DoubleNode newNode = new DoubleNode(newVal);
            if (node.next == null) {
                node.next = newNode;
                newNode.prev = node;
            } else {
                DoubleNode oldnext = node.next;
                node.next = newNode;
                newNode.prev = node;
                newNode.next = oldnext;
                oldnext.prev = newNode;
            }
        } else {
            addAfter(node.next, target, newVal);
        }
    }

    public T remove(T t) {
        Objects.requireNonNull(t);
        return remove(first, t);
    }

    private T remove(DoubleNode node, T t) {
        if (node == null) return null;
        if (node.item.equals(t)) {
            if (node.next == null && node.prev == null) {
                last = null;
                DoubleNode item = node;
                first = null;
                return item.item;
            } else if (node.next == null) {
                DoubleNode prev = node.prev;
                DoubleNode item = node;
                prev.next = null;
                item.prev = null;
                last = prev;
                return item.item;
            } else {
                DoubleNode prev = node.prev;
                DoubleNode next = node.next;
                DoubleNode item = node;
                prev.next = next;
                item.prev = null;
                item.next = null;
                next.prev = prev;
                return item.item;
            }
        } else {
            return remove(node.next, t);
        }
    }

    public void show() {
        show(first);
    }

    private void show(DoubleNode doubleNode) {
        for (DoubleNode item = doubleNode; item != null; item = item.next) {
            System.out.print(item.item + " ");
        }
    }

    public static void main(String[] args) {
        DoubleNodeTask<String> doubleNodeTask = new DoubleNodeTask<>();
        doubleNodeTask.addLast("a");
        doubleNodeTask.removeFirst();
        doubleNodeTask.addFirst("a");
        doubleNodeTask.removeLast();
        doubleNodeTask.show();
    }
}
