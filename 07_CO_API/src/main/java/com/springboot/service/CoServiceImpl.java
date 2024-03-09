package com.springboot.service;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.springboot.binding.CoResponse;
import com.springboot.entity.CitizenAppEntity;
import com.springboot.entity.CoTriggerEntity;
import com.springboot.entity.DcCaseEntity;
import com.springboot.entity.EligDtlsEntity;
import com.springboot.repo.CitizenAppRepository;
import com.springboot.repo.CoTriggerRepo;
import com.springboot.repo.DcCaseRepo;
import com.springboot.repo.EligDtlsRepo;
import com.springboot.utils.EmailUtils;

public class CoServiceImpl implements CoService {

	@Autowired
	private CoTriggerRepo coTrgRepo;

	@Autowired
	public EligDtlsRepo eligRepo;

	@Autowired
	private DcCaseRepo dcCaseRepo;

	@Autowired
	private CitizenAppRepository appRepo;

	@Autowired
	private EmailUtils emailUtils;

	@Override
	public CoResponse processPendingTriggers() {
		CoResponse response = new CoResponse();
		CitizenAppEntity appEntity = null;

		// fetch all pending triggers
		List<CoTriggerEntity> pendingTrgs = coTrgRepo.findByTrgStatus("pending");

		response.setTotalTrigger(Long.valueOf(pendingTrgs.size()));

		// Process each pending triggers
		for (CoTriggerEntity entity : pendingTrgs) {
			// Get eligibility data based on casenum
			EligDtlsEntity elig = eligRepo.findByCaseNum(entity.getCaseNum());

			// Get citizen data based on casenum
			Optional<DcCaseEntity> findById = dcCaseRepo.findById(entity.getCaseNum());
			if (findById.isPresent()) {
				DcCaseEntity dcCaseEntity = findById.get();
				Integer appId = dcCaseEntity.getAppId();
				Optional<CitizenAppEntity> appEntityOptional = appRepo.findById(appId);
				if (appEntityOptional.isPresent()) {
					appEntity = appEntityOptional.get();
				}
			}

			Long failed = 0l;
			Long success = 0l;

			try {
				generateAndSendPdf(elig, appEntity);
				success++;
			} catch (Exception e) {
				e.printStackTrace();
				failed++;
			}

			response.setSuccTrigger(success);
			response.setFailedTrigger(failed);

		}

		return response;
	}

	public void generateAndSendPdf(EligDtlsEntity eligData, CitizenAppEntity appEntity) throws Exception {
		Document document = new Document(PageSize.A4);

		File file = new File(eligData.getCaseNum() + ".pdf");
		FileOutputStream fos = null;

		try {
			fos = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		PdfWriter.getInstance(document, fos);

		document.open();
		Font fontHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		fontHeader.setSize(18);
		fontHeader.setColor(Color.BLUE);

		Paragraph p = new Paragraph("Eligibility Report", fontHeader);
		p.setAlignment(Paragraph.ALIGN_CENTER);

		document.add(p);

		PdfPTable table = new PdfPTable(7);
		table.setWidthPercentage(100f);
		table.setWidths(new float[] { 1.5f, 3.5f, 3.0f, 1.5f, 3.0f, 1.5f, 3.0f });
		table.setSpacingBefore(10);

		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(Color.BLUE);
		cell.setPadding(5);

		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(Color.WHITE);

		cell.setPhrase(new Phrase("Citizen Name", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Plan Name", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Plan Status", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Plan Start Date", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Plan End Date", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Benefit Amount", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Denial Reason", font));
		table.addCell(cell);

		table.addCell(appEntity.getFullName());
		table.addCell(eligData.getPlanName());
		table.addCell(eligData.getPlanStatus());
		table.addCell(eligData.getPlanStartDate() + "");
		table.addCell(eligData.getPlanEnDate() + "");
		table.addCell(eligData.getBenefitAmt() + "");
		table.addCell(eligData.getDenialReason() + "");

		document.add(table);

		document.close();

		String subject = "HIS Eligibility Info";
		String body = "HIS Eligibility Info";

		emailUtils.sendEmail(appEntity.getEmail(), subject, body, file);
		updateTrigger(eligData.getCaseNum(), file);

		file.delete();

	}

	private void updateTrigger(Long caseNum, File file) throws IOException {
		CoTriggerEntity coEntity = coTrgRepo.findByCaseNum(caseNum);

		byte[] arr = new byte[(byte) file.length()];

		FileInputStream fis = new FileInputStream(file);
		fis.read(arr);

		coEntity.setCoPdf(arr);
		coEntity.setTrgStatus("Completed");
		coTrgRepo.save(coEntity);
		fis.close();
	}

}
