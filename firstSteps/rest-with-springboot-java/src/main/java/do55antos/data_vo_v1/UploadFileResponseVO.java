package do55antos.data_vo_v1;

import java.io.Serializable;

public class UploadFileResponseVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String finename;
	private String donwloadURI;
	private String fileType;
	private long fileSize;

	public UploadFileResponseVO() {}
	
	public UploadFileResponseVO(String finename, String donwloadURI, String fileType, long fileSize) {
		this.finename = finename;
		this.donwloadURI = donwloadURI;
		this.fileType = fileType;
		this.fileSize = fileSize;
	}

	public String getFilename() {
		return finename;
	}

	public void setFilename(String finename) {
		this.finename = finename;
	}

	public String getDonwloadURI() {
		return donwloadURI;
	}

	public void setDonwloadURI(String donwloadURI) {
		this.donwloadURI = donwloadURI;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

}
