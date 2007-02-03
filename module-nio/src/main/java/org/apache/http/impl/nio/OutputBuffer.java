/*
 * $HeadURL$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */
package org.apache.http.impl.nio;

import java.io.IOException;

import org.apache.http.nio.ContentEncoder;

public class OutputBuffer extends ExpandableBuffer implements ContentOutputBuffer {
    
    private boolean endOfStream;
    
    public OutputBuffer(int buffersize) {
        super(buffersize);
        this.endOfStream = false;
    }

    public void produceContent(final ContentEncoder encoder) throws IOException {
        setOutputMode();
        encoder.write(this.buffer);
        if (!hasData() && this.endOfStream) {
            encoder.complete();
        }
    }
    
    public void write(final byte[] b, int off, int len) throws IOException {
        if (b == null) {
            return;
        }
        if (this.endOfStream) {
            return;
        }
        setInputMode();
        ensureCapacity(this.buffer.position() + len);
        this.buffer.put(b, off, len);
    }

    public void write(final byte[] b) throws IOException {
        if (b == null) {
            return;
        }
        if (this.endOfStream) {
            return;
        }
        write(b, 0, b.length);
    }

    public void write(int b) throws IOException {
        if (this.endOfStream) {
            return;
        }
        setInputMode();
        ensureCapacity(this.capacity() + 1);
        this.buffer.put((byte)b);
    }
    
    public void clear() {
        super.clear();
        this.endOfStream = false;
    }
    
    public void flush() {
    }

    public void shutdown() {
        this.endOfStream = true;
    }
    
}