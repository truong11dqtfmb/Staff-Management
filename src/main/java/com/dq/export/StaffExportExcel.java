package com.dq.export;

import com.dq.entity.Staff;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class StaffExportExcel {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<Staff> listStaffs;

    public StaffExportExcel(List<Staff> listStaffs) {
        this.listStaffs = listStaffs;
        workbook = new XSSFWorkbook();
    }


    private void writeHeaderLine() {
        sheet = workbook.createSheet("Staffs");

        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        createCell(row, 0, "Staff ID", style);
        createCell(row, 1, "Full Name", style);
        createCell(row, 2, "Email", style);
        createCell(row, 3, "Gender", style);
        createCell(row, 4, "Phone Number", style);
        createCell(row, 5, "Photo", style);
        createCell(row, 6, "Salary", style);

    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value instanceof Float) {
            cell.setCellValue((Float) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private void writeDataLines() {
        int rowCount = 1;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        for (Staff staff : listStaffs) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            createCell(row, columnCount++, staff.getId(), style);
            createCell(row, columnCount++, staff.getName(), style);
            createCell(row, columnCount++, staff.getEmail(), style);
            createCell(row, columnCount++, staff.isGender() ? "Male" : "Female", style);
            createCell(row, columnCount++, staff.getPhone(), style);
            createCell(row, columnCount++, staff.getPhoto(), style);
            createCell(row, columnCount++, staff.getSalary(), style);

        }
    }

    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
        writeDataLines();

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();

    }
}
