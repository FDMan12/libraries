package com.cgvsu.affinetransformation;

import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;
import com.cgvsu.objreader.ObjReader;
import com.cgvsu.objwriter.ObjWriter;
import io.github.annusshka.Math.Matrix.Matrix;
import io.github.annusshka.Math.Vector.Vector;
import io.github.annusshka.Math.Vector.Vector3f;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;

class AffineTransformationTest {
    AffineTransformation affineTransformation;

    @Test
    void vertexScalingTest() {
        Vector3f scaling = new Vector3f(new float[]{2, 3, 5});
        Vector3f vertex = new Vector3f(new float[]{2, 3, 4});
        Assertions.assertArrayEquals(AffineTransformation.vertexScaling(vertex, scaling).getVector(),
                new float[]{4, 9, 20});
    }

    @Test
    void vertexEmptyScalingTest() {
        Vector3f scaling = new Vector3f(new float[]{1, 1, 1});
        Vector3f vertex = new Vector3f(new float[]{2, 3, 4});
        Assertions.assertArrayEquals(AffineTransformation.vertexScaling(vertex, scaling).getVector(),
                vertex.getVector());
    }

    @Test
    void vertexTranslationTest() {
        Vector3f translation = new Vector3f(new float[]{2, 3, 5});
        Vector3f vertex = new Vector3f(new float[]{2, 3, 4});
        Assertions.assertArrayEquals(AffineTransformation.vertexTranslation(vertex, translation).getVector(),
                new float[]{4, 6, 9});
    }

    @Test
    void vertexEmptyTranslationTest() {
        Vector3f translation = new Vector3f(new float[]{0, 0, 0});
        Vector3f vertex = new Vector3f(new float[]{2, 3, 4});
        Assertions.assertArrayEquals(AffineTransformation.vertexTranslation(vertex, translation).getVector(),
                vertex.getVector());
    }

    @Test
    void vertexRotationTest(){
        Vector3f rotation = new Vector3f(new float[]{90, 0, 0});
        Vector3f vertex = new Vector3f(new float[]{2, 3, 4});
        Assertions.assertArrayEquals(AffineTransformation.vertexRotation(vertex, rotation).getVector(),
                new float[]{2, -4, 3});
    }

    @Test
    void vertexRotationEmptyTest(){
        Vector3f rotation = new Vector3f(new float[]{0, 0, 0});
        Vector3f vertex = new Vector3f(new float[]{2, 3, 4});
        Assertions.assertArrayEquals(AffineTransformation.vertexRotation(vertex, rotation).getVector(),
                vertex.getVector());
    }

    @Test
    void normalScalingTest() {
        Vector3f scaling = new Vector3f(new float[]{2, 3, 4});
        Vector3f vertex1 = new Vector3f(new float[]{2, 2, 2});
        Vector3f vertex2 = new Vector3f(new float[]{1, 1, 1});
        Vector3f vertex3 = new Vector3f(new float[]{3, 3, 3});
        Vector3f side1 = new Vector3f(new float[]
                {vertex3.get(0) - vertex1.get(0), vertex3.get(1) - vertex1.get(1), vertex3.get(2) - vertex1.get(2)});
        Vector3f side2 = new Vector3f(new float[]
                {vertex3.get(0) - vertex2.get(0), vertex3.get(1) - vertex2.get(1), vertex3.get(2) - vertex2.get(2)});
        Vector3f normal = new Vector3f();
        normal.crossProduct(side1, side2);

        vertex1 = AffineTransformation.vertexScaling(vertex1, scaling);
        vertex2 = AffineTransformation.vertexScaling(vertex2, scaling);
        vertex3 = AffineTransformation.vertexScaling(vertex3, scaling);
        normal = AffineTransformation.normalScaling(normal, scaling);

        side1 = new Vector3f(new float[]
                {vertex3.get(0) - vertex1.get(0), vertex3.get(1) - vertex1.get(1), vertex3.get(2) - vertex1.get(2)});
        side2 = new Vector3f(new float[]
                {vertex3.get(0) - vertex2.get(0), vertex3.get(1) - vertex2.get(1), vertex3.get(2) - vertex2.get(2)});
        Vector3f newNormal = new Vector3f();
        newNormal.crossProduct(side1, side2);
        Assertions.assertArrayEquals(newNormal.getVector(), normal.getVector());
    }

    @Test
    void normalTranslationTest() {
        Vector3f translation = new Vector3f(new float[]{2, 3, 4});
        Vector3f vertex1 = new Vector3f(new float[]{2, 2, 2});
        Vector3f vertex2 = new Vector3f(new float[]{1, 1, 1});
        Vector3f vertex3 = new Vector3f(new float[]{3, 3, 3});
        Vector3f side1 = new Vector3f(new float[]
                {vertex3.get(0) - vertex1.get(0), vertex3.get(1) - vertex1.get(1), vertex3.get(2) - vertex1.get(2)});
        Vector3f side2 = new Vector3f(new float[]
                {vertex3.get(0) - vertex2.get(0), vertex3.get(1) - vertex2.get(1), vertex3.get(2) - vertex2.get(2)});
        Vector3f normal = new Vector3f();
        normal.crossProduct(side1, side2);

        vertex1 = AffineTransformation.vertexTranslation(vertex1, translation);
        vertex2 = AffineTransformation.vertexTranslation(vertex2, translation);
        vertex3 = AffineTransformation.vertexTranslation(vertex3, translation);
        normal = AffineTransformation.normalTranslation(normal, translation);

        side1 = new Vector3f(new float[]
                {vertex3.get(0) - vertex1.get(0), vertex3.get(1) - vertex1.get(1), vertex3.get(2) - vertex1.get(2)});
        side2 = new Vector3f(new float[]
                {vertex3.get(0) - vertex2.get(0), vertex3.get(1) - vertex2.get(1), vertex3.get(2) - vertex2.get(2)});
        Vector3f newNormal = new Vector3f();
        newNormal.crossProduct(side1, side2);
        Assertions.assertArrayEquals(newNormal.getVector(), normal.getVector());
    }

    @Test
    void normalRotationTest(){
        Vector3f rotation = new Vector3f(new float[]{90, 0, 0});
        Vector3f vertex1 = new Vector3f(new float[]{2, 2, 2});
        Vector3f vertex2 = new Vector3f(new float[]{1, 1, 1});
        Vector3f vertex3 = new Vector3f(new float[]{3, 3, 3});
        Vector3f side1 = new Vector3f(new float[]
                {vertex3.get(0) - vertex1.get(0), vertex3.get(1) - vertex1.get(1), vertex3.get(2) - vertex1.get(2)});
        Vector3f side2 = new Vector3f(new float[]
                {vertex3.get(0) - vertex2.get(0), vertex3.get(1) - vertex2.get(1), vertex3.get(2) - vertex2.get(2)});
        Vector3f normal = new Vector3f();
        normal.crossProduct(side1, side2);

        vertex1 = AffineTransformation.vertexRotation(vertex1, rotation);
        vertex2 = AffineTransformation.vertexRotation(vertex2, rotation);
        vertex3 = AffineTransformation.vertexRotation(vertex3, rotation);
        normal = AffineTransformation.normalRotation(normal, rotation);

        side1 = new Vector3f(new float[]
                {vertex3.get(0) - vertex1.get(0), vertex3.get(1) - vertex1.get(1), vertex3.get(2) - vertex1.get(2)});
        side2 = new Vector3f(new float[]
                {vertex3.get(0) - vertex2.get(0), vertex3.get(1) - vertex2.get(1), vertex3.get(2) - vertex2.get(2)});
        Vector3f newNormal = new Vector3f();
        newNormal.crossProduct(side1, side2);
        Assertions.assertArrayEquals(newNormal.getVector(), normal.getVector());
    }

    @Test
    void createScalingMatrixTest() {
        try {
            Vector3f scaling = new Vector3f(new float[]{2, 3, 5});
            Matrix result = AffineTransformation.createScalingMatrix(scaling);
            Assertions.assertArrayEquals(result.getVector(), new float[]{2, 0, 0, 0, 3, 0, 0, 0, 5});
        } catch (Exception e){
            Assertions.fail();
        }
    }

    @Test
    void createTranslationMatrixTest() {
        try {
            Vector3f translation = new Vector3f(new float[]{2, 3, 5});
            Matrix result = AffineTransformation.createTranslationMatrix(translation);
            Assertions.assertArrayEquals(result.getVector(), new float[]{1, 0, 0, 2, 0, 1, 0, 3, 0, 0, 1, 5, 0, 0, 0, 1});
        } catch (Exception e){
            Assertions.fail();
        }
    }

    @Test
    void createRotationMatrixTest(){
        try {
            Vector3f rotation = new Vector3f(new float[]{90, 90, 90});
            Matrix[] result = AffineTransformation.createRotationMatrix(rotation);
            Assertions.assertArrayEquals(result[0].getVector(), new float[]{1, 0, 0, 0, 0, -1, 0, 1, 0});
            Assertions.assertArrayEquals(result[1].getVector(), new float[]{0, 0, 1, 0, 1, 0, -1, 0, 0});
            Assertions.assertArrayEquals(result[2].getVector(), new float[]{0, -1, 0, 1, 0, 0, 0, 0, 1});
        } catch (Exception e){
            Assertions.fail();
        }
    }

    @Test
    void modelTransformTest(){
        Model model = new Model();
        model.addVertex(0, 0, 0);
        model.addVertex(0, 0, 1);
        model.addVertex(1, 0, 1);
        model.addVertex(1, 0, 0);
        model.addVertex(0, 1, 0);
        model.addVertex(0, 1, 1);
        model.addVertex(1, 1, 1);
        model.addVertex(1, 1, 0);
        model.addPolygon(1, 2, 3, 4);
        model.addPolygon(5, 6, 7, 8);
        model.addPolygon(1, 2, 6, 5);
        model.addPolygon(2, 3, 7, 6);
        model.addPolygon(3, 4, 8, 7);
        model.addPolygon(1, 4, 8, 5);

        Vector3f scaling = new Vector3f(new float[]{2, 2, 2});
        Vector3f translation = new Vector3f(new float[]{2, 3, 4});
        Vector3f rotation = new Vector3f(new float[]{90, 0, 0});
        Model result = AffineTransformation.modelTransform(model, translation, rotation, scaling);
        for(int i = 0; i < 8; i++){
            Assertions.assertEquals((model.vertices.get(i).x) * 2 + 2, result.vertices.get(i).x);
            Assertions.assertEquals(-(model.vertices.get(i).z) * 2 + 3, result.vertices.get(i).y);
            Assertions.assertEquals((model.vertices.get(i).y) * 2 + 4, result.vertices.get(i).z);
        }
        Assertions.assertEquals(model.polygons.size(), result.polygons.size());
        Assertions.assertEquals(model.textureVertices.size(), result.textureVertices.size());
    }
}