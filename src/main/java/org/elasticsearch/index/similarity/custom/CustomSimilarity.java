package org.elasticsearch.index.similarity.custom;

import org.apache.lucene.index.FieldInvertState;
import org.apache.lucene.search.similarities.TFIDFSimilarity;
import org.apache.lucene.search.similarities.DefaultSimilarity;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.SmallFloat;

/**
 * Custom Similarity class
 *
 * Very similar to {@link DefaultSimilarity}, but norms can be either encode to a single byte
 * (as done in DefaultSimilarity) or encoded without precision loss
 *
 * Set compress_norm to have original behaviour.
 *
 */
public class CustomSimilarity extends TFIDFSimilarity {

  private static final float[] NORM_TABLE = new float[256];
  static {
    for(int i = 0; i < 256; ++i) {
      NORM_TABLE[i] = SmallFloat.byte315ToFloat((byte)i);
    }
  }

  boolean discountOverlaps;
  boolean compressNorms;


  public void setDiscountOverlaps(boolean discountOverlaps) {
    this.discountOverlaps = discountOverlaps;
  }

  public void setCompressNorms(boolean compressNorms) {
    this.compressNorms = compressNorms;
  }

  public boolean getDiscountOverlaps() {
    return discountOverlaps;
  }

  public boolean getCompressNorms() {
    return compressNorms;
  }

  @Override
  public float coord(int overlap, int maxOverlap) {
    return (float)overlap / (float)maxOverlap;
  }

  @Override
  public float queryNorm(float sumOfSquaredWeights) {
    return (float)(1.0D / Math.sqrt((double)sumOfSquaredWeights));
  }

  @Override
  public float tf(float freq) {
    if (freq > 0) {
      return 1.0f;
    } else {
      return 0.0f;
    }
  }

  @Override
  public float idf(long docFreq, long numDocs) {
    return (float)(Math.log((double)numDocs / (double)(docFreq + 1L)) + 1.0D);
  }

  @Override
  public float lengthNorm(FieldInvertState state) {
    int numTerms;
    if (this.discountOverlaps) {
      numTerms = state.getLength() - state.getNumOverlap();
    } else {
      numTerms = state.getLength();
    }

    return state.getBoost() * (float)(1.0D / Math.sqrt((double)numTerms));
  }

  @Override
  public float decodeNormValue(long encodedNorm) {
    if (compressNorms) {
      return NORM_TABLE[(int)(encodedNorm & 255L)];
    } else {
      return Float.intBitsToFloat((int) encodedNorm);
    }
  }

  @Override
  public final long encodeNormValue(float norm) {
    if (compressNorms) {
      return (long)SmallFloat.floatToByte315(norm);
    } else {
      return (long)Float.floatToRawIntBits(norm);
    }
  }

  @Override
  public float sloppyFreq(int distance) {
    return 1.0F / (float)(distance + 1);
  }

  @Override
  public float scorePayload(int i, int i1, int i2, BytesRef bytesRef) {
    return 1.0F;
  }

}
