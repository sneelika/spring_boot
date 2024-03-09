package com.springboot.service;

import java.awt.Color;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.springboot.entity.EligibilityDetails;
import com.springboot.repo.EligibilityDetailsRepo;
import com.springboot.request.SearchRequest;
import com.springboot.response.SearchResponse;

@Service
public class ReportServiceImpl implements ReportService {

	@Autowired
	private EligibilityDetailsRepo eligRepo;

	@Override
	public List<String> getUniquePlanNames() {
		return eligRepo.findPlanNames();
	}

	@Override
	public List<String> getUniquePlanStatuses() {
		return eligRepo.findPlanStatuses();
	}

	@Override
	public List<SearchResponse> search(SearchRequest request) {
		List<SearchResponse> response = new ArrayList<>();

		EligibilityDetails queryBuilder = new EligibilityDetails();
		String planName = request.getPlanName();

		if (planName != null && !planName.equals("")) {
			queryBuilder.setPlanName(planName);
		}

		String planStatus = request.getPlanStatus();
		if (planStatus != null && !planStatus.equals("")) {
			queryBuilder.setPlanStatus(planStatus);
		}

		LocalDate planStartDate = request.getPlanStartDate();
		if (planStartDate != null) {
			queryBuilder.setPlanStartDate(planStartDate);
		}

		LocalDate planEnDate = request.getPlanEndDate();
		if (planEnDate != null) {
			queryBuilder.setPlanEnDate(planEnDate);
		}

		Example<EligibilityDetails> example = Example.of(queryBuilder);

		List<EligibilityDetails> entities = eligRepo.findAll(example);

		for (EligibilityDetails entity : entities) {
			SearchResponse sr = new SearchResponse();
			BeanUtils.copyProperties(entity, sr);
			response.add(sr);
		}

		return response;
	}

	@Override
	public void generateExcel(HttpServletResponse response) throws IOException {

		List<EligibilityDetails> entities = eligRepo.findAll();

		HSSFWorkbook workBook = new HSSFWorkbook();
		HSSFSheet Sheet = workBook.createSheet();
		HSSFRow headerRow = Sheet.createRow(0);

		headerRow.createCell(0).setCellValue("Name");
		headerRow.createCell(1).setCellValue("Mobile");
		headerRow.createCell(2).setCellValue("Gender");
		headerRow.createCell(3).setCellValue("SSN");

		int i = 1;

		for (EligibilityDetails entity : entities) {

			HSSFRow dataRow = Sheet.createRow(i);
			dataRow.createCell(0).setCellValue(entity.getName());
			dataRow.createCell(1).setCellValue(String.valueOf(entity.getMobile()));
			dataRow.createCell(2).setCellValue(String.valueOf(entity.getGender()));
			dataRow.createCell(3).setCellValue(String.valueOf(entity.getSsn()));
			i++;
		}

		ServletOutputStream outputStream = response.getOutputStream();
		workBook.write(outputStream);
		workBook.close();
		outputStream.close();

	}

	@Override
	public void generatePdf(HttpServletResponse response) throws DocumentException, IOException {
		List<EligibilityDetails> entities = eligRepo.findAll();
		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, response.getOutputStream());

		document.open();
		Font fontHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		fontHeader.setSize(18);
		fontHeader.setColor(Color.BLUE);

		Paragraph p = new Paragraph("Search Report", fontHeader);
		p.setAlignment(Paragraph.ALIGN_CENTER);

		document.add(p);

		PdfPTable table = new PdfPTable(5);
		table.setWidthPercentage(100f);
		table.setWidths(new float[] { 1.5f, 3.5f, 3.0f, 3.0f, 1.5f });
		table.setSpacingBefore(10);

		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(Color.BLUE);
		cell.setPadding(5);

		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(Color.WHITE);

		cell.setPhrase(new Phrase("Name", font));

		table.addCell(cell);

		cell.setPhrase(new Phrase("E-mail", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Phone No.", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Gender", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("SSN", font));
		table.addCell(cell);

		for (EligibilityDetails entity : entities) {
			table.addCell(entity.getName());
			table.addCell(entity.getEmail());
			table.addCell(String.valueOf(entity.getMobile()));
			table.addCell(String.valueOf(entity.getGender()));
			table.addCell(String.valueOf(entity.getSsn()));
		}

		document.add(table);

		document.close();

	}

}
