package android.bigplan.lego.model;

/**
 * 版本升级数据模型
 * 
 * @author FingerArt FingerArt.me
 * @date 2015年12月10日 下午8:19:02
 *
 */
public class UpdateVersion {

	/**
	 * 升级内容描述
	 */
	private String content;
	/**
	 * 是否强制升级
	 */
	private int forced;
	/**
	 * 是否有升级, 优先级高于forced
	 */
	private int upgrade;
	/**
	 * 下载的URL
	 */
	private String url;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getForced() {
		return forced;
	}

	public void setForced(int forced) {
		this.forced = forced;
	}

	public int getUpgrade() {
		return upgrade;
	}

	public void setUpgrade(int upgrade) {
		this.upgrade = upgrade;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "UpdateVersion [content=" + content + ", forced=" + forced
				+ ", upgrade=" + upgrade + ", url=" + url + "]";
	}

}
