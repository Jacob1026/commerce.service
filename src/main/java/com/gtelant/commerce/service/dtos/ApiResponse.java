package com.gtelant.commerce.service.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ApiResponse <T>{
    private boolean success;
    private String message;
    private T data;
    private  int code;


    public static <T> ApiResponse  <T> suceess(T data){
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setData(data);
        response.setCode(200);
        response.setMessage("操作成功");
        return response;
    }

    public static <T> ApiResponse  <T> error(String message, int code){
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setData(null);
        response.setCode(code);
        response.setMessage(message);
        return response;
    }
}

