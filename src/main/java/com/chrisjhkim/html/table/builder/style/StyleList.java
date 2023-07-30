package com.chrisjhkim.html.table.builder.style;

import java.util.*;

public class StyleList {
	private List<Style> styles = new ArrayList<>();

	public void add(Style style) {
		this.styles.add(style);
	}
	public void addAll(Collection<Style> styles) {
		this.styles.addAll(styles);
	}


	public boolean isEmpty() {
		return this.styles.isEmpty();
	}

	public String toStyleString() {
		Map<String , String> map = new HashMap<>();
		this.styles.forEach(style -> {
			String valueOrDefault = map.getOrDefault(style.getKey(), "");
			map.put(style.getKey(), valueOrDefault+" "+style.getValue());
		});
		StringBuilder sb = new StringBuilder();
		sb.append("'");
		map.forEach((key, values) -> {
			sb.append(key).append(":").append(values).append(";");
		});
		sb.append("'");
		return sb.toString();
	}
}
