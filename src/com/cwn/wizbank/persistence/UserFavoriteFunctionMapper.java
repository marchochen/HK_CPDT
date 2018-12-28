package com.cwn.wizbank.persistence;

import java.util.List;
import java.util.Map;

import com.cwn.wizbank.entity.UserFavoriteFunction;


public interface UserFavoriteFunctionMapper extends BaseMapper<UserFavoriteFunction>{

	List<UserFavoriteFunction> selectList(Map<String, Object> map);

	void deleteFavorite(Map<String, Object> map);


}