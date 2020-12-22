package org.jeecg.common.util;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.NumberToTextConverter;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author Andy
 * @Date 2020/9/16
 * desc: 导入动态表头类 返回Map<String, String> 默认excel会是double类型 支持 1行和两行表头，三级表头需要另行处理
 */
public class ExcelImportMapHelper {

    public List<Map<String, String>> createWorkbookStyle(Workbook workbook, int headRows) {
        //取得第一张表
        Sheet sheet = workbook.getSheetAt(0);
        List<Map<String, String>> list = new ArrayList<>();
        //导入必须要表头 行数必须大于表头数量
        if (headRows < 1 || sheet.getLastRowNum() < headRows) {
            return list;
        }
        //第1行标题
        Row rowTitle = sheet.getRow(0);

        //用表头去算有多少列，不然从下面的行计算列的话，空的就不算了
        int colCount = sheet.getRow(0).getLastCellNum();
        //判断是否具有合并单元格
        boolean isTitleMerge = isMergedRegion(sheet, 0, 0);
        Row rowChildTitle = null;
        if (isTitleMerge) { //合并单元格 ，存在多行表头
            if (headRows < 2) {
                return list;
            }
            rowChildTitle = sheet.getRow(1); //二级表头
        }
        // 过滤表头
        for (int i = headRows; i <= sheet.getLastRowNum(); i++) {
            Map map = new HashMap<String, String>();
            Row row = sheet.getRow(i);
            //过滤空行
            if (row == null || checkRowNull(row, colCount)) {
                continue;
            }
            // 从表头下列开始 循环列
            for (int j = 0; j < colCount; j++) {
                Cell cell = row.getCell(j);
                String cellTitle = null;
                if (isTitleMerge) {
                    cellTitle = getMergedRegionValue(sheet, 0, j);
                    String childTitle = formatCell(rowChildTitle.getCell(j));
                    if (oConvertUtils.isNotEmpty(childTitle)) {
                        cellTitle = cellTitle + "_" + childTitle;
                    }
                } else {
                    cellTitle = formatCell(rowTitle.getCell(j));
                }
                String cellValue = formatCell(cell);
                map.put(cellTitle, cellValue);
            }
            list.add(map);
        }
        return list;
    }

    /**
     * 获取合并单元格的值
     *
     * @param sheet
     * @param row    行下标
     * @param column 列下标
     * @return
     */
    public String getMergedRegionValue(Sheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();

        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress ca = sheet.getMergedRegion(i);
            int firstColumn = ca.getFirstColumn();
            int lastColumn = ca.getLastColumn();
            int firstRow = ca.getFirstRow();
            int lastRow = ca.getLastRow();
            if (row >= firstRow && row <= lastRow) {
                if (column >= firstColumn && column <= lastColumn) {
                    Row fRow = sheet.getRow(firstRow);
                    Cell fCell = fRow.getCell(firstColumn);
                    return formatCell(fCell);
                }
            }
        }
        return "";
    }

    /**
     * 判断指定的单元格是否是合并单元格
     *
     * @param sheet
     * @param row    行下标
     * @param column 列下标
     * @return
     */
    public boolean isMergedRegion(Sheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if (row >= firstRow && row <= lastRow) {
                if (column >= firstColumn && column <= lastColumn) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断整行是否为空
     *
     * @param row    excel得行对象
     * @param maxRow 有效值得最大列数
     */
    private boolean checkRowNull(Row row, int maxRow) {
        int num = 0;
        for (int j = 0; j < maxRow; j++) {
            Cell cell = row.getCell(j);
            if (cell == null || cell.equals("") || cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
                num++;
            }
        }
        if (maxRow == num) {
            return true;
        }

        return false;
    }

    public String formatCell(Cell cell) {
        if (cell == null) {
            return null;
        }
        String ret;
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                ret = cell.getStringCellValue();
                break;
            case Cell.CELL_TYPE_FORMULA:
                Workbook wb = cell.getSheet().getWorkbook();
                CreationHelper crateHelper = wb.getCreationHelper();
                FormulaEvaluator evaluator = crateHelper.createFormulaEvaluator();
                ret = formatCell(evaluator.evaluateInCell(cell));
                break;
            case Cell.CELL_TYPE_NUMERIC:
                if (HSSFDateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式
                    SimpleDateFormat sdf = null;
                    if (cell.getCellStyle().getDataFormat() == HSSFDataFormat.getBuiltinFormat("h:mm")) {
                        sdf = DateUtils.short_time_sdf.get();
                    } else {// 日期
                        sdf = DateUtils.date_sdf.get();
                    }
                    Date date = cell.getDateCellValue();
                    ret = sdf.format(date);
                } else if (cell.getCellStyle().getDataFormat() == 58) {
                    // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
                    double value = cell.getNumericCellValue();
                    Date date = HSSFDateUtil.getJavaDate(value);
                    ret = DateUtils.date2Str(date, DateUtils.date_sdf.get());
                } else {
                    ret = NumberToTextConverter.toText(cell.getNumericCellValue());
                }
                break;
            case Cell.CELL_TYPE_BLANK:
                ret = "";
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                ret = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_ERROR:
                ret = null;
                break;
            default:
                ret = null;
        }
        return ret.trim();
    }

}
