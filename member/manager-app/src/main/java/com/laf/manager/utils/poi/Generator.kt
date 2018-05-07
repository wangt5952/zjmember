package com.laf.manager.utils.poi

import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.springframework.stereotype.Component
import java.io.OutputStream


@Component
class Generator {
    fun generate(outputStream: OutputStream, titles: List<String>, data: List<*>, sheetName:String) {
//        val fmt = SimpleDateFormat("dd-MMM")
        val wb = HSSFWorkbook()

        val sheet = wb.createSheet(sheetName)

        //turn off gridlines
//        sheet.isDisplayGridlines = false
//        sheet.isPrintGridlines = false
        sheet.fitToPage = true
        sheet.horizontallyCenter = true
        val printSetup = sheet.printSetup
        printSetup.landscape = true

        //the following three statements are required only for HSSF
        sheet.autobreaks = true
        printSetup.fitHeight = 1.toShort()
        printSetup.fitWidth = 1.toShort()

        //the header row: centered text in 48pt font
        val headerRow = sheet.createRow(0)
//        headerRow.heightInPoints = 12.75f
        for (i in 0 until titles.size) {
            val title = titles[i]
            val cell = headerRow.createCell(i)
            cell.setCellValue(title)
            cell.setCellStyle(POICellStyle.getHeaderStyle(wb))
        }

        //freeze the first row
//        sheet.createFreezePane(0, 1)

        var row: Row
        var cell: Cell
        var rownum = 1
        var i = 0
        val cellStyle = POICellStyle.getCellNormalCenterStyle(wb)

        while (i < data.size) {
            row = sheet.createRow(rownum)
//            if (data[i] == null) {
//                i++
//                rownum++
//                continue
//            }

//            val (rowData, cellStyle) = data[i] as Tuple2<List<String>, CellStyle>
            val rowData = data[i] as List<String>

            for (j in 0 until rowData.size) {
                cell = row.createCell(j)

                cell.setCellValue(rowData[j])
                cell.setCellStyle(cellStyle)
            }

            i++
            rownum++
        }

//        sheet.setColumnWidth(0, 256*6);
//        sheet.setColumnWidth(1, 256*33);
//        sheet.setColumnWidth(2, 256*20);
//        sheet.setZoom(75); //75% scale

//        val input = ByteArrayInputStream(wb.bytes)
//
//        val buffer = ByteArray(4096)
//        var bytesRead = -1
//
//        while ((bytesRead = input.read(buffer)) != -1) {
//            .write(buffer, 0, bytesRead)
//        }

        wb.write(outputStream)
        outputStream.close()
        wb.close()
    }
}