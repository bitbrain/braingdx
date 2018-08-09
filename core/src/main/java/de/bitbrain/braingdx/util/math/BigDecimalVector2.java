package de.bitbrain.braingdx.util.math;

import ch.obermuhlner.math.big.BigDecimalMath;
import com.badlogic.gdx.math.MathUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class BigDecimalVector2 implements Serializable, GenericVector<BigDecimalVector2, BigDecimal> {

   private static final long serialVersionUID = 913902788239530931L;

   private static final MathContext DEFAULT_MATH_CONTEXT = MathContext.UNLIMITED;
   private static final BigDecimal SQRT_DIG = new BigDecimal(150, DEFAULT_MATH_CONTEXT);
   private static final BigDecimal SQRT_PRE = new BigDecimal(10, DEFAULT_MATH_CONTEXT).pow(SQRT_DIG.intValue());

   public final static BigDecimalVector2 X = new BigDecimalVector2(1, 0);
   public final static BigDecimalVector2 Y = new BigDecimalVector2(0, 1);
   public final static BigDecimalVector2 Zero = new BigDecimalVector2(0, 0);

   public final MathContext mathContext;

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
      this(DEFAULT_MATH_CONTEXT);
   }

   public BigDecimalVector2(MathContext mathContext) {
      this.mathContext = mathContext;
      this.x = new BigDecimal(0f, mathContext);
      this.y = new BigDecimal(0f, mathContext);
   }

   /**
    * Constructs a vector with the given components
    *
    * @param x The x-component
    * @param y The y-component
    */
   public BigDecimalVector2(float x, float y) {
      this(DEFAULT_MATH_CONTEXT);
      this.x = new BigDecimal(x, mathContext);
      this.y = new BigDecimal(y, mathContext);
   }

   /**
    * Constructs a vector from the given vector
    *
    * @param v The vector
    */
   public BigDecimalVector2(BigDecimalVector2 v) {
      this(DEFAULT_MATH_CONTEXT);
      set(v);
   }

   @Override
   public BigDecimalVector2 cpy() {
      return new BigDecimalVector2(this);
   }

   public static BigDecimal len(BigDecimal x, BigDecimal y, MathContext mathContext) {
      return BigDecimalMath.sqrt(x.pow(2).add(y.pow(2)), mathContext);
   }

   @Override
   public BigDecimal len() {
      return BigDecimalMath.sqrt(x.pow(2).add(y.pow(2)), mathContext);
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

   public BigDecimalVector2 set(float x, float y) {
      this.x = new BigDecimal(x, mathContext);
      this.y = new BigDecimal(y, mathContext);
      return this;
   }

   public BigDecimalVector2 set(double x, double y) {
      this.x = new BigDecimal(x, mathContext);
      this.y = new BigDecimal(y, mathContext);
      return this;
   }

   @Override
   public BigDecimalVector2 sub(BigDecimalVector2 v) {
      this.x = x.subtract(v.x);
      this.y = y.subtract(v.y);
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
         this.x = x.divide(len, mathContext);
         this.y = y.divide(len, mathContext);
      }
      return this;
   }

   @Override
   public BigDecimalVector2 add(BigDecimalVector2 v) {
      this.x = x.add(v.x);
      this.y = y.add(v.y);
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
      this.x = x.multiply(scalar);
      this.y = y.multiply(scalar);
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
}
