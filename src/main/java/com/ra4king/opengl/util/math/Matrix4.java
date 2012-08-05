package com.ra4king.opengl.util.math;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

public class Matrix4 {
	private FloatBuffer matrix;
	
	public Matrix4() {
		matrix = BufferUtils.createFloatBuffer(16);
	}
	
	public Matrix4(float[] m) {
		this();
		put(m);
	}
	
	public Matrix4(Matrix4 m) {
		this();
		put(m);
	}
	
	public Matrix4 clear() {
		matrix.clear();
		return this;
	}
	
	public Matrix4 clearToIdentity() {
		return clear().put(0,1).put(5,1).put(10,1).put(15,1);
	}
	
	public Matrix4 clearToOrtho(float left, float right, float bottom, float top, float near, float far) {
		return clear().put(0,2/(right-left)).put(5,2/(top-bottom)).put(10,-2/(far-near)).put(12,-(right+left)/(right-left)).put(13,-(top+bottom)/(top-bottom)).put(14,-(far+near)/(far-near)).put(15,1);
	}
	
	public Matrix4 clearToPerspective(float fovRad, float width, float height, float near, float far) {
		float fov = 1 / (float)Math.tan(fovRad/2);
		return clear().put(0,fov/(width/height)).put(5,fov).put(10,(far+near)/(near-far)).put(14,(2*far*near)/(near-far)).put(11,-1);
	}
	
	public float get(int index) {
		return matrix.get(index);
	}
	
	public Matrix4 put(int index, float f) {
		matrix.put(index, f);
		return this;
	}
	
	public Matrix4 put(float[] m) {
		if(m.length < 16)
			throw new IllegalArgumentException("float array must have at least 16 values.");
		
		matrix.position(0);
		matrix.put(m,0,16);
		
		return this;
	}
	
	public Matrix4 put(Matrix4 m) {
		FloatBuffer b = m.getBuffer();
		while(b.hasRemaining())
			matrix.put(b.get());
		
		return this;
	}
	
	public Matrix4 mult(float[] m) {
		float[] newm = new float[16];
		for(int a = 0; a < 16; a += 4) {
			newm[a+0] = get(0)*m[a] + get(4)*m[a+1] + get(8)*m[a+2] + get(12)*m[a+3];
			newm[a+1] = get(1)*m[a] + get(5)*m[a+1] + get(9)*m[a+2] + get(13)*m[a+3];
			newm[a+2] = get(2)*m[a] + get(6)*m[a+1] + get(10)*m[a+2] + get(14)*m[a+3];
			newm[a+3] = get(3)*m[a] + get(7)*m[a+1] + get(11)*m[a+2] + get(15)*m[a+3];
		}
		
		put(newm);
		
		return this;
	}
	
	public Matrix4 mult(Matrix4 m) {
		float[] newm = new float[16];
		
		for(int a = 0; a < 16; a += 4) {
			newm[a+0] = get(0)*m.get(a) + get(4)*m.get(a+1) + get(8)*m.get(a+2) + get(12)*m.get(a+3);
			newm[a+1] = get(1)*m.get(a) + get(5)*m.get(a+1) + get(9)*m.get(a+2) + get(13)*m.get(a+3);
			newm[a+2] = get(2)*m.get(a) + get(6)*m.get(a+1) + get(10)*m.get(a+2) + get(14)*m.get(a+3);
			newm[a+3] = get(3)*m.get(a) + get(7)*m.get(a+1) + get(11)*m.get(a+2) + get(15)*m.get(a+3);
		}
		
		put(newm);
		
		return this;
	}
	
	public Matrix4 translate(float x, float y, float z) {
		float[] m = new float[16];
		
		m[0] = 1;
		m[5] = 1;
		m[10] = 1;
		m[15] = 1;
		
		m[12] = x;
		m[13] = y;
		m[14] = z;
		
		return mult(m);
	}
	
	public Matrix4 translate(Vector3 vec) {
		return translate(vec.x(), vec.y(), vec.z());
	}
	
	public Matrix4 scale(float x, float y, float z) {
		float[] m = new float[16];
		
		m[0] = x;
		m[5] = y;
		m[10] = z;
		m[15] = 1;
		
		return mult(m);
	}
	
	public Matrix4 scale(Vector3 vec) {
		return scale(vec.x(), vec.y(), vec.z());
	}
	
	public Matrix4 rotate(float angle, float x, float y, float z) {
		float cos = (float)Math.cos(angle);
		float sin = (float)Math.sin(angle);
		float invCos = 1-cos;
		
		Vector3 v = new Vector3(x,y,z).normalize();
		
		float[] m = new float[16];
		m[0] = v.x()*v.x() + (1 - v.x()*v.x())*cos;
		m[4] = v.x()*v.y()*invCos - v.z()*sin;
		m[8] = v.x()*v.z()*invCos + v.y()*sin;
		
		m[1] = v.y()*v.x()*invCos + v.z()*sin;
		m[5] = v.y()*v.y() + (1-v.y()*v.y())*cos;
		m[9] = v.y()*v.z()*invCos - v.x()*sin;
		
		m[2] = v.z()*v.x()*invCos - v.y()*sin;
		m[6] = v.z()*v.y()*invCos + v.x()*sin;
		m[10] = v.z()*v.z() + (1-v.z()*v.z())*cos;
		
		m[15] = 1;
		
		for(int a = 0; a < 4; a++)
			System.out.println(m[a] + " " + m[a+4] + " " + m[a+8] + " " + m[a+12]);
		System.out.println();
		
		return mult(m);
	}
	
	public Matrix4 rotate(float angle, Vector3 vec) {
		return rotate(angle, vec.x(), vec.y(), vec.z());
	}
	
	public FloatBuffer getBuffer() {
		return (FloatBuffer)matrix.limit(16).position(0);
	}
}
