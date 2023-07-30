package com.chrisjhkim.html.table.builder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class HtmlBuilderTester {

	public void createHtml(String htmlString){
		String table = "<table><tr><th>Name</th><th>Age</th><th>Occupation</th></tr><tr><td>John</td><td>30</td><td>Engineer</td></tr><tr><td>Mary</td><td>25</td><td>Teacher</td></tr><tr><td>David</td><td>35</td><td>Doctor</td></tr></table>";
		createFile(true,"result", htmlString, true);
	}

	private void createFile(boolean preserverLastFile, String fileName, String htmlBodyString, boolean includeHtmlHeadersAndEtc){
		String projectPath = System.getProperty("user.dir"); // 프로젝트 경로
		String directoryPath = projectPath + "/src/test/resources"; // test 폴더 경로

		// 파일명 중복을 처리하기 위한 번호
		int fileNumber = 0;

		// 파일 객체 생성
		File file;
		String fileFullName;
		// 중복된 파일명이 없을 때까지 반복
		do {
			// 파일명 생성 (처음엔 그냥 fileName, 두 번째부터는 fileName + 번호)
			fileFullName = fileNumber == 0
					? fileName + ".html"
					: fileName + fileNumber + ".html";
			file = new File(directoryPath, fileFullName);
			fileNumber++;
		} while (file.exists()); // 동일한 이름의 파일이 존재하는지 확인

		try {
			// 파일을 실제로 생성하고 FileWriter 와 BufferedWriter 를 사용하여 내용을 씁니다.
			boolean created = file.createNewFile();
			if (created) {
				System.out.println("파일이 성공적으로 생성되었습니다. "+ fileFullName);

				// 파일에 내용 쓰기
				FileWriter fileWriter = new FileWriter(file);
				BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

				// 예시로 파일에 기본 내용을 작성해보겠습니다.

				if ( includeHtmlHeadersAndEtc ) {
					bufferedWriter.write("<html>");
					bufferedWriter.newLine();
					bufferedWriter.write("<head><title>" + fileName + "</title></head>");
					bufferedWriter.newLine();
					bufferedWriter.write("<body>");
					bufferedWriter.newLine();
				}

				bufferedWriter.write(htmlBodyString);

				if ( includeHtmlHeadersAndEtc ){
					bufferedWriter.newLine();
					bufferedWriter.write("</body>");
					bufferedWriter.newLine();
					bufferedWriter.write("</html>");
				}

				// 버퍼를 비우고 파일을 닫습니다.
				bufferedWriter.flush();
				bufferedWriter.close();

				System.out.println("파일에 내용이 성공적으로 쓰였습니다.");
			} else {
				System.out.println("파일 생성에 실패하였습니다.");
			}
		} catch (IOException e) {
			System.out.println("파일 생성 및 쓰기 중 오류가 발생하였습니다: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
