package main.world.systems.light.shape;

import main.world.systems.light.WorldShape;

public class WorldPoly implements WorldShape {
    public float x;
    public float y;
    public float[] edges, edgesOut;

    public WorldPoly(){

    }

    public WorldPoly(float x, float y, float[] edges){
        this.x = x;
        this.y = y;
        this.edges = new float[edges.length];
        for (int i = 0; i < edges.length; i++) {
            this.edges[i] = edges[i];
        }
    }

    public WorldPoly set(float x, float y, float[] edges){
        this.x = x;
        this.y = y;
        for (int i = 0; i < edges.length; i++) {
            this.edges[i] = edges[i];
        }
        return this;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public float[] edges() {
        return edges;
    }
}
