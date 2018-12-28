package com.cwn.wizbank.services;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.cwn.wizbank.base.BaseTest;

public class InstructorCommentServiceTest extends BaseTest {

	@Autowired
	InstructorCommentService instructorCommentService;
	
	@Test
	public void testComment() {
		//instructorCommentService.comment(27, 41, "我是来改评论的", 3d, 3.1d, 3.2d, 3.3d,"");
	}

}
