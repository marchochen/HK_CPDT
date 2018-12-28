/**
 * 
 */
package com.cwn.wizbank.entity.vo;

/**
 * @author leon.li
 *
 */
public class LearningMapVo {
	
	public final static float unitYpx = 24.5f;		//年的单元格宽度
	public final static float unitMpx = 15;		//月的单元格宽度
	
	
	private float left;
	private float width;
	
	
	public float getLeft() {
		return left;
	}
	public void setLeft(float left) {
		this.left = left;
	}
	public float getWidth() {
		return width;
	}
	public void setWidth(float width) {
		this.width = width;
	}

}
