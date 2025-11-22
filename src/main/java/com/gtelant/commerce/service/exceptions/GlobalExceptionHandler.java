package com.gtelant.commerce.service.exceptions;

import com.gtelant.commerce.service.dtos.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j // Lombok 會自動生成名為 'log' 的 logger，不需手動宣告
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 1. 處理資源找不到 (404) - 這是配合 Service 改寫最重要的一步
     * 當 Service 拋出 ResourceNotFoundException 時執行此處
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFoundException(ResourceNotFoundException e) {
        log.warn("資源找不到: {}", e.getMessage()); // 404 通常用 warn 級別即可
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(e.getMessage(), 404));
    }

    /**
     * 2. 處理參數驗證失敗 (400) - 例如 @NotNull, @Size
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationException(MethodArgumentNotValidException e) {
        // 取得第一個驗證失敗的錯誤訊息 (例如: "分類名稱不能為空")
        String errorMessage = e.getBindingResult().getFieldError() != null
                ? e.getBindingResult().getFieldError().getDefaultMessage()
                : "參數驗證失敗";

        log.warn("參數驗證失敗: {}", errorMessage);

        // 建議：直接回傳具體的 errorMessage 給前端，方便除錯
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(errorMessage, 400));
    }

    /**
     * 3. 處理業務邏輯異常 (400)
     * 自定義的業務規則錯誤，例如 "庫存不足"、"優惠券過期"
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Object>> handleBusinessException(BusinessException e) {
        log.warn("業務異常: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(e.getMessage(), e.getCode()));
    }

    /**
     * 4. 處理未知異常 (500) - 兜底機制
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(Exception e) {
        log.error("系統發生未知異常", e); // 500 錯誤一定要印出 stack trace (e)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("系統發生錯誤，請稍後再試", 500));
    }
}