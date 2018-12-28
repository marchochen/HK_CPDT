package com.cwn.wizbank.persistence;

import com.cwn.wizbank.entity.UserGrade;
import org.apache.ibatis.annotations.Param;

public interface UserGradeMapper extends BaseMapper<UserGrade>{
	
	UserGrade getByTitle(@Param("title") String title);
}
