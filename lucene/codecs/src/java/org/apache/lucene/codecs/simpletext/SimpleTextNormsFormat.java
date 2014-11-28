package org.apache.lucene.codecs.simpletext;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException;

import org.apache.lucene.codecs.DocValuesConsumer;
import org.apache.lucene.codecs.DocValuesProducer;
import org.apache.lucene.codecs.NormsConsumer;
import org.apache.lucene.codecs.NormsFormat;
import org.apache.lucene.codecs.NormsProducer;
import org.apache.lucene.index.FieldInfo;
import org.apache.lucene.index.NumericDocValues;
import org.apache.lucene.index.SegmentReadState;
import org.apache.lucene.index.SegmentWriteState;
import org.apache.lucene.util.Accountable;

/**
 * plain-text norms format.
 * <p>
 * <b>FOR RECREATIONAL USE ONLY</b>
 * 
 * @lucene.experimental
 */
public class SimpleTextNormsFormat extends NormsFormat {
  private static final String NORMS_SEG_EXTENSION = "len";
  
  @Override
  public NormsConsumer normsConsumer(SegmentWriteState state) throws IOException {
    return new SimpleTextNormsConsumer(state);
  }
  
  @Override
  public NormsProducer normsProducer(SegmentReadState state) throws IOException {
    return new SimpleTextNormsProducer(state);
  }
  
  /**
   * Reads plain-text norms.
   * <p>
   * <b>FOR RECREATIONAL USE ONLY</b>
   * 
   * @lucene.experimental
   */
  public static class SimpleTextNormsProducer extends NormsProducer {
    private final SimpleTextDocValuesReader impl;
    
    public SimpleTextNormsProducer(SegmentReadState state) throws IOException {
      // All we do is change the extension from .dat -> .len;
      // otherwise this is a normal simple doc values file:
      impl = new SimpleTextDocValuesReader(state, NORMS_SEG_EXTENSION);
    }
    
    @Override
    public NumericDocValues getNorms(FieldInfo field) throws IOException {
      return impl.getNumeric(field);
    }
    
    @Override
    public void close() throws IOException {
      impl.close();
    }
    
    @Override
    public long ramBytesUsed() {
      return impl.ramBytesUsed();
    }
    
    @Override
    public Iterable<? extends Accountable> getChildResources() {
      return impl.getChildResources();
    }

    @Override
    public void checkIntegrity() throws IOException {
      impl.checkIntegrity();
    }

    @Override
    public String toString() {
      return getClass().getSimpleName() + "(" + impl + ")";
    }
  }
  
  /**
   * Writes plain-text norms.
   * <p>
   * <b>FOR RECREATIONAL USE ONLY</b>
   * 
   * @lucene.experimental
   */
  public static class SimpleTextNormsConsumer extends NormsConsumer {
    private final SimpleTextDocValuesWriter impl;
    
    public SimpleTextNormsConsumer(SegmentWriteState state) throws IOException {
      // All we do is change the extension from .dat -> .len;
      // otherwise this is a normal simple doc values file:
      impl = new SimpleTextDocValuesWriter(state, NORMS_SEG_EXTENSION);
    }
    
    @Override
    public void addNormsField(FieldInfo field, Iterable<Number> values) throws IOException {
      impl.addNumericField(field, values);
    }

    @Override
    public void close() throws IOException {
      impl.close();
    }
  }
}
