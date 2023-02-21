package com.dq.export;

import java.awt.Color;
import java.io.IOException;
import java.util.List;


import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

import com.dq.entity.Staff;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

public class StaffExportPDF {
    private List<Staff> listStaffs;

    public StaffExportPDF(List<Staff> listStaffs) {
        this.listStaffs = listStaffs;
    }

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.GREEN);
        cell.setPadding(7);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("Staff ID", font));

        table.addCell(cell);

        cell.setPhrase(new Phrase("Full Name", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Email", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Gender", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Phone Number", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Photo", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Salary", font));
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table) {
        for (Staff staff : listStaffs) {
            table.addCell(String.valueOf(staff.getId()));
            table.addCell(staff.getName());
            table.addCell(staff.getEmail());
            table.addCell(staff.isGender() ? "Male" : "Female");
            table.addCell(staff.getPhone());
            table.addCell(staff.getPhoto());
            table.addCell(String.valueOf(staff.getSalary()));
        }
    }

    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.GREEN);

        Paragraph p = new Paragraph("List of Staff", font);
        p.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(p);

        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{1.5f, 3.0f, 3.0f, 1.5f, 3.5f, 4.5f, 1.5f});
        table.setSpacingBefore(10);

        writeTableHeader(table);
        writeTableData(table);

        document.add(table);

        document.close();

    }
}
