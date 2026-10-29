package game.model;

import java.io.Serializable;

/** generic numbered slot containing saved data. */
public class GameSlot<T extends Serializable> implements Serializable {

    private static final long serialVersionUID = 1L;
    private int number;
    private T data;

    public GameSlot(int number, T data) {
        this.number = number;
        this.data = data;
    }

    public int getNumber() {
        return number;
    }

    public T getData() {
        return data;
    }
}
