package com.chrisjhkim.html.table.builder;

import com.chrisjhkim.html.table.builder.style.Style;
import org.junit.jupiter.api.Test;

class HtmlTableBuilderTest {


	@Test
	void test(){
		// Test # Given
		HtmlTableBuilder builder = new HtmlTableBuilder()
				.beautify()
				;

		builder.tableStyle( Style.BorderCollapse.COLLAPSE)
				.allColumnStyle(Style.Padding.px(8), Style.Border.px(1), Style.Border.SOLID)
				.startHeaderTr()
					.th("Name", Style.of("background-color", "grey"), Style.of("color","white"))
					.th("Age")
					.th("Occupation")
				.finishHeaderTr()
				.startBodyTr()
					.td("John")
					.td("30")
					.td("Engineer")
				.finishBodyTr()
				.startBodyTr()
					.td("Mary")
					.td("25")
					.td("Teacher")
				.finishBodyTr()
				.startBodyTr()
					.td("David")
					.td("35")
					.td("Doctor")
				.finishBodyTr();

		// Test # When
		String table = builder.build();

		// Test # Then



		HtmlBuilderTester tester = new HtmlBuilderTester();
		tester.createHtml(table);
	}
}