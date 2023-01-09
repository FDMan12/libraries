package io.github.annusshka;

import io.github.annusshka.Math.Vector.Vector;
import io.github.annusshka.Math.Vector.Vector2f;
import io.github.annusshka.Math.Vector.Vector3f;
import io.github.annusshka.Math.Vector.Vector4f;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class VectorTest {
    static final float EPS = 1e-6f;

    @Test
    public void isEqual() {
        Vector2f vector2f1 = new Vector2f(new float[]{1, 2});
        Vector2f vector2f2 = new Vector2f(new float[]{1, 3});
        Vector2f vector2f3 = new Vector2f(new float[]{1, 2});

        Assertions.assertFalse(vector2f1.isEqual(vector2f2));
        Assertions.assertTrue(vector2f1.isEqual(vector2f3));

        Vector3f vector3f1 = new Vector3f(new float[]{1, 2.2f, 3});
        Vector3f vector3f2 = new Vector3f(new float[]{1, 2, 4});
        Vector3f vector3f3 = new Vector3f(new float[]{1, 2.2000001f, 3});

        Assertions.assertFalse(vector3f1.isEqual(vector3f2));
        Assertions.assertTrue(vector3f1.isEqual(vector3f3));

        Vector3f vector4f1 = new Vector3f(new float[]{1, 2, 3, 4});
        Vector3f vector4f2 = new Vector3f(new float[]{1, 2, 4, 3});
        Vector3f vector4f3 = new Vector3f(new float[]{1, 2, 3, 4});

        Assertions.assertFalse(vector4f1.isEqual(vector4f2));
        Assertions.assertTrue(vector4f1.isEqual(vector4f3));
    }

    @Test
    public void sumVector() throws Vector.VectorException {
        Vector2f vector2f1 = new Vector2f(new float[]{3, 5});
        Vector2f vector2f2 = new Vector2f(new float[]{-3, 5});
        Vector3f vector3f1 = new Vector3f(new float[]{1, 2, 3});
        Vector3f vector3f2 = new Vector3f(new float[]{1, 2, 4});
        Vector4f vector4f1 = new Vector4f(new float[]{1, 2, 3, 4});

        Assertions.assertArrayEquals(Vector.sumVector(vector2f1, vector2f2).getVector(), new float[]{0, 10});

        try {
            Vector.sumVector(vector3f1, vector4f1);
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof Vector.VectorException);
            Assertions.assertFalse(e.getMessage().isEmpty());
            Assertions.assertEquals("Vectors of different sizes can't be summed", e.getMessage());
        }

        Assertions.assertArrayEquals(Vector.sumVector(vector3f1, vector3f2).getVector(), new float[]{2, 4, 7});
        Assertions.assertArrayEquals(vector3f2.minusVector(vector3f2, vector3f1).getVector(), new float[]{0, 0, 1});
    }

    @Test
    public void sumWithConstant() {
        Vector2f vector2f = new Vector2f(new float[]{3, 5});
        Vector3f vector3f = new Vector3f(new float[]{1, 2, 3});
        Vector4f vector4f = new Vector4f(new float[]{1, 2, 3, 4});

        Assertions.assertArrayEquals(vector2f.sumWithConstant(5.3f).getVector(), new float[]{8.3f, 10.3f});
        Assertions.assertArrayEquals(vector3f.sumWithConstant(0).getVector(), new float[]{1, 2, 3});
        Assertions.assertArrayEquals(vector4f.sumWithConstant(-2).getVector(), new float[]{-1, 0, 1, 2});
    }

    @Test
    public void minusWithConstant() {
        Vector2f vector2f = new Vector2f(new float[]{3, 5});
        Vector2f resultVector = new Vector2f(new float[]{-0.3f, 1.7f});
        Assertions.assertTrue(vector2f.minusWithConstant(3.3f).isEqual(resultVector));

        Vector3f vector3f = new Vector3f(new float[]{1, 2, 3});
        Assertions.assertArrayEquals(vector3f.minusWithConstant(0).getVector(), new float[]{1, 2, 3});

        Vector4f vector4f = new Vector4f(new float[]{1, 2, 3, 4});
        Assertions.assertArrayEquals(vector4f.minusWithConstant(-2).getVector(), new float[]{3, 4, 5, 6});
    }

    @Test
    public void multiplicateVectorOnConstant() {
        Vector2f vector2f = new Vector2f(new float[]{3, 4});
        Assertions.assertArrayEquals(vector2f.multiplicateVectorOnConstant(0).getVector(), new float[]{0, 0});

        Vector3f vector3f = new Vector3f(new float[]{2, 3, 4});
        Vector3f resultVector = new Vector3f(new float[]{4.8f, 7.2f, 9.6f});
        Assertions.assertTrue(vector3f.multiplicateVectorOnConstant(2.4f).isEqual(resultVector));

        Vector4f vector4f = new Vector4f(new float[]{-4, 3, 4, -5});
        Assertions.assertArrayEquals(vector4f.multiplicateVectorOnConstant(-3).getVector(), new float[]{12, -9, -12, 15});
    }

    @Test
    public void divideVectorOnConstant() throws Vector.VectorException {
        Vector2f vector2f = new Vector2f(new float[]{3, 4});
        try {
            vector2f.divideVectorOnConstant(0);
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof Vector.VectorException);
            Assertions.assertFalse(e.getMessage().isEmpty());
            Assertions.assertEquals("Division by zero", e.getMessage());
        }

        Vector3f vector3f = new Vector3f(new float[]{2, 3, 4});
        Vector3f resultVector1 = new Vector3f(new float[]{0.8333333f, 1.25f, 1.6666666f});
        Assertions.assertTrue(vector3f.divideVectorOnConstant(2.4f).isEqual(resultVector1));

        Vector4f vector4f = new Vector4f(new float[]{-4, 3, 4, -5});
        Vector4f resultVector2 = new Vector4f(new float[]{0.4f, -0.3f, -0.4f, 0.5f});
        Assertions.assertTrue(vector4f.divideVectorOnConstant(-10).isEqual(resultVector2));
    }

    @Test
    public void getVectorLength() {
        Vector2f vector2f = new Vector2f(new float[]{3, 4});
        Assertions.assertEquals(vector2f.getVectorLength(), 5);

        Vector3f vector3f = new Vector3f(new float[]{0, 3.3f, 4.1f});
        float result1 = 5.2630789f;
        Assertions.assertTrue(Math.abs(vector3f.getVectorLength() - result1) < EPS);

        Vector4f vector4f = new Vector4f(new float[]{-4, 3, 4, -5});
        float result2 = 8.1240384f;
        Assertions.assertTrue(Math.abs(vector4f.getVectorLength() - result2) < EPS);
    }

    @Test
    public void normalizeVector() throws Vector.VectorException {
        Vector2f vector2f = new Vector2f(new float[]{0, 0});
        try {
            vector2f.normalizeVector();
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof Vector.VectorException);
            Assertions.assertFalse(e.getMessage().isEmpty());
            Assertions.assertEquals("Division by zero", e.getMessage());
        }

        Vector3f vector3f = new Vector3f(new float[]{0, 3.3f, 4.1f});
        Vector3f resultVector1 = new Vector3f(new float[]{0, 0.6270094f, 0.7790116f});
        Assertions.assertTrue(vector3f.normalizeVector().isEqual(resultVector1));

        Vector4f vector4f = new Vector4f(new float[]{-8, 3, 2, -2});
        Vector4f resultVector2 = new Vector4f(new float[]{-0.8888888f, 0.3333333f, 0.2222222f, -0.2222222f});
        Assertions.assertTrue(vector4f.normalizeVector().isEqual(resultVector2));
    }

    @Test
    public void dotProduct() {
        Vector2f vector2f1 = new Vector2f(new float[]{0, 1});
        Vector2f vector2f2 = new Vector2f(new float[]{5, 6});
        Assertions.assertEquals(Vector.dotProduct(vector2f1, vector2f2), 6);

        Vector3f vector3f1 = new Vector3f(new float[]{0, 3.3f, 4.1f});
        Vector3f vector3f2 = new Vector3f(new float[]{-7.3f, -1, 3.4f});
        float result1 = 10.64f;
        Assertions.assertTrue(Math.abs(Vector.dotProduct(vector3f1, vector3f2) - result1) < EPS);

        Vector4f vector4f1 = new Vector4f(new float[]{-8, 3, 2, -2});
        Vector4f vector4f2 = new Vector4f(new float[]{0.7f, 3, 0, 1});
        float result2 = 1.4f;
        Assertions.assertTrue(Math.abs(Vector.dotProduct(vector4f1, vector4f2) - result2) < EPS);
    }

    @Test
    public void crossProduct() {
        Vector3f vector3f1 = new Vector3f(new float[]{-2, 3, 0});
        Vector3f vector3f2 = new Vector3f(new float[]{-2, 0, 6});
        Vector3f vector3d = new Vector3f(new float[3]);
        vector3d.crossProduct(vector3f1, vector3f2);

        Assertions.assertArrayEquals(vector3d.getVector(), new float[]{18, 12, 6});
    }


}
