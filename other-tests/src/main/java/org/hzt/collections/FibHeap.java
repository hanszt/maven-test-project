package org.hzt.collections;

// Operations on Fibonacci Heap in Java

// Node creation
class Node {
    Node parent;
    Node left;
    Node right;
    Node child;
    int degree;
    boolean mark;
    int key;

    public Node() {
        this.degree = 0;
        this.mark = false;
        this.parent = null;
        this.left = this;
        this.right = this;
        this.child = null;
        this.key = Integer.MAX_VALUE;
    }

    Node(int x) {
        this();
        this.key = x;
    }

    void setParent(Node x) {
        this.parent = x;
    }

    Node getParent() {
        return this.parent;
    }

    void setLeft(Node x) {
        this.left = x;
    }

    Node getLeft() {
        return this.left;
    }

    void setRight(Node x) {
        this.right = x;
    }

    Node getRight() {
        return this.right;
    }

    void setChild(Node x) {
        this.child = x;
    }

    Node getChild() {
        return this.child;
    }

    void setDegree(int x) {
        this.degree = x;
    }

    int getDegree() {
        return this.degree;
    }

    void setMark(boolean m) {
        this.mark = m;
    }

    boolean getMark() {
        return this.mark;
    }

    void setKey(int x) {
        this.key = x;
    }

    int getKey() {
        return this.key;
    }
}

/**
 * @see <a href="https://www.programiz.com/dsa/fibonacci-heap">Fibonacci Heap</a>
 */
public class FibHeap {
    Node min;
    int n;
    boolean trace;
    Node found;

    public boolean getTrace() {
        return trace;
    }

    public void setTrace(boolean t) {
        this.trace = t;
    }

    public static FibHeap create_heap() {
        return new FibHeap();
    }

    FibHeap() {
        min = null;
        n = 0;
        trace = false;
    }

    private void insert(Node x) {
        if (min == null) {
            min = x;
            x.setLeft(min);
            x.setRight(min);
        } else {
            x.setRight(min);
            x.setLeft(min.getLeft());
            min.getLeft().setRight(x);
            min.setLeft(x);
            if (x.getKey() < min.getKey()) {
                min = x;
            }
        }
        n += 1;
    }

    public void insert(int key) {
        insert(new Node(key));
    }

    public void display() {
        display(min);
        System.out.println();
    }

    private void display(Node node) {
        System.out.print("(");
        if (node == null) {
            System.out.print(")");
        } else {
            Node current = node;
            do {
                System.out.print(current.getKey());
                Node child = current.getChild();
                display(child);
                System.out.print("->");
                current = current.getRight();
            } while (current != node);
            System.out.print(")");
        }
    }

    public static void merge_heap(FibHeap H1, FibHeap H2, FibHeap H3) {
        H3.min = H1.min;

        if (H1.min != null && H2.min != null) {
            Node t1 = H1.min.getLeft();
            Node t2 = H2.min.getLeft();
            H1.min.setLeft(t2);
            t1.setRight(H2.min);
            H2.min.setLeft(t1);
            t2.setRight(H1.min);
        }
        if (H1.min == null || (H2.min != null && H2.min.getKey() < H1.min.getKey())) {
            H3.min = H2.min;
        }
        H3.n = H1.n + H2.n;
    }

    public int find_min() {
        return this.min.getKey();
    }

    private void display_node(Node z) {
        System.out.println("right: " + ((z.getRight() == null) ? "-1" : z.getRight().getKey()));
        System.out.println("left: " + ((z.getLeft() == null) ? "-1" : z.getLeft().getKey()));
        System.out.println("child: " + ((z.getChild() == null) ? "-1" : z.getChild().getKey()));
        System.out.println("degree " + z.getDegree());
    }

    public int extract_min() {
        Node z = this.min;
        if (z != null) {
            Node c = z.getChild();
            Node k = c, p;
            if (c != null) {
                do {
                    p = c.getRight();
                    insert(c);
                    c.setParent(null);
                    c = p;
                } while (c != null && c != k);
            }
            z.getLeft().setRight(z.getRight());
            z.getRight().setLeft(z.getLeft());
            z.setChild(null);
            if (z == z.getRight()) {
                this.min = null;
            } else {
                this.min = z.getRight();
                this.consolidate();
            }
            this.n -= 1;
            return z.getKey();
        }
        return Integer.MAX_VALUE;
    }

    public void consolidate() {
        double phi = (1 + Math.sqrt(5)) / 2;
        int Dofn = (int) (Math.log(this.n) / Math.log(phi));
        Node[] A = new Node[Dofn + 1];
        for (int i = 0; i <= Dofn; ++i) {
            A[i] = null;
        }
        Node w = min;
        if (w != null) {
            Node check = min;
            do {
                Node x = w;
                int d = x.getDegree();
                while (A[d] != null) {
                    Node y = A[d];
                    if (x.getKey() > y.getKey()) {
                        Node temp = x;
                        x = y;
                        y = temp;
                        w = x;
                    }
                    fib_heap_link(y, x);
                    check = x;
                    A[d] = null;
                    d += 1;
                }
                A[d] = x;
                w = w.getRight();
            } while (w != null && w != check);
            this.min = null;
            for (int i = 0; i <= Dofn; ++i) {
                if (A[i] != null) {
                    insert(A[i]);
                }
            }
        }
    }

    // Linking operation
    private void fib_heap_link(Node y, Node x) {
        y.getLeft().setRight(y.getRight());
        y.getRight().setLeft(y.getLeft());

        Node p = x.getChild();
        if (p == null) {
            y.setRight(y);
            y.setLeft(y);
        } else {
            y.setRight(p);
            y.setLeft(p.getLeft());
            p.getLeft().setRight(y);
            p.setLeft(y);
        }
        y.setParent(x);
        x.setChild(y);
        x.setDegree(x.getDegree() + 1);
        y.setMark(false);
    }

    // Search operation
    private void find(int key, Node c) {
        if (found == null && c != null) {
            Node temp = c;
            do {
                if (key == temp.getKey()) {
                    found = temp;
                } else {
                    Node k = temp.getChild();
                    find(key, k);
                    temp = temp.getRight();
                }
            } while (temp != c && found == null);
        }
    }

    public Node find(int k) {
        found = null;
        find(k, this.min);
        return found;
    }

    public void decrease_key(int key, int nval) {
        Node x = find(key);
        decrease_key(x, nval);
    }

    // Decrease key operation
    private void decrease_key(Node x, int k) {
        if (k > x.getKey()) {
            return;
        }
        x.setKey(k);
        Node y = x.getParent();
        if (y != null && x.getKey() < y.getKey()) {
            cut(x, y);
            cascading_cut(y);
        }
        if (x.getKey() < min.getKey()) {
            min = x;
        }
    }

    // Cut operation
    private void cut(Node x, Node y) {
        x.getRight().setLeft(x.getLeft());
        x.getLeft().setRight(x.getRight());

        y.setDegree(y.getDegree() - 1);

        x.setRight(null);
        x.setLeft(null);
        insert(x);
        x.setParent(null);
        x.setMark(false);
    }

    private void cascading_cut(Node y) {
        Node z = y.getParent();
        if (z != null) {
            if (y.getMark() == false) {
                y.setMark(true);
            } else {
                cut(y, z);
                cascading_cut(z);
            }
        }
    }

    // Delete operations
    public void delete(Node x) {
        decrease_key(x, Integer.MIN_VALUE);
        int p = extract_min();
    }

    public static void main(String... args) {
        FibHeap obj = create_heap();
        obj.insert(7);
        obj.insert(26);
        obj.insert(30);
        obj.insert(39);
        obj.insert(10);
        obj.display();

        System.out.println(obj.extract_min());
        obj.display();
        System.out.println(obj.extract_min());
        obj.display();
        System.out.println(obj.extract_min());
        obj.display();
        System.out.println(obj.extract_min());
        obj.display();
        System.out.println(obj.extract_min());
        obj.display();
    }
}
