package com.cwn.wizbank.test;

import org.junit.Test;

public class PropTest {

	
	@Test
	public void test01(){
		System.out.println(System.getProperty("user.dir"));
		System.out.println(this.getClass().getResource("").getPath());
		System.out.println(this.getClass().getResource("/").getPath());
		
		String path = this.getClass().getResource("").getPath();
		path = path.substring(0, path.lastIndexOf("/"));
		path = path.substring(0, path.lastIndexOf("/") + 1);
		System.out.println(path);
	}
}
