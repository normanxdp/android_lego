package android.bigplan.lego.model;

import java.io.Serializable;
import java.util.List;

/**
 * 首页配置
 * 
 * @author FingerArt FingerArt.me
 * @date 2015年11月25日 下午12:07:21
 */
public class IndexConfig implements Serializable {

	List<IndexModule> module;

	public List<IndexModule> getModule() {
		return module;
	}

	public void setModule(List<IndexModule> module) {
		this.module = module;
	}


	public String toString() {
		return "IndexConfig [module=" + module + "]";
	}

}
