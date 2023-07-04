package com.hl72pdf.controller;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v251.segment.OBX;
import ca.uhn.hl7v2.model.v251.message.ORU_R01;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.util.Terser;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

@Controller

public class OruMessageController {

    @PostMapping(value = "/generate-pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    
    public ResponseEntity<byte[]> generatePdf(@RequestBody String oruMessage) throws IOException {
    	
        try {
        	
            // Create the HAPI context
        	
        	HapiContext context = new DefaultHapiContext();
        	
            // Parse the ORU message
        	
        	Parser parser = context.getPipeParser();
            Message message = parser.parse(oruMessage);
            
            //Object of ORU datatype
            
            ORU_R01 msg;
            
            
            try {
                msg = (ORU_R01) parser.parse(oruMessage);
            }
            catch (Exception e) 
            {
                e.printStackTrace();
                
                //System.out.println("Message is invalid.");
                return null ;
            }
            
            
            /* 
              		 * Setting the following property allows you to specify a default
               		 * value to assume if OBX-2 is missing. 
           */
             
    		context.getParserConfiguration().setDefaultObx2Type("ST");
    		  
    		// Parsing now succeeds
    		
    		msg = (ORU_R01) parser.parse(oruMessage);
    		System.out.println("Message is valid");

            // Extract and print specific segments and fields using Terser
    		
            Terser terser = new Terser(msg);
            
            
            /* Message Header Segment */
            
            String Send_App = terser.get("/.MSH-3");
            
           //System.out.println("Sending Application :" + Send_App);
            
            
   /*----------------------------------------Paitent Identification----------------------------------------------------------*/
            
            String SeqNo = terser.get("/.PID-1");
            String PaitentID = terser.get("/.PID-2");
            String FirstName = terser.get("/.PID-5-1");
            String LastName = terser.get("/.PID-5-2");
            String DOB = terser.get("/.PID-7");
            DOB = DOB.substring(6,8) + "/" + DOB.substring(4,6)+ "/" + DOB.substring(0,4);
            String Address = terser.get("/.PID-11-1")+","+terser.get("/.PID-11-2")+","+terser.get("/.PID-11-3")+","+terser.get("/.PID-11-4")+","+terser.get("/.PID-11-5") + "," + terser.get("/.PID-11-6");
            String Sex =  terser.get("/.PID-8");
            String PhoneNo= terser.get("/.PID-13");
            
   /*---------------------------------------Observation Request Segment----------------------------------------------------------*/         
                 
            String descrp = terser.get("/.OBR-4-2");
            String collect_time = terser.get("/.OBR-7");
            collect_time = collect_time.substring(8, 10) + ":" + collect_time.substring(10, 12) + ", " + collect_time.substring(6,8) + "/" + collect_time.substring(4,6)+ "/" + collect_time.substring(0,4);
            String result_time =  terser.get("/.OBR-14");
            result_time = result_time.substring(8, 10) + ":" + result_time.substring(10, 12) + ", " + result_time.substring(6,8) + "/" + result_time.substring(4,6)+ "/" + result_time.substring(0,4);
            String result_status = terser.get("/.OBR-25");
            
   /*---------------------------------------Observation/Result Segment-----------------------------------------------------------*/
            
            /* Count no. of OBX segments */
            
             int obxCount = (int) (msg. getPATIENT_RESULT().getORDER_OBSERVATION().getOBSERVATIONReps());
            
             System.out.println("No.of OBX count : " + obxCount);
             
             int cnt = 0;
             
             String key = "/.OBSERVATION(" + cnt +")";
             
             String comp_descr = terser.get(key + "/OBX-3-2");
             String obs_val = terser.get(key + "/OBX-5");
             String obs_unit = terser.get(key + "/OBX-6");
             String ref_range = terser.get(key + "/OBX-7"); 
             String verdict = terser.get(key + "/OBX-8");
             String obs_res_stats = terser.get(key + "/OBX-11");
             
             
   /*---------------------------------------PDF Generation Segment-----------------------------------------------------------*/
             
            // Generate the PDF document
            
            
            Document document = new Document();
            document.setPageSize(PageSize.A4);
            File file = new File("D:/Downloads/result.pdf");
            FileOutputStream outputStream = new FileOutputStream(file);
            PdfWriter.getInstance(document, outputStream);
            document.open();
            
            // TITLE 
    		Font fontTitle = FontFactory.getFont(FontFactory.TIMES_BOLD);
    		fontTitle.setSize(20);
    		Paragraph paragraph = new Paragraph("LAB REPORT", fontTitle);
    		paragraph.setAlignment(Paragraph.ALIGN_CENTER);
    		paragraph.setSpacingAfter(10);

            Font font = FontFactory.getFont("Times New Roman", BaseFont.IDENTITY_H, true, 12, Font.NORMAL, BaseColor.BLACK);
            Font ft = FontFactory.getFont("Times New Roman", BaseFont.IDENTITY_H, true, 12, Font.UNDERLINE, BaseColor.BLACK);
            document.add(paragraph);
            addUnderline(document);
    
           /*--------------------------------------------------------------------------------------*/  
            
            document.add(new Paragraph("Paitent ID : " + PaitentID, font));
            document.add(new Paragraph("Serial No. : " + SeqNo)); 
            document.add(new Paragraph("Patient Name : " + FirstName + " " + LastName, font));
            document.add(new Paragraph("DOB : " + DOB , font));
            document.add(new Paragraph("Gender : " + Sex, font));
            document.add(new Paragraph("Address : " + Address, font));
            document.add(new Paragraph("Contact : " + PhoneNo, font));
            document.add( new Paragraph(" "));
            addUnderline(document);
            
          /*--------------------------------------------------------------------------------------*/  
            
            document.add( new Paragraph("Sample Description : " + descrp));
            document.add( new Paragraph("Collection Time :  " + collect_time));
            document.add( new Paragraph("Result Time : " + result_time));
            document.add( new Paragraph("Result Status :" + result_status));
            document.add( new Paragraph(" "));
            addUnderline(document);

         /*----------------------------------------------------------------------------------------*/
            float[] columnWidths = { 10f, 5f, 5f, 7f, 5f, 5f };
            
            PdfPTable table = new PdfPTable(columnWidths);
            
            document.add(new Paragraph(" "));
     
            PdfPCell cell = new PdfPCell(new Phrase("Compositon", ft ));
            cell.setPadding(5);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell.setPhrase(new Phrase("Value", ft ));
            table.addCell(cell);
            
            cell.setPhrase(new Phrase("Unit", ft ));
            table.addCell(cell);
            
            cell.setPhrase(new Phrase("Range", ft ));
            table.addCell(cell);
            
            cell.setPhrase(new Phrase("Verdict", ft ));
            table.addCell(cell);
            
            cell.setPhrase(new Phrase("Status", ft ));
            table.addCell(cell);
            
            while(cnt < obxCount) {
            	
     /*     document.add( new Paragraph("Composition Description : " + comp_descr));
            document.add( new Paragraph("Observation Value : " + obs_val));
            document.add( new Paragraph("Obseravtion Unit : " + obs_unit));
            document.add( new Paragraph("Reference Range : " + ref_range));
            document.add( new Paragraph("Verdict : " + verdict));
            document.add( new Paragraph("Observation Result Status : " + obs_res_stats));
            document.add( new Paragraph("--------------------------------------------------------------")); 
     */
            	
    	   cell.setPhrase(new Phrase(comp_descr));
    	   table.addCell(cell);

    	   cell.setPhrase(new Phrase(obs_val));
    	   table.addCell(cell);
    	   
    	   cell.setPhrase(new Phrase(obs_unit));
    	   table.addCell(cell);
    	   
    	   cell.setPhrase(new Phrase(ref_range));
    	   table.addCell(cell);
    	   
    	   cell.setPhrase(new Phrase(verdict));
    	   table.addCell(cell);
    	   
    	   cell.setPhrase(new Phrase(obs_res_stats));
    	   table.addCell(cell);
    	   	
            
            cnt++;
            
            key = "/.OBSERVATION(" + cnt +")";
            
            comp_descr = terser.get(key + "/OBX-3-2");
            obs_val = terser.get(key + "/OBX-5");
            obs_unit = terser.get(key + "/OBX-6");
            ref_range = terser.get(key + "/OBX-7"); 
            verdict = terser.get(key + "/OBX-8");
            obs_res_stats = terser.get(key + "/OBX-11");
            
            }
            
            document.add(table);
            document.close();

            
            // Set the response headers
            
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "result.pdf");

            // Return the PDF file as a byte array
            
            byte[] pdfBytes = Files.readAllBytes(file.toPath());
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
            
            } 
        
        catch (IOException | DocumentException | HL7Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    private static void addUnderline(Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.BOTTOM);
        cell.setBorderWidthBottom(1f);
        cell.setPaddingBottom(5f);
        table.addCell(cell);
        document.add(table);
    }
}