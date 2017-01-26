package org.ebay.datameta.test.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.ebay.datameta.test.util.ByteTestUtils.byteArrayInputImage;
import static org.ebay.datameta.test.util.ByteTestUtils.getByteArrayImage;

/**
 * @author mbergens Michael Bergens
 */
public class DataInOutMock implements DataInput, DataOutput {

    private static final Logger L = LoggerFactory.getLogger(DataInOutMock.class);
    private byte[] buffer;
    private ByteArrayOutputStream byteOut = new ByteArrayOutputStream(100);
    private ByteArrayInputStream byteIn;
    private final String name;

    public DataInOutMock(final String name) {
        if(name == null || name.length() < 1) throw new IllegalArgumentException("Please provide the name for the instance");
        this.name = name;
    }

    /**
     * Useful to distinguish between the streams in the output
     */
    public String getName() { return name;}

    public void flipOutToIn() {
        byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        if(L.isDebugEnabled()) L.debug(name + ": Flip Out->In, byte array image:" + byteArrayInputImage(byteIn));
    }

    @Override public void readFully(byte[] b) throws IOException {
        //noinspection ResultOfMethodCallIgnored
        byteIn.read(b);
    }

    @Override public void readFully(byte[] b, int off, int len) throws IOException {
        //noinspection ResultOfMethodCallIgnored
        byteIn.read(b, off, len);
        if(L.isDebugEnabled()) L.debug(name + ":Read from " + getByteArrayImage(b) + ", off=" + off + ", len = " + len
            + ", byte array image:" + byteArrayInputImage(byteIn));
    }

    @Override public int skipBytes(int n) throws IOException {return 0;}

    @Override public boolean readBoolean() throws IOException {
        byte b = (byte)(0xFF & byteIn.read());
        if(L.isDebugEnabled()) L.debug(String.format("%s: Read byte to boolean: %02X %<d", name, b));
        return b != 0;
    }

    @Override public byte readByte() throws IOException {
        byte b = (byte)(0xFF & byteIn.read());
        if(L.isDebugEnabled()) L.debug(String.format("%s: Read byte: %02X=%<d", name, b));
        return b;
    }

    private UnsupportedOperationException unsup(final String opName) {
        return new UnsupportedOperationException(opName + " - unsupported feature of the " + getClass().getName());
    }
    @Override public int readUnsignedByte() throws IOException {
        throw unsup("readUnsignedByte");
    }

    @Override public short readShort() throws IOException {
        byte[] buff = new byte[2];
        byteIn.read(buff);
        ByteBuffer bb = ByteBuffer.wrap(buff);
        final short result = bb.getShort();
        if(L.isDebugEnabled()) L.debug(name + ":Read short from " + getByteArrayImage(buff) + ", result=" + result);
        return result;
    }

    @Override public int readUnsignedShort() throws IOException { throw unsup("readUnsignedShort"); }

    @Override public char readChar() throws IOException { throw unsup("readChar"); }

    @Override public int readInt() throws IOException {
        byte[] buff = new byte[4];
        byteIn.read(buff);
        ByteBuffer bb = ByteBuffer.wrap(buff);
        final int result = bb.getInt();
        if(L.isDebugEnabled()) L.debug(name + ":Read int from " + getByteArrayImage(buff) + ", result=" + result);
        return result;
    }

    @Override public long readLong() throws IOException {
        byte[] buff = new byte[8];
        byteIn.read(buff);
        ByteBuffer bb = ByteBuffer.wrap(buff);
        final long result = bb.getLong();
        if(L.isDebugEnabled()) L.debug(name + ":Read Long from " + getByteArrayImage(buff) + ", result=" + result);
        return result;
    }

    @Override public float readFloat() throws IOException {
        byte[] buff = new byte[4];
        byteIn.read(buff);
        ByteBuffer bb = ByteBuffer.wrap(buff);
        final float result = bb.getFloat();
        if(L.isDebugEnabled()) L.debug(name + ":Read float from " + getByteArrayImage(buff) + ", result=" + result);
        return result;
    }

    @Override public double readDouble() throws IOException {
        byte[] buff = new byte[8];
        byteIn.read(buff);
        ByteBuffer bb = ByteBuffer.wrap(buff);
        final double result = bb.getDouble();
        if(L.isDebugEnabled()) L.debug(name + ":Read double from " + getByteArrayImage(buff) + ", result=" + result);
        return result;
    }

    @Override public String readLine() throws IOException {
        throw new UnsupportedOperationException("readLine - unsupported feature of the " + getClass().getName());
    }

    @Override public String readUTF() throws IOException {
        throw new UnsupportedOperationException("readUTF - unsupported feature of the " + getClass().getName());
    }

    @Override public void write(int b) throws IOException {
        byteOut.write(b);
        if(L.isDebugEnabled()) L.debug(String.format("%s: wrote byte: %02X %<d", name, b));
    }

    @Override public void write(byte[] b) throws IOException {
        byteOut.write(b);
        if(L.isDebugEnabled()) L.debug(name + ":Wrote " + getByteArrayImage(b) + ", bytes: "
            + getByteArrayImage(byteOut.toByteArray()));
    }

    @Override public void write(byte[] b, int off, int len) throws IOException {
        byteOut.write(b, off, len);
        if(L.isDebugEnabled()) L.debug(name + ":Wrote " + getByteArrayImage(b, off, len) + ", bytes: "
            + getByteArrayImage(byteOut.toByteArray()));
    }

    @Override public void writeBoolean(boolean v) throws IOException {
        final byte b = (byte)(v ? 1 : 0);
        byteOut.write(b);
        if(L.isDebugEnabled()) L.debug(String.format("%s: wrote boolean %b byte: %02X %<d", name, v, b));
    }

    @Override public void writeByte(int v) throws IOException {
        byteOut.write(v);
        if(L.isDebugEnabled()) L.debug(name + ":Wrote " + v + ", bytes: " + getByteArrayImage(byteOut.toByteArray()));
    }

    @Override public void writeShort(int v) throws IOException {
        final byte[] buff = new byte[2];
        ByteBuffer byteBuffer = ByteBuffer.wrap(buff);
        byteBuffer.putShort((short) v);
        byteOut.write(buff);
        if(L.isDebugEnabled()) L.debug(name + ":Wrote short " + v + "=='" + ((char)v) + "', bytes: "
            + getByteArrayImage(byteOut.toByteArray()));
    }

    @Override public void writeChar(int v) throws IOException {
        final byte[] buff = new byte[1]; // keep UTF8 in mind
        ByteBuffer byteBuffer = ByteBuffer.wrap(buff);
        byteBuffer.putChar((char) v);
        byteOut.write(buff);
        if(L.isDebugEnabled()) L.debug(name + ":Wrote char " + v + "=='" + ((char)v) + "', bytes: "
            + getByteArrayImage(byteOut.toByteArray()));
    }

    @Override public void writeInt(int v) throws IOException {
        final byte[] intBuf = new byte[4];
        ByteBuffer byteBuffer = ByteBuffer.wrap(intBuf);
        byteBuffer.putInt(v);
        byteOut.write(intBuf);
        if(L.isDebugEnabled()) L.debug(name + ":Wrote int " + v + ", bytes: "
            + getByteArrayImage(byteOut.toByteArray()));
    }

    @Override public void writeLong(long v) throws IOException {
        final byte[] longBuf = new byte[8];
        ByteBuffer byteBuffer = ByteBuffer.wrap(longBuf);
        byteBuffer.putLong(v);
        byteOut.write(longBuf);
        if(L.isDebugEnabled()) L.debug(name + ":Wrote long " + v + ", bytes: "
            + getByteArrayImage(byteOut.toByteArray()));
    }

    @Override public void writeFloat(float v) throws IOException {
        final byte[] buff = new byte[4];
        ByteBuffer byteBuffer = ByteBuffer.wrap(buff);
        byteBuffer.putFloat(v);
        byteOut.write(buff);
        if(L.isDebugEnabled()) L.debug(name + ":Wrote float " + v + ", bytes: "
            + getByteArrayImage(byteOut.toByteArray()));
    }

    @Override public void writeDouble(double v) throws IOException {
        final byte[] buff = new byte[8];
        ByteBuffer byteBuffer = ByteBuffer.wrap(buff);
        byteBuffer.putDouble(v);
        byteOut.write(buff);
        if(L.isDebugEnabled()) L.debug(name + ":Wrote double " + v + ", bytes: "
            + getByteArrayImage(byteOut.toByteArray()));
    }

    @Override public void writeBytes(String s) throws IOException { throw unsup("writeBytes(String)"); }

    @Override public void writeChars(String s) throws IOException { throw unsup("writeChars(String)"); }

    @Override public void writeUTF(String s) throws IOException { throw unsup("writeUTF"); }
}
