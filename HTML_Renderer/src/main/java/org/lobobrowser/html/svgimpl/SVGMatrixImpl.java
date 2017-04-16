package org.lobobrowser.html.svgimpl;

import java.awt.geom.AffineTransform;

import org.lobobrowser.w3c.svg.SVGException;
import org.lobobrowser.w3c.svg.SVGMatrix;
import org.w3c.dom.DOMException;

public class SVGMatrixImpl implements SVGMatrix {

	private AffineTransform transform;
	
	private float a;
	private float b;
	private float c;
	private float d;
	private float e;
	private float f;

	public SVGMatrixImpl() {}

	public SVGMatrixImpl(float a, float b, float c, float d, float e, float f) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		this.e = e;
		this.f = f;
	}

	@Override
	public float getA() {
		return a;
	}

	@Override
	public void setA(float a) throws DOMException {
		this.a = a;

	}

	@Override
	public float getB() {
		return b;
	}

	@Override
	public void setB(float b) throws DOMException {
		this.b = b;
	}

	@Override
	public float getC() {
		return c;
	}

	@Override
	public void setC(float c) throws DOMException {
		this.c = c;
	}

	@Override
	public float getD() {
		return d;
	}

	@Override
	public void setD(float d) throws DOMException {
		this.d = d;
	}

	@Override
	public float getE() {
		return e;
	}

	@Override
	public void setE(float e) throws DOMException {
		this.e = e;
	}

	@Override
	public float getF() {
		return f;
	}

	@Override
	public void setF(float f) throws DOMException {
		this.f = f;

	}

	@Override
	public SVGMatrix multiply(SVGMatrix secondMatrix) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SVGMatrix inverse() throws SVGException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SVGMatrix translate(float x, float y) {
		SVGMatrixImpl mtrx = new SVGMatrixImpl();
		mtrx.setTransform(new AffineTransform(a, b, c, d, e, f));
		return mtrx;

	}

	@Override
	public SVGMatrix scale(float scaleFactor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SVGMatrix scaleNonUniform(float scaleFactorX, float scaleFactorY) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SVGMatrix rotate(float angle) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SVGMatrix rotateFromVector(float x, float y) throws SVGException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SVGMatrix flipX() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SVGMatrix flipY() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SVGMatrix skewX(float angle) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SVGMatrix skewY(float angle) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * @return the transform
	 */
	public AffineTransform getTransform() {
		return transform;
	}
	
	/**
	 * @param transform the transform to set
	 */
	public void setTransform(AffineTransform transform) {
		this.transform = transform;
	}


}
