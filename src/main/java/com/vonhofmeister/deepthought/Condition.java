package com.vonhofmeister.deepthought;

public interface Condition<T> {
    public int evaluate(T value);
}
