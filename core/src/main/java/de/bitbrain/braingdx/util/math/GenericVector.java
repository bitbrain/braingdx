package de.bitbrain.braingdx.util.math;

public interface GenericVector<Vector extends GenericVector<Vector, Type>, Type> {
   /**
    * @return a copy of this vector
    */
   Vector cpy();

   /**
    * @return The euclidean length
    */
   Type len();

   /**
    * This method is faster than {@link com.badlogic.gdx.math.Vector#len()} because it avoids calculating a square root. It is useful for comparisons,
    * but not for getting exact lengths, as the return value is the square of the actual length.
    *
    * @return The squared euclidean length
    */
   Type len2();

   /**
    * Sets this vector from the given vector
    *
    * @param v The vector
    * @return This vector for chaining
    */
   Vector set(Vector v);

   /**
    * Subtracts the given vector from this vector.
    *
    * @param v The vector
    * @return This vector for chaining
    */
   Vector sub(Vector v);

   /**
    * Normalizes this vector. Does nothing if it is zero.
    *
    * @return This vector for chaining
    */
   Vector nor();

   /**
    * Adds the given vector to this vector
    *
    * @param v The vector
    * @return This vector for chaining
    */
   Vector add(Vector v);

   /**
    * @param v The other vector
    * @return The dot product between this and the other vector
    */
   Type dot(Vector v);

   /**
    * Scales this vector by a scalar
    *
    * @param scalar The scalar
    * @return This vector for chaining
    */
   Vector scl(Type scalar);

   /**
    * Sets this vector to the unit vector with a random direction
    *
    * @return This vector for chaining
    */
   Vector setToRandomDirection();
}