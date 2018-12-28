package com.cwn.wizbank.entity;

/**
 * 封装了投票结果信息
 * 
 * @author Andrew 2015-6-11 下午1:00
 */
public class VotingResponseResult implements java.io.Serializable {
	
	private static final long serialVersionUID = -423941114839296519L;

	/**
	 * 投票回答的总人数
	 * 下面的count/total 即可得改选项的百分比数
	 */
	private Long total;
	
	/**
	 *选项被选中的人数
	 */
	private Long count;
	
	/**
	 * 选项展示的标题
	 */
	private String label;

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}
}
