package com.dk.util.network.low;

import java.io.IOException;
import java.io.OutputStream;

public class OutputStreamProgress extends OutputStream {

    private final OutputStream outstream;
    private volatile long bytesWritten=0;
    private OutputStreamProgressListener listener;
    
    public OutputStreamProgress(OutputStream outstream) {
        this.outstream = outstream;
    }

    @Override
    public void write(int b) throws IOException {
        outstream.write(b);
        bytesWritten++;
        if(listener != null){
        	listener.onWrite(bytesWritten);
        }
    }

    @Override
    public void write(byte[] b) throws IOException {
        outstream.write(b);
        bytesWritten += b.length;
        if(listener != null){
        	listener.onWrite(bytesWritten);
        }
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        outstream.write(b, off, len);
        bytesWritten += len;
        if(listener != null){
        	listener.onWrite(bytesWritten);
        }
    }

    @Override
    public void flush() throws IOException {
        outstream.flush();
    }

    @Override
    public void close() throws IOException {
        outstream.close();
    }

    public long getWrittenLength() {
        return bytesWritten;
    }
	public void setListener(OutputStreamProgressListener listener) {
		this.listener = listener;
	}
}