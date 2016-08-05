package com.app.votodo.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by anuj on 04/08/16.
 */
public class wavIO {
    private String myPath;
    private long myChunkSize;
    private long mySubChunk1Size;
    private int myFormat;
    private long myChannels;
    private long mySampleRate;
    private long myByteRate;
    private int myBlockAlign;
    private int myBitsPerSample;
    private long myDataSize;

    public byte[] myData;

    // get/set for the Path property
    public String getPath() {
        return myPath;
    }

    public void setPath(String newPath) {
        myPath = newPath;
    }

    // empty constructor
    public wavIO() {
        myPath = "";
    }

    // constructor takes a wav path
    public wavIO(String tmpPath) {
        myPath = tmpPath;
    }

    // read a wav file into this class
    public boolean read() {
        DataInputStream inFile = null;
        myData = null;
        byte[] tmpLong = new byte[4];
        byte[] tmpInt = new byte[2];

        try {
            inFile = new DataInputStream(new FileInputStream(myPath));

            //System.out.println("Reading wav file...\n"); // for debugging only

            String chunkID = "" + (char) inFile.readByte() + (char) inFile.readByte() + (char) inFile.readByte() + (char) inFile.readByte();

            inFile.read(tmpLong); // read the ChunkSize
            myChunkSize = byteArrayToLong(tmpLong);

            String format = "" + (char) inFile.readByte() + (char) inFile.readByte() + (char) inFile.readByte() + (char) inFile.readByte();

            // print what we've read so far
            //System.out.println("chunkID:" + chunkID + " chunk1Size:" + myChunkSize + " format:" + format); // for debugging only

            String subChunk1ID = "" + (char) inFile.readByte() + (char) inFile.readByte() + (char) inFile.readByte() + (char) inFile.readByte();

            inFile.read(tmpLong); // read the SubChunk1Size
            mySubChunk1Size = byteArrayToLong(tmpLong);

            inFile.read(tmpInt); // read the audio format.  This should be 1 for PCM
            myFormat = byteArrayToInt(tmpInt);

            inFile.read(tmpInt); // read the # of channels (1 or 2)
            myChannels = byteArrayToInt(tmpInt);

            inFile.read(tmpLong); // read the samplerate
            mySampleRate = byteArrayToLong(tmpLong);

            inFile.read(tmpLong); // read the byterate
            myByteRate = byteArrayToLong(tmpLong);

            inFile.read(tmpInt); // read the blockalign
            myBlockAlign = byteArrayToInt(tmpInt);

            inFile.read(tmpInt); // read the bitspersample
            myBitsPerSample = byteArrayToInt(tmpInt);

            // print what we've read so far
            //System.out.println("SubChunk1ID:" + subChunk1ID + " SubChunk1Size:" + mySubChunk1Size + " AudioFormat:" + myFormat + " Channels:" + myChannels + " SampleRate:" + mySampleRate);


            // read the data chunk header - reading this IS necessary, because not all wav files will have the data chunk here - for now, we're just assuming that the data chunk is here
            String dataChunkID = "" + (char) inFile.readByte() + (char) inFile.readByte() + (char) inFile.readByte() + (char) inFile.readByte();

            inFile.read(tmpLong); // read the size of the data
            myDataSize = byteArrayToLong(tmpLong);


            // read the data chunk
            myData = new byte[(int) myDataSize];
            inFile.read(myData);

            // close the input stream
            inFile.close();
        } catch (Exception e) {
            return false;
        }

        return true; // this should probably be something more descriptive
    }

    // write out the wav file
    public boolean save() {
        try {
            DataOutputStream outFile = new DataOutputStream(new FileOutputStream(myPath));

            // write the wav file per the wav file format
            outFile.writeBytes("RIFF");                    // 00 - RIFF
            outFile.write(intToByteArray((int) myChunkSize), 0, 4);        // 04 - how big is the rest of this file?
            outFile.writeBytes("WAVE");                    // 08 - WAVE
            outFile.writeBytes("fmt ");                    // 12 - fmt
            outFile.write(intToByteArray((int) mySubChunk1Size), 0, 4);    // 16 - size of this chunk
            outFile.write(shortToByteArray((short) myFormat), 0, 2);        // 20 - what is the audio format? 1 for PCM = Pulse Code Modulation
            outFile.write(shortToByteArray((short) myChannels), 0, 2);    // 22 - mono or stereo? 1 or 2?  (or 5 or ???)
            outFile.write(intToByteArray((int) mySampleRate), 0, 4);        // 24 - samples per second (numbers per second)
            outFile.write(intToByteArray((int) myByteRate), 0, 4);        // 28 - bytes per second
            outFile.write(shortToByteArray((short) myBlockAlign), 0, 2);    // 32 - # of bytes in one sample, for all channels
            outFile.write(shortToByteArray((short) myBitsPerSample), 0, 2);    // 34 - how many bits in a sample(number)?  usually 16 or 24
            outFile.writeBytes("data");                    // 36 - data
            outFile.write(intToByteArray((int) myDataSize), 0, 4);        // 40 - how big is this data chunk
            outFile.write(myData);                        // 44 - the actual data itself - just a long string of numbers
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }

        return true;
    }

    // return a printable summary of the wav file
    public String getSummary() {
        //String newline = System.getProperty("line.separator");
        String newline = "<br>";
        String summary = "<html>Format: " + myFormat + newline + "Channels: " + myChannels + newline + "SampleRate: " + mySampleRate + newline + "ByteRate: " + myByteRate + newline + "BlockAlign: " + myBlockAlign + newline + "BitsPerSample: " + myBitsPerSample + newline + "DataSize: " + myDataSize + "</html>";
        return summary;
    }

// ===========================
// CONVERT BYTES TO JAVA TYPES
// ===========================

    // these two routines convert a byte array to a unsigned short
    public static int byteArrayToInt(byte[] b) {
        int start = 0;
        int low = b[start] & 0xff;
        int high = b[start + 1] & 0xff;
        return (int) (high << 8 | low);
    }

    // these two routines convert a byte array to an unsigned integer
    public static long byteArrayToLong(byte[] b) {
        int start = 0;
        int i = 0;
        int len = 4;
        int cnt = 0;
        byte[] tmp = new byte[len];
        for (i = start; i < (start + len); i++) {
            tmp[cnt] = b[i];
            cnt++;
        }
        long accum = 0;
        i = 0;
        for (int shiftBy = 0; shiftBy < 32; shiftBy += 8) {
            accum |= ((long) (tmp[i] & 0xff)) << shiftBy;
            i++;
        }
        return accum;
    }

    // ===========================
    // CONVERT JAVA TYPES TO BYTES
    // ===========================
    // returns a byte array of length 4
    private static byte[] intToByteArray(int i) {
        byte[] b = new byte[4];
        b[0] = (byte) (i & 0x00FF);
        b[1] = (byte) ((i >> 8) & 0x000000FF);
        b[2] = (byte) ((i >> 16) & 0x000000FF);
        b[3] = (byte) ((i >> 24) & 0x000000FF);
        return b;
    }

    // convert a short to a byte array
    public static byte[] shortToByteArray(short data) {
        return new byte[]{(byte) (data & 0xff), (byte) ((data >>> 8) & 0xff)};
    }

}
