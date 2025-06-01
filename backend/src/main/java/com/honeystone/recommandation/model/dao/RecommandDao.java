package com.honeystone.recommandation.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.honeystone.board.model.type.Level;
import com.honeystone.common.dto.board.GetBoard;

@Mapper
public interface RecommandDao {

	public List<GetBoard> getList(Level level);

}
