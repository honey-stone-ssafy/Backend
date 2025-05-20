package com.honeystone.recommandation.model.service;

import java.util.List;

import com.honeystone.board.model.type.Level;
import com.honeystone.recommandation.model.type.LocClass;

public interface RecommandService {

	public List<LocClass> getRecommandationList(Level level);

}
