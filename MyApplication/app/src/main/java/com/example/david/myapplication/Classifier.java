package com.example.david.myapplication;

/**
 * Created by pedro on 10/7/17.
 */

public interface Classifier {
    String name();

    Classification recognize(final float[] pixels);
    Classification recognize(final int[] pixels);
}
