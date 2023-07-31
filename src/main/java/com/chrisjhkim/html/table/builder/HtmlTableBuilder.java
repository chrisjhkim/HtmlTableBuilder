package com.chrisjhkim.html.table.builder;

import com.chrisjhkim.html.table.builder.style.Style;
import com.chrisjhkim.html.table.builder.style.StyleList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Builder for table in html
 * <p>
 * generates {@code <html>} {@code <table>}
 */
public class HtmlTableBuilder {
	private static String INDENT = "    ";
	private static String LINE_BREAK = "\r\n";

	// style
	private final StyleList tableStyles = new StyleList();
	private final StyleList allColumnStyles = new StyleList();
	// lines
	private final List<TableLine> tableLines = new ArrayList<>();

	// Builder Options
	private boolean includeLineBreak = false;
	// Building functional params
	private final StringBuilder sb = new StringBuilder();

	/**
	 * adda single line (eg. {@code <th>} or {@code <td>} ) to the table
	 */
	public void addLine(TableLine line){
		tableLines.add(line);

	}

	/**
	 * build {@code <html>} {@code <table>} to String from values.
	 * <p>
	 * the result starts from {@code <table>} and ends with {@code </table>}
	 *
	 * @return {@code <table>} /* table headers and body  * /  {@code </table>}
	 */
	public String build() {
		if ( !includeLineBreak ){
			LINE_BREAK = "";
			INDENT = "";
		}


		int currentDepth = 1;
		sb.append(INDENT.repeat(currentDepth++));
		sb.append("<table");

		if ( !this.tableStyles.isEmpty() ){
			sb.append(" style=").append(this.tableStyles.toStyleString());
		}

		sb.append(">");
		appendLineBreak();


		for (TableLine tableLine : tableLines) {
			appendTableLine(tableLine, currentDepth);
		}

		appendLine(INDENT.repeat(--currentDepth),"</table>");

		return sb.toString();
	}

	private void appendLineBreak() {
		if ( includeLineBreak) {
			sb.append(LINE_BREAK);
		}
	}

	private void appendTableLine(TableLine tableLine, int currentDepth) {

		appendLine(INDENT.repeat(currentDepth++), "<tr>");

		for (TableColumn tableColumn : tableLine.getTableColumns()) {
			String tagName = tableLine.lineType.getRowTagName();

			StyleList styles = tableColumn.styleList;
			if ( !this.allColumnStyles.isEmpty()){
				// 테이블에서 설정한 global column style 있으면 global table style + allColumn Style
				styles = this.allColumnStyles.overwriteWith(styles);
			}
			sb.append(INDENT.repeat(currentDepth)).append("<").append(tagName);
			if ( !styles.isEmpty()){
				sb.append(" style=").append(styles.toStyleString());
			}
			sb.append(" >");

			appendLine(tableColumn.getValue(),"</",tagName,">");



		}
		appendLine(INDENT.repeat(--currentDepth), "</tr>");
	}

	@SuppressWarnings("UnusedReturnValue")
	private StringBuilder appendLine(String ... str) {
		for (String s : str) {
			if ( s != null ) {
				sb.append(s);
			}
		}

		appendLineBreak();

		return sb;
	}

	/**
	 * Start building {@code <tr>} by using returned {@link HtmlTableLineBuilder}.
	 * <p></p>
	 * Automatically sets line's type as  header ( {@code <th>} )
	 * and returns {@link HtmlTableHeaderBuilder} which extends
	 * {@link HtmlTableLineBuilder}.
	 * <p></p>
	 * Use {@link HtmlTableLineBuilder#endLine() } to finish the line.
	 * <p></p>
	 * Adding multiple header lines is possible.
	 * <p></p>
	 *
	 * @return HtmlTableHeaderBuilder
	 * @see HtmlTableLineBuilder
	 * @see HtmlTableLineBuilder#endLine()
	 */
	public HtmlTableHeaderBuilder startHeaderTr() {
		return new HtmlTableHeaderBuilder(this);
	}
	/**
	 * Start building {@code <tr>} by using returned {@link HtmlTableBodyBuilder}.
	 * <p></p>
	 * Automatically sets line's type as  header ( {@code <td>} )
	 * and returns {@link HtmlTableBodyBuilder} which extends
	 * {@link HtmlTableLineBuilder}.
	 * <p></p>
	 * Use {@link HtmlTableLineBuilder#endLine() } to finish the line.
	 * <p></p>
	 * Adding multiple header lines is possible.
	 * <p></p>
	 *
	 * @return {@link HtmlTableBodyBuilder}
	 * @see HtmlTableLineBuilder
	 * @see HtmlTableLineBuilder#endLine()
	 */
	public HtmlTableBodyBuilder startBodyTr() {
		return new HtmlTableBodyBuilder(this);
	}

	public HtmlTableBuilder tableStyle(Style... styles) {
		this.tableStyles.addAll(Arrays.asList(styles));
		return this;
	}

	public HtmlTableBuilder allColumnStyle(Style... styles) {
		this.allColumnStyles.addAll(Arrays.asList(styles));
		return this;
	}

	/**
	 * Includes indents and line breaks when building html table.
	 *
	 */
	public HtmlTableBuilder beautify() {
		this.includeLineBreak = true;
		return this;
	}

	/**
	 * Class for building header of the table.
	 * <p>
	 * Provides methods for setting value and options for {@code <th>} tag
	 */
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
		private final HtmlTableBuilder parent;
		private final TableLine line;
		public HtmlTableLineBuilder(HtmlTableBuilder htmlTableBuilder, LineType lineType) {
			this.parent = htmlTableBuilder;

			this.line = new TableLine(lineType);
			this.parent.addLine(this.line);
		}

		@SuppressWarnings("UnusedReturnValue")
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
		private final StyleList styleList = new StyleList();

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
