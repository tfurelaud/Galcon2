/*
 * Copyright (c) 1997, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */


/**
 * The Point2D class defines a point representing a location
 * in {@code (x,y)} coordinate space.
 * 

 */
public class Point2D {
    /**
     * The X coordinate of this Point2D.
     */
    public float x;

    /**
     * The Y coordinate of this Point2D.
     */
    public float y;

    /**
     * Constructs and initializes a Point2D with
     * coordinates (0, 0).
     */
    public Point2D() { }

    /**
     * Constructs and initializes a Point2D with
     * the specified coordinates.
     *
     * @param x the X coordinate of the newly
     *          constructed Point2D
     * @param y the Y coordinate of the newly
     *          constructed Point2D
     */
    public Point2D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Sets the location of this Point2D to the
     * specified float coordinates.
     *
     * @param x the new X coordinate of this {@code Point2D}
     * @param y the new Y coordinate of this {@code Point2D}
     */
    public void setLocation(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Sets the location of this Point2D to the same
     * coordinates as the specified Point2D object.
     * @param p the specified Point2D to which to set
     * this Point2D
     */
    public void setLocation(Point2D p) {
        setLocation(p.x, p.y);
    }

    /**
     * Returns the square of the distance between two points.
     *
     * @param x1 the X coordinate of the first specified point
     * @param y1 the Y coordinate of the first specified point
     * @param x2 the X coordinate of the second specified point
     * @param y2 the Y coordinate of the second specified point
     * @return the square of the distance between the two
     * sets of specified coordinates.
     */
    public static float distanceSq(float x1, float y1, float x2, float y2) {
        x1 -= x2;
        y1 -= y2;
        return (x1 * x1 + y1 * y1);
    }

    /**
     * Returns the distance between two points.
     *
     * @param x1 the X coordinate of the first specified point
     * @param y1 the Y coordinate of the first specified point
     * @param x2 the X coordinate of the second specified point
     * @param y2 the Y coordinate of the second specified point
     * @return the distance between the two sets of specified
     * coordinates.
     */
    public static float distance(float x1, float y1, float x2, float y2) {
        x1 -= x2;
        y1 -= y2;
        return (float) Math.sqrt(x1 * x1 + y1 * y1);
    }

    /**
     * Returns the square of the distance from this
     * Point2D to a specified point.
     *
     * @param px the X coordinate of the specified point to be measured
     *           against this Point2D
     * @param py the Y coordinate of the specified point to be measured
     *           against this Point2D
     * @return the square of the distance between this
     * Point2D and the specified point.
     */
    public float distanceSq(float px, float py) {
        px -= x;
        py -= y;
        return (px * px + py * py);
    }

    /**
     * Returns the square of the distance from this
     * Point2D to a specified Point2D.
     *
     * @param pt the specified point to be measured
     *           against this Point2D
     * @return the square of the distance between this
     * Point2D to a specified Point2D.
     */
    public float distanceSq(Point2D pt) {
        float px = pt.x - this.x;
        float py = pt.y - this.y;
        return (px * px + py * py);
    }

    /**
     * Returns the distance from this Point2D to
     * a specified point.
     *
     * @param px the X coordinate of the specified point to be measured
     *           against this Point2D
     * @param py the Y coordinate of the specified point to be measured
     *           against this Point2D
     * @return the distance between this Point2D
     * and a specified point.
     */
    public float distance(float px, float py) {
        px -= x;
        py -= y;
        return (float) Math.sqrt(px * px + py * py);
    }

    /**
     * Returns the distance from this Point2D to a
     * specified Point2D.
     *
     * @param pt the specified point to be measured
     *           against this Point2D
     * @return the distance between this Point2D and
     * the specified Point2D.
     */
    public float distance(Point2D pt) {
        float px = pt.x - this.x;
        float py = pt.y - this.y;
        return (float) Math.sqrt(px * px + py * py);
    }

    /**
     * Returns the hashcode for this Point2D.
     * @return      a hash code for this Point2D.
     */
    public int hashCode() {
        int bits = java.lang.Float.floatToIntBits(x);
        bits ^= java.lang.Float.floatToIntBits(y) * 31;
        return bits;
    }

    /**
     * Determines whether or not two points are equal. Two instances of
     * Point2D are equal if the values of their
     * x and y member fields, representing
     * their position in the coordinate space, are the same.
     * @param obj an object to be compared with this Point2D
     * @return true if the object to be compared is
     *         an instance of Point2D and has
     *         the same values; false otherwise.
     */
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof Point2D) {
            Point2D p2d = (Point2D) obj;
            return (x == p2d.x) && (y == p2d.y);
        }
        return false;
    }

    /**
     * Returns a String that represents the value
     * of this Point2D.
     * @return a string representation of this Point2D.
     */
    public String toString() {
        return "Point2D["+x+", "+y+"]";
    }
}
