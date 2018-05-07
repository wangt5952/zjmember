package com.laf.manager.utils.poi

import org.apache.poi.ss.usermodel.*

class POICellStyle private constructor() {
    companion object {

        private fun createBorderedStyle(wb: Workbook): CellStyle {
//            val thin = BorderStyle.THIN
//            val black = IndexedColors.BLACK.getIndex()

            val style = wb.createCellStyle()
//            style.setBorderRight(thin)
//            style.rightBorderColor = black
//            style.setBorderBottom(thin)
//            style.bottomBorderColor = black
//            style.setBorderLeft(thin)
//            style.leftBorderColor = black
//            style.setBorderTop(thin)
//            style.topBorderColor = black

            return style
        }

        fun getHeaderStyle(wb: Workbook): CellStyle {
            val style = createBorderedStyle(wb)
            val headerFont = wb.createFont()
            headerFont.bold = true
            headerFont.fontHeightInPoints = 14.toShort()
            style.setAlignment(HorizontalAlignment.CENTER)
//            style.fillForegroundColor = IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex()
//            style.setFillPattern(FillPatternType.SOLID_FOREGROUND)
            style.setFont(headerFont)

            return style
        }

        fun getHeaderDateStyle(wb: Workbook): CellStyle {
            val df = wb.createDataFormat()
            val style = createBorderedStyle(wb)
            val headerFont = wb.createFont()
            style.setAlignment(HorizontalAlignment.CENTER);
            style.fillForegroundColor = IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex();
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            style.setFont(headerFont);
            style.dataFormat = df!!.getFormat("d-mmm");

            return style
        }

        fun getCellBStyle(wb: Workbook): CellStyle {

            val font1 = wb.createFont()
            font1.bold = true
            val style = createBorderedStyle(wb)
            style.setAlignment(HorizontalAlignment.LEFT)
            style.setFont(font1)

            return style
        }

        fun getCellBCenterStyle(wb: Workbook): CellStyle {
            val font1 = wb.createFont()
            font1.bold = true
            val style = createBorderedStyle(wb)
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setFont(font1)

            return style
        }

        fun getCellGStyle(wb: Workbook): CellStyle {
            val df = wb.createDataFormat()
            val font1 = wb.createFont()
            font1.bold = true
            val style = createBorderedStyle(wb);
            style.setAlignment(HorizontalAlignment.RIGHT);
            style.setFont(font1);
            style.fillForegroundColor = IndexedColors.GREY_25_PERCENT.getIndex();
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            style.dataFormat = df!!.getFormat("d-mmm");

            return style
        }

        fun getCellBbStyle(wb: Workbook): CellStyle {
            val font2 = wb.createFont()
            font2.color = IndexedColors.BLUE.getIndex()
            font2.bold = true
            val style = createBorderedStyle(wb)
            style.setAlignment(HorizontalAlignment.LEFT)
            style.setFont(font2)

            return style
        }

        fun getCellBgStyle(wb: Workbook): CellStyle {
            val df = wb.createDataFormat()
            val font1 = wb.createFont()
            font1.bold = true
            val style = createBorderedStyle(wb);
            style.setAlignment(HorizontalAlignment.RIGHT);
            style.setFont(font1);
            style.fillForegroundColor = IndexedColors.GREY_25_PERCENT.getIndex();
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            style.dataFormat = df!!.getFormat("d-mmm");

            return style
        }

        fun getCellHStyle(wb: Workbook): CellStyle {
            val font3 = wb.createFont()
            font3.fontHeightInPoints = 14.toShort()
            font3.color = IndexedColors.DARK_BLUE.getIndex()
            font3.bold = true
            val style = createBorderedStyle(wb)
            style.setAlignment(HorizontalAlignment.LEFT)
            style.setFont(font3)
            style.wrapText = true

            return style
        }

        fun getCellNormalStyle(wb: Workbook): CellStyle {
            val style = createBorderedStyle(wb);
            style.setAlignment(HorizontalAlignment.LEFT);
            style.wrapText = true

            return style
        }

        fun getCellNormalCenterStyle(wb: Workbook): CellStyle {
            val font1 = wb.createFont()
            font1.fontHeightInPoints = 13.toShort()
            val style = createBorderedStyle(wb);
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setFont(font1)
//            style.wrapText = true;

            return style
        }

        fun getCellNormalDate(wb: Workbook): CellStyle {
            val df = wb.createDataFormat()
            val style = createBorderedStyle(wb)
            style.setAlignment(HorizontalAlignment.RIGHT);
            style.wrapText = true;
            style.dataFormat = df!!.getFormat("d-mmm");

            return style
        }

        fun getCellIndented(wb: Workbook): CellStyle {
            val style = createBorderedStyle(wb)
            style.setAlignment(HorizontalAlignment.LEFT)
            style.indention = 1.toShort()
            style.wrapText = true

            return style
        }

        fun getCellBlue(wb: Workbook): CellStyle {
            val style = createBorderedStyle(wb)
            style.setAlignment(HorizontalAlignment.LEFT)
            style.indention = 1.toShort()
            style.wrapText = true

            return style
        }

//        val HEADER = getHeaderDateStyle()
//        val HEADER_DATE = getHeaderDateStyle()
//        val CELL_B = getCellBStyle()
//        val CELL_B_CENTER = getCellBCenterStyle()
//        val CELL_G = getCellGStyle()
//        val CELL_BB = getCellBbStyle()
//        val CELL_BG = getCellBgStyle()
//        val CELL_H = getCellHStyle()
//        val CELL_NORMAL = getCellNormalStyle()
//        @JvmField
//        val CELL_NORMAL_CENTER = getCellNormalCenterStyle()
//        val CELL_NORMAL_DATE = getCellNormalDate()
//        val CELL_INDENTED = getCellIndented()
//        val CELL_BLUE = getCellBlue()
    }
}