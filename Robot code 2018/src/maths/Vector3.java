/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maths;

/**
 *
 * @author 18wakayamats
 */
public class Vector3 implements Comparable<Vector3> {

    public static final Vector3 ZERO = new Vector3(0, 0, 0);
    public static final Vector3 UP = new Vector3(0, 1, 0);
    public static final Vector3 DOWN = new Vector3(0, -1, 0);
    public static final Vector3 RIGHT = new Vector3(1, 0, 0);
    public static final Vector3 LEFT = new Vector3(-1, 0, 0);
    public static final Vector3 FORWARDS = new Vector3(0, 0, 1);
    public static final Vector3 BACKWARDS = new Vector3(0, 0, -1);
    
    private double x;
    private double y;
    private double z;
    
    public Vector3() {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Vector3(Vector3 other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double length() {
        return Math.sqrt(x * x + y * y + z + z);
    }

    @Override
    public String toString() {
        return x + "," + y + "," + z;
    }
    
    @Override
    public int compareTo(Vector3 other) {
        if (this.x != other.x) {
            return (int) (this.x - other.x);
        } else if (this.y != other.y) {
            return (int) (this.y - other.y);
        } else {
            return (int) (this.z - other.z);
        }
    }

    public static Vector3 add(Vector3 v1, Vector3 v2) {
        return new Vector3(
                v1.getX() + v2.getX(),
                v1.getY() + v2.getY(),
                v1.getZ() + v2.getZ());
    }

    public static Vector3 subtract(Vector3 v1, Vector3 v2) {
        return new Vector3(
                v1.getX() - v2.getX(),
                v1.getY() - v2.getY(),
                v1.getZ() - v2.getZ());
    }

    public static Vector3 multiply(Vector3 v1, Vector3 v2) {
        return new Vector3(
                v1.getX() * v2.getX(),
                v1.getY() * v2.getY(),
                v1.getZ() * v2.getZ());
    }

    public static Vector3 multiply(Vector3 v1, double x) {
        return new Vector3(
                v1.getX() * x,
                v1.getY() * x,
                v1.getZ() * x);
    }

    public static Vector3 divide(Vector3 v1, Vector3 v2) {
        return new Vector3(
                v1.getX() / v2.getX(),
                v1.getY() / v2.getY(),
                v1.getZ() / v2.getZ());
    }

    public static Vector3 divide(Vector3 v1, double x) {
        return new Vector3(
                v1.getX() / x,
                v1.getY() / x,
                v1.getZ() / x);
    }

    public static Vector3 multiply(double x, Vector3 v2) {
        return new Vector3(
                x * v2.getX(),
                x * v2.getY(),
                x * v2.getZ());
    }

    public static Vector3 average(Vector3 v1, Vector3 v2) {
        return divide(add(v1, v2), 2);
    }

    // Deprecated now that we have the copy constructor?
    public static Vector3 clone(Vector3 v) {
        return new Vector3(v.getX(), v.getY(), v.getZ());
    }

    public static Vector3 up() {
        return new Vector3(0, 1, 0);
    }

    public static Vector3 down() {
        return new Vector3(0, -1, 0);
    }

    public static Vector3 right() {
        return new Vector3(1, 0, 0);
    }

    public static Vector3 left() {
        return new Vector3(-1, 0, 0);
    }

    public static Vector3 forwards() {
        return new Vector3(0, 0, 1);
    }

    public static Vector3 backwards() {
        return new Vector3(0, 0, -1);
    }

    public static Vector3 zero() {
        return new Vector3(0, 0, 0);
    }

}
