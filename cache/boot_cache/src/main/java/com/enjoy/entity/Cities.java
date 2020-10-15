package com.enjoy.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

@ApiModel(value = "地市信息表")
public class Cities implements Serializable {

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "")
    private Integer id;
    
	@ApiModelProperty(value = "")
    private String city;
	@ApiModelProperty(value = "")
    private String cityid;
	@ApiModelProperty(value = "")
    private String provinceid;
    private List<Areas> areases;

    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    public String getCityid() {
        return this.cityid;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
    }
    public String getProvinceid() {
        return this.provinceid;
    }

    public void setProvinceid(String provinceid) {
        this.provinceid = provinceid;
    }

}