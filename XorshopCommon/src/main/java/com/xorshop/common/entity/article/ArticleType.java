package com.xorshop.common.entity.article;

public enum ArticleType {
	MENU_BOUND {
		public String toString() { return "Menu-Bound"; }
	},

	FREE {
		public String toString() { return "Free"; }
	}
}
