/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maths;

/**
 *
 * @author watom
 */
public class Ray {

    private Vector3 start;
    private double xRot;
    private double yRot;

    public Ray(Vector3 start, double xRot, double yRot) {
        this.start = start;
        this.xRot = xRot;
        this.yRot = yRot;
    }

    public Vector3 getStart() {
        return start;
    }

    public double getXR() {
        return xRot;
    }

    public double getYR() {
        return yRot;
    }

//    public byte[] getBytes() {
//        byte[] output = new byte[0];
//        output = addArray(output, start.getBytes()); //0-24
//        output = addArray(output, ByteUtils.toByteArray(xRot)); //24-32
//        output = addArray(output, ByteUtils.toByteArray(yRot)); //32-40
//        return output;
//    }
//
//    @Override
//    public void loadBytes(byte[] buf) {
//        start = Vector3.loadBytes(ByteUtils.portionOf(buf, 0, 24));
//        xRot = ByteUtils.toDouble(ByteUtils.portionOf(buf, 24, 32));
//        yRot = ByteUtils.toDouble(ByteUtils.portionOf(buf, 32, 40));
//    }
//
//    @Override
//    public String getName() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }

    public String toString() {
        return "start: " + start.toString() + "\n " + xRot + " " + yRot;
    }

}
