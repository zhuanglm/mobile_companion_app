/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.networking.streaming;


import java.io.Closeable;
import java.io.IOException;

/**
 * Created by nick on 2017-04-04.
 */
public class ParsingDecoder implements Closeable {
    private static final int BUFFER_SIZE = 1024 * 1024;

    private final byte[] mBuffer = new byte[BUFFER_SIZE];
    private int mLocation = 0;
    private int mValue = 0xFFFFFFFF; // previous 4 bytes in the stream. When it is 0x00000001 we pass to mDecoder

    public DecoderImpl mDecoder;

    public ParsingDecoder(DecoderImpl decoder) {
        mDecoder = decoder;
        decoder.initializeDecoder();
    }

    public void consume(byte[] data) throws IOException {
        if (data == null) return;

        for (int i = 0; i < data.length; i++) {
            if (mLocation >= BUFFER_SIZE) {
                throw new IOException("Buffer too small for frame!");
            }
            mBuffer[mLocation] = data[i];
            mValue = mValue << 8 | ((int) data[i] & 0x000000FF);

            if (mValue == 0x00000001) {
                //byte[] frameData = Arrays.copyOf(mBuffer, mLocation+1);
                mDecoder.readFrame(mBuffer, mLocation + 1);
                mLocation = 0;
                mValue = 0xFFFFFFFF;
            } else {
                mLocation++;
            }
        }
    }

    @Override
    public void close() {
        mDecoder.close();
    }
}
