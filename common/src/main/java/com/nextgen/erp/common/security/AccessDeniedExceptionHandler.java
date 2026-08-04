package com.nextgen.erp.common.security;

import com.nextgen.erp.common.dto.ApiResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Every business service has its own {@code GlobalExceptionHandler} with a
 * catch-all {@code @ExceptionHandler(Exception.class)} mapped to 500 —
 * which, without this, intercepts {@link AccessDeniedException} (thrown by
 * {@code @PreAuthorize} denials) before Spring Security's own handling ever
 * sees it, turning a should-be 403 into a generic 500.
 *
 * <p>Spring does NOT compare exception-type specificity across
 * {@code @ControllerAdvice} beans — {@code ExceptionHandlerExceptionResolver}
 * walks its (order-sorted) advice list and uses the first bean that can
 * resolve the exception at all, even via a broad {@code Exception.class}
 * catch-all. Each service's own advice bean is registered before this
 * auto-configured one, so without an explicit {@code @Order} it would win
 * that race purely by going first. {@code HIGHEST_PRECEDENCE} guarantees
 * this bean is checked before any service-local advice, regardless of
 * registration order.
 */
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AccessDeniedExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccessDenied(AccessDeniedException ex) {

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error("You do not have permission to perform this action"));
    }
}
