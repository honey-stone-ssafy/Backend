package com.honeystone.recommandation.model.type;

import com.honeystone.board.model.type.Location;

public class LocClass {
	public int idx; // 인덱스
	public int cnt; // 카운트
	public Location loc; // 지점
	public String korLoc;

	public LocClass() {
	}

	public LocClass(int idx, int cnt, Location loc, String korLoc) {
		this.idx = idx;
		this.cnt = cnt;
		this.loc = loc;
		this.korLoc =korLoc;
	}

	@Override
	public String toString() {
		return "LocClass{" +
			"idx=" + idx +
			", cnt=" + cnt +
			", loc=" + loc +
			", korLoc='" + korLoc + '\'' +
			'}';
	}
}
