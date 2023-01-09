package com.cgvsu.affinetransformation;

import com.cgvsu.model.Model;

import io.github.annusshka.Math.Matrix.Matrix;
import io.github.annusshka.Math.Matrix.Matrix3f;
import io.github.annusshka.Math.Matrix.Matrix4f;
import io.github.annusshka.Math.Vector.Vector3f;
import io.github.annusshka.Math.Vector.Vector4f;


public class AffineTransformation {
    private static float roundByAccuracy(float value){
        if(Math.abs(value) < 10e-10){
            return 0;
        } else if (1 - Math.abs(value) < 10e-10){
            return Math.signum(value);
        }
        return value;
    }

    /**
     * Применяет аффинные преобразования к модели, используя заданные параметры
     * @param model Исходная модель
     * @param translation Параметры смещения
     * @param rotation Параметры вращения
     * @param scaling Параметры масштабирования
     * @return Преобразованная модель
     */
    public static Model modelTransform(Model model, Vector3f translation, Vector3f rotation, Vector3f scaling){
        //Scaling
        //Rotation
        //Translation
        Model result = new Model();
        for (int i = 0; i < model.vertices.size(); i++) {
            Vector3f vertex = new Vector3f(new float[]{model.vertices.get(i).x, model.vertices.get(i).y, model.vertices.get(i).z});
            vertex = vertexScaling(vertex, scaling);
            vertex = vertexRotation(vertex, rotation);
            vertex = vertexTranslation(vertex, translation);
            result.addVertex(vertex.get(0), vertex.get(1), vertex.get(2));
        }
        for (int i = 0; i < model.normals.size(); i++) {
            Vector3f normal = new Vector3f(new float[]{model.vertices.get(i).x, model.vertices.get(i).y, model.vertices.get(i).z});
            normal = normalScaling(normal, scaling);
            normal = normalRotation(normal, rotation);
            normal = normalTranslation(normal, translation);
            result.addNormal(normal.get(0), normal.get(1), normal.get(2));
        }
        result.textureVertices.addAll(model.textureVertices);
        result.polygons.addAll(model.polygons);
        return result;
    }

    /**
     * Смещает нормаль, используя заданные параметры
     * @param normal нормаль
     * @param translation Параметры смещения
     * @return Полученная нормаль
     */
    public static Vector3f normalTranslation(Vector3f normal, Vector3f translation){
        Vector3f result;
        Matrix4f translationMatrix = createTranslationMatrix(translation);
        try {
            Vector4f tempVector = new Vector4f(new float[]{normal.get(0), normal.get(1), normal.get(2), 1});
            tempVector = (Vector4f) (Matrix.transposeMatrix(Matrix.getInverseMatrix(translationMatrix))).multiplicateOnVector(tempVector);
            result = new Vector3f(new float[]{tempVector.get(0), tempVector.get(1), tempVector.get(2)});
            return result;
        } catch (Exception e) {
            return normal;
        }
    }

    /**
     * Вращает нормаль, используя заданные параметры
     * @param normal нормаль
     * @param rotation Параметры вращения
     * @return Полученная нормаль
     */
    public static Vector3f normalRotation(Vector3f normal, Vector3f rotation){
        Vector3f result = new Vector3f(new float[]{normal.get(0), normal.get(1), normal.get(2)});
        Matrix3f[] rotationMatrix = createRotationMatrix(rotation);
        try {
            result = (Vector3f) (Matrix.transposeMatrix(Matrix.getInverseMatrix(rotationMatrix[0]))).multiplicateOnVector(result);
            result = (Vector3f) (Matrix.transposeMatrix(Matrix.getInverseMatrix(rotationMatrix[1]))).multiplicateOnVector(result);
            result = (Vector3f) (Matrix.transposeMatrix(Matrix.getInverseMatrix(rotationMatrix[2]))).multiplicateOnVector(result);
            return result;
        } catch (Exception e) {
            return normal;
        }
    }

    /**
     * Масштабирует нормаль, используя заданные параметры
     * @param normal нормаль
     * @param scaling Параметры масштабирования
     * @return Полученная нормаль
     */
    public static Vector3f normalScaling(Vector3f normal, Vector3f scaling){
        Vector3f result;
        Matrix3f scalingMatrix = createScalingMatrix(scaling);
        try {
            result = (Vector3f) (Matrix.transposeMatrix(Matrix.getInverseMatrix(scalingMatrix))).multiplicateOnVector(normal);
            return result;
        } catch (Exception e) {
            return normal;
        }
    }

    /**
     * Смещает вершину, используя заданные параметры
     * @param vertex Вершина
     * @param translation Параметры смещения
     * @return Полученная вершина
     */
    public static Vector3f vertexTranslation(Vector3f vertex, Vector3f translation){
        Vector3f result;
        Matrix4f translationMatrix = createTranslationMatrix(translation);
        try {
            Vector4f tempVector = new Vector4f(new float[]{vertex.get(0), vertex.get(1), vertex.get(2), 1});
            tempVector = (Vector4f) translationMatrix.multiplicateOnVector(tempVector);
            result = new Vector3f(new float[]{tempVector.get(0), tempVector.get(1), tempVector.get(2)});
            return result;
        } catch (Exception e) {
            return vertex;
        }
    }

    /**
     * Вращает вершину, используя заданные параметры
     * @param vertex Вершина
     * @param rotation Параметры вращения
     * @return Полученная вершина
     */
    public static Vector3f vertexRotation(Vector3f vertex, Vector3f rotation){
        Vector3f result = new Vector3f(new float[]{vertex.get(0), vertex.get(1), vertex.get(2)});
        Matrix3f[] rotationMatrix = createRotationMatrix(rotation);
        try {
            result = (Vector3f) rotationMatrix[0].multiplicateOnVector(result);
            result = (Vector3f) rotationMatrix[1].multiplicateOnVector(result);
            result = (Vector3f) rotationMatrix[2].multiplicateOnVector(result);
            return result;
        } catch (Exception e) {
            return vertex;
        }
    }

    /**
     * Масштабирует вершину, используя заданные параметры
     * @param vertex Вершина
     * @param scaling Параметры масштабирования
     * @return Полученная вершина
     */
    public static Vector3f vertexScaling(Vector3f vertex, Vector3f scaling){
        Vector3f result;
        Matrix3f scalingMatrix = createScalingMatrix(scaling);
        try {
            result = (Vector3f) scalingMatrix.multiplicateOnVector(vertex);
            return result;
        } catch (Exception e) {
            return vertex;
        }
    }

    /**
     * Создает матрицу смещения для каждой из осей
     * @param translation Вектор со значениями смещения для каждой из осей
     * @return Матрица смещения для каждой из осей
     */
    public static Matrix4f createTranslationMatrix(Vector3f translation){
        return new Matrix4f(new float[]{
                1, 0, 0, translation.getVector()[0],
                0, 1, 0, translation.getVector()[1],
                0, 0, 1, translation.getVector()[2],
                0, 0, 0, 1
        });
    }

    /**
     * Создает 3 матрицы вращения относительно каждой из осей
     * @param rotation Вектор с углами поворота относительно каждой из осей соответственно
     * @return Матрицы вращения для каждой из осей
     */
    public static Matrix3f[] createRotationMatrix(Vector3f rotation){
        Matrix3f[] matrices = new Matrix3f[3];
        //Отсутствие поворота
        Vector3f vector = new Vector3f(new float[]{1, 1, 1});
        if(rotation.isEqual(vector)){
            Matrix3f identityMatrix = new Matrix3f(new float[]{1, 0, 0, 0, 1, 0, 0, 0, 1});
            for(int i = 0; i < 3; i++){
                matrices[i] = identityMatrix;
            }
            return matrices;
        }
        //Относительно оси x
        matrices[0] = new Matrix3f(new float[]{
                1, 0, 0,
                0, roundByAccuracy((float) Math.cos(Math.toRadians(rotation.getVector()[0]))),
                roundByAccuracy((float) -Math.sin(Math.toRadians(rotation.getVector()[0]))),
                0, roundByAccuracy((float) Math.sin(Math.toRadians(rotation.getVector()[0]))),
                roundByAccuracy((float) Math.cos(Math.toRadians(rotation.getVector()[0])))
        });
        //Относительно оси y
        matrices[1] = new Matrix3f(new float[]{
                roundByAccuracy((float) Math.cos(Math.toRadians(rotation.getVector()[1]))),
                0, roundByAccuracy((float) Math.sin(Math.toRadians(rotation.getVector()[1]))),
                0, 1, 0,
                roundByAccuracy((float) -Math.sin(Math.toRadians(rotation.getVector()[1]))), 0,
                roundByAccuracy((float) Math.cos(Math.toRadians(rotation.getVector()[1])))
        });
        //Относительно оси z
        matrices[2] = new Matrix3f(new float[]{
                roundByAccuracy((float) Math.cos(Math.toRadians(rotation.getVector()[2]))),
                roundByAccuracy((float) -Math.sin(Math.toRadians(rotation.getVector()[2]))), 0,
                roundByAccuracy((float) Math.sin(Math.toRadians(rotation.getVector()[2]))),
                roundByAccuracy((float) Math.cos(Math.toRadians(rotation.getVector()[2]))), 0,
                0, 0, 1
        });
        return matrices;
    }

    /**
     * Создает матрицу масштабирования по параметрам
     * @param scaling Вектор со значениями масштабирования относительно осей
     * @return Матрица масштабирования
     */
    public static Matrix3f createScalingMatrix(Vector3f scaling){
        return new Matrix3f(new float[]{
                scaling.getVector()[0], 0, 0,
                0, scaling.getVector()[1], 0,
                0, 0, scaling.getVector()[2]
        });
    }
}
