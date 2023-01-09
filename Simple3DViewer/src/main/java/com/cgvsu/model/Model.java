package com.cgvsu.model;
import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;

import java.util.*;

public class Model {
    public void addVertex(float x, float y, float z){
        vertices.add(new Vector3f(x, y, z));
    }
    public void addNormal(float x, float y, float z){
        normals.add(new Vector3f(x, y, z));
    }
    public void addPolygon(Integer... args){
        ArrayList<Integer> arrayList = new ArrayList<>();
        Polygon polygon = new Polygon();
        Collections.addAll(arrayList, args);
        polygon.setVertexIndices(arrayList);
        polygons.add(polygon);
    }
    public ArrayList<Vector3f> vertices = new ArrayList<Vector3f>();
    public ArrayList<Vector2f> textureVertices = new ArrayList<Vector2f>();
    public ArrayList<Vector3f> normals = new ArrayList<Vector3f>();
    public ArrayList<Polygon> polygons = new ArrayList<Polygon>();
}
