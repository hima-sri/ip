package com.thbs.questionMS.service;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.thbs.questionMS.exception.ExcelProcessingException;
import com.thbs.questionMS.exception.InvalidFileFormatException;
import com.thbs.questionMS.model.Question;
import com.thbs.questionMS.repository.QuestionRepository;


@Service
public class QuestionsService {

	@Autowired
	private QuestionRepository questionRepository;
	@Autowired
	private SequenceGeneratorService sequenceGeneratorService;


	public void save(MultipartFile file) throws ExcelProcessingException {
		try {
			List<Question> questions = convertExcelToListOfQuestion(file.getInputStream());
			assignCustomIds(questions);
			questionRepository.saveAll(questions);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void assignCustomIds(List<Question> questions) {
		for (Question question : questions) {
			question.setQuestionId(sequenceGeneratorService.generateSequence(Question.SEQUENCE_NAME));
		}
	}

	public List<Question> getAllQuestions1() {
		return questionRepository.findAll();
	}

	public Question saveQuestion(Question question) {
		return questionRepository.save(question);
	}

	public static void checkExcelFormat(MultipartFile file) throws InvalidFileFormatException {
		String contentType = file.getContentType();

		if (!contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
			throw new InvalidFileFormatException("Invalid file format. Please upload an Excel file.");
		}
	}


	Map<String, String> optionsMap = new LinkedHashMap<>();
	private void processCellData(Cell cell, int cellId, Question question) {

		switch (cellId) {
			case 0:
		    	String subject = cell.getStringCellValue().trim();
		    	question.setSubject(capitalize(subject));
		    break;
			case 1:
				String subTopic = cell.getStringCellValue().trim();
				question.setSubTopic(capitalize(subTopic));
		    break;
			case 2:
			    String level = cell.getStringCellValue().trim();
			    question.setLevel(capitalize(level));
			    break;
			case 3:
			    String title = cell.getStringCellValue().trim();
			    question.setTitle(capitalize(title));
			    break;
			case 4:
			    String questionType = cell.getStringCellValue().trim();
			    question.setQuestionType(capitalize(questionType));
			    break;
			case 5:
			    String answerType = cell.getStringCellValue().trim();
			    question.setAnswerType(capitalize(answerType));
			    break;
			case 6:
			    String answerString = cell.getStringCellValue();
			    List<String> answers = Arrays.asList(answerString.split("\\s*,\\s*"));
			    List<String> capitalizedAnswers = new ArrayList<>();
			    for (String answer : answers) {
			        capitalizedAnswers.add(capitalize(answer));
			    }
			    question.setAnswers(capitalizedAnswers);
			    break;
			case 7:
				String optionA = cell.getStringCellValue().trim();
				if (!optionA.isEmpty()) {
					question.getOptions().put("A", optionA);
				}
				break;
			case 8:
				String optionB = cell.getStringCellValue().trim();
				if (!optionB.isEmpty()) {
					question.getOptions().put("B", optionB);
				}
				break;
			case 9:
				String optionC = cell.getStringCellValue().trim();
				if (!optionC.isEmpty()) {
					question.getOptions().put("C", optionC);
				}
				break;
			case 10:
				String optionD = cell.getStringCellValue().trim();
				if (!optionD.isEmpty()) {
					question.getOptions().put("D", optionD);
				}
				break;
			case 11:
				String optionE = cell.getStringCellValue().trim();
				if (!optionE.isEmpty()) {
					question.getOptions().put("E", optionE);
				}
				break;
			case 12:
				String optionF = cell.getStringCellValue().trim();
				if (!optionF.isEmpty()) {
					question.getOptions().put("F", optionF);
				}
				break;
			case 13:
				String optionG = cell.getStringCellValue().trim();
				if (!optionG.isEmpty()) {
					question.getOptions().put("G", optionG);
				}
				break;
			case 14:
				String optionH = cell.getStringCellValue().trim();
				if (!optionH.isEmpty()) {
					question.getOptions().put("H", optionH);
				}
				break;
			default:
				break;
		}
	}
	
	private String capitalize(String str) {
	    if (str == null || str.isEmpty()) {
	        return str;
	    }
	    // Split the string into words
	    String[] words = str.split("\\s+");
	    StringBuilder result = new StringBuilder();
	    for (String word : words) {
	        // Capitalize the first letter of each word
	        result.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");
	    }
	    // Trim trailing space and return the capitalized string
	    return result.toString().trim();
	}


	public List<Question> convertExcelToListOfQuestion(InputStream inputStream) throws ExcelProcessingException {
		List<Question> list = new ArrayList<>();

		try {
			XSSFWorkbook workBook = new XSSFWorkbook(inputStream);
			XSSFSheet sheet = workBook.getSheet("data");

			Iterator<Row> iterator = sheet.iterator();
			int rowNumber = 0;

			while (iterator.hasNext()) {
				Row row = iterator.next();
				if (rowNumber == 0) {
					rowNumber++;
					continue; // Skip header row
				}

				Iterator<Cell> cells = row.iterator();
				int cellId = 0;
				optionsMap.clear(); // Clear options map for each new row

				Question question = new Question();

				while (cells.hasNext()) {
					Cell cell = cells.next();
					processCellData(cell, cellId, question);
					cellId++;
				}

				// Add question only if subject inputStream not empty (to avoid extra entries)
				if (question.getSubject() != null && !question.getSubject().isEmpty()) {
					list.add(question);
				}
			}

			workBook.close();
		} catch (IOException e) {
			throw new ExcelProcessingException("Error processing Excel file.", e);
		}

		for (Question question : list) {
			question.setQuestionId(sequenceGeneratorService.generateSequence(Question.SEQUENCE_NAME));
			questionRepository.save(question);
		}

		return list;

	}

//	public List<Question> getQuestionsBySubject(String subject) {
//		// Implement the logic to retrieve questions by subject from your repository
//		return questionRepository.findBySubject(subject);
//	}
}

