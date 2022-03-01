import java.util.Scanner;

@SuppressWarnings("unchecked")
class CollectionException extends Exception {
    public CollectionException(String msg) {
        super(msg);
    }
}


@SuppressWarnings("unchecked")
interface Collection {
    static final String ERR_MSG_EMPTY = "Collection is empty.";
    static final String ERR_MSG_FULL = "Collection is full.";

    boolean isEmpty();

    boolean isFull();

    int size();

    String toString();
}


@SuppressWarnings("unchecked")
interface Stack<T> extends Collection {
    T top() throws CollectionException;

    void push(T x) throws CollectionException;

    T pop() throws CollectionException;
}


@SuppressWarnings("unchecked")
interface Deque<T> extends Collection {
    T front() throws CollectionException;

    T back() throws CollectionException;

    void enqueue(T x) throws CollectionException;

    void enqueueFront(T x) throws CollectionException;

    T dequeue() throws CollectionException;

    T dequeueBack() throws CollectionException;
}


@SuppressWarnings("unchecked")
interface Sequence<T> extends Collection {
    static final String ERR_MSG_INDEX = "Wrong index in sequence.";

    T get(int i) throws CollectionException;

    void add(T x) throws CollectionException;
}

@SuppressWarnings("unchecked")
class ArrayDeque<T> implements Deque<T>, Stack<T>, Sequence<T> {
    private static final int DEFAULT_CAPACITY = 64;

    // Tukaj napiši svojo kodo.
    private T[] FIELD = (T[]) new Object[DEFAULT_CAPACITY];
    private int CURRENT_SIZE = 0;
    private int FRONT = 0;
    private int BACK = 0;

    //COLLECTION:
    @Override
    public boolean isEmpty() {
        return CURRENT_SIZE == 0;
    }

    @Override
    public boolean isFull() {
        return CURRENT_SIZE == DEFAULT_CAPACITY;
    }

    @Override
    public int size() {
        return this.CURRENT_SIZE;
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        //str.append("[");
        if (this.CURRENT_SIZE > 0) {
            str.append(this.FIELD[this.FRONT]);
        }
        for (int i = 0; i < this.CURRENT_SIZE - 1; i++) {
            str.append(" ").append(this.FIELD[next(this.FRONT + i)]);
        }
        //str.append("]");
        return str.toString();
    }

    private int next(int elem) {
        return (elem + 1) % DEFAULT_CAPACITY;
    }

    private int prev(int elem) {
        return (DEFAULT_CAPACITY + elem - 1) % DEFAULT_CAPACITY;
    }

    // STACK
    @Override
    public T top() throws CollectionException {
        if (isEmpty()) {
            throw new CollectionException(ERR_MSG_EMPTY);
        }
        return this.FIELD[prev(this.BACK)];
    }

    @Override
    public void push(T x) throws CollectionException {
        if (isFull()) {
            throw new CollectionException(ERR_MSG_FULL);
        }
        this.FIELD[this.BACK] = x;
        this.BACK = next(this.BACK);
        this.CURRENT_SIZE++;

    }

    @Override
    public T pop() throws CollectionException {
        if (isEmpty()) {
            throw new CollectionException(ERR_MSG_EMPTY);
        }
        try {
            this.BACK = prev(this.BACK);
            return this.FIELD[this.BACK];
        } finally {
            this.FIELD[this.BACK] = null;
            this.CURRENT_SIZE--;
        }
    }

    @Override
    public T front() throws CollectionException {
        if (isEmpty()) {
            throw new CollectionException(ERR_MSG_EMPTY);
        }
        return this.FIELD[this.FRONT];
    }

    @Override
    public T back() throws CollectionException {
        return this.top();
    }

    // DEQUEUE
    @Override
    public void enqueue(T x) throws CollectionException {
        this.push(x);
    }

    @Override
    public void enqueueFront(T x) throws CollectionException {
        if (isFull()) {
            throw new CollectionException(ERR_MSG_FULL);
        }
        this.FRONT = prev(this.FRONT);
        this.FIELD[this.FRONT] = x;
        this.CURRENT_SIZE++;
    }

    @Override
    public T dequeueBack() throws CollectionException {
        return this.pop();
    }

    @Override
    public T dequeue() throws CollectionException {
        if (isEmpty()) {
            throw new CollectionException(ERR_MSG_EMPTY);
        }
        try {
            return this.FIELD[this.FRONT];
        } finally {
            this.FIELD[this.FRONT] = null;
            this.FRONT = next(this.FRONT);
            this.CURRENT_SIZE--;
        }
    }

    // SEQUENCE
    private int index(int i) {
        return (this.FRONT + i) % DEFAULT_CAPACITY;  // get real index of element
    }

    @Override
    public T get(int i) throws CollectionException {
        if (isEmpty()) {
            throw new CollectionException(ERR_MSG_EMPTY);
        }
        if (i < 0 || i > this.CURRENT_SIZE) {
            throw new CollectionException(ERR_MSG_INDEX);
        }
        return this.FIELD[this.index(i)];
    }

    @Override
    public void add(T x) throws CollectionException {
        this.push(x);
    }
}

//// ............................................

@SuppressWarnings("unchecked")
class Kalkulator {

    Sequence<Stack<String>> seq;
    boolean cond;
    String line;
    int instructIndex;
    Scanner sc_n;

    Kalkulator() throws CollectionException {
        instructIndex = 0;
        cond = false;
        this.seq = new ArrayDeque<>();
        for (int i = 0; i < 42; i++) {
            Stack<String> _1 = new ArrayDeque<>();
            this.seq.add(_1);
        }
        this.start();
    }

    private void start() throws CollectionException {
        Scanner sc_v;
        sc_v = new Scanner(System.in);
        while (sc_v.hasNextLine()) {
            line = sc_v.nextLine();
            sc_n = new Scanner(line);
            while (sc_n.hasNext()) {
                instruction(sc_n.next());
            }
            reset();
        }
    }

    private void reset() throws CollectionException {
        this.instructIndex = 0;
        this.cond = false;
        this.seq = new ArrayDeque<>();
        for (int i = 0; i < 42; i++) {
            Stack<String> _1 = new ArrayDeque<>();
            this.seq.add(_1);
        }
    }

    private void instruction(String instr) throws CollectionException {
        if (instr.charAt(0) == '?') {
            if (this.cond) {
                instr = instr.substring(1);
            } else {
                return;
            }
        }
        switch (instr) {
            case ("echo"): {
                this.echo();
                break;
            }
            case ("pop"): {
                this.pop();
                break;
            }
            case ("dup"): {
                this.dup();
                break;
            }
            case ("dup2"): {
                this.dup2();
                break;
            }
            case ("swap"): {
                this.swap();
                break;
            }
            case ("char"): {
                this.charact();
                break;
            }
            case ("even"): {
                this.even();
                break;
            }
            case ("odd"): {
                this.odd();
                break;
            }
            case ("!"): {
                this.factoriel();
                break;
            }
            case ("len"): {
                this.len();
                break;
            }
            case ("<>"): {
                this.notEqual();
                break;
            }
            case ("<"): {
                this.lower();
                break;
            }
            case ("<="): {
                this.lowerEqual();
                break;
            }
            case ("=="): {
                this.equal();
                break;
            }
            case (">"): {
                this.higher();
                break;
            }
            case (">="): {
                this.higherEqual();
                break;
            }
            case ("+"): {
                this.sum();
                break;
            }
            case ("-"): {
                this.sub();
                break;
            }
            case ("*"): {
                this.multip();
                break;
            }
            case ("/"): {
                this.divide();
                break;
            }
            case ("%"): {
                this.remain();
                break;
            }
            case ("."): {
                this.stick();
                break;
            }
            case ("rnd"): {
                this.rnd();
                break;
            }
            case ("then"): {
                this.then();
                break;
            }
            case ("else"): {
                this._else();
                break;
            }
            case ("print"): {
                this.print();
                break;
            }
            case ("clear"): {
                this.clear();
                break;
            }
            case ("run"): {
                this.run(Integer.parseInt(this.seq.get(0).pop()));
                break;
            }
            case ("loop"): {
                this.loop();
                break;
            }
            case ("fun"): {
                this.fun();
                break;
            }
            case ("move"): {
                this.move();
                break;
            }
            case ("reverse"): {
                this.reverse();
                break;
            }
            default: {
                this.pushTo0(instr);
                break;
            }
        }
    }

    private Stack<String> reverseRet(Stack<String> stack) throws CollectionException {
        Stack<String> newStack = new ArrayDeque<>();
        Stack<String> tempStack = new ArrayDeque<>();
        while (!stack.isEmpty()) {
            String x = stack.pop();
            newStack.push(x);
            tempStack.push(x);
        }

        while (!tempStack.isEmpty()) {
            stack.push(tempStack.pop());
        }
        return newStack;
    }

    private void pushTo0(String x) throws CollectionException {
        this.seq.get(0).push(x);
    }

    private void echo() throws CollectionException {
        if (!this.seq.get(0).isEmpty()) {
            System.out.println(this.seq.get(0).top());
        } else {
            System.out.println();
        }
    }

    private void pop() throws CollectionException {
        seq.get(0).pop();
    }

    private void dup() throws CollectionException {
        this.pushTo0(this.seq.get(0).top());
    }

    private void dup2() throws CollectionException {
        String _1 = this.seq.get(0).pop();
        String _2 = this.seq.get(0).pop();
        this.pushTo0(_2);
        this.pushTo0(_1);
        this.pushTo0(_2);
        this.pushTo0(_1);
    }

    private void swap() throws CollectionException {
        String _1 = this.seq.get(0).pop();
        String _2 = this.seq.get(0).pop();
        this.pushTo0(_1);
        this.pushTo0(_2);
    }

    private void charact() throws CollectionException {
        char _1 = (char) Integer.parseInt(this.seq.get(0).pop());
        this.pushTo0(Character.toString(_1));
    }

    private void even() throws CollectionException {
        if (Integer.parseInt(this.seq.get(0).pop()) % 2 == 0) {
            this.pushTo0(String.valueOf(1));
        } else {
            this.pushTo0(String.valueOf(0));
        }
    }

    private void odd() throws CollectionException {
        int val = Integer.parseInt(this.seq.get(0).pop());
        if (val % 3 == 0 && val != 0) {
            this.pushTo0(String.valueOf(1));
        } else {
            this.pushTo0(String.valueOf(0));
        }
    }

    private void factoriel() throws CollectionException {
        int fc = Integer.parseInt(this.seq.get(0).pop());
        int val = fc >= 0 ? 1 : -1;
        for (int i = 2; i <= Math.abs(fc); i++) {
            val *= i;
        }
        this.pushTo0(String.valueOf(val));
    }

    private void len() throws CollectionException {
        String _1 = this.seq.get(0).pop();
        this.pushTo0(String.valueOf(_1.length()));
    }

    private void notEqual() throws CollectionException {
        String _1 = this.seq.get(0).pop();
        String _2 = this.seq.get(0).pop();
        this.pushTo0(_1.equals(_2) ? "0" : "1");
    }

    private void lower() throws CollectionException {
        int y = Integer.parseInt(this.seq.get(0).pop());
        int x = Integer.parseInt(this.seq.get(0).pop());
        this.pushTo0(x < y ? "1" : "0");
    }

    private void lowerEqual() throws CollectionException {
        int y = Integer.parseInt(this.seq.get(0).pop());
        int x = Integer.parseInt(this.seq.get(0).pop());
        this.pushTo0(x <= y ? "1" : "0");
    }

    private void equal() throws CollectionException {
        String _1 = this.seq.get(0).pop();
        String _2 = this.seq.get(0).pop();
        this.pushTo0(_1.equals(_2) ? "1" : "0");
    }

    private void higher() throws CollectionException {
        int y = Integer.parseInt(this.seq.get(0).pop());
        int x = Integer.parseInt(this.seq.get(0).pop());
        this.pushTo0(x > y ? "1" : "0");
    }

    private void higherEqual() throws CollectionException {
        int y = Integer.parseInt(this.seq.get(0).pop());
        int x = Integer.parseInt(this.seq.get(0).pop());
        this.pushTo0(x >= y ? "1" : "0");
    }

    private void sum() throws CollectionException {
        int _1 = Integer.parseInt(this.seq.get(0).pop());
        int _2 = Integer.parseInt(this.seq.get(0).pop());
        this.pushTo0(String.valueOf(_1 + _2));
    }

    private void sub() throws CollectionException {
        int _1 = Integer.parseInt(this.seq.get(0).pop());
        int _2 = Integer.parseInt(this.seq.get(0).pop());
        this.pushTo0(String.valueOf(_2 - _1));
    }

    private void multip() throws CollectionException {
        int _1 = Integer.parseInt(this.seq.get(0).pop());
        int _2 = Integer.parseInt(this.seq.get(0).pop());
        this.pushTo0(String.valueOf(_1 * _2));
    }

    private void divide() throws CollectionException {
        int _1 = Integer.parseInt(this.seq.get(0).pop());
        int _2 = Integer.parseInt(this.seq.get(0).pop());
        if (_1 == 0 || _2 == 0) {
            this.pushTo0(String.valueOf(0));
        } else {
            this.pushTo0(String.valueOf(Math.abs(_1) > Math.abs(_2) ? (_1 / _2) : (_2 / _1)));
        }
    }

    private void remain() throws CollectionException {
        int _1 = Integer.parseInt(this.seq.get(0).pop());
        int _2 = Integer.parseInt(this.seq.get(0).pop());
        this.pushTo0(String.valueOf(_2 % _1));
    }

    private void stick() throws CollectionException {
        String _1 = this.seq.get(0).pop();
        String _2 = this.seq.get(0).pop();
        this.pushTo0(_2 + _1);
    }

    private void rnd() throws CollectionException {
        int _1 = Integer.parseInt(this.seq.get(0).pop());
        int _2 = Integer.parseInt(this.seq.get(0).pop());
        int x = _1 < _2 ? _1 : _2;
        int y = _1 + _2 - x;
        this.pushTo0(String.valueOf((int) (Math.random() * (x - y) + y)));
    }

    private void then() throws CollectionException {
        this.cond = !this.seq.get(0).pop().equals("0");
    }

    private void _else() {
        this.cond = !this.cond;
    }

    private void print() throws CollectionException {
        int _1 = Integer.parseInt(this.seq.get(0).pop());
        System.out.println(this.seq.get(_1));
    }

    private void clear() throws CollectionException {
        int _1 = Integer.parseInt(this.seq.get(0).pop());
        while (!this.seq.get(_1).isEmpty()) {
            this.seq.get(_1).pop();
        }
    }

    private void run(int stackNum) throws CollectionException {
        Stack<String> stack = this.seq.get(stackNum);
        Stack<String> tempStack = reverseRet(stack);

        while (!tempStack.isEmpty()) {
            instruction(tempStack.pop());
        }

    }

    private void loop() throws CollectionException {
        String stackNum = this.seq.get(0).pop();
        int stackLoop = Integer.parseInt(this.seq.get(0).pop());
        for (int i = 0; i < stackLoop; i++) {
            run(Integer.parseInt(stackNum));
        }
    }

    private void fun() throws CollectionException {
        int stckNum = Integer.parseInt(this.seq.get(0).pop());
        int funNum = Integer.parseInt(this.seq.get(0).pop());
        for (int i = 0; i < funNum; i++) {
            this.seq.get(stckNum).push(sc_n.next());
        }
    }

    private void move() throws CollectionException {
        int stckNum = Integer.parseInt(this.seq.get(0).pop());
        int elemNum = Integer.parseInt(this.seq.get(0).pop());
        for (int i = 0; i < elemNum; i++) {
            this.seq.get(stckNum).push(this.seq.get(0).pop());
        }
    }

    private void reverse() throws CollectionException {
        int stckNum = Integer.parseInt(this.seq.get(0).pop());
        Stack<String> stck = this.seq.get(stckNum);
        Stack<String> temp1 = new ArrayDeque<>();
        Stack<String> temp2 = new ArrayDeque<>();
        while (!stck.isEmpty()) {
            temp1.push(stck.pop());
        }
        while (!temp1.isEmpty()) {
            temp2.push(temp1.pop());
        }
        while (!temp2.isEmpty()) {
            stck.push(temp2.pop());
        }
    }
}

@SuppressWarnings("unchecked")
public class Naloga1 {
    public static void main(String[] args) throws CollectionException {
        Kalkulator calc = new Kalkulator();

    }
}
