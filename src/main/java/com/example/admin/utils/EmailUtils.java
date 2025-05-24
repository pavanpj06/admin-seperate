package com.example.admin.utils;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.example.admin.bindings.UserAccountForm;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Component
public class EmailUtils {

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private ResourceLoader resourceLoader;

	public boolean emailSending(String email, String tempPassword) throws MessagingException, IOException {
		boolean flag = false;

		try {
			Resource resource = resourceLoader.getResource("classpath:templates/password-reset.html");
			String emailContent = new String(Files.readAllBytes(Paths.get(resource.getURI())));

			// Replace placeholder with the actual temporary password
			emailContent = emailContent.replace("{{tempPassword}}", tempPassword);

			// Create email message
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.setTo(email);
			helper.setSubject("IES - Password Reset");
			helper.setText(emailContent, true); // true -> enables HTML format

			// Send the email
			javaMailSender.send(message);
			flag = true;
		} catch (IOException e) {
			throw new RuntimeException("Failed to load email template", e);
		}
		return flag;
	}
	
	
	
	
	
	


	    public byte[] generateAccountsExcel(List<UserAccountForm> accounts) throws IOException {
	        try (Workbook workbook = new XSSFWorkbook();
	        		ByteArrayOutputStream out = new ByteArrayOutputStream()) {
	            Sheet sheet = workbook.createSheet("Accounts");

	            String[] columns = {"S.NO", "Name", "Email", "Mobile", "Gender", "SSN"};
	            Row headerRow = sheet.createRow(0);
	            for (int i = 0; i < columns.length; i++) {
	                headerRow.createCell(i).setCellValue(columns[i]);
	            }

	            int rowIndex = 1;
	            for (UserAccountForm account : accounts) {
	                Row row = sheet.createRow(rowIndex++);
	                row.createCell(0).setCellValue(account.getId());
	                row.createCell(1).setCellValue(account.getFullName());
	                row.createCell(2).setCellValue(account.getEmail());
	                row.createCell(3).setCellValue(account.getMobileNumber());
	                row.createCell(4).setCellValue(account.getGender().name());
	                row.createCell(5).setCellValue(account.getSsNumber());
	            }

	            for (int i = 0; i < columns.length; i++) {
	                sheet.autoSizeColumn(i);
	            }

	            workbook.write(out);
	            return out.toByteArray();
	        }
	    }
	    
	    
	    public byte[] generateAccountsTxt(List<UserAccountForm> accounts) throws IOException {
	        StringBuilder sb = new StringBuilder();

	        // Header
	        sb.append("S.NO\tName\tEmail\tMobile\tGender\tSSN\n");

	        // Data rows
	        int index = 1;
	        for (UserAccountForm account : accounts) {
	            sb.append(index++).append("\t")
	              .append(account.getFullName()).append("\t")
	              .append(account.getEmail()).append("\t")
	              .append(account.getMobileNumber()).append("\t")
	              .append(account.getGender().name()).append("\t")
	              .append(account.getSsNumber()).append("\n");
	        }

	        return sb.toString().getBytes(StandardCharsets.UTF_8);
	    }

	

}
