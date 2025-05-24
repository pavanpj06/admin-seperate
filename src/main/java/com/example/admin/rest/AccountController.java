package com.example.admin.rest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.http.MediaType;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.admin.bindings.EditUserAccount;
import com.example.admin.bindings.PlanStatsResponse;
import com.example.admin.bindings.UnLockAccountForm;
import com.example.admin.bindings.UserAccountForm;
import com.example.admin.constants.AppConstants;
import com.example.admin.service.AccountService;
import com.example.admin.utils.EmailUtils;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;



@RestController
//@CrossOrigin(origins = "*")
public class AccountController {

	Map<String, String> messageResponse = new HashMap<String, String>();

	@Autowired
	private AccountService accountService;

	// spring AOP

	
	@Autowired
	private EmailUtils utils;
	private Logger logger = LoggerFactory.getLogger(AccountController.class);

	@PostMapping("/account-creation")
	@CrossOrigin(origins = "http://localhost:3000")
	public ResponseEntity<Map<String, String>> accountCreation(@Valid @RequestBody UserAccountForm acForm)
			throws MessagingException, IOException {
		logger.debug("Account creation process started");
		ResponseEntity<Map<String, String>> response = null;
		boolean userAccount = accountService.createUserAccount(acForm);
		if (userAccount) {
			messageResponse.put("message", AppConstants.ACCOUNT_CREATION_MSG);
			response = new ResponseEntity<>(messageResponse, HttpStatus.CREATED);// 201
		} else {
			messageResponse.put("message", "With this mail id account is already exists");
			response = new ResponseEntity<>(messageResponse, HttpStatus.CONFLICT); // 409
		}

		return response;
	}

	@GetMapping("fetch-all-user-accounts")
	public ResponseEntity<Page<UserAccountForm>> fetchAccountDetail(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size, @RequestParam(defaultValue = "id") String sortField,
	        @RequestParam(defaultValue = "asc") String sortOrder) {

		Page<UserAccountForm> allUserAccountDetails = accountService.getAllUserAccountDetails(page, size,sortField,sortOrder);

		return new ResponseEntity<>(allUserAccountDetails, HttpStatus.OK);

	}
	
	@PutMapping("edit-user-account/{id}")
	@CrossOrigin(origins = "http://localhost:3000")
	public ResponseEntity<Map<String,String>> editUserAccount(  @PathVariable("id") Long userId, @Valid  @RequestBody  EditUserAccount ed) {
		String status = accountService.editUserAccountById(userId,ed);
		
		
		messageResponse.put("message", status);
		return new ResponseEntity<>(messageResponse, HttpStatus.OK);
	}

	/*
	 * @PutMapping("/update-account-status/{userId}/{status}") public
	 * ResponseEntity<List<UserAccountForm>> updateTheRecord(@PathVariable("userId")
	 * Integer userId,
	 * 
	 * @PathVariable("status") String status) {
	 * accountService.accountStatusChange(userId, status);
	 * 
	 * List<UserAccountForm> allUserAccountDetails = (List<UserAccountForm>)
	 * accountService .getAllUserAccountDetails(userId, userId);
	 * 
	 * return new ResponseEntity<>(allUserAccountDetails, HttpStatus.OK); }
	 */

	@PostMapping("/unlock-account")
	@CrossOrigin(origins = "http://localhost:3000")
	public ResponseEntity<Map<String, String>> unlockTheAccount(@RequestBody UnLockAccountForm unlckForm) {

		ResponseEntity<Map<String, String>> response = null;

		String unlockTheAccount = accountService.unlockTheAccount(unlckForm);

		if (unlockTheAccount.contains("unlocking is done")) {

			messageResponse.put("message", unlockTheAccount);
			response = new ResponseEntity<>(messageResponse, HttpStatus.OK);
		} else
			messageResponse.put("message", unlockTheAccount);
		response = new ResponseEntity<>(messageResponse, HttpStatus.BAD_REQUEST);

		return response;

	}
	
	@DeleteMapping("/delete-by-id/{userId}")
	@CrossOrigin(origins = "http://localhost:3000")
	public ResponseEntity<Map<String, String>> deletyById(@PathVariable("userId") Long id) {
		String deleteById = accountService.deleteById(id);	
			messageResponse.put("message", deleteById);
		return  ResponseEntity.ok(messageResponse);
	}
	
	
	@GetMapping("get-all-plans-count")
	@CrossOrigin(origins = "http://localhost:3000")
	public ResponseEntity<PlanStatsResponse> getAllPlansCount() {
	    return new ResponseEntity<>(accountService.getAllPlansCountWithDates(), HttpStatus.OK);
	}




	@GetMapping("/export-records-inexcel")
	public ResponseEntity<byte[]> exportAccountsToExcel() throws IOException {
	    List<UserAccountForm> accounts = accountService
	            .getAllUserAccountDetails(0, Integer.MAX_VALUE, "id", "asc")
	            .getContent();

	    byte[] excelBytes = utils.generateAccountsExcel(accounts);
	    String filename = URLEncoder.encode("Accounts.xlsx", StandardCharsets.UTF_8);

	    return ResponseEntity.ok()
	            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
	            .contentType(MediaType.parseMediaType(
	                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
	            .body(excelBytes);
	}
	@GetMapping("download/accounts-txt")
	public ResponseEntity<byte[]> downloadAccountsTxt() throws IOException {
	    List<UserAccountForm> accounts = accountService.getAllAccounts(); // your service method
	    byte[] fileContent = utils.generateAccountsTxt(accounts);

	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.TEXT_PLAIN);
	    headers.setContentDisposition(ContentDisposition.builder("attachment")
	        .filename("Accounts.txt")
	        .build());

	    return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
	}



}
