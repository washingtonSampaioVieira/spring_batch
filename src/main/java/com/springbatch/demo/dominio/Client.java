package com.springbatch.demo.dominio;


import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Client {
    private int id;
    @SerializedName("nome")
    private String name;
    @SerializedName("endereco")
    private String address;
}
