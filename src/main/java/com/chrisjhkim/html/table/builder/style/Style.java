package com.chrisjhkim.html.table.builder.style;


import java.util.List;
import java.util.stream.Collectors;

public class Style {


	//	private String borderCollapse;
	private final String key;
	private final String value;

	protected Style(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}



	public static class BorderCollapse extends Style{
		private static final String KEY = "border-collapse";

		private BorderCollapse(String value) {
			super(KEY, value);
		}
		/**
		 * border-collapse: collapse;
		 */
		public static BorderCollapse COLLAPSE = new BorderCollapse("collapse");
	}
	public static class Padding extends Style{
		private static final String KEY = "padding";
		private Padding(String value){
			super(KEY, value);
		}

		public static Padding px(int value){
			return new Padding(value+"px");
		}
	}

	public static class Border extends Style {
		private static final String KEY = "border";

		private Border(String value){
			super(KEY,value);
		}

		public static Border px(int value){
			return new Border(value+"px");
		}

		public static Border SOLID = new Border("solid");
	}

}
