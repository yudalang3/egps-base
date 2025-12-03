package msaoperator.alignment.sequence;

/**
 * Implementation of sequence data structure for multiple sequence alignment operations.
 */
public class Sequence implements SequenceI {

	private String seqName;
	private String seq;
	private char[] seqChars;
	private int startRes;
	private int endRes;
	private int startPro;
	private int endPro;

	public Sequence(String seqName, String seq) {
		this.seqName = seqName;
		this.seq = seq;
		seqChars = seq.toCharArray();
	}


	@Override
	public void setSeqName(String seqName) {
		this.seqName = seqName;
	}

	@Override
	public String getSeqName() {

		return seqName;
	}

	@Override
	public void setStartPro(int start) {
		startPro = start;
	}

	@Override
	public int getStartPro() {
		return startPro;
	}

	@Override
	public void setEndPro(int end) {
		endPro = end;
	}

	@Override
	public int getEndPro() {
		return endPro;
	}

	@Override
	public void setSeq(String seq) {
		this.seq = seq;
		seqChars = seq.toCharArray();
	}

	@Override
	public String getSeqAsString(int start, int end) {
		if (start > end) {
			return null;
		}

		if (start < 0) {
			start = 0;
		}

		if (end >= getLength()) {
			end = getLength();
		}
		if (start >= getLength()) {

			start = getLength();
		}

		return seq.substring(start, end);
	}

	@Override
	public char[] getSeq() {

		return seqChars;
	}

	@Override
	public void setStartRes(int startRes) {
		this.startRes = startRes;
	}

	@Override
	public int getStartRes() {
		return startRes;
	}

	@Override
	public void setEndRes(int endRes) {
		this.endRes = endRes;
	}

	@Override
	public int getEndRes() {
		return endRes;
	}

	@Override
	public int getLength() {
		return seq.length();
	}

	@Override
	public char getCharAt(int i) {

		if (i >= 0 && i < seqChars.length) {
			return seqChars[i];
		}
		return ' ';
	}

	@Override
	public String getSeqAsString() {
		return seq;
	}

	@Override
	public SequenceI getSequenceI(int startRes, int endRes) {
		String seqAsString = getSeqAsString(startRes, endRes);
        return new Sequence(seqName, seqAsString);
	}

	
	@Override
	public String toString() {
		return seqName +"    "+seq;
	}
}