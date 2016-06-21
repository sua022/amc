package cn.hugoz.amc.parser;

public class WildcardTypeEntity extends TypeEntity {

	private TypeEntity ext;// extends
	private TypeEntity sup;// super

	public TypeEntity getExt() {
		return ext;
	}

	public void setExt(TypeEntity ext) {
		this.ext = ext;
	}

	public TypeEntity getSup() {
		return sup;
	}

	public void setSup(TypeEntity sup) {
		this.sup = sup;
	}

}
