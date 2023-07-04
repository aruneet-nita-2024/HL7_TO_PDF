# HL7 to PDF Converter

This project provides a Java class `OruMessageController` that converts HL7 messages to PDF format. It uses the HAPI library to parse HL7 messages and the iText library to generate the PDF document.

## Prerequisites

- Java Development Kit (JDK) version 8 or above
- HAPI library
- iText library

## Usage

1. Import the necessary libraries and classes.

2. Create a controller class, `OruMessageController`, and annotate it with `@Controller`.

3. Define a method `generatePdf` with the `@PostMapping` annotation to handle the HL7 to PDF conversion request.

4. Wrap the code in a try-catch block to handle exceptions.

5. Create a `HapiContext` object to initialize the HAPI context.

6. Parse the HL7 message using the `Parser`.

7. Extract the necessary information from the parsed HL7 message using the `Terser`.

8. Generate the PDF document using the iText library.

9. Set the response headers and return the PDF file as a byte array.

10. Optionally, you can add a helper method `addUnderline` to enhance the document layout.

11. Customize the method logic according to your requirements, extracting the desired information from the HL7 message and adding it to the PDF document.

12. Compile and run the application.

## API Endpoint

The controller exposes the following API endpoint:

- **POST** `/generate-pdf` - Converts an HL7 message to a PDF document. Expects the HL7 message as a request body.

 Request Body:
`MSH|^~\&|SendingApp|SendingFacility|ReceivingApp|ReceivingFacility|20230620120000||ORU^R01|MSG001|P|2.5.1
PID|1|54321|12345^^^HospitalID||Doe^John||19800101|M|||123 Main St^^New York^NY^10001||(555)555-5555||S||123456789|98765432
OBR|1|1234567^^^HospitalID|9876543^^^LabID|CBC^Complete Blood Count|R||20230620110000||||PhysicianID^Doe^John^^Dr.^^^|||20230620120000
OBX|1|NM|WBC^White Blood Cell Count^LN||6.7|10.3/uL|4.5-11.0|N|||F
OBX|2|NM|RBC^Red Blood Cell Count^LN||5.2|10.6 mg/dL|4.7-6.1|N|||F
OBX|3|NM|HGB^Hemoglobin^LN||15.0|g/dL|14.0-18.0|N|||F
OBX|4|NM|HCT^Hematocrit^LN||45.0|%|42.0-52.0|N|||F
OBX|5|NM|PLT^Platelet Count^LN||250|10^3/uL|150-450|N|||F
NTE|1||Additional comments or notes regarding the test results.`

Response: https://drive.google.com/file/d/1-SxGF2Y-WSgLVy8ajrWNMroVUjG0-Uip/view?usp=sharing
 
The response will contain the PDF document as a byte array.

## Example HL7 to PDF Conversion Flow

1. Create the HAPI context and parse the ORU message using the `Parser`.

2. Extract specific segments and fields from the message using the `Terser`.

3. Generate the PDF document using the iText library.

4. Set up the document structure, including the title, patient information, observation request details, and observation/result table.

5. Customize the appearance and layout of the PDF document using the provided methods.

6. Save the PDF document to a file.

7. Set the appropriate response headers for the API endpoint.

8. Return the PDF file as a byte array in the API response.

Note: This README provides a basic overview of the code and its functionality. Further modifications and enhancements may be required based on specific use cases and project requirements.


