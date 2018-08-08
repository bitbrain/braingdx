package de.bitbrain.braingdx.util.math;

import com.badlogic.gdx.math.MathUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

// TODO
public class BigDecimalVector2 implements Serializable, GenericVector<BigDecimalVector2, BigDecimal> {

   private static final long serialVersionUID = 913902788239530931L;

   private static final BigDecimal SQRT_DIG = new BigDecimal(150);
   private static final BigDecimal SQRT_PRE = new BigDecimal(10).pow(SQRT_DIG.intValue());

   public final static BigDecimalVector2 X = new BigDecimalVector2(1, 0);
   public final static BigDecimalVector2 Y = new BigDecimalVector2(0, 1);
   public final static BigDecimalVector2 Zero = new BigDecimalVector2(0, 0);

   /**
    * the x-component of this vector
    **/
   public BigDecimal x;
   /**
    * the y-component of this vector
    **/
   public BigDecimal y;

   /**
    * Constructs a new vector at (0,0)
    */
   public BigDecimalVector2() {
   }

   /**
    * Constructs a vector with the given components
    *
    * @param x The x-component
    * @param y The y-component
    */
   public BigDecimalVector2(float x, float y) {
      this.x = new BigDecimal(x);
      this.y = new BigDecimal(y);
   }

   /**
    * Constructs a vector from the given vector
    *
    * @param v The vector
    */
   public BigDecimalVector2(BigDecimalVector2 v) {
      set(v);
   }

   @Override
   public BigDecimalVector2 cpy() {
      return new BigDecimalVector2(this);
   }

   /**
    * Uses Newton Raphson to compute the square root of a BigDecimal.
    *
    * @author Luciano Culacciatti
    * @url http://www.codeproject.com/Tips/257031/Implementing-SqrtRoot-in-BigDecimal
    */
   public static BigDecimal sqrt(BigDecimal c) {
      return sqrtNewtonRaphson(c, new BigDecimal(1), new BigDecimal(1).divide(SQRT_PRE));
   }

   public static BigDecimal len(BigDecimal x, BigDecimal y) {
      return sqrt(x.pow(2).add(y.pow(2)));
   }

   @Override
   public BigDecimal len() {
      return sqrt(x.pow(2).add(y.pow(2)));
   }

   public static BigDecimal len2(BigDecimal x, BigDecimal y) {
      return x.pow(2).add(y.pow(2));
   }

   @Override
   public BigDecimal len2() {
      return x.pow(2).add(y.pow(2));
   }

   @Override
   public BigDecimalVector2 set(BigDecimalVector2 v) {
      x = v.x;
      y = v.y;
      return this;
   }

   /**
    * Sets the components of this vector
    *
    * @param x The x-component
    * @param y The y-component
    * @return This vector for chaining
    */
   public BigDecimalVector2 set(BigDecimal x, BigDecimal y) {
      this.x = x;
      this.y = y;
      return this;
   }

   @Override
   public BigDecimalVector2 sub(BigDecimalVector2 v) {
      x = x.subtract(v.x);
      y = y.subtract(v.y);
      return this;
   }

   /**
    * Substracts the other vector from this vector.
    *
    * @param x The x-component of the other vector
    * @param y The y-component of the other vector
    * @return This vector for chaining
    */
   public BigDecimalVector2 sub(BigDecimal x, BigDecimal y) {
      this.x = x.subtract(x);
      this.y = y.subtract(y);
      return this;
   }

   @Override
   public BigDecimalVector2 nor() {
      BigDecimal len = len();
      if (len.signum() != 0) {
         x = x.divide(len);
         y = y.divide(len);
      }
      return this;
   }

   @Override
   public BigDecimalVector2 add(BigDecimalVector2 v) {
      x = x.add(v.x);
      y = y.add(v.y);
      return this;
   }

   public static BigDecimal dot(BigDecimal x1, BigDecimal y1, BigDecimal x2, BigDecimal y2) {
      return x1.multiply(x2).add(y1.multiply(y2));
   }

   @Override
   public BigDecimal dot(BigDecimalVector2 v) {
      return x.multiply(v.x).add(y.multiply(v.y));
   }

   @Override
   public BigDecimalVector2 scl(BigDecimal scalar) {
      x = x.multiply(scalar);
      y = y.multiply(scalar);
      return this;
   }

   /**
    * Converts this {@code Vector2} to a string in the format {@code (x,y)}.
    *
    * @return a string representation of this object.
    */
   @Override
   public String toString() {
      return "(" + x + "," + y + ")";
   }

   /**
    * @return the angle in degrees of this vector (point) relative to the x-axis. Angles are towards the positive y-axis
    * (typically counter-clockwise) and between 0 and 360.
    */
   public float angle() {
      float angle = (float) Math.atan2(y.floatValue(), x.floatValue()) * MathUtils.radiansToDegrees;
      if (angle < 0) angle += 360;
      return angle;
   }

   /**
    * @return the angle in radians of this vector (point) relative to the x-axis. Angles are towards the positive y-axis.
    * (typically counter-clockwise)
    */
   public float angleRad() {
      return (float) Math.atan2(y.floatValue(), x.floatValue());
   }

   /**
    * Sets the angle of the vector in degrees relative to the x-axis, towards the positive y-axis (typically counter-clockwise).
    *
    * @param degrees The angle in degrees to set.
    */
   public BigDecimalVector2 setAngle(float degrees) {
      return setAngleRad(degrees * MathUtils.degreesToRadians);
   }

   /**
    * Sets the angle of the vector in radians relative to the x-axis, towards the positive y-axis (typically counter-clockwise).
    *
    * @param radians The angle in radians to set.
    */
   public BigDecimalVector2 setAngleRad(float radians) {
      this.set(len(), BigDecimal.ZERO);
      this.rotateRad(radians);
      return this;
   }

   /**
    * Rotates the Vector2 by the given angle, counter-clockwise assuming the y-axis points up.
    *
    * @param degrees the angle in degrees
    */
   public BigDecimalVector2 rotate(float degrees) {
      return rotateRad(degrees * MathUtils.degreesToRadians);
   }

   /**
    * Rotates the Vector2 by the given angle, counter-clockwise assuming the y-axis points up.
    *
    * @param radians the angle in radians
    */
   public BigDecimalVector2 rotateRad(float radians) {
      BigDecimal cos = new BigDecimal(Math.cos(radians));
      BigDecimal sin = new BigDecimal(Math.sin(radians));

      this.x = x.multiply(cos).subtract(y.multiply(sin));
      this.y = y.multiply(sin).add(y.multiply(cos));

      return this;
   }

   @Override
   public BigDecimalVector2 setToRandomDirection() {
      float theta = MathUtils.random(0f, MathUtils.PI2);
      return this.set(new BigDecimal(MathUtils.cos(theta)), new BigDecimal(MathUtils.sin(theta)));
   }

   private static BigDecimal sqrtNewtonRaphson(BigDecimal c, BigDecimal xn, BigDecimal precision) {
      BigDecimal fx = xn.pow(2).add(c.negate());
      BigDecimal fpx = xn.multiply(new BigDecimal(2));
      BigDecimal xn1 = fx.divide(fpx, 2 * SQRT_DIG.intValue(), RoundingMode.HALF_DOWN);
      xn1 = xn.add(xn1.negate());
      BigDecimal currentSquare = xn1.pow(2);
      BigDecimal currentPrecision = currentSquare.subtract(c);
      currentPrecision = currentPrecision.abs();
      if (currentPrecision.compareTo(precision) <= -1) {
         return xn1;
      }
      return sqrtNewtonRaphson(c, xn1, precision);
   }
}
