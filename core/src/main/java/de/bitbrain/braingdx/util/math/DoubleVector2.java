package de.bitbrain.braingdx.util.math;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.NumberUtils;

public class DoubleVector2 {

   public final static DoubleVector2 X = new DoubleVector2(1, 0);
   public final static DoubleVector2 Y = new DoubleVector2(0, 1);
   public final static DoubleVector2 Zero = new DoubleVector2(0, 0);

   /** the x-component of this vector **/
   public double x;
   /** the y-component of this vector **/
   public double y;

   /** Constructs a new vector at (0,0) */
   public DoubleVector2 () {
   }

   /** Constructs a vector with the given components
    * @param x The x-component
    * @param y The y-component */
   public DoubleVector2 (double x, double y) {
      this.x = x;
      this.y = y;
   }

   /** Constructs a vector from the given vector
    * @param v The vector */
   public DoubleVector2 (DoubleVector2 v) {
      set(v);
   }

   
   public DoubleVector2 cpy () {
      return new DoubleVector2(this);
   }

   public static double len (double x, double y) {
      return (double)Math.sqrt(x * x + y * y);
   }

   
   public double len () {
      return (double)Math.sqrt(x * x + y * y);
   }

   public static double len2 (double x, double y) {
      return x * x + y * y;
   }

   
   public double len2 () {
      return x * x + y * y;
   }

   
   public DoubleVector2 set (DoubleVector2 v) {
      x = v.x;
      y = v.y;
      return this;
   }

   /** Sets the components of this vector
    * @param x The x-component
    * @param y The y-component
    * @return This vector for chaining */
   public DoubleVector2 set (double x, double y) {
      this.x = x;
      this.y = y;
      return this;
   }

   
   public DoubleVector2 sub (DoubleVector2 v) {
      x -= v.x;
      y -= v.y;
      return this;
   }

   /** Substracts the other vector from this vector.
    * @param x The x-component of the other vector
    * @param y The y-component of the other vector
    * @return This vector for chaining */
   public DoubleVector2 sub (double x, double y) {
      this.x -= x;
      this.y -= y;
      return this;
   }

   
   public DoubleVector2 nor () {
      double len = len();
      if (len != 0) {
         x /= len;
         y /= len;
      }
      return this;
   }

   
   public DoubleVector2 add (DoubleVector2 v) {
      x += v.x;
      y += v.y;
      return this;
   }

   /** Adds the given components to this vector
    * @param x The x-component
    * @param y The y-component
    * @return This vector for chaining */
   public DoubleVector2 add (double x, double y) {
      this.x += x;
      this.y += y;
      return this;
   }

   public static double dot (double x1, double y1, double x2, double y2) {
      return x1 * x2 + y1 * y2;
   }

   
   public double dot (DoubleVector2 v) {
      return x * v.x + y * v.y;
   }

   public double dot (double ox, double oy) {
      return x * ox + y * oy;
   }

   
   public DoubleVector2 scl (double scalar) {
      x *= scalar;
      y *= scalar;
      return this;
   }

   /** Multiplies this vector by a scalar
    * @return This vector for chaining */
   public DoubleVector2 scl (double x, double y) {
      this.x *= x;
      this.y *= y;
      return this;
   }

   
   public DoubleVector2 scl (DoubleVector2 v) {
      this.x *= v.x;
      this.y *= v.y;
      return this;
   }

   
   public DoubleVector2 mulAdd (DoubleVector2 vec, double scalar) {
      this.x += vec.x * scalar;
      this.y += vec.y * scalar;
      return this;
   }

   
   public DoubleVector2 mulAdd (DoubleVector2 vec, DoubleVector2 mulVec) {
      this.x += vec.x * mulVec.x;
      this.y += vec.y * mulVec.y;
      return this;
   }

   public static double dst (double x1, double y1, double x2, double y2) {
      final double x_d = x2 - x1;
      final double y_d = y2 - y1;
      return (double)Math.sqrt(x_d * x_d + y_d * y_d);
   }

   
   public double dst (DoubleVector2 v) {
      final double x_d = v.x - x;
      final double y_d = v.y - y;
      return (double)Math.sqrt(x_d * x_d + y_d * y_d);
   }

   /** @param x The x-component of the other vector
    * @param y The y-component of the other vector
    * @return the distance between this and the other vector */
   public double dst (double x, double y) {
      final double x_d = x - this.x;
      final double y_d = y - this.y;
      return (double)Math.sqrt(x_d * x_d + y_d * y_d);
   }

   public static double dst2 (double x1, double y1, double x2, double y2) {
      final double x_d = x2 - x1;
      final double y_d = y2 - y1;
      return x_d * x_d + y_d * y_d;
   }

   
   public double dst2 (DoubleVector2 v) {
      final double x_d = v.x - x;
      final double y_d = v.y - y;
      return x_d * x_d + y_d * y_d;
   }

   /** @param x The x-component of the other vector
    * @param y The y-component of the other vector
    * @return the squared distance between this and the other vector */
   public double dst2 (double x, double y) {
      final double x_d = x - this.x;
      final double y_d = y - this.y;
      return x_d * x_d + y_d * y_d;
   }

   
   public DoubleVector2 limit (double limit) {
      return limit2(limit * limit);
   }

   
   public DoubleVector2 limit2 (double limit2) {
      double len2 = len2();
      if (len2 > limit2) {
         return scl((double)Math.sqrt(limit2 / len2));
      }
      return this;
   }

   
   public DoubleVector2 clamp (double min, double max) {
      final double len2 = len2();
      if (len2 == 0f) return this;
      double max2 = max * max;
      if (len2 > max2) return scl((double)Math.sqrt(max2 / len2));
      double min2 = min * min;
      if (len2 < min2) return scl((double)Math.sqrt(min2 / len2));
      return this;
   }

   
   public DoubleVector2 setLength (double len) {
      return setLength2(len * len);
   }

   
   public DoubleVector2 setLength2 (double len2) {
      double oldLen2 = len2();
      return (oldLen2 == 0 || oldLen2 == len2) ? this : scl((double)Math.sqrt(len2 / oldLen2));
   }

   /** Converts this {@code DoubleVector2} to a string in the format {@code (x,y)}.
    * @return a string representation of this object. */
   
   public String toString () {
      return "(" + x + "," + y + ")";
   }

   /** Sets this {@code DoubleVector2} to the value represented by the specified string according to the format of {@link #toString()}.
    * @param v the string.
    * @return this vector for chaining */
   public DoubleVector2 fromString (String v) {
      int s = v.indexOf(',', 1);
      if (s != -1 && v.charAt(0) == '(' && v.charAt(v.length() - 1) == ')') {
         try {
            double x = Double.parseDouble(v.substring(1, s));
            double y = Double.parseDouble(v.substring(s + 1, v.length() - 1));
            return this.set(x, y);
         } catch (NumberFormatException ex) {
            // Throw a GdxRuntimeException
         }
      }
      throw new GdxRuntimeException("Malformed DoubleVector2: " + v);
   }

   /** Left-multiplies this vector by the given matrix
    * @param mat the matrix
    * @return this vector */
   public DoubleVector2 mul (Matrix3 mat) {
      double x = this.x * mat.val[0] + this.y * mat.val[3] + mat.val[6];
      double y = this.x * mat.val[1] + this.y * mat.val[4] + mat.val[7];
      this.x = x;
      this.y = y;
      return this;
   }

   /** Calculates the 2D cross product between this and the given vector.
    * @param v the other vector
    * @return the cross product */
   public double crs (DoubleVector2 v) {
      return this.x * v.y - this.y * v.x;
   }

   /** Calculates the 2D cross product between this and the given vector.
    * @param x the x-coordinate of the other vector
    * @param y the y-coordinate of the other vector
    * @return the cross product */
   public double crs (double x, double y) {
      return this.x * y - this.y * x;
   }

   /** @return the angle in degrees of this vector (point) relative to the x-axis. Angles are towards the positive y-axis
    *         (typically counter-clockwise) and between 0 and 360. */
   public double angle () {
      double angle = (double)Math.atan2(y, x) * MathUtils.radiansToDegrees;
      if (angle < 0) angle += 360;
      return angle;
   }

   /** @return the angle in degrees of this vector (point) relative to the given vector. Angles are towards the positive y-axis
    *         (typically counter-clockwise.) between -180 and +180 */
   public double angle (DoubleVector2 reference) {
      return (double)Math.atan2(crs(reference), dot(reference)) * MathUtils.radiansToDegrees;
   }

   /** @return the angle in radians of this vector (point) relative to the x-axis. Angles are towards the positive y-axis.
    *         (typically counter-clockwise) */
   public double angleRad () {
      return (double)Math.atan2(y, x);
   }

   /** @return the angle in radians of this vector (point) relative to the given vector. Angles are towards the positive y-axis.
    *         (typically counter-clockwise.) */
   public double angleRad (DoubleVector2 reference) {
      return (double)Math.atan2(crs(reference), dot(reference));
   }

   /** Sets the angle of the vector in degrees relative to the x-axis, towards the positive y-axis (typically counter-clockwise).
    * @param degrees The angle in degrees to set. */
   public DoubleVector2 setAngle (double degrees) {
      return setAngleRad(degrees * MathUtils.degreesToRadians);
   }

   /** Sets the angle of the vector in radians relative to the x-axis, towards the positive y-axis (typically counter-clockwise).
    * @param radians The angle in radians to set. */
   public DoubleVector2 setAngleRad (double radians) {
      this.set(len(), 0f);
      this.rotateRad(radians);

      return this;
   }

   /** Rotates the DoubleVector2 by the given angle, counter-clockwise assuming the y-axis points up.
    * @param degrees the angle in degrees */
   public DoubleVector2 rotate (double degrees) {
      return rotateRad(degrees * MathUtils.degreesToRadians);
   }

   /** Rotates the DoubleVector2 by the given angle around reference vector, counter-clockwise assuming the y-axis points up.
    * @param degrees the angle in degrees
    * @param reference center DoubleVector2 */
   public DoubleVector2 rotateAround (DoubleVector2 reference, double degrees) {
      return this.sub(reference).rotate(degrees).add(reference);
   }

   /** Rotates the DoubleVector2 by the given angle, counter-clockwise assuming the y-axis points up.
    * @param radians the angle in radians */
   public DoubleVector2 rotateRad (double radians) {
      double cos = (double)Math.cos(radians);
      double sin = (double)Math.sin(radians);

      double newX = this.x * cos - this.y * sin;
      double newY = this.x * sin + this.y * cos;

      this.x = newX;
      this.y = newY;

      return this;
   }

   /** Rotates the DoubleVector2 by the given angle around reference vector, counter-clockwise assuming the y-axis points up.
    * @param radians the angle in radians
    * @param reference center DoubleVector2 */
   public DoubleVector2 rotateAroundRad (DoubleVector2 reference, double radians) {
      return this.sub(reference).rotateRad(radians).add(reference);
   }

   /** Rotates the DoubleVector2 by 90 degrees in the specified direction, where >= 0 is counter-clockwise and < 0 is clockwise. */
   public DoubleVector2 rotate90 (int dir) {
      double x = this.x;
      if (dir >= 0) {
         this.x = -y;
         y = x;
      } else {
         this.x = y;
         y = -x;
      }
      return this;
   }

   
   public DoubleVector2 lerp (DoubleVector2 target, double alpha) {
      final double invAlpha = 1.0f - alpha;
      this.x = (x * invAlpha) + (target.x * alpha);
      this.y = (y * invAlpha) + (target.y * alpha);
      return this;
   }

   
   public DoubleVector2 interpolate (DoubleVector2 target, float alpha, Interpolation interpolation) {
      return lerp(target, interpolation.apply(alpha));
   }

   
   public DoubleVector2 setToRandomDirection () {
      double theta = MathUtils.random(0f, MathUtils.PI2);
      return this.set(MathUtils.cos((float) theta), MathUtils.sin((float) theta));
   }

   
   public int hashCode () {
      final int prime = 31;
      long result = 1;
      result = prime * result + NumberUtils.doubleToLongBits(x);
      result = prime * result + NumberUtils.doubleToLongBits(y);
      return (int) result;
   }

   
   public boolean equals (Object obj) {
      if (this == obj) return true;
      if (obj == null) return false;
      if (getClass() != obj.getClass()) return false;
      DoubleVector2 other = (DoubleVector2)obj;
      if (NumberUtils.doubleToLongBits(x) != NumberUtils.doubleToLongBits(other.x)) return false;
      if (NumberUtils.doubleToLongBits(y) != NumberUtils.doubleToLongBits(other.y)) return false;
      return true;
   }

   
   public boolean epsilonEquals (DoubleVector2 other, double epsilon) {
      if (other == null) return false;
      if (Math.abs(other.x - x) > epsilon) return false;
      if (Math.abs(other.y - y) > epsilon) return false;
      return true;
   }

   /** Compares this vector with the other vector, using the supplied epsilon for fuzzy equality testing.
    * @return whether the vectors are the same. */
   public boolean epsilonEquals (double x, double y, double epsilon) {
      if (Math.abs(x - this.x) > epsilon) return false;
      if (Math.abs(y - this.y) > epsilon) return false;
      return true;
   }

   /** Compares this vector with the other vector using MathUtils.double_ROUNDING_ERROR for fuzzy equality testing
    * @param other other vector to compare
    * @return true if vector are equal, otherwise false */
   public boolean epsilonEquals (final DoubleVector2 other) {
      return epsilonEquals(other, MathUtils.FLOAT_ROUNDING_ERROR);
   }

   /** Compares this vector with the other vector using MathUtils.double_ROUNDING_ERROR for fuzzy equality testing
    * @param x x component of the other vector to compare
    * @param y y component of the other vector to compare
    * @return true if vector are equal, otherwise false */
   public boolean epsilonEquals (double x, double y) {
      return epsilonEquals(x, y, MathUtils.FLOAT_ROUNDING_ERROR);
   }

   
   public boolean isUnit () {
      return isUnit(0.000000001f);
   }

   
   public boolean isUnit (final double margin) {
      return Math.abs(len2() - 1f) < margin;
   }

   
   public boolean isZero () {
      return x == 0 && y == 0;
   }

   
   public boolean isZero (final double margin) {
      return len2() < margin;
   }

   
   public boolean isOnLine (DoubleVector2 other) {
      return MathUtils.isZero((float) (x * other.y - y * other.x));
   }

   
   public boolean isOnLine (DoubleVector2 other, float epsilon) {
      return MathUtils.isZero((float) (x * other.y - y * other.x), epsilon);
   }

   
   public boolean isCollinear (DoubleVector2 other, float epsilon) {
      return isOnLine(other, epsilon) && dot(other) > 0f;
   }

   
   public boolean isCollinear (DoubleVector2 other) {
      return isOnLine(other) && dot(other) > 0f;
   }

   
   public boolean isCollinearOpposite (DoubleVector2 other, float epsilon) {
      return isOnLine(other, epsilon) && dot(other) < 0f;
   }

   
   public boolean isCollinearOpposite (DoubleVector2 other) {
      return isOnLine(other) && dot(other) < 0f;
   }

   
   public boolean isPerpendicular (DoubleVector2 vector) {
      return MathUtils.isZero((float) dot(vector));
   }

   
   public boolean isPerpendicular (DoubleVector2 vector, float epsilon) {
      return MathUtils.isZero((float) dot(vector), epsilon);
   }

   
   public boolean hasSameDirection (DoubleVector2 vector) {
      return dot(vector) > 0;
   }

   
   public boolean hasOppositeDirection (DoubleVector2 vector) {
      return dot(vector) < 0;
   }

   
   public DoubleVector2 setZero () {
      this.x = 0;
      this.y = 0;
      return this;
   }
}
