package Controller;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Stack;
import java.util.stream.Collectors;

public class EqTree implements Serializable {
    static class Node implements Serializable {
        String data;
        Node left;
        Node right;

        Node (String data){
            this.data = data;
            left = null;
            right = null;
        }

        Node (String data,Node left,Node right) {
            this.data = data;
            this.left = left;
            this.right = right;
        }

        Node (Node e) {
            data = e.data;
            left = e.left;
            right = e.right;
        }

        @Override
        public String toString() {
            if (data.matches("-?\\d+\\.?\\d*")) {
                return data;
            } else if (right == null)
                return left.toString();
            else
                return "(" + left.toString() + data + right.toString() + ")";
        }

        public float result() {
            if (data.matches("-?\\d+\\.?\\d*")) {
                return Float.parseFloat(data);
            }
            else {
                if (data.equals("+"))
                    return left.result() + right.result();
                else if (data.equals("-"))
                    return left.result() - right.result();
                else if (data.equals("*"))
                    return left.result() * right.result();
                else if (data.equals("/"))
                    return left.result() / right.result();
                else if (data.equals("("))
                    return left.result();
                else return 0;
            }
        }

    }

    public static Node root;

    public EqTree(String eq) {
        eq = eq.replaceAll("\\s","");
        eq = eq.replaceAll("\\)-",")+-");

        var num = Arrays.stream(eq.split("[+*/()]")).collect(Collectors.toCollection(ArrayDeque::new));
        var sym = Arrays.stream(String.join("",eq.split("-?\\d+(\\.\\d*)?")).split("")).collect(Collectors.toCollection(ArrayDeque::new));

        while (num.contains(""))
            num.remove("");

        while (sym.contains(""))
            sym.remove("");



        root = new Node(num.pop());

        Stack<Node> save = new Stack<>();

        int flag = 0;

        Node cur = root;

        String t = sym.pop();

        for (;;t = sym.pop()) {

            int check = flag;

            if (t.equals("("))
                flag++;
            else if (t.equals(")"))
                flag--;

            if (flag == 0) {
                if (t.equals(cur.data)) {
                    pushLeft(cur, num.pop(), t);
                } else if (t.equals("+")) {
                    pushLeft(cur, num.pop(), t);
                } else {
                    Node aux = new Node(t);
                    aux.left = cur.right;
                    aux.right = new Node(num.pop());
                    cur.right = aux;
                }
            } else if (flag > 0){
                save.add(cur);
                while (cur.right != null) {
                    cur = cur.right;
                }
                cur.left = new Node(sym.pop(),new Node(cur.data),new Node(num.pop()));
                cur.data = t;
                cur = cur.left;
                flag--;
            } else {
                cur = save.pop();
                if (cur.right != null) {
                    while (cur.right.right != null) {
                        cur = cur.right;
                    }
                    cur.right = cur.right.left;
                } else {
                    cur = cur.left;
                }
                flag++;
            }
            if (sym.size() == 0) break;
        }
    }

    public void pushLeft(Node cur,String right, String data) {
        cur.left = new Node(cur.data,new Node(cur.left),cur.right);
        cur.right = new Node(right);
        cur.data = data;
        ///System.out.println(cur);
    }

    public void pushRight(String left, String data) {
        Node aux = new Node(data);
        aux.right = root;
        aux.left = new Node(left);
        root = aux;
    }

    @Override
    public String toString() {

        return root.toString();

    }

    public float result() {

        return root.result();
    }

    public boolean check() {
        try {
            result();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}

