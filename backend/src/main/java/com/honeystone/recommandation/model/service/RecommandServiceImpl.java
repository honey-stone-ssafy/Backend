package com.honeystone.recommandation.model.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.honeystone.board.model.type.Level;
import com.honeystone.board.model.type.Location;
import com.honeystone.common.dto.board.GetBoard;
import com.honeystone.exception.ServerException;
import com.honeystone.recommandation.model.dao.RecommandDao;
import com.honeystone.recommandation.model.type.LocClass;

@Service
public class RecommandServiceImpl implements RecommandService {

	private final RecommandDao recommandDao;
	
	public RecommandServiceImpl(RecommandDao recommandDao) {
		this.recommandDao = recommandDao;
	}
	
	@Override
	public List<LocClass> getRecommandationList(Level level) throws ServerException {
		List<GetBoard> boardList = recommandDao.getList(level);

		LocClass[] rank = new LocClass[14];
		rank[0] = new LocClass(0, 0, Location.HONGDAE, Location.HONGDAE.getKorName());
		rank[1] = new LocClass(1, 0, Location.ILSAN, Location.ILSAN.getKorName());
		rank[2] = new LocClass(2, 0, Location.MAGOK, Location.MAGOK.getKorName());
		rank[3] = new LocClass(3, 0, Location.SEOULDAE, Location.SEOULDAE.getKorName());
		rank[4] = new LocClass(4, 0, Location.YANGJAE, Location.YANGJAE.getKorName());
		rank[5] = new LocClass(5, 0, Location.SINLIM, Location.SINLIM.getKorName());
		rank[6] = new LocClass(6, 0, Location.YEONNAM, Location.YEONNAM.getKorName());
		rank[7] = new LocClass(7, 0, Location.GANGNAM, Location.GANGNAM.getKorName());
		rank[8] = new LocClass(8, 0, Location.SADANG, Location.SADANG.getKorName());
		rank[9] = new LocClass(9, 0, Location.SINSA, Location.SINSA.getKorName());
		rank[10] = new LocClass(10, 0, Location.NONHYEON, Location.NONHYEON.getKorName());
		rank[11] = new LocClass(11, 0, Location.MULLAE, Location.MULLAE.getKorName());
		rank[12] = new LocClass(12, 0, Location.ISU, Location.ISU.getKorName());
		rank[13] = new LocClass(13, 0, Location.SUNGSU, Location.SUNGSU.getKorName());


		for(GetBoard loc : boardList) {
			Location cur = loc.getLocation();
			switch(cur) {
			case HONGDAE :
				rank[0].cnt++;
				break;
			case ILSAN :
				rank[1].cnt++;
				break;
			case MAGOK :
				rank[2].cnt++;
				break;
			case SEOULDAE :
				rank[3].cnt++;
				break;
			case YANGJAE :
				rank[4].cnt++;
				break;
			case SINLIM :
				rank[5].cnt++;
				break;
			case YEONNAM :
				rank[6].cnt++;
				break;
			case GANGNAM :
				rank[7].cnt++;
				break;
			case SADANG :
				rank[8].cnt++;
				break;
			case SINSA :
				rank[9].cnt++;
				break;
			case NONHYEON :
				rank[10].cnt++;
				break;
			case MULLAE :
				rank[11].cnt++;
				break;
			case ISU :
				rank[12].cnt++;
				break;
			case SUNGSU :
				rank[13].cnt++;
				break;
			default:
				break;
			}
		}
		
		Arrays.sort(rank, (a, b) -> b.cnt - a.cnt); // 내림차순으로 순위 정렬
		
		List<LocClass> result = new ArrayList<>();
		
		for(LocClass l : rank) {
			result.add(l);
		}

		//매퍼에서 오늘날짜 기준으로 7일 이전의 값들만 받아오도록 조건 넣기
		return result;
	}

}
