package com.pan;

import java.io.Serializable;

public class TestObj implements Serializable{
	private static final long serialVersionUID = 1L;
	
	
	private boolean good; // 援助角色 是否做过修改  如果没做过修改，则同步原角色最新数据   默认是false   修改过则不需要同步
	private boolean isChange; // 援助角色 是否做过修改  如果没做过修改，则同步原角色最新数据   默认是false   修改过则不需要同步
	private int heroDefienId; // 援助角色 定义表id
	
	public int getHeroDefienId() {
		return heroDefienId;
	}

	public void setHeroDefienId(int heroDefienId) {
		this.heroDefienId = heroDefienId;
	}

	public boolean isChange() {
		return isChange;
	}

	public void setChange(boolean isChange) {
		this.isChange = isChange;
	}

	public boolean isGood() {
		return good;
	}

	public void setGood(boolean good) {
		this.good = good;
	}
	
	
}
