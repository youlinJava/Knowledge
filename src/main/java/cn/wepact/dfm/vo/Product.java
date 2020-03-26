package cn.wepact.dfm.vo;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Product {
	
	private Integer id;
	
	private String code;
	
	private String label;
	
	private List<Product> children;

}
