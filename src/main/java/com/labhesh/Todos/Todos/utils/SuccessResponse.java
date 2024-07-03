package com.labhesh.Todos.Todos.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SuccessResponse {
    private String message;
    private Object data;
    private List<Object> datas;
}
