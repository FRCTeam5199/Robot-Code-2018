package networking;

import java.nio.ByteBuffer;

public class ByteUtils
{
  public static byte[] toByteArray(boolean value)
  {
    byte[] output = new byte[1];
    if (value) {
      output[0] = 1;
    }
    return output;
  }
  
  public static byte[] toByteArray(int value)
  {
    byte[] bytes = new byte[4];
    ByteBuffer.wrap(bytes).putInt(value);
    return bytes;
  }
  
  public static byte[] toByteArray(float value)
  {
    byte[] bytes = new byte[4];
    ByteBuffer.wrap(bytes).putFloat(value);
    return bytes;
  }
  
  public static byte[] toByteArray(double value)
  {
    byte[] bytes = new byte[8];
    ByteBuffer.wrap(bytes).putDouble(value);
    return bytes;
  }
  
  public static boolean toBoolean(byte[] bytes)
  {
    return bytes[0] == 1;
  }
  
  public static int toInt(byte[] bytes)
  {
    return ByteBuffer.wrap(bytes).getInt();
  }
  
  public static float toFloat(byte[] bytes)
  {
    return ByteBuffer.wrap(bytes).getFloat();
  }
  
  public static double toDouble(byte[] bytes)
  {
    return ByteBuffer.wrap(bytes).getDouble();
  }
  
  public static byte[] addArray(byte[] b1, byte[] b2)
  {
    byte[] output = new byte[b1.length + b2.length];
    System.arraycopy(b1, 0, output, 0, b1.length);
    System.arraycopy(b2, 0, output, b1.length, b2.length);
    return output;
  }
  
  public static byte[] portionOf(byte[] input, int s, int e)
  {
    byte[] output = new byte[e - s];
    for (int i = s; i < e; i++) {
      output[(i - s)] = input[i];
    }
    return output;
  }
}
