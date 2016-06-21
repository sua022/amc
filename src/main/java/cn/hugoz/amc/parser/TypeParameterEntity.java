package cn.hugoz.amc.parser;

import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.TypeParameter;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

public class TypeParameterEntity extends TypeEntity {

	private ArrayList<TypeEntity> typeBounds;

	public ArrayList<TypeEntity> getTypeBounds() {
		return typeBounds;
	}

	public void setTypeBounds(ArrayList<TypeEntity> typeBounds) {
		this.typeBounds = typeBounds;
	}
	
	public TypeParameterEntity copy() {
		TypeParameterEntity entity = new TypeParameterEntity();
		entity.setSimpleName(getSimpleName());
		entity.setFullName(getFullName());
		entity.setTypeBounds(getTypeBounds());
		return entity;
	}

	public static TypeParameterEntity createTypeParameter(FileEntity fileEntity, ClassEntity classEntity,
			MethodEntity methodEntity, TypeParameter parameter) {
		TypeParameterEntity entity = new TypeParameterEntity();
		entity.setSimpleName(parameter.getName());
		entity.setFullName(parameter.getName());

		List<ClassOrInterfaceType> bounds = parameter.getTypeBound();
		if (bounds != null && bounds.size() > 0) {
			ArrayList<TypeEntity> list = new ArrayList<>();
			for (ClassOrInterfaceType cif : bounds) {
				list.add(TypeEntity.createTypeEntity(fileEntity,classEntity,methodEntity, cif));
			}
			entity.setTypeBounds(list);
		}

		return entity;
	}

}
