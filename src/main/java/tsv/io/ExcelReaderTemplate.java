package tsv.io;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * <h1>目的</h1>
 * <p>
 * 
 * 这个目的是 读取 excel文件的第一个sheet，形成一个表格对象。这个表格对象是很基础的实现。
 * </p>
 * 
 * <h1>输入/输出</h1>
 * 
 * <p>
 * 输入一个xlsx格式的文件，输出一个简单的 Table类。
 * </p>
 * 
 * <h1>使用方法</h1>
 * 
 * <blockquote> 直接调用readFile 方法 </blockquote>
 * 
 * <h1>注意点</h1>
 * <ol>
 * <li>快捷使用</li>
 * <li>现在只能当做脚本来用，如果要用的话注意设置 <code>setter</code>方法等</li>
 * </ol>
 * 
 * @implSpec 直接掉包 POI ，创建了一个 KitTable类来存储结果
 * 
 * @author yudal
 *
 */
public class ExcelReaderTemplate {
	private static final Logger logger = LoggerFactory.getLogger(ExcelReaderTemplate.class);

	private final String XLS = "xls";
	private final String XLSX = "xlsx";

	/**
	 * 根据文件后缀名类型获取对应的工作簿对象
	 * 
	 * @param inputStream 读取文件的输入流
	 * @param fileType    文件后缀名类型（xls或xlsx）
	 * @return 包含文件数据的工作簿对象
	 * @throws IOException
	 */
	private Workbook getWorkbook(InputStream inputStream, String fileType) throws IOException {
		Workbook workbook = null;
		if (fileType.equalsIgnoreCase(XLS)) {
			workbook = new HSSFWorkbook(inputStream);
		} else if (fileType.equalsIgnoreCase(XLSX)) {
			workbook = new XSSFWorkbook(inputStream);
		}
		return workbook;
	}

	/**
	 * 这是一个快速读取内容的方法，它要求Excel文件的内容是表格化的，不能有合并的单元格。所有格式会被取消
	 * 
	 * @return
	 * @throws IOException
	 */
	public KitTable readExcelFirstSheet(String fileName) throws IOException {
		Workbook workbook = null;
		FileInputStream inputStream = null;

		try {
			// 获取Excel后缀名
			String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
			// 获取Excel文件
			File excelFile = new File(fileName);
			if (!excelFile.exists()) {
				logger.warn("指定的Excel文件不存在！");
				return null;
			}

			// 获取Excel工作簿
			inputStream = new FileInputStream(excelFile);
			workbook = getWorkbook(inputStream, fileType);

			// 读取excel中的数据
			KitTable excel = parseExcel(workbook);
			excel.setPath(fileName);
			return excel;
		} catch (Exception e) {
			logger.warn("解析Excel失败，文件名：" + fileName + " 错误信息：" + e.getMessage());
			return null;
		} finally {
			try {
				if (null != workbook) {
					workbook.close();
				}
				if (null != inputStream) {
					inputStream.close();
				}
			} catch (Exception e) {
				logger.warn("关闭数据流出错！错误信息：" + e.getMessage());
				return null;
			}
		}

	}

	/**
	 * 解析Excel数据
	 * 
	 * @param workbook Excel工作簿对象
	 * @return 解析结果
	 */
	private KitTable parseExcel(Workbook workbook) {
		KitTable kitTable = new KitTable();

		// 解析sheet
		// 现在只处理第一个 Sheet
		int numOfSheets = 1;
		for (int sheetNum = 0; sheetNum < numOfSheets; sheetNum++) {
			Sheet sheet = workbook.getSheetAt(sheetNum);

			// 校验sheet是否合法
			if (sheet == null) {
				continue;
			}

			// 获取第一行数据
			int firstRowNum = sheet.getFirstRowNum();
			Row firstRow = sheet.getRow(firstRowNum);
			if (null == firstRow) {
				logger.warn("解析Excel失败，在第一行没有读取到任何数据！");
			}

			List<String> header = convertRowToData(firstRow);
			kitTable.setHeaderNames(header);

			// 解析每一行的数据，构造数据对象
			int rowStart = firstRowNum + 1;
			int rowEnd = sheet.getPhysicalNumberOfRows();
			for (int rowNum = rowStart; rowNum < rowEnd; rowNum++) {
				Row row = sheet.getRow(rowNum);
				if (null == row) {
					continue;
				}

				List<String> resultData = convertRowToData(row);
				if (null == resultData) {
					logger.warn("第 " + row.getRowNum() + "行数据不合法，已忽略！");
					continue;
				}
				kitTable.getContents().add(resultData);
			}
		}

//		List<String> lines = FileUtils.readLines(new File(fileLocation), StandardCharsets.UTF_8);
//
//		Iterator<String> iterator = lines.iterator();
//		
//		String[] headers = EGPSStringUtil.split(iterator.next(), '\t');
//		kitTable.getHeaderNames().addAll(Arrays.asList(headers));
//		
//		while (iterator.hasNext()) {
//			String next = iterator.next();
//			String[] line = EGPSStringUtil.split(next, '\t');
//			List<String> asList = Arrays.asList(line);
//			kitTable.getContents().add(asList);
//		}

		return kitTable;
	}

	/**
	 * 将单元格内容转换为字符串
	 * 
	 * @param cell
	 * @return
	 */
	private String convertCellValueToString(Cell cell) {
		if (cell == null) {
			return null;
		}
		String returnValue = null;
		switch (cell.getCellType()) {
		case NUMERIC: // 数字
			Double doubleValue = cell.getNumericCellValue();

			// 格式化科学计数法，取一位整数
			DecimalFormat df = new DecimalFormat("0");
			returnValue = df.format(doubleValue);
			break;
		case STRING: // 字符串
			returnValue = cell.getStringCellValue();
			break;
		case BOOLEAN: // 布尔
			Boolean booleanValue = cell.getBooleanCellValue();
			returnValue = booleanValue.toString();
			break;
		case BLANK: // 空值
			break;
		case FORMULA: // 公式
			returnValue = cell.getCellFormula();
			break;
		case ERROR: // 故障
			break;
		default:
			break;
		}
		return returnValue;
	}

	/**
	 * 提取每一行中需要的数据，构造成为一个结果数据对象
	 *
	 * 当该行中有单元格的数据为空或不合法时，忽略该行的数据
	 *
	 * @param row 行数据
	 * @return 解析后的行数据对象，行数据错误时返回null
	 */
	private List<String> convertRowToData(Row row) {

		List<String> ret = new ArrayList<>();

		Iterator<Cell> cellIterator = row.cellIterator();
		while (cellIterator.hasNext()) {
			Cell next = cellIterator.next();
			String str = convertCellValueToString(next);
			ret.add(str);
		}

		return ret;
	}
}
