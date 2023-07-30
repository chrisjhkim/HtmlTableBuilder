package com.chrisjhkim.html.table.builder;

import com.chrisjhkim.html.table.builder.style.Style;
import com.chrisjhkim.html.table.builder.style.StyleList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HtmlTableBuilder {

	// style
	private StyleList tableStyles = new StyleList();
	// lines
	private List<TableLine> tableLines = new ArrayList<>();

	// Builder Options
	private boolean includeLineBreak = true;
	// Building functional params
	private StringBuilder sb = new StringBuilder();

	public void addLine(TableLine line){
		tableLines.add(line);

	}
	public String build() {
		if ( this.tableStyles.isEmpty() ){
			appendLine("<table>");
		}else{
//			StringBuilder sb = new StringBuilder();
			sb.append("<table style=");
			sb.append(this.tableStyles.toStyleString());
			sb.append(">");

			appendLineBreak();

		}


		for (TableLine tableLine : tableLines) {
			appendTableLine(tableLine);
		}

		appendLine("</table>");

		return sb.toString();
	}

	private void appendLineBreak() {
		if ( includeLineBreak) {
			sb.append("\r\n");
		}
	}

	private void appendTableLine(TableLine tableLine) {
		appendLine("<tr>");

		for (TableColumn tableColumn : tableLine.getTableColumns()) {
			String tagName = tableLine.lineType.getRowTagName();

			if ( tableColumn.styleList.isEmpty() ){
				appendLine("<", tagName, ">",tableColumn.getValue(),"</",tagName,">");
			}else{
				sb.append("<")
						.append(tagName)
						.append(" style=").append(tableColumn.styleList.toStyleString());
				sb.append(">");

				appendLine(tableColumn.getValue(),"</",tagName,">");

			}


		}
		appendLine("</tr>");
	}

	private StringBuilder appendLine(String ... str) {
		for (String s : str) {
			if ( s != null ) {
				sb.append(s);
			}
		}

		appendLineBreak();

		return sb;
	}

	public HtmlTableHeaderBuilder startHeaderTr() {
		return new HtmlTableHeaderBuilder(this);
	}
	public HtmlTableBodyBuilder startBodyTr() {
		return new HtmlTableBodyBuilder(this);
	}

	public HtmlTableBuilder tableStyle(Style... styles) {
		this.tableStyles.addAll(Arrays.asList(styles));
		return this;
	}


	public static class HtmlTableHeaderBuilder extends HtmlTableLineBuilder{
		public HtmlTableHeaderBuilder(HtmlTableBuilder htmlTableBuilder) {
			super(htmlTableBuilder, LineType.HEADER);
		}

		public HtmlTableHeaderBuilder th(String columnValue, Style ... styles){
			super.addColumn(columnValue, styles);
			return this;
		}

		public HtmlTableBuilder finishHeaderTr(){
			return super.endLine();
		}
	}
	public static class HtmlTableBodyBuilder extends HtmlTableLineBuilder{
		public HtmlTableBodyBuilder(HtmlTableBuilder htmlTableBuilder) {
			super(htmlTableBuilder, LineType.BODY);
		}
		public HtmlTableBodyBuilder td(String columnValue){
			super.addColumn(columnValue);
			return this;
		}
		public HtmlTableBuilder finishBodyTr(){
			return super.endLine();
		}
	}

	public static class HtmlTableLineBuilder{
		private HtmlTableBuilder parent;
		private TableLine line;
		public HtmlTableLineBuilder(HtmlTableBuilder htmlTableBuilder, LineType lineType) {
			this.parent = htmlTableBuilder;

			this.line = new TableLine(lineType);
			this.parent.addLine(this.line);
		}

		public HtmlTableLineBuilder addColumn(String columnValue, Style... styles) {
			this.line.addColumn(columnValue, styles);
			return this;
		}

		public HtmlTableBuilder endLine() {
			return this.parent;
		}
	}

	public static class TableLine {
		private final LineType lineType;
		List<TableColumn> tableColumns = new ArrayList<>();

		public TableLine(LineType lineType) {
			this.lineType = lineType;
		}

		public List<TableColumn> getTableColumns() {
			return tableColumns;
		}

		public void addColumn(String columnValue, Style ... styles) {
			TableColumn tableColumn = new TableColumn(columnValue, styles);
			this.tableColumns.add(tableColumn);
		}
	}
	private static class TableColumn{
		private final String value;
		private StyleList styleList = new StyleList();

		public TableColumn(String value, Style ... styles) {
			this.value = value;
			for (Style style : styles) {
				this.styleList.add(style);
			}
		}

		public String getValue() {
			return value;
		}
	}
}
