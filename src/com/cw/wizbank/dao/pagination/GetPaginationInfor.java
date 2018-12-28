/*
 * 该类用于处理分页技术
 */
package com.cw.wizbank.dao.pagination;

import java.util.ArrayList;
import java.util.List;

public class GetPaginationInfor {
	private final static String FIRST = "first";
	private final static String LAST = "last";
	private final static String PRE = "pre";
	private final static String NEXT = "next";
	
	private long allReocordCount;//总记录数量
	private long oneTimeCount;//一页显示的记录数量
	private long displaypageCount;//显示页码导航的数量 从 1开始计数
	
	public GetPaginationInfor(){
		
	}
	
	//默认一次5条记录，页面导航为10个
	public GetPaginationInfor(long allrecordCount,Object oneTimeCountParam, Object displayPageCountParam,
			long defaultOneTimeCount, long defaultOnePageCount){
		this.allReocordCount=allrecordCount;//初始化记录总数量
		
		this.oneTimeCount = getLong(oneTimeCountParam, defaultOneTimeCount);
		if(this.oneTimeCount == -1){
			long theOneTimeCount = Long.valueOf(String.valueOf(oneTimeCountParam)).longValue();
			if(theOneTimeCount < 1){//一页最少显示一条记录
				this.oneTimeCount = 1;
			}else if(theOneTimeCount > allrecordCount){
				this.oneTimeCount = allrecordCount;//一页最多显示记录的总量数
			}else{
				this.oneTimeCount = theOneTimeCount;//一页指定显示多少条记录
			}
		}
		
		//初始化显示的页面数量,并且保证显示的页面在相应的范围内
		this.displaypageCount = getLong(displayPageCountParam, defaultOnePageCount);
		if(this.displaypageCount == -1){
			long theDisPlayPageCount = Long.valueOf(String.valueOf(displayPageCountParam)).longValue();
			if(theDisPlayPageCount < 1){
				this.displaypageCount = 1;//一页最少显示一条页面导航
			}else if(theDisPlayPageCount > allrecordCount){
				this.displaypageCount=allrecordCount;//一页最多显示页面数量为记录的数量
			}else{
				this.displaypageCount = theDisPlayPageCount;
			}
		}
	}

	public long getCountRecord() {
		return allReocordCount;
	}

	public void setCountReocrd(long countRecord) {
		this.allReocordCount = countRecord;
	}

	public long getOneTimeCount() {
		return oneTimeCount;
	}

	public void setOneTimeCount(long oneTimeCount) {
		this.oneTimeCount = oneTimeCount;
	}
	
	public long getDisplaypageCount() {
		return displaypageCount;
	}

	public void setDisplaypageCount(long displaypageCount) {
		this.displaypageCount = displaypageCount;
	}
	
	//算出总共有多少页
	private long getAllPageCount(){
		long pageCount=0;
		if(allReocordCount%oneTimeCount==0){
			pageCount=allReocordCount/oneTimeCount;
		}else{
			pageCount=(allReocordCount/oneTimeCount)+1;
		}
		
		return pageCount;
		
	}
	//得到该页面的页面导航
	private List getDisplayWhichPage(long nowwhichPage){
		long pageCount=this.getAllPageCount();
		long lefeAndRight=this.displaypageCount/2;
		long startPage=0;
		long endPage;
		if(this.displaypageCount%2==0){
			startPage=nowwhichPage-lefeAndRight+1;
			endPage=nowwhichPage+lefeAndRight;
		}else{
			startPage=nowwhichPage-lefeAndRight;
			endPage=nowwhichPage+lefeAndRight;
		}
		//保证显示的页面的数量不会越界
		if(startPage<1){
			startPage=1;
		}
		
		if(endPage>pageCount){
			endPage=pageCount;
		}
		
		if( (endPage-startPage +1 )< this.displaypageCount){
			if(endPage==pageCount){
				while(startPage>1 && (endPage-startPage +1 )< this.displaypageCount){
					--startPage;
				}
			}else if(startPage==1){
				while(endPage<pageCount && (endPage-startPage +1 )< this.displaypageCount){
					endPage++;
				}
			}
		}
		
		List list = new ArrayList();
		for(long n=startPage;n<=endPage;n++){
			list.add(new Long(n));
		}
		return list;
	}
	
	//一页显示哪些记录0 9 / 10 19 
	private long[] getOnePageRecord(long nowWhichPage){
		long pageCount=getAllPageCount();
		
		if(nowWhichPage>pageCount || nowWhichPage<1){
			nowWhichPage=1;//保证显示的页面号码，在相应的范围内，如果超过侧从一开始
		}
		long startRecord=(nowWhichPage-1)*oneTimeCount;
		long endRecord=0;
		
		if(nowWhichPage==pageCount){
			endRecord=allReocordCount-1;//如果为最后一页，最后一条记录为记录的数量
		}else{
			endRecord=nowWhichPage*oneTimeCount-1;
		}
		if(startRecord<0){
			startRecord=0;
			endRecord=oneTimeCount-1;
		}
		return new long[]{startRecord,endRecord};
	}
	
	/*
	 * 该方法通过当前页码及param（first——第一页 ，last——最后一页,next——下一页,pre——上一页 ，
	 * 具体数字——具体页码得到具体分页信息如果没有任何记录侧返回null；
	 *页面导航信息为一个List列表（null ， 参数转换异常）
	 * 5 条记录 ，10 页面导航
	 */
	//当前页面，默认为第一页，以及换页参数，默认为不换页
	public AllPaginationInfors getAllInformation(long nowWhichPageNumber,Object param){
		if(this.allReocordCount<1){
			return null;
		}
		
		long nowWhichPage=1;
		
		if(nowWhichPageNumber > 1){
			nowWhichPage=nowWhichPageNumber;
			//限制当前页在相应的页码范围
			if(nowWhichPage<1){
				nowWhichPage=1;
			}else if(nowWhichPage>this.getAllPageCount()){
				nowWhichPage=this.getAllPageCount();
			}
		}
		
		AllPaginationInfors allInfor=null;//由于保存信息结果
		long processNowWhichPage=1;//跳转到哪一页
		
		if(param==null){
			processNowWhichPage=nowWhichPageNumber;
		}else if (param.equals(FIRST)){
			processNowWhichPage=1;
		}else if(param.equals(LAST)){
			processNowWhichPage=this.getAllPageCount();
		}else if(param.equals(PRE)){
			if(nowWhichPage==1){
				processNowWhichPage=1;
			}else{
				processNowWhichPage=nowWhichPage-1;//如果已经为第一页侧为第一页
			}
		}else if(param.equals(NEXT)){
			if(nowWhichPage==this.getAllPageCount()){
				processNowWhichPage=this.getAllPageCount();//如果已经为最后一页，侧为最后一页
			}else{
				processNowWhichPage=nowWhichPage+1;
			}
		}else{
			try {
				processNowWhichPage=Long.valueOf(String.valueOf(param)).longValue();
			} catch (Exception e) {
				processNowWhichPage = nowWhichPageNumber;
			}
			//限制页面没有越界
			if(processNowWhichPage<1){
				processNowWhichPage=1;
			}else if(processNowWhichPage>this.getAllPageCount()){
				processNowWhichPage=this.getAllPageCount();
			}
		}
		
		long[] theRecords=this.getOnePageRecord(processNowWhichPage);
		List pageList = this.getDisplayWhichPage(processNowWhichPage);
		allInfor=new AllPaginationInfors(theRecords[0],theRecords[1],pageList,processNowWhichPage,getAllPageCount());
		
		return allInfor;
	}
	
	private long getLong(Object longValue, long defaultLongvalue){
		if(longValue == null){
			return defaultLongvalue;
		}
		
		try{
			Long.valueOf(String.valueOf(longValue));
		}catch(Exception e){
			return defaultLongvalue;
		}
		
		return -1;
	}

}
