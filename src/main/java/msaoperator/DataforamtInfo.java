package msaoperator;

import java.util.Optional;

/**
 * Container class for data format validation results.
 * 
 * <p>
 * This class holds the result of data format validation operations, including
 * success status, format code, and optional private format-specific information.
 * Used primarily in MSA (Multiple Sequence Alignment) file format detection and validation.
 * </p>
 * 
 * <h2>Key Components:</h2>
 * <ul>
 *   <li><strong>isSuccess:</strong> Whether format validation succeeded</li>
 *   <li><strong>dataFormatCode:</strong> Format identifier or error code</li>
 *   <li><strong>dataForamtPrivateInfor:</strong> Optional format-specific metadata</li>
 * </ul>
 * 
 * <h2>Usage Example:</h2>
 * <pre>
 * // Successful validation
 * DataforamtInfo info = new DataforamtInfo(true, FORMAT_FASTA, privateInfo);
 * if (info.isSuccess()) {
 *     int format = info.getDataFormatCode();
 *     // Process based on format
 * }
 * 
 * // Failed validation with error code
 * DataforamtInfo error = new DataforamtInfo(false, ERROR_INVALID_FORMAT);
 * </pre>
 * 
 * <h2>Note:</h2>
 * <p>
 * When {@code isSuccess} is false, {@code dataFormatCode} contains an error code
 * indicating what went wrong during format detection.
 * </p>
 * 
 * @author eGPS Development Team
 * @version 1.0
 * @since 1.0
 * @see DataForamtPrivateInfor
 */
public class DataforamtInfo {
	
	private boolean isSuccess;
	/**
	 * If not success, the this variable will be error code!
	 */
	private int dataFormatCode;
	
	private DataForamtPrivateInfor dataForamtPrivateInfor;

	public DataforamtInfo(boolean isSuccess, int dataFormatCode, DataForamtPrivateInfor dataForamtPrivateInfor) {
		this.isSuccess = isSuccess;
		this.dataFormatCode = dataFormatCode;
		this.dataForamtPrivateInfor = dataForamtPrivateInfor;
	}

	public DataforamtInfo(boolean b, int filetypeerror) {
		this(b, filetypeerror, null);
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public int getDataFormatCode() {
		return dataFormatCode;
	}

	public Optional<DataForamtPrivateInfor> getDataForamtPrivateInfor() {
		return Optional.ofNullable(dataForamtPrivateInfor);
	}
	
	public void setDataForamtPrivateInfor(DataForamtPrivateInfor dataForamtPrivateInfor) {
		this.dataForamtPrivateInfor = dataForamtPrivateInfor;
	}

}
