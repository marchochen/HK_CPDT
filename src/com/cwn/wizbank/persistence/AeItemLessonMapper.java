package com.cwn.wizbank.persistence;

import java.util.List;

import com.cwn.wizbank.entity.AeItemLesson;


public interface AeItemLessonMapper extends BaseMapper<AeItemLesson>{

	List<AeItemLesson> getList(long itmId);

}