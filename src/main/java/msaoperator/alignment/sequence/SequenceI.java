package msaoperator.alignment.sequence;

/**
 * The sequence interface, constructed for every sequence object!
 */
public interface SequenceI {

	void setSeqName(String seqName);

	String getSeqName();

	void setStartPro(int start);

	int getStartPro();

	void setEndPro(int end);

	int getEndPro();

	void setStartRes(int startRes);

	int getStartRes();

	void setEndRes(int endRes);

	int getEndRes();

	int getLength();

	void setSeq(String sequence);

	String getSeqAsString(int startRes, int endRes);

	char[] getSeq();

	String getSeqAsString();

	char getCharAt(int i);

	SequenceI getSequenceI(int startRes, int endRes);

}
