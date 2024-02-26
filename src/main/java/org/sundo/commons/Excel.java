package org.sundo.commons;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Excel {
    private final HttpServletResponse response;

    /**
     * 엑셀 생성
     * @param fileName : 파일명
     * @param rows : 엑셀 행별 데이터
     */
    public void create(String fileName, List<Object[]> rows) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();

        for (int i = 0; i < rows.size(); i++) {
            Row row = sheet.createRow(i); // 엑셀 행 생성
            Object[] items = rows.get(i);

            int cellNum = 0;
            for (Object item : items) {
                Cell cell = row.createCell(cellNum++);
                if (item instanceof String) {
                    cell.setCellValue((String) item);
                } else if (item instanceof Integer) {
                    cell.setCellValue((Integer) item);
                } else if (item instanceof Double) {
                    cell.setCellValue((Double) item);
                }

            }

        }

        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            workbook.write(bout);
            bout.close();

            fileName = new String(fileName.getBytes(), "ISO8859_1");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setIntHeader("Expires", 0);
            response.setHeader("Cache-Control", "must-revalidate");
            response.setHeader("Pragma", "public");
            response.setContentLength(bout.size());

            OutputStream out = response.getOutputStream();
            out.write(bout.toByteArray());
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}