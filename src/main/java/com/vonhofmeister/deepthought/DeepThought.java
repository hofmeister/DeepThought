package com.vonhofmeister.deepthought;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class DeepThought<T, A> {

    private final Map<A, List<Condition<T>>> conditions = new HashMap<A, List<Condition<T>>>();

    public ChainedCondition<T> is(final A... possibleAnswers) {
        ChainedCondition<T> out = new ChainedCondition<T>();
        for (A answer : possibleAnswers) {
            if (!conditions.containsKey(answer)) {
                conditions.put(answer, new ArrayList<Condition<T>>());
            }
            conditions.get(answer).add(out);
        }
        return out;
    }

    public List<A> getAnswer(final T target) {
        final List<A> out = new ArrayList<A>();
        final Map<A, Integer> scoreBoard = new HashMap<A, Integer>();
        int topScore = 0;
        for (Entry<A, List<Condition<T>>> entry : conditions.entrySet()) {
            A a = entry.getKey();
            int score = 0;
            for (Condition<T> cond : entry.getValue()) {
                score += cond.evaluate(target);
            }
            
            if (score > topScore) {
                topScore = score;
            }

            scoreBoard.put(a, score);
        }

        //Find the valid answers
        for (Entry<A, Integer> entry : scoreBoard.entrySet()) {
            if (entry.getValue() == topScore) {
                out.add(entry.getKey());
            }
        }
        return out;
    }
    public class ChainedCondition<T> implements Condition<T> {
        private final List<Condition<T>> conditions = new ArrayList<Condition<T>>();



        public int evaluate(final T value) {
            int out = 0;
            for(Condition<T> cond:conditions) {
                out += cond.evaluate(value);
            }
            return out;
        }

        public ChainedCondition<T> when(final Condition<T> condition) {
            conditions.add(condition);
            return this;
        }

        public ChainedCondition<T> whenNot(final Condition<T> condition) {
            conditions.add(new Condition<T>() {
                public int evaluate(T value) {
                    return condition.evaluate(value)*-1;
                }
            });
            return this;
        }

        public ChainedCondition<T> and(Condition<T> condition) {
            return when(condition);
        }

        public ChainedCondition<T> andNot(Condition<T> condition) {
            return whenNot(condition);
        }
    }
}
