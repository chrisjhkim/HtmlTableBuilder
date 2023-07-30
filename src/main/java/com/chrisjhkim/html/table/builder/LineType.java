package com.chrisjhkim.html.table.builder;

public enum LineType {
	HEADER, BODY;
	public String getRowTagName(){
		switch (this){
			case HEADER:
				return "th";
			case BODY:
				return "td";
		}
		throw new IllegalStateException("unexpected LineType");
	}

}
